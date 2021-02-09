package woo.young.tobyproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
import woo.young.tobyproject.dao.UserDao;
import woo.young.tobyproject.domain.Level;
import woo.young.tobyproject.domain.User;
import woo.young.tobyproject.factory.DaoFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserServiceImpl userServiceImpl;
    UserDao userDao;
    UpgradeLevelPolicy upgradeLevelPolicy;
    PlatformTransactionManager ptm;
    UserService userService;
    ApplicationContext ac;
    MailSender mailSender;

    User user1;
    User user2;
    User user3;
    User user4;
    User user5;
    List<User> lists = new ArrayList<>();

    @BeforeEach
    void bean(){
        ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        userDao = ac.getBean(UserDao.class);
//        userService = ac.getBean("proxyUserService", UserService.class);
//        userService = ac.getBean("userServiceTx", UserService.class);
//        userService = ac.getBean("userService", UserService.class);
        userService = ac.getBean("userServiceFactory", UserService.class);


        userDao.deleteAll();

        user1 = new User("id6", "name1", "pw1", Level.BASIC, 49, 0, "ma");
        user2 = new User("id2","name2", "pw2", Level.BASIC, 50, 0, "aa");
        user3 = new User("id7","name3", "pw3", Level.SILVER, 50, 29, "f");
        user4 = new User("id4","name3", "pw3", Level.SILVER, 50, 30, "f");
        user5 = new User("id5","name3", "pw3", Level.GOLD, 0, 0, "f");
        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);
        userDao.add(user4);
        userDao.add(user5);
    }

    @Test
    public void update() throws Exception{
        assertThrows(RuntimeException.class, ()->{userService.upgradeLevels();});
        User user = userDao.get(user2.getId());
        assertEquals(user.getLevel(), Level.BASIC);
    }

    void checkLevel(User user, Level level){
        assertEquals(user.getLevel(), level);
    }




}