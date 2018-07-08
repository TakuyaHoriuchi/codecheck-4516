package recipe.entry;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@PropertySource({"classpath:application.properties"})
public class DataSourceProfiler {
    @Autowired
    UrlSet urlSet;
    
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().url(urlSet.url).build();
    }
    
    @Component
    @ConfigurationProperties(prefix="spring.datasource")
    @Data
    @NoArgsConstructor
    public class UrlSet {
        private String url;
    }

}
