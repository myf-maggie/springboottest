package com.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by zxc on 2019/12/16.
 */
public class KafkaCon {
    public static void main(String[] args){
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "10.3.115.73:9092");//xxx是服务器集群的ip
        properties.put("group.id", "jd-group");//组名 不同组名可以重复消费。例如你先使用了组名A消费了kafka的1000条数据，
        // 但是你还想再次进行消费这1000条数据，
        // 并且不想重新去产生，那么这里你只需要更改组名就可以重复消费了
        properties.put("enable.auto.commit", "true");//是否自动提交，默认为true。
        properties.put("auto.commit.interval.ms", "1000");//从poll(拉)的回话处理时长
        properties.put("auto.offset.reset", "latest");//：消费规则，默认earliest 。
        //earliest: 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费 。
        //latest: 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据 。
        //none: topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常。
        properties.put("session.timeout.ms", "30000");//超时时间。
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList("testTopic"));
        while (true) {
            ConsumerRecords<String, String> records=null;
            do {
                records= kafkaConsumer.poll(100);
                if (records==null){break;}
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("-----------------");
                    System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                    System.out.println();
                }
            }while (records.isEmpty());

        }

    }

}


