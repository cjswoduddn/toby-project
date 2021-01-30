package woo.young.tobyproject.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {

    private final DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void jdbcContextWithStatementStrategy(Strategy strategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = strategy.makePreparedStatement(c);
            ps.executeUpdate();
        }catch(SQLException e){
            throw e;
        }finally {
            if(ps != null){
                try {
                    ps.close();
                }catch (SQLException e){
                }
            }
            if(c != null){
                try {
                    c.close();
                }catch (SQLException e){
                }
            }
        }
    }
    public void executeSql(final String query, Object... objects) throws SQLException {
        jdbcContextWithStatementStrategy(new Strategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement(query);
                for(int i = 0; i < objects.length; i++){
                    ps.setString(i+1, (String)objects[i]);
                }
                return ps;
            }
        });
    }

    private static interface Strategy{
        public PreparedStatement makePreparedStatement(Connection c) throws SQLException;
    }

}
