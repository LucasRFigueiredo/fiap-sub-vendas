package com.venda.api.application.service;

import com.venda.api.application.port.input.SaleUseCase;
import com.venda.api.application.port.output.SaleRepositoryPort;
import com.venda.api.domain.model.Sale;
import com.venda.api.domain.model.StatusPagamento;
import com.venda.api.dto.SaleRequestDTO;
import com.venda.api.dto.VehicleResponseDTO;
import com.venda.api.dto.WebhookPagamentoDTO;
import com.venda.api.exception.NotFoundException;
import com.venda.api.infrastructure.client.VehicleClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService implements SaleUseCase {

    private final SaleRepositoryPort saleRepository;
    private final VehicleClient vehicleClient;

    @Override
    public Sale efetuarVenda(SaleRequestDTO saleRequest) {
        VehicleResponseDTO vehicle = vehicleClient.obterVeiculo(saleRequest.getVehicleId());

        if (vehicle == null) {
            throw new NotFoundException("Veículo com ID " + saleRequest.getVehicleId() + " não encontrado.");
        }

        Sale sale = new Sale();
        sale.setIdVehicle(vehicle.getId());
        sale.setPrecoVeiculo(vehicle.getPreco());
        sale.setCpfComprador(saleRequest.getCpfComprador());
        sale.setDataVenda(saleRequest.getDataVenda());
        sale.setStatusPagamento(StatusPagamento.PENDENTE);

        return saleRepository.save(sale);
    }

    @Override
    public void atualizarStatusPagamento(WebhookPagamentoDTO webhookPagamento) {
        Sale sale = saleRepository.findById(webhookPagamento.getSaleId())
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        sale.setStatusPagamento(webhookPagamento.getStatusPagamento());
        saleRepository.save(sale);
    }

    @Override
    public List<Sale> listarVeiculosDisponiveis() {
        return saleRepository.findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.PENDENTE);
    }

    @Override
    public List<Sale> listarVeiculosVendidos() {
        return saleRepository.findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.EFETUADO);
    }
}
