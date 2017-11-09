package org.liko.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicSender {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            //声明一个匹配Exchange
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            //声明多个Routing Key
            String[] routingKeys = new String[]{"quick.orange.rabbit",
                                                "lazy.orange.elephant",
                                                "quick.orange.fox",
                                                "lazy.brown.fox",
                                                "quick.brown.fox",
                                                "quick.orange.male.rabbit",
                                                "lazy.orange.male.rabbit"};
            //发送消息
            for (String severity : routingKeys) {
                String msg = "From " + severity + " routingKey's message!";
                channel.basicPublish(EXCHANGE_NAME, severity, null, msg.getBytes());
                System.out.println("TopicSend [x] Send '" + severity + "':'" + msg + "'");
            }

            channel.close();
            connection.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
