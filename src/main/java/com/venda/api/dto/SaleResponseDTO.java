package com.venda.api.dto;

import com.venda.api.domain.model.StatusPagamento;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SaleResponseDTO {
    private Long id;
    private String cpfComprador;
    private LocalDate dataVenda;
    private StatusPagamento statusPagamento;
}