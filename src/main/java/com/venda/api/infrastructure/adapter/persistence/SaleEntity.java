package com.venda.api.infrastructure.adapter.persistence;

import com.venda.api.domain.model.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "vendas-java")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf_comprador", nullable = false)
    private String cpfComprador;

    @Column(name = "data_venda", nullable = false)
    private LocalDate dataVenda;

    @Column(name = "status_pagamento", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "preco_veiculo", nullable = false)
    private Double precoVeiculo;
}