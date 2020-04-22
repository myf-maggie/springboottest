package com.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;


@Configuration
public class RedisConfig {

    @Autowired
    JedisConnectionFactory jedisConnectionFactory;


    @Bean
    public static JedisConnectionFactory jedisConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();

        String sentinelsS ="10.0.18.200:26379|10.0.18.203:26379|10.0.18.204:26379";
        try {
            String[] nodes = StringUtils.split(sentinelsS, '|');
            for (String node : nodes) {
                String[] parts = StringUtils.split(node, ':');
                if(parts.length == 2)
                    sentinelConfig.addSentinel(new RedisNode(parts[0], Integer.valueOf(parts[1])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sentinelConfig.setMaster("mymaster");
        sentinelConfig.setPassword(RedisPassword.of("4SXD5S884CS"));

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1024);
        jedisPoolConfig.setMaxIdle(200);
        jedisPoolConfig.setMaxWaitMillis(1000);

        JedisClientConfiguration.JedisClientConfigurationBuilder clienConfigBuilder = JedisClientConfiguration.builder();
        clienConfigBuilder.readTimeout(Duration.ofMillis(2000));
        clienConfigBuilder.connectTimeout(Duration.ofMillis(2000));
        clienConfigBuilder.usePooling().poolConfig(jedisPoolConfig);
        JedisClientConfiguration clienConfig = clienConfigBuilder.build();

        JedisConnectionFactory factory = new JedisConnectionFactory(sentinelConfig, clienConfig);
        return factory;
    }


    @Bean
    public RedisTemplate<Integer, String> redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
