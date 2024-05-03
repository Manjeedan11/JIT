package com.affc.ait.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailHandler extends AsyncTask<Integer, Void, Boolean>{

    private static String sender = "johnadrian654@gmail.com";
    private static String password = "Nopapa@no69";
    private static String stringHost = "smtp.gmail.com";
    private String receiver;
    private String subject;
    private boolean isCode;

    public EmailHandler(String receiver, String subject, boolean isCode){
        this.receiver = receiver;
        this.subject = subject;
        this.isCode = isCode;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", stringHost);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            if (isCode) {
                mimeMessage.setSubject("Your Confirmation Code");
                mimeMessage.setText("Your confirmation code is: " + subject);
            } else {
                mimeMessage.setSubject("Congrats on enrolling!");
                mimeMessage.setText(subject);
            }
            Transport.send(mimeMessage);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Log.e("TAG", "sent successfully: " + receiver + " " + subject);
        } else {
            Log.e("TAG", "failed to send");
        }
    }


}
