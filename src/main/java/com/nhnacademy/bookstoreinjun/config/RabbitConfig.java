package com.nhnacademy.bookstoreinjun.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${rabbit.inventory.increase.exchange.name}")
    private String increaseInventoryExchangeName;
    @Value("${rabbit.inventory.increase.queue.name}")
    private String increaseInventoryQueueName;
    @Value("${rabbit.inventory.increase.routing.key}")
    private String increaseInventoryRoutingKey;
    @Value("${rabbit.inventory.increase.dlq.queue.name}")
    private String increaseInventoryDlqQueueName;
    @Value("${rabbit.inventory.increase.dlq.routing.key}")
    private String increaseInventoryDlqRoutingKey;


    @Value("${rabbit.inventory.decrease.exchange.name}")
    private String decreaseInventoryExchangeName;
    @Value("${rabbit.inventory.decrease.queue.name}")
    private String decreaseInventoryQueueName;
    @Value("${rabbit.inventory.decrease.routing.key}")
    private String decreaseInventoryRoutingKey;
    @Value("${rabbit.inventory.decrease.dlq.queue.name}")
    private String decreaseInventoryDlqQueueName;
    @Value("${rabbit.inventory.decrease.dlq.routing.key}")
    private String decreaseInventoryDlqRoutingKey;


    @Value("${rabbit.cart.checkout.exchange.name}")
    private String cartCheckoutExchangeName;
    @Value("${rabbit.cart.checkout.queue.name}")
    private String cartCheckoutQueueName;
    @Value("${rabbit.cart.checkout.routing.key}")
    private String cartCheckoutRoutingKey;
    @Value("${rabbit.cart.checkout.dlq.queue.name}")
    private String cartCheckoutDlqQueueName;
    @Value("${rabbit.cart.checkout.dlq.routing.key}")
    private String cartCheckoutDlqRoutingKey;


    @Bean
    DirectExchange increaseInventoryExchange() {
        return new DirectExchange(increaseInventoryExchangeName);
    }

    @Bean
    DirectExchange decreaseInventoryExchange() {
        return new DirectExchange(decreaseInventoryExchangeName);
    }

    @Bean
    DirectExchange cartCheckoutExchange() {
        return new DirectExchange(cartCheckoutExchangeName);
    }

    @Bean
    Queue increaseInventoryQueue() {
        return QueueBuilder.durable(increaseInventoryQueueName)
                .withArgument("x-dead-letter-exchange", increaseInventoryExchangeName)
                .withArgument("x-dead-letter-routing-key", increaseInventoryDlqRoutingKey)
                .build();
    }

    @Bean
    Queue decreaseInventoryQueue() {
        return QueueBuilder.durable(decreaseInventoryQueueName)
                .withArgument("x-dead-letter-exchange", decreaseInventoryExchangeName)
                .withArgument("x-dead-letter-routing-key", decreaseInventoryDlqRoutingKey)
                .build();
    }

    @Bean
    Queue cartCheckoutQueue() {
        return QueueBuilder.durable(cartCheckoutQueueName)
                .withArgument("x-dead-letter-exchange", cartCheckoutExchangeName)
                .withArgument("x-dead-letter-routing-key", cartCheckoutDlqRoutingKey)
                .build();
    }

    @Bean
    Queue increaseInventoryDlqQueue() {
                    return new Queue(increaseInventoryDlqQueueName);
    }

    @Bean
    Queue decreaseInventoryDlqQueue() {
        return new Queue(decreaseInventoryDlqQueueName);
    }

    @Bean
    Queue cartCheckoutDlqQueue() {
        return new Queue(cartCheckoutDlqQueueName);
    }

    @Bean
    Binding increaseInventoryBinding() {    return BindingBuilder.bind(increaseInventoryQueue()).to(increaseInventoryExchange()).with(increaseInventoryRoutingKey);}

    @Bean
    Binding decreaseInventoryBinding() {    return BindingBuilder.bind(decreaseInventoryQueue()).to(decreaseInventoryExchange()).with(decreaseInventoryRoutingKey);}

    @Bean
    Binding cartCheckoutBinding() {    return BindingBuilder.bind(cartCheckoutQueue()).to(cartCheckoutExchange()).with(cartCheckoutRoutingKey);}

    @Bean
    Binding increaseInventoryDlqBinding() { return BindingBuilder.bind(increaseInventoryDlqQueue()).to(increaseInventoryExchange()).with(increaseInventoryDlqRoutingKey);}

    @Bean
    Binding decreaseInventoryDlqBinding() { return BindingBuilder.bind(decreaseInventoryDlqQueue()).to(decreaseInventoryExchange()).with(decreaseInventoryDlqRoutingKey);}

    @Bean
    Binding cartCheckoutDlqBinding() { return BindingBuilder.bind(cartCheckoutDlqQueue()).to(cartCheckoutExchange()).with(cartCheckoutDlqRoutingKey);}

}
