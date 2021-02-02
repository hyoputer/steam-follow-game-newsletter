package me.hyoputer;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// 출처 https://ktko.tistory.com/entry/JAVA-SMTP%EC%99%80-Mail-%EB%B0%9C%EC%86%A1%ED%95%98%EA%B8%B0Google-Naver

public class Mail {

    private String EMAIL_ID;
    private String EMAIL_PASSWORD;

    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final int SMTP_PORT = 465;

    public Mail(String id, String password) {
        EMAIL_ID = id;
        EMAIL_PASSWORD = password;
    }

    public void sendMail(String title, String content) {
        
        Properties prop = new Properties();
        prop.put("mail.smtp.host", SMTP_SERVER); 
        prop.put("mail.smtp.port", SMTP_PORT); 
        prop.put("mail.smtp.auth", "true"); 
        prop.put("mail.smtp.ssl.enable", "true"); 
        prop.put("mail.smtp.ssl.trust", SMTP_SERVER);
        
        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_ID, EMAIL_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_ID));

            //수신자메일주소
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(EMAIL_ID)); 

            // Subject
            message.setSubject(title); //메일 제목을 입력

            // Text
            message.setText(content);    //메일 내용을 입력

            // send the message
            Transport.send(message); ////전송
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
