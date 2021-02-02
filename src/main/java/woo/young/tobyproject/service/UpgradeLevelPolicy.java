package woo.young.tobyproject.service;

import woo.young.tobyproject.domain.User;

public interface UpgradeLevelPolicy {

    public boolean canUpgradeLevel(User user);
    public void upgradeLevel(User user);
}
