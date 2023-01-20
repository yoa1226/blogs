package com.blog.db;

import blog.model.User;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JdbcTest {

  private static final String DRIVER = "org.h2.Driver";
  private static final String URL = "jdbc:h2:~/test";
  private static final String USER = "sa";
  private static final String PASSWORD = "sa";

  private static Connection conn;

  static {
    try {
      //这里不用加载也行，获取连接的时候会自动加载
      Class.forName(DRIVER);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Connection getConnection() {
    try {
      return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @BeforeAll
  @SuppressWarnings({"SqlResolve", "SqlIdentifier"})
  public static void createTable() throws SQLException {
    String createSql = """
           create table user (
           id bigint generated by default as identity,
           city varchar(255),
           name varchar(255),
           primary key (id));
        """;
    conn = getConnection();
    Statement st = conn.createStatement();
    st.executeUpdate(createSql);
    st.close();
  }

  @AfterAll
  @SuppressWarnings({"SqlResolve", "SqlIdentifier"})
  public static void dropTable() throws SQLException {
    String dropSql = " drop table if exists user CASCADE ; ";
    Statement st = conn.createStatement();
    st.executeUpdate(dropSql);
    st.close();
    conn.close();
  }

  @Test
  @SuppressWarnings({"SqlResolve", "SqlIdentifier"})
  public void insert() throws SQLException {
    String insertSql = "insert into user (id, city, name) values (default, 'wuhan', 'mike')";
    Statement st = conn.createStatement();
    st.executeUpdate(insertSql);

    String selectSql = "select id, city , name from user";
    ResultSet rs = st.executeQuery(selectSql);

    FastList<User> userList = FastList.newList(1);

    while (rs.next()) {
      User user = new User().setId(rs.getInt("id"))
          .setName(rs.getString("name"))
          .setCity(rs.getString("city"));
      userList.add(user);
    }
    assertEquals(1, userList.size());
    assertNotNull(userList.getFirst());
    assertEquals(1, userList.getFirst().getId());
    assertEquals("wuhan", userList.getFirst().getCity());
    assertEquals("mike", userList.getFirst().getName());

    st.close();
  }

}
