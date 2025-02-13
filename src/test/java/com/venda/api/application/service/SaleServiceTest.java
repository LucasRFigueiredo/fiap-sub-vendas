package com.venda.api.application.service;

import com.venda.api.application.port.output.SaleRepositoryPort;
import com.venda.api.domain.model.Sale;
import com.venda.api.domain.model.StatusPagamento;
import com.venda.api.dto.SaleRequestDTO;
import com.venda.api.dto.VehicleResponseDTO;
import com.venda.api.dto.WebhookPagamentoDTO;
import com.venda.api.exception.NotFoundException;
import com.venda.api.infrastructure.client.VehicleClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        SaleRequestDTO request = new SaleRequestDTO(1L, "12345678900", LocalDate.now());
        VehicleResponseDTO vehicleResponse = new VehicleResponseDTO(1L, "Toyota", "Corolla", 2022, "Branco", 90000.0);

        when(vehicleClient.obterVeiculo(1L)).thenReturn(vehicleResponse);
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sale result = saleService.efetuarVenda(request);

        assertNotNull(result);
        assertEquals(1L, result.getIdVehicle());
        assertEquals("12345678900", result.getCpfComprador());
        assertEquals(StatusPagamento.PENDENTE, result.getStatusPagamento());
        verify(vehicleClient, times(1)).obterVeiculo(1L);
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void testEfetuarVenda_VehicleNotFound() {
        SaleRequestDTO request = new SaleRequestDTO(1L, "12345678900", LocalDate.now());

        when(vehicleClient.obterVeiculo(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> saleService.efetuarVenda(request));
        verify(vehicleClient, times(1)).obterVeiculo(1L);
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void testAtualizarStatusPagamento_Success() {
        WebhookPagamentoDTO webhook = new WebhookPagamentoDTO(1L, StatusPagamento.EFETUADO);
        Sale sale = new Sale(1L, 1L, "12345678900", LocalDate.now(), StatusPagamento.PENDENTE, 90000.0);

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        saleService.atualizarStatusPagamento(webhook);

        assertEquals(StatusPagamento.EFETUADO, sale.getStatusPagamento());
        verify(saleRepository, times(1)).findById(1L);
        verify(saleRepository, times(1)).save(sale);
    }

    @Test
    void testAtualizarStatusPagamento_SaleNotFound() {
        WebhookPagamentoDTO webhook = new WebhookPagamentoDTO(1L, StatusPagamento.EFETUADO);

        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> saleService.atualizarStatusPagamento(webhook));
        verify(saleRepository, times(1)).findById(1L);
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void testListarVeiculosDisponiveis() {
        Sale sale = new Sale(1L, 1L, "12345678900", LocalDate.now(), StatusPagamento.PENDENTE, 90000.0);
        VehicleResponseDTO vehicle = new VehicleResponseDTO(1L, "Toyota", "Corolla", 2022, "Branco", 90000.0);

        when(saleRepository.findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.PENDENTE))
                .thenReturn(List.of(sale));
        when(vehicleClient.listarVeiculosOrdenados()).thenReturn(List.of(vehicle));

        List<VehicleResponseDTO> result = saleService.listarVeiculosDisponiveis();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getMarca());
        verify(saleRepository, times(1)).findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.PENDENTE);
        verify(vehicleClient, times(1)).listarVeiculosOrdenados();
    }

    @Test
    void testListarVeiculosVendidos() {
        Sale sale = new Sale(1L, 1L, "12345678900", LocalDate.now(), StatusPagamento.EFETUADO, 90000.0);
        VehicleResponseDTO vehicle = new VehicleResponseDTO(1L, "Toyota", "Corolla", 2022, "Branco", 90000.0);

        when(saleRepository.findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.EFETUADO))
                .thenReturn(List.of(sale));
        when(vehicleClient.listarVeiculosOrdenados()).thenReturn(List.of(vehicle));

        List<VehicleResponseDTO> result = saleService.listarVeiculosVendidos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Corolla", result.get(0).getModelo());
        verify(saleRepository, times(1)).findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.EFETUADO);
        verify(vehicleClient, times(1)).listarVeiculosOrdenados();
    }
}
