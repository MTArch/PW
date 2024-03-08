package com.parserlabs.phr.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.parserlabs.phr.addednew.CustomSpanned;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 * 
 * @apiNote https://redislabs.com/redis-best-practices/basic-rate-limiting/
 *
 */
@Service
@Slf4j
@CustomSpanned
public class RedisRateLimiterService {

	@Autowired
	private StringRedisTemplate stringTemplate;

	@Value("${verify.flood.request:5}")
	private Integer REQUESTS_PER_HOUR;

	@Value("${verify.otp.flood.lock:1}")
	private Integer REQUESTS_LOCK_HOUR;

	private static final String REDIS_PHR_ID_TOKEN_KEY = "PHR-ID-EMAIL-TOKEN";

	/***
	 * Stored and check the value count in the REDIS
	 * 
	 * @param key
	 * @param numberOfAttempt
	 * @param validity
	 * @param timeunit
	 * @return
	 */
	public boolean isAllowed(String key, int numberOfAttempt, int validity, TimeUnit timeunit) {
		return checkValidity(key, numberOfAttempt, validity, timeunit);
	}

	/*
	 * Check data validity
	 * 
	 * @param key
	 * 
	 * @param requestPerHours
	 * 
	 * @param requestPerHoursLock
	 * 
	 * @param timeUnits
	 * 
	 * @return
	 */
	private boolean checkValidity(String key, int requestPerHours, int requestPerHoursLock, TimeUnit timeUnits) {
		try {
			ValueOperations<String, String> operations = stringTemplate.opsForValue();
			String requests = operations.get(key);
			if (StringUtils.isNotBlank(requests) && Integer.parseInt(requests) >= requestPerHours) {
				return false;
			}
			List<Object> txResults = stringTemplate.execute(new SessionCallback<List<Object>>() {
				@Override
				public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
					final StringRedisTemplate redisTemplate = (StringRedisTemplate) operations;
					final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
					operations.multi();
					valueOperations.increment(key);
					redisTemplate.expire(key, requestPerHoursLock, timeUnits);
					// This will contain the results of all operations in the transaction
					return operations.exec();
				}
			});
			log.info("REDIS Current request count: {}", txResults.get(0));
		} catch (Exception e) {
			log.error("Error while validating Brute force Attack/flood with redis service. {}", e.getMessage());
		}
		return true;
	}

	/**
	 * Method to fetch/store data in REDIS with validity
	 * 
	 * @param key
	 * @param Data
	 * @param requestPerHoursLock
	 * @return
	 */
	public String fetchStoreDataToTempalteRedis(String key, String Data, long validity, TimeUnit timeunit) {
		String result = null;
		try {
			// Get the data from the Redis
			if (!StringUtils.isEmpty(key)) {
				HashOperations<String, String, String> operations = stringTemplate.opsForHash();
				result = operations.get(key, REDIS_PHR_ID_TOKEN_KEY);
				if (StringUtils.isNotBlank(result))
					return result;
			}

			if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(Data)) {
				// Saved the data to REDIS
				List<Object> txResults = stringTemplate.execute(new SessionCallback<List<Object>>() {
					@Override
					public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
						final StringRedisTemplate redisTemplate = (StringRedisTemplate) operations;
						final HashOperations<String, String, String> valueOperations = redisTemplate.opsForHash();
						operations.multi();
						valueOperations.put(key, REDIS_PHR_ID_TOKEN_KEY, Data);
						redisTemplate.expire(key, validity, timeunit);
						// This will contains the results of all operations in the transaction
						return operations.exec();
					}
				});
				log.trace("REDIS Current: {}", txResults.get(0));
				result = "saved";
			}
		} catch (Exception e) {
			log.error("Error while getting email verification ref txn with redis. " + e.getMessage());
		}
		return result;
	}

	/**
	 * This method remove the key if exist in the Redis
	 * 
	 * @param key
	 * @return
	 */
	public boolean flushKey(String key) {
		if (StringUtils.isNotBlank(key)) {
			HashOperations<String, String, String> operations = stringTemplate.opsForHash();
			operations.delete(REDIS_PHR_ID_TOKEN_KEY, key);
			log.info("Data flush for key: {}", key);
		}
		return true;

	}

	public boolean checkBruteAttack(String key, int requestPerHours, int requestPerHoursLock, TimeUnit timeUnits) {
		return checkValidity(key, requestPerHours, requestPerHoursLock, timeUnits);
	}

}
