package woo.young.tobyproject.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import woo.young.tobyproject.domain.User;

public class UserServiceTx implements UserService{

    private final UserService userService;
    private final PlatformTransactionManager ptm;

    public UserServiceTx(UserService userService, PlatformTransactionManager ptm) {
        this.userService = userService;
        this.ptm = ptm;
    }

    @Override
    public void upgradeLevels() {
        TransactionStatus status = ptm.getTransaction(new DefaultTransactionDefinition());
        try {
            userService.upgradeLevels();
        }catch (RuntimeException e){
            ptm.rollback(status);
            throw e;
        }
    }

    @Override
    public void add(User user) {
        userService.add(user);
    }
}
