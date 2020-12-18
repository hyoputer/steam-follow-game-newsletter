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

public class Mail {

    private static final String EMAIL_ID = System.getenv("EMAIL_ID");
    private static final String EMAIL_PASSWORD = System.getenv("EMAIL_PASSWORD");

    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final int SMTP_PORT = 465;

    public static void sendMail(String title, String content) {
        
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
