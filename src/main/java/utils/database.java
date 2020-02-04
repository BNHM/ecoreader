package utils;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Creates the connection for the backend bcid database.
 * Settings come from the util.SettingsManager/Property file defining the user/password/url/class
 * for the mysql database where the data lives.
 */
public class database {

    // Mysql Connection
    protected Connection conn;
//    final static Logger logger = LoggerFactory.getLogger(database.class);

    /**
     * Load settings for creating this database connection from the ecoreader.props file
     */
    public database() {
        try {
            SettingsManager sm = SettingsManager.getInstance();
            sm.loadProperties();
            String User = sm.retrieveValue("user");
            String Password = sm.retrieveValue("password");
            String Url = sm.retrieveValue("url");
            String dbClass = sm.retrieveValue("class");

            Class.forName(dbClass);
            conn = DriverManager.getConnection(Url, User, Password);
        } catch (ClassNotFoundException e) {
            throw new ServerErrorException("Server Error","Driver issues accessing database", e);
        } catch (SQLException e) {
            throw new ServerErrorException("Server Error","SQL Exception accessing database", e);
        }

    }

    public Connection getConn() {
        return conn;
    }

    public void close(Statement stmt, ResultSet rs) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
//                logger.warn("SQLException while attempting to close PreparedStatement.", e);
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
//                logger.warn("SQLException while attempting to close ResultSet.", e);
            }
        }
        return;
    }


    public void close(PreparedStatement stmt, ResultSet rs) {
       close((Statement)stmt,rs);
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
//            logger.warn("SQLException while attempting to close connection.", e);
        }
        return;
    }
//
//    /**
//     * Return the userID given a username
//     * @param username
//     * @return
//     */
//    public Integer getUserId(String username) {
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        try {
//            String sql = "Select user_id from users where username=?";
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, username);
//
//            rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                return rs.getInt("user_id");
//            }
//        } catch (SQLException e) {
//            throw new ServerErrorException("Server Error",
//                    "SQLException attempting to getUserId when given the username: {}", e);
//        } finally {
//            close(stmt, rs);
//        }
//        return null;
//    }
//    /**
//     * Return the username given a userId
//     * @param userId
//     * @return
//     */
//    public String getUserName(Integer userId) {
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        try {
//            String sql = "SELECT username FROM users WHERE user_id = ?";
//            stmt = conn.prepareStatement(sql);
//            stmt.setInt(1, userId);
//
//            rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                return rs.getString("username");
//            }
//        } catch (SQLException e) {
//            throw new ServerErrorException("Server Error",
//                    "SQLException attempting to getUserName when given the userId: {}", e);
//        } finally {
//            close(stmt, rs);
//        }
//        return null;
//    }

}
