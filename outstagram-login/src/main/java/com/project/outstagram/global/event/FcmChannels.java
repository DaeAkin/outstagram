package com.project.outstagram.global.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface FcmChannels {

    @Input("fcmChannel")
    SubscribableChannel pushAlert();
}
