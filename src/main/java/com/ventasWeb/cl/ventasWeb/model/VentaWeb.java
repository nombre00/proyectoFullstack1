package com.ventasWeb.cl.ventasWeb.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VentaWeb")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaWeb {

    // Atributos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (length = 10)
    private Long id_ventaWeb;

    @ManyToOne
    @JoinColumn (name = "run_cliente")
    private Cliente cliente; 

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_envio")
    private Envio envio;

    private Integer totalPagado;

    private String direccion;

    private String fecha;

    @OneToOne
    private Factura factura;

    // One to many es la relación y cascade es para que las acciones afecten las tablas referenciadas en cascada.
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn ()
    @JsonIgnore
    private List<Cliente_ventaWeb> detallesCompras;
}
