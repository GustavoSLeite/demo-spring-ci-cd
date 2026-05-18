package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraServiceTest {

    private final CalculadoraService calculadoraService = new CalculadoraService();

    @Test
    public void testDivisaoSucesso() {
        double resultado = calculadoraService.dividir(10, 2);
        assertEquals(5.0, resultado);
    }

    @Test
    public void testDivisaoPorZero() {
        assertThrows(ResponseStatusException.class,
            () -> calculadoraService.dividir(10, 0));
    }

    @Test
    public void testDivisaoNegativa() {
        double resultado = calculadoraService.dividir(-10, 2);
        assertEquals(-5.0, resultado);
    }

    @Test
    public void testDivisaoComDecimais() {
        double resultado = calculadoraService.dividir(7, 2);
        assertEquals(3.5, resultado);
    }
}

