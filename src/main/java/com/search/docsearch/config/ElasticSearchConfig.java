package com.search.docsearch.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.search.docsearch.except.TrustManagerException;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableElasticsearchRepositories
@ComponentScan
@Slf4j
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    @Value("${elasticsearch.url}")
    public String elasticsearchUrl;
    @Value("${elasticsearch.username}")
    public String elasticsearchUsername;
    @Value("${elasticsearch.password}")
    public String elasticsearchPassword;
    @Value("${elasticsearch.cerFilePath}")
    public String cerFilePath;
    @Value("${elasticsearch.cerPassword}")
    public String cerPassword;
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        SSLContext sc = null;
        try {
            TrustManager[] tm = {new MyX509TrustManager(cerFilePath, cerPassword)};
            sc = SSLContext.getInstance("SSL", "SunJSSE");
            sc.init(null, tm, SecureRandom.getInstance("NativePRNGBlocking"));
        } catch (Exception e) {
            log.error(e.toString());
        }
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elasticsearchUrl)
                .usingSsl(sc, new NullHostNameVerifier())
                .withBasicAuth(elasticsearchUsername, elasticsearchPassword)
                .build();
        return RestClients.create(clientConfiguration).rest();
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
            } catch(Exception e) {
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
    public static class NullHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostName, SSLSession session) {
            return !Objects.isNull(hostName) || !Objects.isNull(session);
        }
    }
}
