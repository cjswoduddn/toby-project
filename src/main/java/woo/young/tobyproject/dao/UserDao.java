package woo.young.tobyproject.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import woo.young.tobyproject.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDao {

    private final RowMapper<User> mapper;
    private final JdbcTemplate jdbcTemplate;

    public UserDao(RowMapper<User> mapper, JdbcTemplate jdbcTemplate) {
        this.mapper = mapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(User user) throws ClassNotFoundException, SQLException{
        String query = "insert into users(id, name, password) values(?, ?, ?)";
        jdbcTemplate.update(query, user.getId(), user.getName(), user.getPassword());
    }

    public void deleteAll() throws SQLException{
        jdbcTemplate.update("delete from users");
    }

    public List<User> getAll(){
        return jdbcTemplate.query("select * from users", this.mapper);
    }

    public User get(String id) throws ClassNotFoundException, SQLException{
        return
                jdbcTemplate.queryForObject("select * from users where id = ?",
                        new Object[]{id},
                        this.mapper
                );

    }


    public int getCount() throws SQLException{
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

}
