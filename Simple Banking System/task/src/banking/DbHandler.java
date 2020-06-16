package banking;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

  public void insertNewCard(Connection conn, String cardNumber, String pin) {
    String sql = "INSERT INTO card(number, pin) VALUES(?, ?);";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, cardNumber);
      pstmt.setString(2, pin);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public int checkBalance(Connection conn, String loggedCardNumber) {
    int balance = 0;
    String sql = "SELECT balance FROM card WHERE number = ?;";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, loggedCardNumber);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        balance = rs.getInt("balance");
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return balance;
  }

  public boolean hasCard(Connection conn, String cardNumber, String pin) {
    String sql = "SELECT * FROM card WHERE number = ? AND pin = ?;";
    int rowCount = 0;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, cardNumber);
      pstmt.setString(2, pin);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        rowCount++;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return rowCount == 1;
  }

  public void deleteCard(Connection conn, String cardNumber) {
    String sql = "DELETE FROM card WHERE number = ?;";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, cardNumber);
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

  public void updateBalance(Connection conn, String cardNumber, int income) {
    String sql = "UPDATE card SET balance = (balance + ?) WHERE number = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, income);
      pstmt.setString(2, cardNumber);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
