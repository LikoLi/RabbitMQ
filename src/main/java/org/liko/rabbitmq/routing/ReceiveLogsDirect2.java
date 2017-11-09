package org.liko.rabbitmq.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect2 {
    //EXCHANGE name
    private static final String EXCHANGE_NAME = "direct_logs";
    //Routing keys
    private static final String[] routingKeys = new String[]{"error"};

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明Exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        //获取匿名队列名称
        String queueName = channel.queueDeclare().getQueue();
        //根据Routing Key 进行绑定
        for (String routingKey : routingKeys) {
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
            System.out.println("ReceiveLogsDirect2 exchage : " + EXCHANGE_NAME + ", queue : " + queueName + ", BindRoutingKey : " + routingKey);
        }
        System.out.println("ReceiveLogsDirect2 [x] Waiting for message. To exit press CTRL + C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("[x] Received '" + envelope.getRoutingKey() + "':'" + msg + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
