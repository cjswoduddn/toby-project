package woo.young.tobyproject.service;


import org.springframework.mail.MailSender;
import woo.young.tobyproject.dao.UserDao;
import woo.young.tobyproject.domain.Level;
import woo.young.tobyproject.domain.User;

import java.util.List;

public class UserServiceImpl implements UserService{

    private final UserDao userDao;
    private final UpgradeLevelPolicy upgradeLevelPolicy;
    private final MailSender mailSender;


    public UserServiceImpl(UserDao userDao, UpgradeLevelPolicy upgradeLevelPolicy, MailSender mailSender) {
        this.userDao = userDao;
        this.upgradeLevelPolicy = upgradeLevelPolicy;
        this.mailSender = mailSender;
    }

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (upgradeLevelPolicy.canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private void upgradeLevel(User user) {
        upgradeLevelPolicy.upgradeLevel(user);
        userDao.update(user);
        ((MailSenderImpl)mailSender).sendUpgradeEMail(user);
    }

    public void add(User user) {
        if(user.getLevel() == null)
            user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
