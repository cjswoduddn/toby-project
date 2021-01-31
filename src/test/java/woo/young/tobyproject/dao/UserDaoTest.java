package woo.young.tobyproject.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import woo.young.tobyproject.domain.User;
import woo.young.tobyproject.exception.DuplicateUserIdException;
import woo.young.tobyproject.factory.DaoFactory;

import java.util.List;

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
        userDao = ac.getBean(UserDaoJdbc.class);
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
        assertThrows(DuplicateKeyException.class, ()->userDao.add(user1));
//        assertThrows(DuplicateUserIdException.class, () -> userDao.add(user1));

        //then
    }

    @Test
    void NoGetData() throws Exception{
        assertThrows(EmptyResultDataAccessException.class, ()->userDao.get("hello"));

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
    @Test
    public void getAll() throws Exception{
        //given
        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);

        //when
        List<User> list = userDao.getAll();

        //then
        checkSameUser(list.get(0), user1);
        checkSameUser(list.get(1), user2);
        checkSameUser(list.get(2), user3);
    }

    @Test
    void noItemGetAll() throws Exception{
        List<User> all = userDao.getAll();
        assertEquals(all.size(), 0);
    }

    public void checkSameUser(User user1, User user2) throws Exception{
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
    }

}