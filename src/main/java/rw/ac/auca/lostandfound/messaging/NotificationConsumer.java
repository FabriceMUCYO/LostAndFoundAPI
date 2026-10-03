package rw.ac.auca.lostandfound.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rw.ac.auca.lostandfound.config.RabbitMQConfig;

@Component
public class NotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotification(String message) {
        System.out.println("=== SIMULATED EMAIL SENT ===");
        System.out.println(message);
        System.out.println("=============================");
    }
}