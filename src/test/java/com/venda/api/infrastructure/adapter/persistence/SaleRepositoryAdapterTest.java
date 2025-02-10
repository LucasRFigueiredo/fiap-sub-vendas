package com.venda.api.infrastructure.adapter.persistence;

import com.venda.api.domain.model.Sale;
import com.venda.api.domain.model.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleRepositoryAdapterTest {

    @Mock
    private JpaSaleRepository jpaSaleRepository;

    @InjectMocks
    private SaleRepositoryAdapter saleRepositoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Sale sale = new Sale(1L, 2L, "12345678901", LocalDate.now(), StatusPagamento.PENDENTE, 50000.0);
        SaleEntity saleEntity = new SaleEntity(1L, "12345678901", LocalDate.now(), StatusPagamento.PENDENTE, 2L, 50000.0);

        when(jpaSaleRepository.save(any(SaleEntity.class))).thenReturn(saleEntity);

        Sale savedSale = saleRepositoryAdapter.save(sale);

        assertNotNull(savedSale);
        assertEquals(sale.getId(), savedSale.getId());
        assertEquals(sale.getCpfComprador(), savedSale.getCpfComprador());
        verify(jpaSaleRepository, times(1)).save(any(SaleEntity.class));
    }

    @Test
    void testFindById() {
        SaleEntity saleEntity = new SaleEntity(1L, "12345678901", LocalDate.now(), StatusPagamento.EFETUADO, 2L, 50000.0);

        when(jpaSaleRepository.findById(1L)).thenReturn(Optional.of(saleEntity));

        Optional<Sale> foundSale = saleRepositoryAdapter.findById(1L);

        assertTrue(foundSale.isPresent());
        assertEquals(saleEntity.getId(), foundSale.get().getId());
        assertEquals(saleEntity.getStatusPagamento(), foundSale.get().getStatusPagamento());
        verify(jpaSaleRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllByStatusPagamentoOrderByPrecoVeiculoAsc() {
        SaleEntity saleEntity1 = new SaleEntity(1L, "12345678901", LocalDate.now(), StatusPagamento.EFETUADO, 2L, 45000.0);
        SaleEntity saleEntity2 = new SaleEntity(2L, "12345678902", LocalDate.now(), StatusPagamento.EFETUADO, 3L, 60000.0);

        when(jpaSaleRepository.findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.EFETUADO))
                .thenReturn(List.of(saleEntity1, saleEntity2));

        List<Sale> sales = saleRepositoryAdapter.findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.EFETUADO);

        assertNotNull(sales);
        assertEquals(2, sales.size());
        assertEquals(45000.0, sales.get(0).getPrecoVeiculo());
        assertEquals(60000.0, sales.get(1).getPrecoVeiculo());
        verify(jpaSaleRepository, times(1)).findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.EFETUADO);
    }
}
