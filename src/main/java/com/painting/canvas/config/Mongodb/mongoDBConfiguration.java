package com.painting.canvas.config.Mongodb;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class mongoDBConfiguration extends AbstractMongoClientConfiguration {


    @Override
    protected String getDatabaseName() {
        return "Mongo";
    }
}
