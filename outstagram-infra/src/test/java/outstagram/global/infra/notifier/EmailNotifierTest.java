package outstagram.global.infra.notifier;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.IntergrationTest;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailNotifierTest extends IntergrationTest {


    @Autowired
    Notifier.EmailNotifier emailNotifier;

    private static final String target = "mindh890@gmail.com";


    //직접 확인
    @Test
    public void 이메일_전송_테스트() {
        //given
        String subject = "이메일 전송 테스트";
        String content = "이메일 내용";

        //when
        boolean result = emailNotifier.sendMail(target, subject, content);

        //then
        assertThat(result).isTrue();

    }
}