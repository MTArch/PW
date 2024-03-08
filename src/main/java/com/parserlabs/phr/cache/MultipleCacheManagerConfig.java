package com.parserlabs.phr.cache;

import java.net.URISyntaxException;
import java.time.Duration;

import javax.cache.Caching;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@ConditionalOnExpression("${redis.service.enabled:true}")
public class MultipleCacheManagerConfig {

	@Value("${redis.cache.host}")
	private String redisHost;

	@Value("${redis.cache.port:6379}")
	private int redisPort;

	@Value("${redis.cache.password}")
	private String redisPassword;

	@Value("${redis.cache.eviction.time:1440}")
	private long cacheEvictionTimeInMins;
	
	@Bean("ehCacheManager")
	@Primary
	public CacheManager ehCacheManager() throws URISyntaxException {
		return new JCacheCacheManager(Caching.getCachingProvider().getCacheManager(getClass().getResource("/ehcache.xml").toURI(), getClass().getClassLoader()));
	}

	@Bean
	@Lazy
	public JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(redisHost.trim(), redisPort);
				standaloneConfig.setPassword(redisPassword.trim());
		return new JedisConnectionFactory(standaloneConfig);
	}

	@Bean("redisCacheManager")
	@Lazy
	public CacheManager redisCacheManager() {
		return RedisCacheManager.builder(jedisConnectionFactory()).build();
	}

	@Bean
	@Lazy
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		try {
			redisTemplate.setConnectionFactory(jedisConnectionFactory());
		} catch (Exception e) {
			log.warn("Unable to create RedisTemplate object due to JedisConnectionFactory error. Message [{}]",
					e.getMessage());
		}
		return redisTemplate;
	}
	
	@SuppressWarnings("unused")
	private RedisCacheConfiguration redisCacheConfig() {
		Duration timeToLive = Duration.ofHours(cacheEvictionTimeInMins);
		return RedisCacheConfiguration.defaultCacheConfig().entryTtl(timeToLive);
	}
}
