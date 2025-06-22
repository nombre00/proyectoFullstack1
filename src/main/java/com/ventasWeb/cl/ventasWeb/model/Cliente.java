package com.ventasWeb.cl.ventasWeb.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table (name = "Cliente")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    
    // Atributos.
    @Id
    @Column (length = 8)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long run_cliente;

    @Column (nullable = false)
    private char dv;

    @Column (nullable = false)
    private String clave;

    @Column (length = 70, nullable = false)
    private String nombre_completo;

    @Column (length = 50, nullable = false)
    private String mail;

    @Column (length = 200, nullable = true)
    private String direccion;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Carrito carrito;

    // Carritos viejos.
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Carrito> carritosViejos = new ArrayList<>();

    // One to many es la relación y cascade es para que las acciones afecten las tablas referenciadas en cascada.
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<VentaWeb> historial_comprasWeb;

    // One to many es la relación y cascade es para que las acciones afecten las tablas referenciadas en cascada.
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Cliente_ventaWeb> detallesCompras;

    // One to many es la relación y cascade es para que las acciones afecten las tablas referenciadas en cascada.
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Envio> envios = new ArrayList<>();
}  
