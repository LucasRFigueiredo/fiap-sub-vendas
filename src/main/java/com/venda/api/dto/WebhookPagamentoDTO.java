package com.venda.api.dto;

import com.venda.api.domain.model.StatusPagamento;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookPagamentoDTO {

    @NotNull(message = "O ID da venda é obrigatório")
    private Long saleId;

    @NotNull(message = "O status do pagamento é obrigatório")
    private StatusPagamento statusPagamento;
}