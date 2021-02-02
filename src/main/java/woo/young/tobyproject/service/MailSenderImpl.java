package woo.young.tobyproject.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import woo.young.tobyproject.domain.User;

public class MailSenderImpl implements MailSender{

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
    }

    public void sendUpgradeEMail(User user) {
        System.out.println("hello");
    }
}

