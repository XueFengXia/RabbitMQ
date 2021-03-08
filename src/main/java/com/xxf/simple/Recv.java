package com.xxf.simple;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Author:rooten
 * @Date:2021/3/6
 * @Description:
 */
public class Recv {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("182.92.236.141"); // localhost
        factory.setUsername("admin");
        factory.setPassword("password");
        factory.setPort(5672);
        factory.setVirtualHost("/dev");
        //消费者一般不增加自动关闭
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // consumerTag 是固定的 可以做此会话的名字， deliveryTag 每次接收消息+1
                System.out.println("consumerTag消息标识="+consumerTag);
                //可以获取交换机，路由健等
                System.out.println("envelope元数据="+envelope);
                System.out.println("properties配置信息="+properties);
                System.out.println("body="+new String(body,"utf-8"));

            }
        };
        channel.basicConsume(QUEUE_NAME,true,consumer);

      /*  DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        //自动确认消息
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });*/
    }
}
