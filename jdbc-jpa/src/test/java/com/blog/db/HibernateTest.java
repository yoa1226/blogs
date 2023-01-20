package com.blog.db;

import blog.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HibernateTest {

  private static SessionFactory sessionFactory;

  @BeforeAll
  public static void init() {
    // A SessionFactory is set up once for an application!
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
        .configure() // configures settings from hibernate.cfg.xml
        .build();
    try {
      sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    } catch (Exception e) {
      // The registry would be destroyed by the SessionFactory,
      // but we had trouble building the SessionFactory, so destroy it manually.
      StandardServiceRegistryBuilder.destroy(registry);
    }
  }

  @AfterAll
  public static void afterAll() {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }

  @SuppressWarnings("JpaQlInspection")
  @Test
  public void Insert() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    session.persist(new User().setName("mike").setCity("wuhan"));
    session.getTransaction().commit();
    session.close();

    session = sessionFactory.openSession();
    session.beginTransaction();
    Query<User> query = session.createQuery("from User", User.class);

    List<?> result = query.list();
    Assertions.assertEquals(1, result.size());
    session.getTransaction().commit();
    session.close();
  }
}
