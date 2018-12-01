package com.github.bael.challenge.webstat;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableReactiveMongoRepositories
public class WebstatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebstatApplication.class, args);
    }


//	@Override
//	protected String getDatabaseName() {
//		return "reactive";
//	}
//
//	@Override
//	@Bean
//	public MongoClient reactiveMongoClient() {
//		return MongoClients.create();
//	}
}
