package bitcoin.controller;

import bitcoin.domain.api.Mappings;
import org.junit.Before;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public abstract class AbstractAlertController {

    private MockMvc mockMvc;

    @Before
    public void createMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
    }

    protected abstract AlertController getController();

    protected HttpStatus getHttpStatus(MvcResult mvcResult) {
        return HttpStatus.valueOf(mvcResult.getResponse().getStatus());
    }

    public MvcResult performPut(String url) throws Exception {
        return mockMvc.perform(put(url)).andReturn();

    }

    public MvcResult performDelete(String url) throws Exception {
        return mockMvc.perform(delete(url)).andReturn();

    }
}
