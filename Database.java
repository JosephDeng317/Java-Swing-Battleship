// Database class handles all things related to the sqlite database

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

class Database {
  /*
   * Connect to a sample database named table.db
   */
  public static Connection connect() {
    // SQLite connection string
    String url = "jdbc:sqlite:table.db";
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return conn;
  }

  /*
   * creates table
   */
  public static void Create() {
    String sql = "CREATE TABLE leaderboard " +
        "(Nickname TEXT NOT NULL," +
        " Score INTEGER NOT NULL)";

    try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.executeUpdate();
      pstmt.close();
      conn.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /*
   * insert into student table
   */
  public static void Insert(String nickname, Integer score) {
    // using REPLACE INTO instead of INSERT INTO to avoid errors
    String sql = "REPLACE INTO leaderboard(Nickname,Score) VALUES(?,?)";

    try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, nickname);
      pstmt.setInt(2, score);
      pstmt.executeUpdate();
      pstmt.close();
      conn.close();
    } catch (SQLException se) {
      System.out.println(se.getMessage());
    }
  }

  /*
   * selects entries from database and orders them by score
   */
  public static List<List<String>> Select() {
    List<List<String>> ResultList = new ArrayList<List<String>>();
    String query = "SELECT * FROM leaderboard ORDER BY Score";

    // create the preparedstatement
    try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(query)) {
      // process the results
      ResultSet rs = pstmt.executeQuery();

      // Stores properties of a ResultSet object, including column count
      ResultSetMetaData rsmd = rs.getMetaData();
      int columnCount = rsmd.getColumnCount();
      // int rowCount = rs.getInt("recordCount");

      while (rs.next()) {
        ArrayList<String> RowList = new ArrayList<String>();
        int i = 1;
        while (i <= columnCount) {
          RowList.add(rs.getString(i++));
        }
        ResultList.add(RowList);
      }
      rs.close();
      pstmt.close();
      conn.close();
    } catch (SQLException se) {
      System.out.print(se.getMessage());
    }
    return ResultList;
  }
}