package org.liko.rabbitmq.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsTopic1 {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            //声明一个匹配模式的交换器
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String queueName = channel.queueDeclare().getQueue();

            // 路由关键字
            String[] routingKeys = new String[]{"*.orange.*"};
            //绑定路由关键字
            for (String bindingKey : routingKeys) {
                channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
                System.out.println("ReceiveLogsTopic1 exchange : " + EXCHANGE_NAME + ", queue : " + queueName + ", BindRoutingKey : " + bindingKey);
            }
            System.out.println("ReceiveLogsTopic1 [*] Waiting for message. To exit press CTRL + C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, "UTF-8");
                    System.out.println("ReceiveLogsTopic1 [x] Received '" + envelope.getRoutingKey() + "':'" + msg + "'");
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
