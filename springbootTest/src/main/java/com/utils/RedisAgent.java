package com.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class RedisAgent {
    private static final Logger logger = LogManager.getLogger(RedisAgent.class.getName());
    private static final Integer REIDS_TIMEOUT_SECONDS = Integer.valueOf("86400");//24 * 60 * 60;
    //private static JedisPool pool;
    private static JedisSentinelPool pool;

    /**
     * 建立连接池 真实环境，一般把配置参数缺抽取出来。
     */
    private static void createJedisPool() {

        Integer maxTotal = Integer.valueOf("1024");
        Integer maxIdle = Integer.valueOf("200");
        Integer maxWait = Integer.valueOf("1000");
        Integer timeout = Integer.valueOf("2000");
        String pass = "4SXD5S884CS";
        String clusterName ="mymaster";

        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(maxTotal);
        // 设置最大阻塞时间，记住是毫秒数milliseconds
        config.setMaxWaitMillis(maxWait);
        // 设置空间连接
        config.setMaxIdle(maxIdle);

        // 创建连接池
        // pool = new JedisPool(query, host, port, 2000, pass);

        pool = new JedisSentinelPool(clusterName, getSentinels(), config, timeout, pass);
        logger.info("redis connection made successful.");
    }

    private static Set<String> getSentinels() {
        Set<String> sentinels = new HashSet<>();

        String hosts ="10.0.18.200:26379|10.0.18.203:26379|10.0.18.204:26379";
        if (StringUtils.isBlank(hosts)) {
            throw new RuntimeException("redis.hosts query is not given");
        }

        Arrays.stream(StringUtils.split(hosts, "|")).forEach(host -> sentinels.add(host));
        return sentinels;
    }

    /**
     * 在多线程环境同步初始化
     */
    public static synchronized void poolInit() {
        if (pool == null) {
            try {
                createJedisPool();
            } catch (Exception ex) {
                logger.fatal("fail to connect to redis!");
                //throw ex;
            }
        }
    }

    /**
     * 获取一个jedis 对象
     *
     * @return
     */
    public static Jedis getJedis() {

        if (pool == null) {
            poolInit();
        }

        // 依然没有值说明连不上
        if (pool == null) {
            return null;
        }

        return pool.getResource();
    }


    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        String value = null;

        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                return null;
            }

            value = jedis.get(key);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("Error when get from redis:" + e.getMessage());
            return null;
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return value;
    }

    /**
     * 存入redis
     *
     * @param key
     * @return
     */
    public static String set(String key, String value) {

        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                return null;
            }

            value = jedis.set(key, value);
        } catch (Exception e) {
            logger.error("failed to save to redis for key {}", key);
            e.printStackTrace();
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return value;
    }

    /**
     * 存入redis 并设默认失效时间
     *
     * @param key
     * @param value
     * @return
     */
    public static String setex(String key, String value) {

        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                return null;
            }

            value = jedis.setex(key, REIDS_TIMEOUT_SECONDS, value);
        } catch (Exception e) {
            logger.error("failed to save to redis for key {}", key);

            e.printStackTrace();
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return value;
    }

    public boolean setnx(String key, String val) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            if (jedis == null) {
                return false;
            }
            return jedis.set(key, val, "NX", "PX", 1000 * 60).
                    equalsIgnoreCase("ok");
        } catch (Exception ex) {
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public int delnx(String key, String val) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            if (jedis == null) {
                return 0;
            }

            //if redis.call('get','orderkey')=='1111' then return redis.call('del','orderkey') else return 0 end
            StringBuilder sbScript = new StringBuilder();
            sbScript.append("if redis.call('get','").append(key).append("')").append("=='").append(val).append("'").
                    append(" then ").
                    append("    return redis.call('del','").append(key).append("')").
                    append(" else ").
                    append("    return 0").
                    append(" end");

            return Integer.valueOf(jedis.eval(sbScript.toString()).toString());
        } catch (Exception ex) {
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }


    public static void close() {
        pool.close();
    }
}