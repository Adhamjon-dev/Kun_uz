package dasturlash.uz.service;

import dasturlash.uz.util.JwtUtil;
import dasturlash.uz.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class EmailSendingService {
    @Value("${spring.mail.username}")
    private String fromAccount;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SmsHistoryService smsHistoryService;
    @Autowired
    private ProfileService profileService;

    public String sendSimpleMessage(String toAccount, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAccount);
        msg.setTo(toAccount);
        msg.setSubject(subject);
        msg.setText(text);
        javaMailSender.send(msg);

        return "Mail was send";
    }

    public void sendRegistrationEmail(String toAccount) {
        Integer smsCode = RandomUtil.fiveDigit();
        String body = "dasturlash.uz partali - ro'yhatdan o'tish uchun tasdiqlash kodi: " + smsCode; // test message
        // ...
        smsHistoryService.save(toAccount, body, String.valueOf(smsCode));
        // ...
        sendSimpleMessage(toAccount, "Complete registration", body);
    }

    public void sendRegistrationEmail2(String toAccount, String name) {
        Integer smsCode = RandomUtil.fiveDigit();
        String body = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "    <style>\n" +
                "        h1 {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        p{\n" +
                "            color: red;\n" +
                "        }\n" +
                "    </style>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Hello dear %s </h1>\n" +
                "<p>Complete you registration code: <b>%d</b></p>\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>"; // test message
        body = String.format(body, name, smsCode);
        // ...
        smsHistoryService.save(toAccount, body, String.valueOf(smsCode));
        // ...
        sendMimeMessage(toAccount, "Complete registration", body);
    }

    public void sendRegistrationEmailLink(String toAccount, String name) {
        String id = profileService.getIdByUsername(toAccount).toString();
        String token =  JwtUtil.encode(id, new LinkedList<>());
        String confirmUrl = "http://localhost:8080/api/v1/auth/registration/email/verification?id=" + token;
        String body = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Email Verification</title>
                <style>
                    h1 { text-align: center; }
                    p { color: red; }
                </style>
            </head>
            <body>
                <h1>Hello dear %s</h1>
                <p>
                    Complete your registration:
                    <b><a href="%s">Click here to verify</a></b>
                </p>
            </body>
            </html>
            """; // test message
        body = String.format(body, name, confirmUrl);
        sendMimeMessage(toAccount, "Complete registration", body);
    }

    public String sendMimeMessage(String toAccount, String subject, String text) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            msg.setFrom(fromAccount);

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(msg);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "Mail was send";
    }
}
