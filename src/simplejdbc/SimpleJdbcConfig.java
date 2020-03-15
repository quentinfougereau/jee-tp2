package simplejdbc;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configurable
@ComponentScan(basePackages = "simplejdbc")
@PropertySource("classpath:config.properties")
public class SimpleJdbcConfig {

    @Bean
    // Définition des paramètres d'accès à la BD
    public DataSource handMadeDataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://localhost/testdb");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    @Bean
    // Création d'une instance de JdbcTemplate
    public JdbcTemplate myJdbcTemplate(@Autowired DataSource ds) {
        var t = new JdbcTemplate(ds);
        return t;
    }

    @Bean
    // Créer une dataSource à partir des propriétés
    public DataSource paramDataSource(
            @Value("${jdbc.url}") String url,
            @Value("${jdbc.user}") String user,
            @Value("${jdbc.password}") String password
    ) {
        var dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

// Définition des paramètres d'accès à la BD
    @Bean
    @Primary
    public DataSource dbcp2DataSource() {
        var dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost/testdb");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setInitialSize(5); // ouvrir cinq connexions
        dataSource.setMaxTotal(5); // pas plus de cinq connexions
        return dataSource;
    }

}