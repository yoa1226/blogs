package com.blog.repo;

import com.blog.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepoTest extends RepoTest {

  @Autowired
  private UserRepo userRepo;

  @Test
  void findByCity() {
    User user = new User().setName("mike").setCity("beijing");
    userRepo.save(user);
    List<User> users = userRepo.findByCity("beijing");
    assertEquals(users.size(), 1);

    user = new User().setName("mike").setCity("shanghai");
    userRepo.save(user);
    users = userRepo.findByCity("beijing");
    assertEquals(users.size(), 1);
  }

}