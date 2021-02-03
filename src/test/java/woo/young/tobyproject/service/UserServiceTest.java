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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    public static class MockUserDao implements UserDao{

        private List<User> users;
        private List<User> updated = new ArrayList<>();

        public MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<User> getAll() {
            return users;
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }
    }

    public static class UserServiceChild extends UserServiceImpl {
        private String id;
        UserDao userDao;
        UpgradeLevelPolicy upgradeLevelPolicy;
        MailSender mailSender;

        public UserServiceChild(UserDao userDao, UpgradeLevelPolicy upgradeLevelPolicy, MailSender mailSender, String id) {
            super(userDao, upgradeLevelPolicy, mailSender);
            this.id = id;
            this.userDao = userDao;
            this.upgradeLevelPolicy = upgradeLevelPolicy;
        }


        @Override
        public void upgradeLevels() {
            List<User> users = userDao.getAll();
            for(User user : users){
                if(user.getId().equals(id)) throw new TestUserServiceException();
                if(upgradeLevelPolicy.canUpgradeLevel(user)) {
                    upgradeLevelPolicy.upgradeLevel(user);
                    userDao.update(user);
                }
            }
        }

    }
    private static class TestUserServiceException extends RuntimeException {
    }

    UserServiceImpl userServiceImpl;
    UserServiceChild userServiceChild;
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
        upgradeLevelPolicy = ac.getBean(UpgradeLevelPolicy.class);
        mailSender = ac.getBean(MailSender.class);
        ptm = ac.getBean(PlatformTransactionManager.class);
        userServiceChild = new UserServiceChild(userDao, upgradeLevelPolicy, mailSender, "2");

        user1 = new User("id6", "name1", "pw1", Level.BASIC, 49, 0, "ma");
        user2 = new User("id2","name2", "pw2", Level.BASIC, 50, 0, "aa");
        user3 = new User("id7","name3", "pw3", Level.SILVER, 50, 29, "f");
        user4 = new User("id4","name3", "pw3", Level.SILVER, 50, 30, "f");
        user5 = new User("id5","name3", "pw3", Level.GOLD, 0, 0, "f");
        lists.add(user1);
        lists.add(user2);
        lists.add(user3);
        lists.add(user4);
        lists.add(user5);
    }

    @Test
    public void update() throws Exception{
        //given
        userService = new UserServiceTx(userServiceChild, ptm);
        userService.upgradeLevels();
        //then
        checkLevel(user1, Level.BASIC);
        checkLevel(user2, Level.BASIC);
        checkLevel(user3, Level.SILVER);
        checkLevel(user4, Level.SILVER);
        checkLevel(user5, Level.GOLD);
    }

    @Test
    public void add() throws Exception{
        //given
        user1.setLevel(null);
        userService = new UserServiceTx(userServiceChild, ptm);

        //when
        userService.add(user1);
        userService.add(user3);

        //then
        assertEquals(userDao.get(user1.getId()).getLevel(), Level.BASIC);
        assertEquals(userDao.get(user3.getId()).getLevel(), Level.SILVER);
    }

    void checkLevel(User user, Level level){
        assertEquals(user.getLevel(), level);
    }




}