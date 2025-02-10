package com.venda.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequestDTO {

    @NotNull(message = "O ID do veículo é obrigatório")
    private Long vehicleId;

    @NotNull(message = "O CPF do comprador é obrigatório")
    private String cpfComprador;

    @NotNull(message = "A data da venda é obrigatória")
    private LocalDate dataVenda;
}