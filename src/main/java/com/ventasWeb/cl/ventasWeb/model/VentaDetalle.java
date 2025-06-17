package com.ventasWeb.cl.ventasWeb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class VentaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ventaDetalle;

    @ManyToOne
    private Producto producto;

    private int cantidad;

    private double subtotal;
}