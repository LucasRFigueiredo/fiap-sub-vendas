package com.venda.api.application.port.input;

import com.venda.api.domain.model.Sale;
import com.venda.api.dto.SaleRequestDTO;
import com.venda.api.dto.VehicleResponseDTO;
import com.venda.api.dto.WebhookPagamentoDTO;

import java.util.List;

public interface SaleUseCase {
    Sale efetuarVenda(SaleRequestDTO saleRequest);
    void atualizarStatusPagamento(WebhookPagamentoDTO webhookPagamento);
    List<VehicleResponseDTO> listarVeiculosDisponiveis();
    List<VehicleResponseDTO> listarVeiculosVendidos();
}