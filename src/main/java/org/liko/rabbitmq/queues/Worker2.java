package org.liko.rabbitmq.queues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker2 {
    private static final String TAKS_QUEUE_NAME = "task_queue";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.queueDeclare(TAKS_QUEUE_NAME, true, false, false, null);
            System.out.println("Worker2 [x] Waiting for message. To exit press CTRL + C");

            //设定每次从队列中获取的数量
            channel.basicQos(1);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, "UTF-8");

                    System.out.println("Worker2 [x] Received : " + msg);

                    try {
                        doWork(msg);
                    } finally {
                        System.out.println("Worker2 [x] Done");
                        //消息处理完成确认
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            //消息消费完成确认
            channel.basicConsume(TAKS_QUEUE_NAME, false, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void doWork(String msg) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
