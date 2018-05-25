package youness.automotive.controller.rest;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration("my-servlet-context.xml")
public class MessageRestControllerTest {

    @Autowired
    private WebApplicationContext context;

    /*
     For more info about MVC test in spring visit
     https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#spring-mvc-test-server
     */
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    //@Test
    public void shouldAvoidRequestsWithInvalidPayload() throws Exception {
        // Arrange & Act
        ResultActions result =
                mockMvc.perform(post("/maintenanceTypes/list").contentType(MediaType.APPLICATION_JSON).content("{"));
        //Assert
        result.andExpect(status().is2xxSuccessful());
        result.andExpect(content().contentType("application/json;charset=UTF-8"));
        result.andExpect(jsonPath("$.types").value("testValue"));
    }

    //http://www.baeldung.com/spring-apache-camel-tutorial
    //https://spring.io/guides/gs/messaging-jms/
}