package ke.co.mspace.staffmanagement.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;

public class EmailUtil {

    private static final String SENDER_EMAIL;
    private static final String APP_PASSWORD;

    static {
        String email = "bazu7642@gmail.com";
        String password = "robz ijfk dibr fhaz";
        try (InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (is != null) {
                Properties p = new Properties();
                p.load(is);
                email = p.getProperty("mail.sender.email", email);
                password = p.getProperty("mail.sender.password", password);
            }
        } catch (Exception e) {
            System.err.println("Could not load email.properties, using defaults: " + e.getMessage());
        }
        SENDER_EMAIL = email;
        APP_PASSWORD = password;
    }

    public static void sendReminderEmail(String recipientEmail, String subject, String messageText) {
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);
            System.out.println("Email reminder sent successfully to: " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email reminder to: " + recipientEmail);
        }
    }
}
