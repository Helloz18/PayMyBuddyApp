package com.PMB.PayMyBuddy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseTestConnection {

	final Logger LOGGER = LoggerFactory.getLogger(DatabaseTestConnection.class);	

	public Connection testConnection() {
		
		LOGGER.info("connection à la BDD");
		
		File config = new File("./src/main/resources/application.properties");
		FileReader reader;
		Connection con = null;
		try {
			reader = new FileReader(config);
			Properties props = new Properties();
			props.load(reader);
			 String host = props.getProperty("spring.datasource.url");
	            con = java.sql.DriverManager.getConnection(
	                    host, 
	                    props.getProperty("spring.datasource.username"), 
	                    props.getProperty("spring.datasource.password")
	                ); 

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return con;
	}

	  public void closeConnection(Connection con){
	        if(con!=null){
	            try {
	                con.close();
	                LOGGER.info("Closing DB connection");
	            } catch (SQLException e) {
	                LOGGER.error("Error while closing connection",e);
	            }
	        }
	    }
}
