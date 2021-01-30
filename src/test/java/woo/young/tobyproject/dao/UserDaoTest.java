package woo.young.tobyproject.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import woo.young.tobyproject.domain.User;
import woo.young.tobyproject.exception.NoGetDadaException;
import woo.young.tobyproject.factory.DaoFactory;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    // fixture
    ApplicationContext ac;
    UserDao userDao;
    User user1;
    User user2;
    User user3;

    @BeforeEach
    void before() throws Exception{
        ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        userDao = ac.getBean(UserDao.class);
        user1 = new User("id1", "name1", "pw1");
        user2 = new User("id2","name2", "pw2");
        user3 = new User("id3","name3", "pw3");
        userDao.deleteAll();
    }

    @Test
    public void addAndGet() throws Exception{
        //given
        int count = userDao.getCount();
        assertEquals(count, 0);

        //when
        userDao.add(user1);
        count = userDao.getCount();
        User test = userDao.get(user1.getId());

        //then
        assertEquals(count, 1);
        assertEquals(user1.getName(), test.getName());
        assertEquals(user1.getPassword(), test.getPassword());
        assertEquals(user1.getId(), test.getId());
    }
    @Test
    public void duplication() throws Exception{
        //given
        userDao.add(user1);
        //when
        assertThrows(SQLException.class, ()->userDao.add(user1));

        //then
    }

    @Test
    void NoGetData() throws Exception{
        assertThrows(NoGetDadaException.class, ()->userDao.get("hello"));

    }

    @Test
    public void deleteAll() throws Exception{
        //given
        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);
        int count = userDao.getCount();
        assertEquals(count, 3);

        //when
        userDao.deleteAll();
        count = userDao.getCount();

        //then
        assertEquals(count, 0);
    }

}