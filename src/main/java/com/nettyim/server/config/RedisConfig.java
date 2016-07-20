package com.nettyim.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableTransactionManagement
@PropertySource(value = {"classpath:application.properties"})
public class RedisConfig {

	@Autowired
	private Environment env;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(Integer.parseInt(env.getProperty("nd.redis.maxIdle").trim()));
        jedisPoolConfig.setTestOnBorrow(Boolean.valueOf(env.getProperty("nd.redis.testOnBorrow").trim()));
//      jedisPoolConfig.setMaxWaitMillis(Integer.parseInt(env.getProperty("nd.redis.maxWaitMillis").trim()));
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(env.getProperty("nd.redis.host").trim());
        jedisConnectionFactory.setPort(Integer.parseInt(env.getProperty("nd.redis.port").trim()));
        jedisConnectionFactory.setDatabase(Integer.parseInt(env.getProperty("nd.redis.database").trim()));
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setPassword(env.getProperty("nd.redis.password").trim());

        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {

        RedisTemplate<String, String> redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}