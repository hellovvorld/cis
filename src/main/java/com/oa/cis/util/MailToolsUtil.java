package com.oa.cis.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 消息邮件工具
 *
 * @author v
 */
public class MailToolsUtil implements Runnable {
    // 收件人邮箱
    private String email;
    // 日期
    private String date;
    // 消息内容
    private String content;

    public MailToolsUtil(String email, String date, String content) {
        this.email = email;
        this.content = content;
        this.date = date;
    }

    public void run() {
        // 1.创建连接对象javax.mail.Session
        // 2.创建邮件对象 javax.mail.Message
        // 3.发送一封激活邮件
        String from = "1091154713@qq.com";// 发件人电子邮箱
        String host = "smtp.qq.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)

        Properties properties = System.getProperties();// 获取系统属性
        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.smtp.auth", "true");// 打开认证

        try {
            //QQ邮箱需要下面这段代码，163邮箱不需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            // 1.获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("1091154713@qq.com", "ocxddbzgojqxjibb"); // 发件人邮箱账号、授权码
                }
            });
            // 2.创建邮件对象
            Message message = new MimeMessage(session);
            // 2.1设置发件人
            message.setFrom(new InternetAddress(from));
            // 2.2设置接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 2.3设置邮件主题
            message.setSubject("系统消息");
            // 2.4设置邮件内容
            message.setContent(GlobalStaticHtml.EMAILMESSAGESTART + date + GlobalStaticHtml.EMAILMESSAGECENTER + content + GlobalStaticHtml.EMAILMESSAGEEND, "text/html;charset=UTF-8");
            // 3.发送邮件
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}