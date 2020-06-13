package com.example.demo.service;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class MailService {

    /**
     * 发件人邮箱地址 chitchat_team@yeah.net
     * @param recipient 用户的邮箱地址
     * @param code      6位数字验证码
     * @throws GeneralSecurityException
     * @throws MessagingException
     */
    public static void sendCodeEmail(String recipient, String code) throws GeneralSecurityException, MessagingException {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.yeah.net");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("chitchat_team@yeah.net", "IKQYPCVPZGVSJAAI");
            }
        });
        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress("chitchat_team@yeah.net"));
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        mimeMessage.setRecipient(Message.RecipientType.CC, new InternetAddress("chitchat_team@yeah.net"));
        mimeMessage.setSubject("欢迎使用ChitChat~");
        mimeMessage.setText("您的验证码是：" + code);
        Transport.send(mimeMessage);
//        MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
//        socketFactory.setTrustAllHosts(true);
//        properties.put("mail.smtp.ssl.enable", "true");
//        properties.put("mail.smtp.ssl.socketFactory", socketFactory);
    }
}
