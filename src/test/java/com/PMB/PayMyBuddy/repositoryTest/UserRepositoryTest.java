package com.PMB.PayMyBuddy.repositoryTest;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    User userTest;
    
    @BeforeEach
    public void setUp(){

    	userTest = new User("email", "password","firstname", "lastname",null,0, "ROLE_USER",true, null, null, null, 
    			null, null);
    	entityManager.persist(userTest);
    	entityManager.flush();
    }
    
    @Test
    public void test_Find_By_Email() {  	 
    	//Given --> setUp()
    	//When
        User userFound = userRepository.findByEmail(userTest.getEmail());
        //Then 
        assertThat(userFound.getEmail()).isEqualTo(userTest.getEmail());
    }
  
    @Test
    public void test_Find_All() {
    	//Given --> setUp()
    	//When
    	List<User> users = userRepository.findAll();
    	//Then
    	assertEquals(users.size(), 1);
    }
    

}
