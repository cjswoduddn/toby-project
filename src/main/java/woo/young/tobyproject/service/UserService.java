package woo.young.tobyproject.service;


import woo.young.tobyproject.dao.UserDao;
import woo.young.tobyproject.domain.Level;
import woo.young.tobyproject.domain.User;

import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for(User user : users){
            if(canUpgradeLevel(user))
                upgradeLevel(user);
        }
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        switch (level){
            case BASIC:
                return user.getLogin() >= 50;
            case SILVER:
                return user.getRecommend() >= 30;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level!");
        }
    }

    public void add(User user) {
        if(user.getLevel() == null)
            user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
