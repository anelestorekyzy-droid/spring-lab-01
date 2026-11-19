
package kz.iitu.springlab.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.util.ReflectionTestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HelloControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        HelloController controller = new HelloController();

        ReflectionTestUtils.setField(controller, "owner", "Anel");

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void helloDefaultNameReturns200() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, world!"));
    }

    @Test
    void helloCustomNameReturns200() throws Exception {
        mockMvc.perform(get("/api/hello")
                        .param("name", "Anel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, Anel!"))
                .andExpect(jsonPath("$.owner").value("Anel"));
    }

    @Test
    void infoReturns200() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner").value("Anel"))
                .andExpect(jsonPath("$.javaVersion").exists())
                .andExpect(jsonPath("$.cpuCores").isNumber());
    }
}
