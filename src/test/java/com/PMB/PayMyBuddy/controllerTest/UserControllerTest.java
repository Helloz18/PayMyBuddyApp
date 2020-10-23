package com.PMB.PayMyBuddy.controllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.PMB.PayMyBuddy.Service.UserService;
import com.PMB.PayMyBuddy.controller.UserController;
import com.PMB.PayMyBuddy.model.User;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
@WithMockUser(roles = { "USER", "ADMIN" })
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserService userService;

	@MockBean
	DataSource dataSource;

	@BeforeEach
	public void setUp() {

	}

	
	@Test
	public void getUsersShouldReturnOk() throws Exception {
		mockMvc.perform(get("/user/all")).andExpect(status().isOk());
	}

	@Test
	public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {

		User user = new User(0, "test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null, null);
		User user2 = new User(0, "x@x.com", "123", "x", "x", null, 0, "ROLE_ADMIN", true, null, null, null, null, null);

		List<User> users = Arrays.asList(user, user2);

		given(userService.findAll()).willReturn(users);
		System.out.println(users);
		
		mockMvc.perform(get("/user/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].email", is(user.getEmail())));

	}
}
