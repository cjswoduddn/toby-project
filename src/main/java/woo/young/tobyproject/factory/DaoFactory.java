package woo.young.tobyproject.factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import woo.young.tobyproject.dao.JdbcContext;
import woo.young.tobyproject.dao.UserDaoJdbc;
import woo.young.tobyproject.dao.UserMapper;
import woo.young.tobyproject.domain.User;
import woo.young.tobyproject.service.*;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    @Bean
    public UserDaoJdbc userDao(){
        return new UserDaoJdbc(userMapper(), jdbcTemplate());
    }

    @Bean
    public RowMapper<User> userMapper(){
        return new UserMapper();
    }
    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
    @Bean
    public JdbcContext jdbcContext(){
        return new JdbcContext(dataSource());
    }
    @Bean(name = "userService")
    UserService userService(){
        return new UserServiceImpl(userDao(), upgradeLevelPolicy(), mailSender());
    }
    @Bean
    UpgradeLevelPolicy upgradeLevelPolicy(){
        return new UpgradeLevelImpl();
    }
    @Bean
    MailSender mailSender(){
        return new MailSenderImpl();
    }
    @Bean
    PlatformTransactionManager platformTransactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }
    @Bean(name = "userServiceTx")
    UserService userServiceTx(){
        return new UserServiceTx(userService(), platformTransactionManager());}

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/backapi");
        dataSource.setUsername("cjswo");
        dataSource.setPassword("duddn");
        return dataSource;
    }
}
