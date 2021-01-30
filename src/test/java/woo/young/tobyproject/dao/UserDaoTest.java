package woo.young.tobyproject.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import woo.young.tobyproject.domain.User;
import woo.young.tobyproject.factory.DaoFactory;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    ApplicationContext ac;
    UserDao userDao;

    @BeforeEach
    void before() throws Exception{
        ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        userDao = ac.getBean(UserDao.class);

        User user = new User();
        user.setId("id");
        user.setName("name");
        user.setPassword("pw");

        userDao.add(user);
        System.out.println("user = " + user);

        User user1 = userDao.get(user.getId());
        System.out.println(user1.getName());
    }

    @Test
    public void daotest() throws Exception{
        //given

        //when

        //then
    }

}