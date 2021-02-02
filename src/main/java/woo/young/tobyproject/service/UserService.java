package woo.young.tobyproject.service;


import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
import woo.young.tobyproject.dao.UserDao;
import woo.young.tobyproject.domain.Level;
import woo.young.tobyproject.domain.User;

import java.util.List;

public class UserService {

    private final UserDao userDao;
    private final UpgradeLevelPolicy upgradeLevelPolicy;
    private final PlatformTransactionManager ptm;
    private final MailSender mailSender;


    public UserService(UserDao userDao, UpgradeLevelPolicy upgradeLevelPolicy, PlatformTransactionManager ptm, MailSender mailSender) {
        this.userDao = userDao;
        this.upgradeLevelPolicy = upgradeLevelPolicy;
        this.ptm = ptm;
        this.mailSender = mailSender;
    }

    public void upgradeLevels() throws Exception{
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
