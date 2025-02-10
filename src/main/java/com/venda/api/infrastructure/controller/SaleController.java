package com.venda.api.infrastructure.controller;

import com.venda.api.application.port.input.SaleUseCase;
import com.venda.api.domain.model.Sale;
import com.venda.api.dto.SaleRequestDTO;
import com.venda.api.dto.SaleResponseDTO;
import com.venda.api.dto.WebhookPagamentoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleUseCase saleUseCase;

    @PostMapping
    public SaleResponseDTO efetuarVenda(@Valid @RequestBody SaleRequestDTO dto) {
        Sale sale = saleUseCase.efetuarVenda(dto);
        return new SaleResponseDTO(sale.getId(), sale.getCpfComprador(), sale.getDataVenda(), sale.getStatusPagamento());

    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> atualizarStatusPagamento(@Valid @RequestBody WebhookPagamentoDTO dto) {
        saleUseCase.atualizarStatusPagamento(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/disponiveis")
    public List<SaleResponseDTO> listarVeiculosDisponiveis() {
        return saleUseCase.listarVeiculosDisponiveis().stream()
                .map(sale -> new SaleResponseDTO(sale.getId(), sale.getCpfComprador(), sale.getDataVenda(), sale.getStatusPagamento()))
                .toList();
    }

    @GetMapping("/vendidos")
    public List<SaleResponseDTO> listarVeiculosVendidos() {
        return saleUseCase.listarVeiculosVendidos().stream()
                .map(sale -> new SaleResponseDTO(sale.getId(), sale.getCpfComprador(), sale.getDataVenda(), sale.getStatusPagamento()))
                .toList();
    }
}