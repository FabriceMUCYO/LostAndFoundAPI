package rw.ac.auca.lostandfound.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rw.ac.auca.lostandfound.config.RabbitMQConfig;

@Component
public class NotificationPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendClaimStatusNotification(String claimantName, String itemName, String status) {
        String message = "Hello " + claimantName + ", your claim for '" + itemName + "' has been " + status + ".";
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_QUEUE, message);
    }
}