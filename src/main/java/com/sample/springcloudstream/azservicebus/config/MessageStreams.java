package com.sample.springcloudstream.azservicebus.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MessageStreams {

    String INPUT1 = "input1";

    String INPUT2 = "input2";

    String OUTPUT = "output";

    @Input(MessageStreams.INPUT1)
    SubscribableChannel input1();

    @Input(MessageStreams.INPUT2)
    SubscribableChannel input2();

    @Output(MessageStreams.OUTPUT)
    MessageChannel output();
}
