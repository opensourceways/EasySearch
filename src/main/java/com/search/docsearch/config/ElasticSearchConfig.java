package com.search.docsearch.config;

import com.search.docsearch.except.TrustManagerException;
import com.search.docsearch.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
@Slf4j
public class ElasticSearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    @Value("${elasticsearch.host}")
    public String elasticsearchHost;
    @Value("${elasticsearch.port}")
    public int elasticsearchPort;
    @Value("${elasticsearch.protocol}")
    public String elasticsearchProtocol;
    @Value("${elasticsearch.username}")
    public String elasticsearchUsername;
    @Value("${elasticsearch.password}")
    public String elasticsearchPassword;
    @Value("${elasticsearch.cerFilePath}")
    public String cerFilePath;
    @Value("${elasticsearch.cerPassword}")
    public String cerPassword;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient elasticsearchClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticsearchUsername, elasticsearchPassword));
        SSLContext sc = null;
        try {
            TrustManager[] tm = {new MyX509TrustManager(cerFilePath, cerPassword)};
            sc = SSLContext.getInstance("SSL", "SunJSSE");
            //也可以使用SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sc.init(null, tm, new SecureRandom());

            SSLIOSessionStrategy sessionStrategy = new SSLIOSessionStrategy(sc, new NoopHostnameVerifier());
            SecuredHttpClientConfigCallback httpClientConfigCallback = new SecuredHttpClientConfigCallback(sessionStrategy,
                    credentialsProvider);

            RestClientBuilder builder = RestClient.builder(constructHttpHosts(Collections.singletonList(elasticsearchHost), elasticsearchPort, elasticsearchProtocol))
                    .setRequestConfigCallback(requestConfig -> requestConfig.setConnectTimeout(5 * 1000)
                            .setConnectionRequestTimeout(5 * 1000)
                            .setSocketTimeout(30 * 1000))
                    .setHttpClientConfigCallback(httpClientConfigCallback);
            final RestHighLevelClient client = new RestHighLevelClient(builder);
            logger.info("es rest client build success {} ", client);

            ClusterHealthRequest request = new ClusterHealthRequest();
            ClusterHealthResponse response = client.cluster().health(request, RequestOptions.DEFAULT);
            logger.info("es rest client health response {} ", response);

            return client;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * constructHttpHosts函数转换host集群节点ip列表。
     */
    public static HttpHost[] constructHttpHosts(List<String> host, int port, String protocol) {
        return host.stream().map(p -> new HttpHost(p, port, protocol)).toArray(HttpHost[]::new);
    }

    /**
     * SecuredHttpClientConfigCallback类定义。
     */
    static class SecuredHttpClientConfigCallback implements RestClientBuilder.HttpClientConfigCallback {
        @Nullable
        private final CredentialsProvider credentialsProvider;

        private final SSLIOSessionStrategy sslStrategy;

        SecuredHttpClientConfigCallback(final SSLIOSessionStrategy sslStrategy,
                                        @Nullable final CredentialsProvider credentialsProvider) {
            this.sslStrategy = Objects.requireNonNull(sslStrategy);
            this.credentialsProvider = credentialsProvider;
        }

        @Nullable
        CredentialsProvider getCredentialsProvider() {
            return credentialsProvider;
        }

        SSLIOSessionStrategy getSSLStrategy() {
            return sslStrategy;
        }

        @Override
        public HttpAsyncClientBuilder customizeHttpClient(final HttpAsyncClientBuilder httpClientBuilder) {
            httpClientBuilder.setSSLStrategy(sslStrategy);
            if (credentialsProvider != null) {
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
            return httpClientBuilder;
        }
    }

    public static class MyX509TrustManager implements X509TrustManager {
        X509TrustManager sunJSSEX509TrustManager;

        MyX509TrustManager(String cerFilePath, String cerPassword) throws TrustManagerException {
            File file = new File(cerFilePath);
            try {
                if (!file.isFile()) {
                    throw new FileNotFoundException("Wrong Certification Path");
                }
                log.info("Loading Keystore {} ...", file);
                InputStream in = new FileInputStream(file);
                KeyStore ks = KeyStore.getInstance("JKS");
                ks.load(in, cerPassword.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
                tmf.init(ks);
                TrustManager[] tms = tmf.getTrustManagers();
                for (TrustManager tm : tms) {
                    if (tm instanceof X509TrustManager) {
                        sunJSSEX509TrustManager = (X509TrustManager) tm;
                        return;
                    }
                }
            } catch (Exception e) {
                throw new TrustManagerException("Couldn't initialize");
            }
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            for (X509Certificate cert : chain) {
                cert.checkValidity(); // 验证证书是否过期
                boolean isCA = cert.getBasicConstraints() >= 0; // 检查是否为 CA 证书
                if (!isCA) {
                    throw new CertificateException("Invalid certificate: not a CA certificate.");
                }
            }
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            for (X509Certificate cert : chain) {
                if (cert.getNotAfter().getTime() < System.currentTimeMillis()) {
                    throw new CertificateException("Server certificate has expired.");
                }
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
