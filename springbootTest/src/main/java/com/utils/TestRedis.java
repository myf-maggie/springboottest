package com.utils;



/**
 * Created by david on 2019/8/5.
 */
public class TestRedis {

//    public static void main(String[] args) {
//
//        TestRedis test = new TestRedis();
//        test.testUserInput();
//    }
//
//    /**
//     * redis操作字符串
//     */
//    public void testString() {
////        //添加数据
////        RedisAgent.set("name", "youcong");
////        System.out.println(jedis.get("name"));
////
////        //拼接字符串
////        RedisAgent.append("name", ".com");
////        System.out.println(jedis.get("name"));
////
////        //删除数据
////        jedis.del("name");
////        System.out.println(jedis.get("name"));
////
////        //设置多个键值对
////        jedis.mset("name", "yc", "age", "22", "qq", "1933108196");
////        jedis.incr("age");//加1操作
////        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
//    }
//
//    public void testUserInput() {
//        User user = new User();
//        user.setUsername("david");
//        user.setPassword("aabbcc");
//
//        String key = "users" + "::" + user.username;
//        RedisAgent.set(key, user.toString());
//        //jedis.setex(key, 30, user.toString());
//        //jedis.setex(key, 30, )
//
//        System.out.println(RedisAgent.get("users::" + user.username));
//
//
//        Tenant tenant = new Tenant();
//        tenant.setId(1);
//        tenant.setName("中智诚");
//        tenant.setBusinessType(BusinessType.valueOf(1));
//
//        RedisAgent.set("tenants::" + tenant.getId(), tenant.toString());
//
//        System.out.println(RedisAgent.get("tenants::" + tenant.getId()));
//
//    }
}
