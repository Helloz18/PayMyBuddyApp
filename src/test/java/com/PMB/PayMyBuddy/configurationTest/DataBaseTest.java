package com.PMB.PayMyBuddy.configurationTest;

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
            
            PreparedStatement ps = conn.prepareStatement("SELECT u.\"firstname\" FROM \"pmb\".user u where u.email='x@x.com'");
            ResultSet rs = ps.executeQuery();
            rs.next();
            String firstname = rs.getString("firstname");
            LOGGER.info("firstame should be x, firstname received : "+firstname);
            assertEquals(firstname, "x");
    	    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

//	@Test
//	void hibernateConnectionTest() {
//		HibernatePersister hb = new HibernatePersister();
//		PhoneNumber test = (PhoneNumber)hb.read(1);
//		System.out.println(test);
//	}
}