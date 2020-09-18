package com.PMB.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DataBaseTest {
	
	final Logger LOGGER = LoggerFactory.getLogger(DataBaseTest.class);	

	@Test
	void testConnectionToBDD() {
		
		LOGGER.info("connection à la BDD");
		
		//get the configuration of the database
    	File config = new File("./src/main/resources/application.properties");
    	 FileReader reader;
		try {
			reader = new FileReader(config);
			Properties props = new Properties();
    	    props.load(reader);
    	    
    	    String host = props.getProperty("spring.datasource.url");
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                    host, 
                    props.getProperty("spring.datasource.username"), 
                    props.getProperty("spring.datasource.password")
                ); 
            
            PreparedStatement ps = conn.prepareStatement("SELECT u.\"firstName\" FROM \"PayMyBuddy\".user u where u.email='test@test.com'");
            ResultSet rs = ps.executeQuery();
            rs.next();
            String firstName = rs.getString("firstName");
            
            assertEquals(firstName, "test");
    	    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

}
