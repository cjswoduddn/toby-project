package woo.young.tobyproject.service;

import woo.young.tobyproject.dao.UserDao;
import woo.young.tobyproject.domain.Level;
import woo.young.tobyproject.domain.User;

public class UpgradeLevelImpl implements UpgradeLevelPolicy{


    @Override
    public boolean canUpgradeLevel(User user) {
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

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
    }
}
