package com.venda.api.infrastructure.controller;

import com.venda.api.application.port.input.SaleUseCase;
import com.venda.api.domain.model.Sale;
import com.venda.api.domain.model.StatusPagamento;
import com.venda.api.dto.SaleRequestDTO;
import com.venda.api.dto.SaleResponseDTO;
import com.venda.api.dto.WebhookPagamentoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SaleControllerTest {

    @InjectMocks
    private SaleController saleController;

    @Mock
    private SaleUseCase saleUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEfetuarVenda_ChamadaAoUseCase() {
        SaleRequestDTO requestDTO = new SaleRequestDTO(2L, "12345678901", LocalDate.of(2025, 2, 3));
        Sale mockSale = new Sale();
        mockSale.setId(1L);
        mockSale.setCpfComprador("12345678901");
        mockSale.setDataVenda(LocalDate.of(2025, 2, 3));
        mockSale.setStatusPagamento(StatusPagamento.PENDENTE);

        when(saleUseCase.efetuarVenda(requestDTO)).thenReturn(mockSale);

        SaleResponseDTO response = saleController.efetuarVenda(requestDTO);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("12345678901", response.getCpfComprador());
        verify(saleUseCase, times(1)).efetuarVenda(requestDTO);
    }


    @Test
    void testAtualizarStatusPagamentoChamadaAoUseCase() {
        WebhookPagamentoDTO webhookPagamentoDTO = new WebhookPagamentoDTO(1L, StatusPagamento.EFETUADO);

        ResponseEntity<Void> response = saleController.atualizarStatusPagamento(webhookPagamentoDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(saleUseCase, times(1)).atualizarStatusPagamento(webhookPagamentoDTO);
    }

    @Test
    void testListarVeiculosDisponiveisChamadaAoUseCase() {
        saleController.listarVeiculosDisponiveis();
        verify(saleUseCase, times(1)).listarVeiculosDisponiveis();
    }

    @Test
    void testListarVeiculosVendidosChamadaAoUseCase() {
        saleController.listarVeiculosVendidos();
        verify(saleUseCase, times(1)).listarVeiculosVendidos();
    }
}
