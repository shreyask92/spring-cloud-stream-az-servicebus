package com.sample.springcloudstream.azservicebus;

import com.sample.springcloudstream.azservicebus.config.MessageStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@EnableBinding(MessageStreams.class)
@RestController
public class TopicPublisher {

    @Autowired
    private MessageStreams source;

    @PostMapping("/publishMessage")
    public void postMessage(@RequestHeader("Label") String label,
                            @RequestBody String message) {
        this.source.output().send(MessageBuilder.withPayload(message)
                .setHeader("Label", label)
                .build());
    }

}
