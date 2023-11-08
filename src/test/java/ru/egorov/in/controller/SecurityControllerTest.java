package ru.egorov.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.in.dto.PlayerDTO;
import ru.egorov.in.dto.SecurityRequest;
import ru.egorov.in.mappers.PlayerMapper;
import ru.egorov.model.Player;
import ru.egorov.service.SecurityService;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(SecurityController.class)
class SecurityControllerTest {

    @MockBean
    private SecurityService securityService;
    @MockBean
    private PlayerMapper playerMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    void testLogin_shouldReturnJwtResponse() throws Exception {
        final String login = "test";
        final String password = "test";
        final SecurityRequest request = new SecurityRequest(login, password);
        JwtResponse jwt = new JwtResponse(login, "accessToken", "refreshToken");
        when(securityService.authorization(login, password)).thenReturn(jwt);
        mvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(jwt)));
    }

    @Test
    void testRegistration_shouldReturnPlayerDTO() throws Exception {
        final String login = "test";
        final String password = "test";
        final SecurityRequest request = new SecurityRequest(login, password);
        Player player = new Player(login, password, BigDecimal.ZERO);
        PlayerDTO playerDTO = new PlayerDTO(login, BigDecimal.ZERO);
        when(securityService.register(login, password)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(playerDTO);
        mvc.perform(post("/auth/registration")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(playerDTO)));

    }
}