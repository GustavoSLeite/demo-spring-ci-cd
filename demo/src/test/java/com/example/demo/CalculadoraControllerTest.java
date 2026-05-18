package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(CalculadoraController.class)
public class CalculadoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculadoraService calculadoraService;

    @Test
    public void testDivisaoSucesso() throws Exception {
        when(calculadoraService.dividir(10, 2)).thenReturn(5.0);

        mockMvc.perform(post("/divisao/10/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("5.0"));
    }

    @Test
    public void testDivisaoPorZero() throws Exception {
        when(calculadoraService.dividir(10, 0))
                .thenThrow(new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Divisão por zero não permitida"
                ));

        mockMvc.perform(post("/divisao/10/0"))
                .andExpect(status().isBadRequest());
    }
}

