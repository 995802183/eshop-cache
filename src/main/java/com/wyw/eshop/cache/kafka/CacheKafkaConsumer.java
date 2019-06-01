package com.wyw.eshop.cache.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CacheKafkaConsumer {

    @KafkaListener(id = "infoChange", topics = "infoChange")
    private void listener(ConsumerRecord<String, String> consumerRecord) {
        new Thread(new KafkaMessageProcessor(consumerRecord)).start();
    }
}
