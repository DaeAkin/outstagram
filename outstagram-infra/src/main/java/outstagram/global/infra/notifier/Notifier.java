package outstagram.global.infra.notifier;

public interface Notifier {
    interface EmailNotifier {
        boolean sendMail(String target, String subject, String content);
    }

}
