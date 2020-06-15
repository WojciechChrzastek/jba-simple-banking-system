package banking;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHandler {

  public void createNewDatabase(String fileName) {
    String url = "jdbc:sqlite:" + fileName;
    try (Connection conn = DriverManager.getConnection(url)) {
      if (conn != null) {
        DatabaseMetaData meta = conn.getMetaData();
//        System.out.println("The driver name is " + meta.getDriverName());
//        System.out.println("A new database has been created.");
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public Connection connect(String dbName) {
    Connection conn = null;
    try {
      String url = "jdbc:sqlite:" + dbName;
      conn = DriverManager.getConnection(url);
//      System.out.println("Connection to SQLite has been established.");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return conn;
  }

  public void createNewTable(String dbName) {
    String url = "jdbc:sqlite:" + dbName;

    String createTable = "CREATE TABLE IF NOT EXISTS card ("
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "	number TEXT,"
            + "	pin TEXT,"
            + "	balance INTEGER DEFAULT 0"
            + ");";

    try (Connection conn = DriverManager.getConnection(url);
         Statement stmt = conn.createStatement()) {
      stmt.execute(createTable);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void insert(Connection conn, String cardNumber, String pin) {
    String sql = "INSERT INTO card(number, pin) VALUES(?, ?);";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, cardNumber);
      pstmt.setString(2, pin);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void closeConnection(Connection conn) {
    try {
      if (conn != null) {
        conn.close();
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}