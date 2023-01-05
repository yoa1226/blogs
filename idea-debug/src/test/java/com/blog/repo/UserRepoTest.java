package com.blog.repo;

import com.blog.model.User;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepoTest extends RepoTest {

  @Autowired
  private UserRepo userRepo;

  @BeforeAll
  public void init() throws SQLException {
    initTable(Lists.fixedSize.of("scripts/create_table_user.sql"));
  }

  @AfterAll
  public void after() throws SQLException {
    dropTable(Lists.fixedSize.of("scripts/drop_table_user.sql"));
  }

  @Test
  void findByCity() {
    User user = new User().setName("mike").setCity("beijing");
    userRepo.save(user);
    FastList<User> users = userRepo.findByCity("beijing");
    assertEquals(users.size(), 1);

    user = new User().setName("mike").setCity("shanghai");
    userRepo.save(user);
    users = userRepo.findByCity("beijing");
    assertEquals(users.size(), 1);

    user = new User().setName("mike").setCity("beijing");
    userRepo.save(user);
    users = userRepo.findByCity("beijing");

    assertEquals(users.size(), 2);
  }

}