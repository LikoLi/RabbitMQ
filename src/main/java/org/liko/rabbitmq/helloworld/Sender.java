package org.liko.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Rabbit MQ Msg Sender
 */
public class Sender {
    private final static String QUEUE_NAME = "LikoQueue";

    public static void main(String args[]) {
        send();
    }

    public static void send() {
        ConnectionFactory factory = null;
        Connection connection = null;
        Channel channel = null;

        try {
            // Create connection factory
            factory = new ConnectionFactory();
            // Set RabbitMQ address
            factory.setHost("localhost");
            // create new connection
            connection = factory.newConnection();
            // create new channel
            channel = connection.createChannel();
            // 声明一个队列
            // 在rabbit mq中, 队列是幂等性的(一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同),
            // 也就是说, 如果不存在, 就给创建, 如果存在, 不会对已经存在的队列产生影响
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String msg = "my first message";
            //发送消息到队列中
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("Send Msg : " + msg);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // close channel and connection
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
