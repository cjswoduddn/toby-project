package woo.young.tobyproject.dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import woo.young.tobyproject.domain.User;
import woo.young.tobyproject.exception.DuplicateUserIdException;

import java.sql.*;
import java.util.List;

public class UserDaoJdbc implements UserDao{

    private final RowMapper<User> mapper;
    private final JdbcTemplate jdbcTemplate;

    public UserDaoJdbc(RowMapper<User> mapper, JdbcTemplate jdbcTemplate) {
        this.mapper = mapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(User user){
        String query = "insert into users(id, name, password, level, login, recommend) values(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, user.getId(), user.getName(), user.getPassword(),
                user.getLevel().toString(), user.getLogin(), user.getRecommend());
    }

    public void deleteAll() {
        jdbcTemplate.update("delete from users");
    }

    public List<User> getAll(){
        return jdbcTemplate.query("select * from users", this.mapper);
    }

    public User get(String id){
        return
                jdbcTemplate.queryForObject("select * from users where id = ?",
                        new Object[]{id},
                        this.mapper
                );

    }


    public int getCount(){
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(
                "update users set name = ?, password = ?, level = ?, login = ?," +
                        "recommend = ? where id = ? ", user.getName(),
                user.getPassword(), user.getLevel().toString(), user.getLogin(), user.getRecommend(),
                user.getId()
        );
    }

}
