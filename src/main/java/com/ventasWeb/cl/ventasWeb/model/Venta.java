package com.ventasWeb.cl.ventasWeb.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Venta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (length = 10)
    private Long id_venta;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_venta")
    private List<VentaDetalle> detalles;

    @Column (length = 10, nullable = false)
    private long total;

    @Column (length = 30, nullable = false)
    private String fecha;

}
