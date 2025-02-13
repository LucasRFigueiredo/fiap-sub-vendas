package com.venda.api.infrastructure.client;

import com.venda.api.dto.VehicleResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private VehicleClient vehicleClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obterVeiculo_Sucesso() {
        VehicleResponseDTO mockResponse = new VehicleResponseDTO(1L, "Toyota", "Corolla", 2022, "Branco", 90000.0);
        when(restTemplate.getForEntity("http://localhost:8080/vehicles/1", VehicleResponseDTO.class))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        VehicleResponseDTO result = vehicleClient.obterVeiculo(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void obterVeiculo_CorpoNulo() {
        when(restTemplate.getForEntity("http://localhost:8080/vehicles/1", VehicleResponseDTO.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        VehicleResponseDTO result = vehicleClient.obterVeiculo(1L);
        assertNull(result);
    }

    @Test
    void obterVeiculo_StatusNao2xx() {
        when(restTemplate.getForEntity("http://localhost:8080/vehicles/1", VehicleResponseDTO.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        VehicleResponseDTO result = vehicleClient.obterVeiculo(1L);
        assertNull(result);
    }

    @Test
    void obterVeiculo_Exception() {
        when(restTemplate.getForEntity("http://localhost:8080/vehicles/1", VehicleResponseDTO.class))
                .thenThrow(new RuntimeException("Erro de conexão"));

        VehicleResponseDTO result = vehicleClient.obterVeiculo(1L);
        assertNull(result);
    }

    @Test
    void listarVeiculosOrdenados_Sucesso() {
        VehicleResponseDTO[] mockResponse = {
                new VehicleResponseDTO(1L, "Toyota", "Corolla", 2022, "Branco", 90000.0),
                new VehicleResponseDTO(2L, "Honda", "Civic", 2021, "Preto", 85000.0)
        };
        when(restTemplate.getForEntity("http://localhost:8080/vehicles/ordenados", VehicleResponseDTO[].class))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        List<VehicleResponseDTO> result = vehicleClient.listarVeiculosOrdenados();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void listarVeiculosOrdenados_CorpoNulo() {
        when(restTemplate.getForEntity("http://localhost:8080/vehicles/ordenados", VehicleResponseDTO[].class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        List<VehicleResponseDTO> result = vehicleClient.listarVeiculosOrdenados();
        assertTrue(result.isEmpty());
    }

    @Test
    void listarVeiculosOrdenados_Exception() {
        when(restTemplate.getForEntity("http://localhost:8080/vehicles/ordenados", VehicleResponseDTO[].class))
                .thenThrow(new RuntimeException("Erro de conexão"));

        List<VehicleResponseDTO> result = vehicleClient.listarVeiculosOrdenados();
        assertTrue(result.isEmpty());
    }
}
