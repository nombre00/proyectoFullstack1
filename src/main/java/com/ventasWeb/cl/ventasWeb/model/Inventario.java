package com.ventasWeb.cl.ventasWeb.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table (name = "Inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    // Atributos.
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (length = 10)
    private Long id_inventario;

    @OneToMany
    @JoinColumn
    private List<Producto> productos;

    @OneToOne
    @JoinColumn (name = "run")
    private Especialista_almacen administrador;
}
