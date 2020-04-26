package cn.zilanxuan.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 功能描述: redis缓存配置类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月9日 下午10:15:12S
 */
@EnableCaching
@Configuration
public class RedisCacheConfig {
	
	/**
	 * 替换默认序列化(默认使用jdk序列化)
	 * @param factory
	 * @return
	 */
	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		return redisTemplate;
	}
	
	/**
	 * 基于SpringBoot2 对 RedisCacheManager 的自定义配置
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
		RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
		RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer);
		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
		defaultCacheConfig.entryTtl(Duration.ofDays(1));
		return new RedisCacheManager(redisCacheWriter,defaultCacheConfig);
	}
}
