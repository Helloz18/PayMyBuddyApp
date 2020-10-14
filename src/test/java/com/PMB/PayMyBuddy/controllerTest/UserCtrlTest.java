//package com.PMB.PayMyBuddy.controllerTest;
//
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.CoreMatchers.containsString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//
//import com.PMB.PayMyBuddy.PayMyBuddyApplication;
//import com.PMB.PayMyBuddy.Service.UserService;
//import com.PMB.PayMyBuddy.controller.UserController;
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(UserController.class)
//@ContextConfiguration(classes=PayMyBuddyApplication.class)
//public class UserCtrlTest {
//
//	@Autowired
//    private MockMvc mockMvc;
//
//	@MockBean
//    UserService userService;
//	
//	
//	  @Test
//	  @WithMockUser // use with mockMvc only
//	  public void testController() throws Exception {
//	        // given
//		  long id = 1;
//		  when(userService.get(id).getFirstname()).thenReturn("x");
//	       // String email = "test@test.com";
//	       // when(userService.getUserByEmail(email).getFirstname()).thenReturn("x");
//
//	        // when + then
//	        mockMvc.perform(get("/user/" + id)).andExpect(content().string(containsString("x")));
//
//	    }
//}
