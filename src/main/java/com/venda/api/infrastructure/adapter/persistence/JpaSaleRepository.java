package com.venda.api.infrastructure.adapter.persistence;

import com.venda.api.domain.model.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaSaleRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento status);
}

