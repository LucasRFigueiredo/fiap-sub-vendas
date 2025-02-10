package com.venda.api.application.port.output;

import com.venda.api.domain.model.Sale;
import com.venda.api.domain.model.StatusPagamento;

import java.util.List;
import java.util.Optional;

public interface SaleRepositoryPort {
    Sale save(Sale sale);
    Optional<Sale> findById(Long id);
    List<Sale> findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento status);
}