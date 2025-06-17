package com.ventasWeb.cl.ventasWeb.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Proveedor {

    // Atributos.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (length = 15)
    private Long id_proveedor;

    @Column (length = 50, nullable = false)
    private String nombre;

    @Column (length = 80, nullable = false)
    private String contacto;

    @Column (length = 11, nullable = false)
    private int telefono;

    @Column (length = 100, nullable = false)
    private String direccion;

    @OneToMany
    private List<Producto> productos;
}
