package com.yosep.user.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class UserToProductProducer {
    private final String createCart = "create-cart";
    private final String deleteCart = "delete-cart";

    private final KafkaTemplate<Integer, String> template;

    public UserToProductProducer(KafkaTemplate<Integer, String> template) {
        this.template = template;
    }

    public void produceCartCreation(String payload) throws ExecutionException, InterruptedException {
        log.info(this.template.send(createCart, payload).get() + "");
    }

    public void produceCartDeletion(String payload) throws ExecutionException, InterruptedException {
        System.out.println(this.template.send(deleteCart, payload).get());
    }
}
