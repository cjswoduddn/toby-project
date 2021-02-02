package woo.young.tobyproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import woo.young.tobyproject.dao.UserDao;
import woo.young.tobyproject.dao.UserDaoJdbc;
import woo.young.tobyproject.domain.Level;
import woo.young.tobyproject.domain.User;
import woo.young.tobyproject.factory.DaoFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    public static class UserServiceChild extends UserService{
        private String id;
        UserDao userDao;
        UpgradeLevelPolicy upgradeLevelPolicy;
        PlatformTransactionManager ptm;

        public UserServiceChild(UserDao userDao, UpgradeLevelPolicy upgradeLevelPolicy, PlatformTransactionManager ptm, String id) {
            super(userDao, upgradeLevelPolicy, ptm);
            this.id = id;
            this.userDao = userDao;
            this.upgradeLevelPolicy = upgradeLevelPolicy;
            this.ptm = ptm;
        }

        public void setPtm(PlatformTransactionManager ptm){
            this.ptm = ptm;
        }

        @Override
        public void upgradeLevels() throws Exception {
            TransactionStatus status = ptm.getTransaction(new DefaultTransactionDefinition());

            List<User> users = userDao.getAll();
            try{
                for(User user : users){
                    if(user.getId().equals(id)) throw new TestUserServiceException();
                    if(upgradeLevelPolicy.canUpgradeLevel(user)) {
                        upgradeLevelPolicy.upgradeLevel(user);
                        userDao.update(user);
                    }
                }
            }catch (RuntimeException e){
                ptm.rollback(status);
                throw e;
            }
        }

    }
    private static class TestUserServiceException extends RuntimeException {
    }

    UserService userService;
    UserDao userDao;
    UpgradeLevelPolicy upgradeLevelPolicy;
    PlatformTransactionManager ptm;

    User user1;
    User user2;
    User user3;
    User user4;
    User user5;
    List<User> lists = new ArrayList<>();

    @BeforeEach
    void bean(){
        userDao = mock(UserDaoJdbc.class);
        upgradeLevelPolicy = new UpgradeLevelImpl();


        user1 = new User("id1", "name1", "pw1", Level.BASIC, 49, 0, "ma");
        user2 = new User("id2","name2", "pw2", Level.BASIC, 50, 0, "aa");
        user3 = new User("id3","name3", "pw3", Level.SILVER, 50, 29, "f");
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
        when(userDao.getAll()).thenReturn(lists);
        userService.upgradeLevels();
        //then
        checkLevel(user1, Level.BASIC);
        checkLevel(user2, Level.SILVER);
        checkLevel(user3, Level.SILVER);
        checkLevel(user4, Level.GOLD);
        checkLevel(user5, Level.GOLD);
    }

    @Test
    public void add() throws Exception{
        //given
        user1.setLevel(null);
        when(userDao.get(user1.getId())).thenReturn(user1);
        when(userDao.get(user3.getId())).thenReturn(user3);

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
    @Test
    public void upgradeAllOrNothing() throws Exception{
        //given
        when(userDao.getAll()).thenReturn(lists);
        ApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        userDao = ac.getBean(UserDao.class);
        ptm = ac.getBean(PlatformTransactionManager.class);
        userDao.deleteAll();
        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);
        userDao.add(user4);
        userDao.add(user5);
        UserService userService = new UserServiceChild(userDao, upgradeLevelPolicy, ptm, "id3");
        DataSource da = ac.getBean(DataSource.class);
        //when
        //then
        assertThrows(TestUserServiceException.class, ()->userService.upgradeLevels());
        checkLevel(userDao.get(user2.getId()), Level.BASIC);
    }

}