package com.ventasWeb.cl.ventasWeb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PedidoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedidoDetalle;

    @ManyToOne
    private Producto producto;

    private int cantidad;
}
