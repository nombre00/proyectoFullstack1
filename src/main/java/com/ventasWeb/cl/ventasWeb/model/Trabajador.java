package com.ventasWeb.cl.ventasWeb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name= "Trabajador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trabajador {

    // Atributos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long run_trabajador;

    @Column (nullable = false)
    private char dv;

    @Column (length = 100, nullable = false)
    private String nombre_completo;

    @Column (length = 15, nullable = false)
    private Long sueldoBase;

    @Column (nullable = false)
    private String horario_de_trabajo;
}
