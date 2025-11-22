package com.test.bank.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CUSTOMER_EXCHANGE = "customer.events.exchange";
    public static final String CUSTOMER_QUEUE_ACCOUNTS = "customer.accounts.queue";

    public static final String RK_CUSTOMER_CREATED = "customer.created";
    public static final String RK_CUSTOMER_UPDATED = "customer.updated";
    public static final String RK_CUSTOMER_DELETED = "customer.deleted";

    @Bean
    public TopicExchange customerExchange() {
        return new TopicExchange(CUSTOMER_EXCHANGE);
    }

    @Bean
    public Queue customerAccountsQueue() {
        return new Queue(CUSTOMER_QUEUE_ACCOUNTS, true);
    }

    @Bean
    public Binding customerCreatedBinding() {
        return BindingBuilder.bind(customerAccountsQueue())
                .to(customerExchange())
                .with(RK_CUSTOMER_CREATED);
    }

    @Bean
    public Binding customerUpdatedBinding() {
        return BindingBuilder.bind(customerAccountsQueue())
                .to(customerExchange())
                .with(RK_CUSTOMER_UPDATED);
    }

    @Bean
    public Binding customerDeletedBinding() {
        return BindingBuilder.bind(customerAccountsQueue())
                .to(customerExchange())
                .with(RK_CUSTOMER_DELETED);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
