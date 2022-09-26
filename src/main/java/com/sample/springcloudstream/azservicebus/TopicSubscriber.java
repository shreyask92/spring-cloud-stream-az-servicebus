package com.sample.springcloudstream.azservicebus;

import com.microsoft.azure.spring.integration.core.AzureHeaders;
import com.microsoft.azure.spring.integration.core.api.Checkpointer;
import com.sample.springcloudstream.azservicebus.config.MessageStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@EnableBinding(MessageStreams.class)
public class TopicSubscriber {

    private final Logger LOGGER = LoggerFactory.getLogger(TopicSubscriber.class);

    @StreamListener(target = MessageStreams.INPUT1)
    public void messageInput1(Message<String> iMessage) throws InterruptedException {

        UUID messageId = iMessage.getHeaders().getId();

        LOGGER.info("--------------------------------------------------------------");
        LOGGER.info("A Message({}) has been Received.",
                messageId);

        String message = iMessage.getPayload();

        // Logic for further processing of the message

        Checkpointer checkpointer = iMessage.getHeaders().get(AzureHeaders.CHECKPOINTER, Checkpointer.class);

        // Upon processing of the message send response success/failure
        boolean success = true;
        if (success) {
            checkpointSuccess(checkpointer, true, messageId);
        } else {
            checkpointFailed(checkpointer, true, messageId);
        }
    }

    @StreamListener(target = MessageStreams.INPUT2, condition = "headers['Label']=='<some_value>'")
    public void messageInput2(Message<String> iMessage) throws InterruptedException {

        // Use similar logic as in messageInput1

    }

    public void checkpointSuccess(Checkpointer checkpointer, Boolean flag, UUID messageId) throws InterruptedException {
        checkpointer.success().handle((r,ex) -> {
            if((ex == null) && flag) {
                LOGGER.info("Message({}) checkpointed as successfully received.", messageId);
                LOGGER.info("--------------------------------------------------------------");
            }
            return null;
        });
    }

    public void checkpointFailed(Checkpointer checkpointer, Boolean flag, UUID messageId) throws InterruptedException {
        checkpointer.failure().handle((r,ex) -> {
            if((ex == null) && flag) {
                LOGGER.info("Message({}) checkpointed as failed to receive.", messageId);
                LOGGER.info("--------------------------------------------------------------");
            }
            return null;
        });
    }

}
