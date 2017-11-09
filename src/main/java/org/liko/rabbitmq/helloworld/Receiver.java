package org.liko.rabbitmq.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver {
    private final static String QUEUE_NAME = "LikoQueue";

    public static void main(String[] args) {
        receiver();
    }

    public static void receiver() {
        ConnectionFactory factory = null;
        Connection connection = null;
        Channel channel = null;

        try {
            // create connection factory
            factory = new ConnectionFactory();
            // set address
            factory.setHost("localhost");
            // new connection
            connection = factory.newConnection();
            // new channel
            channel = connection.createChannel();
            // 声明一个队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            /**
             * DefaultConsumer 类实现了Consumer 接口, 通过传入一个channel, 告诉服务器我们需要哪个Channel 的消息, 如果channel中有消息, 就会执行回调函数 handleDelivery
             */
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("Start process msg!");
                    String msg = new String(body, "UTF-8");
                    System.out.println("Receiver msg :" + msg);
                }
            };
            //自动回复队列应答
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
