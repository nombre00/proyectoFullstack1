package com.ventasWeb.cl.ventasWeb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table (name = "Tienda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tienda {

    // Atributos.
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (length = 15)
    private Long id_tienda;

    @Column (length = 70, nullable = false)
    private String direccion;

    @Column (nullable = false)
    private String horario_atencion;

    @OneToOne
    @JoinColumn (name = "id_inventario")
    private Inventario inventario;

    @Column(length = 300, nullable = false)
    private String politica;
}
