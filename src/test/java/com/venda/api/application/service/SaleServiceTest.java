package com.venda.api.application.service;

import com.venda.api.application.port.output.SaleRepositoryPort;
import com.venda.api.domain.model.Sale;
import com.venda.api.domain.model.StatusPagamento;
import com.venda.api.dto.SaleRequestDTO;
import com.venda.api.dto.VehicleResponseDTO;
import com.venda.api.dto.WebhookPagamentoDTO;
import com.venda.api.infrastructure.client.VehicleClient;
import com.venda.api.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;

    @Mock
    private SaleRepositoryPort saleRepository;

    @Mock
    private VehicleClient vehicleClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEfetuarVenda_Success() {
        SaleRequestDTO request = new SaleRequestDTO(2L, "12345678901", LocalDate.of(2025, 2, 3));
        VehicleResponseDTO vehicleResponse = new VehicleResponseDTO(2L, "Toyota", "corolla", 2023, "preto", 125000.0);

        when(vehicleClient.obterVeiculo(2L)).thenReturn(vehicleResponse);
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sale sale = saleService.efetuarVenda(request);

        assertNotNull(sale);
        assertEquals(2L, sale.getIdVehicle());
        assertEquals(125000.0, sale.getPrecoVeiculo());
        verify(saleRepository).save(any(Sale.class));
    }

    @Test
    void testEfetuarVenda_VehicleNotFound() {
        SaleRequestDTO request = new SaleRequestDTO(2L, "12345678901", LocalDate.of(2025, 2, 3));
        when(vehicleClient.obterVeiculo(2L)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> saleService.efetuarVenda(request));
        assertEquals("Veículo com ID 2 não encontrado.", exception.getMessage());
    }

    @Test
    void testAtualizarStatusPagamento_Success() {
        WebhookPagamentoDTO webhookPagamento = new WebhookPagamentoDTO(1L, StatusPagamento.EFETUADO);
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setStatusPagamento(StatusPagamento.PENDENTE);

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        saleService.atualizarStatusPagamento(webhookPagamento);

        assertEquals(StatusPagamento.EFETUADO, sale.getStatusPagamento());
        verify(saleRepository).save(sale);
    }

    @Test
    void testAtualizarStatusPagamento_SaleNotFound() {
        WebhookPagamentoDTO webhookPagamento = new WebhookPagamentoDTO(1L, StatusPagamento.EFETUADO);
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> saleService.atualizarStatusPagamento(webhookPagamento));
        assertEquals("Venda não encontrada", exception.getMessage());
    }

    @Test
    void testListarVeiculosDisponiveis() {
        Sale sale1 = new Sale();
        sale1.setId(1L);
        sale1.setPrecoVeiculo(100000.0);
        Sale sale2 = new Sale();
        sale2.setId(2L);
        sale2.setPrecoVeiculo(150000.0);

        when(saleRepository.findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.PENDENTE))
                .thenReturn(Arrays.asList(sale1, sale2));

        List<Sale> sales = saleService.listarVeiculosDisponiveis();

        assertEquals(2, sales.size());
        verify(saleRepository).findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.PENDENTE);
    }

    @Test
    void testListarVeiculosVendidos() {
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setPrecoVeiculo(100000.0);

        when(saleRepository.findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.EFETUADO))
                .thenReturn(List.of(sale));

        List<Sale> sales = saleService.listarVeiculosVendidos();

        assertEquals(1, sales.size());
        verify(saleRepository).findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.EFETUADO);
    }
}
