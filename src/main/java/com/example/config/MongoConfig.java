package com.example.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan(basePackages = "com.example")
@EnableMongoRepositories(basePackages = "com.example.database")
public class MongoConfig extends AbstractMongoClientConfiguration {

    // nazwa bazy danych z Atlasa
    @Override
    protected String getDatabaseName() {
        return "grawgo_db";
    }

    // polaczenie sie z MongoDB Atlas
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://admin:tajneHaslo123@cluster0.abcde.mongodb.net/?retryWrites=true&w=majority");
    }
    
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
