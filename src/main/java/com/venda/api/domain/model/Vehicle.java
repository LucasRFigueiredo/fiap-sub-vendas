package com.venda.api.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    private Long id;
    private String marca;
    private String modelo;
    private int ano;
    private String cor;
    private double preco;
    private boolean vendido;
}