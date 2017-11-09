package org.liko.rabbitmq.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MQTest {
    /**
     * 获取临时队列
     */
    @Test
    public void test1() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            // 获取临时队列
            String queue = channel.queueDeclare().getQueue();
            System.out.println(queue);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
