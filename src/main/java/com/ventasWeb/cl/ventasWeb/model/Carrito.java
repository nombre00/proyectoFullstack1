package com.ventasWeb.cl.ventasWeb.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carrito {
    // Un carrito es un carrito de compra que puede contener muchos detalleCarrito (producto, cantidad producto).

    // Atributos. 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carrito;

    // Si el estado es falso aún no se efectúa la compra, si el estado es verdadero es que se pagó.
    @Column 
    private boolean pagado=false;

    // Relación con DetalleCarrito (uno a muchos)
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    @ToString.Exclude
    private List<DetalleCarrito> detallesCarrito = new ArrayList<>();

    // Relación con cliente.
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @JsonIgnore
    @ToString.Exclude
    private Cliente cliente;
}
