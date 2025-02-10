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
    void testObterVeiculo_Success() {
        Long idVeiculo = 1L;
        String expectedUrl = "http://localhost:8080/vehicles/" + idVeiculo;
        VehicleResponseDTO mockResponse = new VehicleResponseDTO();
        mockResponse.setId(idVeiculo);
        mockResponse.setMarca("Ford");
        mockResponse.setModelo("Focus");
        mockResponse.setAno(2022);
        mockResponse.setCor("Preto");
        mockResponse.setPreco(75000.0);

        when(restTemplate.getForEntity(expectedUrl, VehicleResponseDTO.class))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        VehicleResponseDTO result = vehicleClient.obterVeiculo(idVeiculo);

        assertNotNull(result);
        assertEquals(idVeiculo, result.getId());
        assertEquals("Ford", result.getMarca());
        assertEquals("Focus", result.getModelo());
        verify(restTemplate, times(1)).getForEntity(expectedUrl, VehicleResponseDTO.class);
    }

    @Test
    void testObterVeiculo_NotFound() {
        Long idVeiculo = 2L;
        String expectedUrl = "http://localhost:8080/vehicles/" + idVeiculo;

        when(restTemplate.getForEntity(expectedUrl, VehicleResponseDTO.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        VehicleResponseDTO result = vehicleClient.obterVeiculo(idVeiculo);

        assertNull(result);
        verify(restTemplate, times(1)).getForEntity(expectedUrl, VehicleResponseDTO.class);
    }

    @Test
    void testObterVeiculo_Exception() {
        Long idVeiculo = 3L;
        String expectedUrl = "http://localhost:8080/vehicles/" + idVeiculo;

        when(restTemplate.getForEntity(expectedUrl, VehicleResponseDTO.class))
                .thenThrow(new RuntimeException("Error during request"));

        VehicleResponseDTO result = vehicleClient.obterVeiculo(idVeiculo);

        assertNull(result);
        verify(restTemplate, times(1)).getForEntity(expectedUrl, VehicleResponseDTO.class);
    }
}
