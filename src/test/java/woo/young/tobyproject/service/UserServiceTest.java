package woo.young.tobyproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import woo.young.tobyproject.dao.UserDao;
import woo.young.tobyproject.dao.UserDaoJdbc;
import woo.young.tobyproject.domain.Level;
import woo.young.tobyproject.domain.User;
import woo.young.tobyproject.factory.DaoFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    UserService userService;
    UserDao userDao;

    User user1;
    User user2;
    User user3;
    User user4;
    User user5;
    List<User> lists = new ArrayList<>();

    @BeforeEach
    void bean(){
        userDao = mock(UserDaoJdbc.class);
        userService = new UserService(userDao);


        user1 = new User("id1", "name1", "pw1", Level.BASIC, 49, 0);
        user2 = new User("id2","name2", "pw2", Level.BASIC, 50, 0);
        user3 = new User("id3","name3", "pw3", Level.SILVER, 50, 29);
        user4 = new User("ij4","name3", "pw3", Level.SILVER, 50, 30);
        user5 = new User("id5","name3", "pw3", Level.GOLD, 0, 0);
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

}