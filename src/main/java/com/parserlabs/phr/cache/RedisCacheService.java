package com.parserlabs.phr.cache;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class RedisCacheService {

	private static final String REDIS_PHR_ID_TOKEN_KEY = "PHR-ID-TOKEN";

	@Value("${redis.cache.validity.time:120}")
	private long cacheValiditiyTimeInMins;

	@Value("${redis.cache.eviction.time:1440}")
	private long cacheEvictionTimeInMins;

	private HashOperations<String, String, String> hashOperations; // to access REDIS cache

	@Getter
	static class RedisValue implements Serializable {
		private static final long serialVersionUID = -6822685647363155396L;

		private String value;
		private Long timestamp;

		public RedisValue(String value) {
			super();
			this.value = value;
			this.timestamp = System.currentTimeMillis();
		}

		public static RedisValue of(String value) {
			return new RedisValue(value);
		}
	}

	@Autowired
	public RedisCacheService(RedisTemplate<String, String> redisTemplate) {
		try {

			redisTemplate.expire(REDIS_PHR_ID_TOKEN_KEY, cacheEvictionTimeInMins, TimeUnit.MINUTES);

		} catch (Exception e) {
			log.error("Exception occured while fetching  Redis Service. {}", e);
		}
		this.hashOperations = redisTemplate.opsForHash();
	}

	/**
	 * This method will check if the value is present in the cache or not, and the
	 * cached value should be within the 'cacheValiditiyTimeInMins' time.
	 *
	 * by default this will return true if it doesn't get any value
	 *
	 * @param hashKey
	 * @param value
	 * @return
	 */
	public boolean match(String hashKey) {
		boolean doesMatch = false;
		try {
			if (!StringUtils.isEmpty(hashKey)) {
				String cachedValue = hashOperations.get(REDIS_PHR_ID_TOKEN_KEY, hashKey);
				if (Objects.nonNull(cachedValue)) {
					doesMatch = true;
				}
			} else {
				return doesMatch;
			}
		} catch (Exception redisExp) {
			log.warn("Exception occured while fetching the value from the Redis cache.", redisExp);
		}
		return doesMatch;
	}

	/**
	 * This method will put the hash key and value in the cache
	 *
	 * @param hashKey
	 * @param value
	 */
	public boolean put(String hashKey, String value) {
		boolean flag = false;
		try {
			if (!StringUtils.isEmpty(hashKey) && !StringUtils.isEmpty(value)) {
				hashOperations.put(REDIS_PHR_ID_TOKEN_KEY, hashKey, value);
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception redisExp) {
			log.warn("Exception occured while saving the value into the Redis cache.", redisExp);
		}
		return flag;
	}

	/**
	 * This method will remove the key from the cache
	 *
	 * @param hashKey
	 */
	public void remove(String hashKey) {
		try {
			hashOperations.delete(REDIS_PHR_ID_TOKEN_KEY, hashKey);
		} catch (Exception redisExp) {
			log.warn("Exception occured while deleting the value into the Redis cache.", redisExp);
		}
	}

	/**
	 * This method will check if the value is present in the cache or not, and the
	 * cached value should be within the 'cacheValiditiyTimeInMins' time.
	 *
	 * by default this will return value if it doesn't have any key it will return
	 * null
	 *
	 * @param hashKey
	 * @param value
	 * @return
	 */
	public String get(String hashKey) {
		String cachedValue = null;
		try {
			if (!StringUtils.isEmpty(hashKey))
				cachedValue = hashOperations.get(REDIS_PHR_ID_TOKEN_KEY, hashKey);
		} catch (Exception redisExp) {
			log.warn("Exception occured while fetching the value from the Redis cache.", redisExp);
		}
		return cachedValue;
	}
}
