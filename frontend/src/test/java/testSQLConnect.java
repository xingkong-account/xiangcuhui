import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class testSQLConnect {
    @Test
    public void test() {
        String url = "jdbc:mysql://localhost:3306/xiangcuhui?characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "407454xl..";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                System.out.println("Success!");
            } else {
                System.out.println("Failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
