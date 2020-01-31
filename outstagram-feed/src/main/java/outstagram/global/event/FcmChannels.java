package outstagram.global.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface FcmChannels {

    @Output("fcmChannel")
//    @OutputCh("fcmChannel")
    MessageChannel pushAlert();
}
