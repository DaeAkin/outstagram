package outstagram.global.infra.notifier;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class EmailNotifier implements  Notifier.EmailNotifier {
//
//    final JavaMailSender javaMailSender;
//
//    //TODO @Async vs Mono or Fluy differnce
//    @Override
//    public boolean sendMail(String target, String subject, String content)
//    {
//        try{
//            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//            simpleMailMessage.setTo(target);
//            simpleMailMessage.setSubject(subject);
//            simpleMailMessage.setText(content);
//            simpleMailMessage.setFrom("no-reply@outstagram.com");
//
//            log.info("Email 전송 요청 : " + simpleMailMessage.toString());
//            //테스트 환경에서는 전송 안함.
////            if("test".equals(activeProfile)){
////                return true;
////            }
//            javaMailSender.send(simpleMailMessage);
//
//        } catch(Exception e){
//            log.error("Email 전송 실패 : " + e.getMessage());
//            return false;
//        }
//        return true;
//    }
//}

