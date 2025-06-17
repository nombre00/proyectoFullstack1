package com.ventasWeb.cl.ventasWeb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ventasWeb.cl.ventasWeb.model.Envio;
import com.ventasWeb.cl.ventasWeb.model.VentaWeb;
import com.ventasWeb.cl.ventasWeb.repository.ClienteRepository;
import com.ventasWeb.cl.ventasWeb.repository.EnvioRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class EnvioService {

    // creamos una variable que contiene la funcionalidad de repositorio.
    @Autowired
    private EnvioRepository er;
    // Creamos una variable que contiene el repositorio del cliente.
    @Autowired
    private ClienteRepository cr;

    // Métodos.

    // Buscar todos.
    public List<Envio> buscarTodos(){
        return er.findAll();
    }
    // Buscar por id.
    public Envio buscarPorId(long id){
        return er.findById(id).get();
    }
    // Guardar.
    public Envio guardar(Envio e){
        return er.save(e);
    }
    // Borrar.
    public void borrar(long id){
        er.deleteById(id);
    }

    // Buscar envios de un cliente.
    public List<Envio> buscarEnviosCliente(long run){
        // Primero buscamos todas las ventasWeb y las guardamos en una variable.
        List<VentaWeb> ventasWebCliente = cr.findById(run).get().getHistorial_comprasWeb();
        // Creamos un arreglo de envios.
        ArrayList<Envio> envios = new ArrayList<Envio>();
        // Iteramos por la lista de ventas.
        for (VentaWeb vw : ventasWebCliente){
            envios.add(vw.getEnvio());
        }
        // Creamos una lista de enciosCliente y la llenamos con los envios.
        List<Envio> enviosCliente = envios;
        // Retornamos la lista de envios.
        return enviosCliente; 
    }
}
