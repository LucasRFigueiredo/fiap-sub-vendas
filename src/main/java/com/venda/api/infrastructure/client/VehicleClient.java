package com.venda.api.infrastructure.client;

import com.venda.api.dto.VehicleResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Component
public class VehicleClient {

    private final RestTemplate restTemplate;
    private final String vehicleServiceUrl = "http://localhost:8080/vehicles";

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
