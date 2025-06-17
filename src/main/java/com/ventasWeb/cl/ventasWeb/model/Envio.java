package com.ventasWeb.cl.ventasWeb.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Envio {
    // Un envio es cuando se hace la compra por linea de un carrito y se envia al cliente.

    // Atributos.
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_envio;

    @ManyToOne // Un envio puede tener muchos clientes.
    private Cliente cliente;

    private String direccion;

    private String estado;

    private String fecha;

    @OneToOne // Un envio puede tener un carrito. 
    private Carrito carrito;
}
