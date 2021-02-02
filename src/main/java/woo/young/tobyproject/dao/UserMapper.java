package woo.young.tobyproject.dao;

import org.springframework.jdbc.core.RowMapper;
import woo.young.tobyproject.domain.Level;
import woo.young.tobyproject.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getString("level")));
        user.setRecommend(rs.getInt("recommend"));
        user.setLogin(rs.getInt("login"));
        return user;
    }
}
