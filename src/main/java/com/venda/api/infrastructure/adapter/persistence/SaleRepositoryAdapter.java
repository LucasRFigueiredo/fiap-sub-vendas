package com.venda.api.infrastructure.adapter.persistence;

import com.venda.api.application.port.output.SaleRepositoryPort;
import com.venda.api.domain.model.Sale;
import com.venda.api.domain.model.StatusPagamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SaleRepositoryAdapter implements SaleRepositoryPort {

    private final JpaSaleRepository jpaSaleRepository;

    @Override
    public Sale save(Sale sale) {
        SaleEntity entity = new SaleEntity(
                sale.getId(),
                sale.getCpfComprador(),
                sale.getDataVenda(),
                sale.getStatusPagamento(),
                sale.getIdVehicle(),
                sale.getPrecoVeiculo()
        );
        return toModel(jpaSaleRepository.save(entity));
    }


    @Override
    public Optional<Sale> findById(Long id) {
        return jpaSaleRepository.findById(id).map(this::toModel);
    }

    @Override
    public List<Sale> findAllByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento status) {
        return jpaSaleRepository.findAllByStatusPagamentoOrderByPrecoVeiculoAsc(status)
                .stream()
                .map(this::toModel)
                .toList();
    }

    private Sale toModel(SaleEntity entity) {
        return new Sale(
                entity.getId(),
                entity.getVehicleId(),
                entity.getCpfComprador(),
                entity.getDataVenda(),
                entity.getStatusPagamento(),
                entity.getPrecoVeiculo()
        );
    }
}