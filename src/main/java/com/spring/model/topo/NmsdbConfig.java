package com.spring.model.topo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.spring.util.GetConfig;
@Configuration
@Component

public class NmsdbConfig extends AbstractMongoConfiguration {

	
	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		GetConfig getConfig = new GetConfig("config.properties");
		String database = getConfig.getValue("database");
		return database;
	}

	@Override
	@Bean
	public MongoClient mongo() throws Exception {
		// TODO Auto-generated method stub
		MongoClient mongoClient = null;
		GetConfig getConfig = new GetConfig("config.properties");
System.out.println(getConfig.getValue("ip"));
System.out.println(getConfig.getValue("port"));

		try {
			//mongoClient = new MongoClient(new ServerAddress(getConfig.getValue("ip"),new Integer(getConfig.getValue("port"))));
			mongoClient = new MongoClient(new ServerAddress(getConfig.getValue("ip"),27017));

			System.out.println("NMS DB connection established.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mongoClient;
	}

}
