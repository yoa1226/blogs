package com.blog.repo;

import com.blog.model.User;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

//  List<User> findByCity(String city);

  FastList<User> findByCity(String city);
}
