package ua.pp.disik.englishroulette.desktop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("persistence.properties")
public class DBConfiguration {
    @Bean
    public DataSource dataSource(
            @Value("${db.driverClassName:}") String driverClassName,
            @Value("${db.urlPrefix:}") String urlPrefix,
            @Value("${db.filename:}") String urlFilename
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(urlPrefix + urlFilename);
        return dataSource;
    }
}