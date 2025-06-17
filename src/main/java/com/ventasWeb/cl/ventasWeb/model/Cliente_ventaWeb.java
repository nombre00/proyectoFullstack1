package com.ventasWeb.cl.ventasWeb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente_ventaWeb {
    // Tabla de intersección.

    // Atributos.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Cliente_ventaWeb;

    // Relación con cliente.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_cliente", nullable = false)
    @JsonIgnore
    private Cliente cliente;

    // Relación con ventaWeb
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ventaWeb", nullable = false)
    @JsonIgnore
    private VentaWeb ventaWeb;
}
