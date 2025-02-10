package com.venda.api.domain.model;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    private Long id;
    private Long idVehicle;
    private String cpfComprador;
    private LocalDate dataVenda;
    private StatusPagamento statusPagamento;
    private Double precoVeiculo;
}