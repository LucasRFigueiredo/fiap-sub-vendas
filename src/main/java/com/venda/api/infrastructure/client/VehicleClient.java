package com.venda.api.infrastructure.client;

import com.venda.api.dto.VehicleResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class VehicleClient {

    private final RestTemplate restTemplate;
    private final String vehicleServiceUrl = "http://3.82.201.130:8080/vehicles";

    public VehicleClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public VehicleResponseDTO obterVeiculo(Long idVeiculo) {
        String url = vehicleServiceUrl + "/" + idVeiculo;

        try {
            ResponseEntity<VehicleResponseDTO> response = restTemplate.getForEntity(url, VehicleResponseDTO.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
            log.warn("Veículo com ID {} não encontrado ou resposta sem corpo.", idVeiculo);
        } catch (Exception e) {
            log.error("Erro ao obter veículo com ID {}: {}", idVeiculo, e.getMessage(), e);
        }
        return null;
    }

    public List<VehicleResponseDTO> listarVeiculosOrdenados() {
        String url = vehicleServiceUrl + "/ordenados";

        try {
            ResponseEntity<VehicleResponseDTO[]> response = restTemplate.getForEntity(url, VehicleResponseDTO[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
            log.warn("A resposta para a listagem de veículos ordenados está vazia.");
        } catch (Exception e) {
            log.error("Erro ao listar veículos ordenados: {}", e.getMessage(), e);
        }
        return List.of();
    }
}
