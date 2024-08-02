


package com.search.docsearch.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import redis.clients.jedis.JedisPoolConfig;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.time.Duration;


@Configuration
public class RedisConfiguration {
    /**
     * Logger for logging messages in RedisConfiguration class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfiguration.class);

    /**
     * Host of the Redis server.
     */
    @Value("${spring.data.redis.host}")
    private String redisHost;

    /**
     * Port of the Redis server (default: 6379).
     */
    @Value("${spring.data.redis.port:6379}")
    private Integer redisPort = 6379;

    /**
     * Password for connecting to the Redis server.
     */
    @Value("${spring.data.redis.password}")
    private String redisPassword;

    /**
     * Connection timeout for Redis in milliseconds (default: 3000).
     */
    @Value("${spring.data.redis.connect-timeout:3000}")
    private Integer redisConnectTimeout = 3000;

    /**
     * Read timeout for Redis operation in milliseconds (default: 2000).
     */
    @Value("${spring.data.redis.timeout:2000}")
    private Integer redisReadTimeout = 2000;

    /**
     * Path to the CA certificate file for SSL connection to Redis.
     */
    @Value("${redis-global.caPath}")
    private String caPath;

    /**
     * Minimum number of idle connections in the Redis pool.
     */
    @Value("${spring.data.redis.poolmin-idel}")
    private Integer minIdel;

    /**
     * Maximum number of idle connections in the Redis pool.
     */
    @Value("${spring.data.redis.poolmax-idel}")
    private Integer maxIdel;

    /**
     * Maximum number of active connections in the Redis pool.
     */
    @Value("${spring.data.redis.poolmax}")
    private Integer maxPool;


    /**
     * Configures a RedisConnectionFactory bean with the provided JedisClientConfiguration.
     *
     * @param clientConfiguration The JedisClientConfiguration for configuring the Redis connection.
     * @return The configured RedisConnectionFactory bean.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(final JedisClientConfiguration clientConfiguration) {

        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setHostName(redisHost);
        standaloneConfiguration.setPort(redisPort);
        standaloneConfiguration.setPassword(redisPassword);

        return new JedisConnectionFactory(standaloneConfiguration, clientConfiguration);
    }

    /**
     * Configures a JedisClientConfiguration bean.
     *
     * @return The configured JedisClientConfiguration bean.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public JedisClientConfiguration clientConfiguration() throws Exception {
        JedisClientConfiguration.JedisClientConfigurationBuilder configurationBuilder
                = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ZERO)
                .readTimeout(Duration.ofMillis(redisReadTimeout));


        configurationBuilder.useSsl().sslSocketFactory(getTrustStoreSslSocketFactory());

        configurationBuilder.usePooling().poolConfig(redisPoolConfig());

        return configurationBuilder.build();
    }

    /**
     * Retrieves the SSLSocketFactory using the trust store for SSL connections.
     *
     * @return The SSLSocketFactory configured with the trust store.
     * @throws Exception if an error occurs during the process.
     */
    private SSLSocketFactory getTrustStoreSslSocketFactory() throws Exception {
        //加载ca证书
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca;
        try (InputStream is = new FileInputStream(caPath)) {
            ca = cf.generateCertificate(is);
        } catch (Exception e) {
            LOGGER.error("redis ca load error");
            throw e;
        }


        //创建keystore
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        //创建TrustManager
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        //创建SSLContext
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, trustManagerFactory.getTrustManagers(), SecureRandom.getInstanceStrong());
        return context.getSocketFactory();
    }

    /**
     * Configures a JedisPoolConfig for Redis connection pooling.
     *
     * @return The configured JedisPoolConfig.
     */
    private JedisPoolConfig redisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //连接池的最小连接数
        poolConfig.setMinIdle(minIdel);
        //连接池的最大空闲连接数
        poolConfig.setMaxIdle(maxIdel);
        //连接池的最大连接数
        poolConfig.setMaxTotal(maxPool);
        return poolConfig;
    }

    @Bean
    public LettuceClientConfiguration lettuceClientConfiguration() {
        SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofMillis(redisConnectTimeout))
                .keepAlive(true)
                .build();

        ClientOptions clientOptions = ClientOptions.builder()
                .socketOptions(socketOptions)
                .build();

        return LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(redisReadTimeout))
                .clientOptions(clientOptions)
                .build();
    }
}
