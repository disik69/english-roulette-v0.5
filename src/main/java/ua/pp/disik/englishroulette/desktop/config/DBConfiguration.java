package ua.pp.disik.englishroulette.desktop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("persistence.properties")
public class DBConfiguration {
    private final Environment env;

    public DBConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource(@Value("${dbFilename:}") String urlFileName) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("driverClassName"));
        dataSource.setUrl(env.getProperty("urlPrefix") + urlFileName);
        return dataSource;
    }
}