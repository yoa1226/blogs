package com.blog.repo;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.CompositeDatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@EnableJpaRepositories(basePackages = "com.blog.repo")
@EntityScan(basePackages = "com.blog.model")
@DataJpaTest(properties = {
    "spring.test.database.replace=NONE",
    "spring.datasource.url= jdbc:postgresql://localhost:5432/blogs",
    "spring.datasource.name= postgres",
    "spring.datasource.password= postgres"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepoTest {

  @Autowired
  protected DataSource dataSource;

  protected void initTable(List<String> filePaths) throws SQLException {
    this.executeSql(filePaths);
  }

  protected void dropTable(List<String> filePaths) throws SQLException {
    this.executeSql(filePaths);
  }

  private void executeSql(List<String> filePaths) throws SQLException {
    var populator = new CompositeDatabasePopulator();
    filePaths.forEach(each -> populator.addPopulators(addPopulators(each)));
    populator.populate(dataSource.getConnection());
  }

  protected DatabasePopulator addPopulators(String filePath) {
    return new ResourceDatabasePopulator(new ClassPathResource(filePath));
  }
}
