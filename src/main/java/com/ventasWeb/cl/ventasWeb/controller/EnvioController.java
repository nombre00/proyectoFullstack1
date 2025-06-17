package com.ventasWeb.cl.ventasWeb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.ventasWeb.cl.ventasWeb.model.Envio;
import com.ventasWeb.cl.ventasWeb.service.EnvioService;


@RestController
@RequestMapping ("/api/v1/envio")
public class EnvioController {

    // Creamos una variable que contiene la funcionalidad del service.
    @Autowired
    private EnvioService es;

    // Métodos.

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los envios.
    @GetMapping("/listar")
    public ResponseEntity<List<Envio>> buscarTodos(){
        // Buscamos los envios y los guardamos en una variable.
        List<Envio> envios = es.buscarTodos();
        // Si no encontramos envios retornamos una respuesta vacía.
        if (envios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Si tenemos carritos los retornamos dentro de una respuesta.
        return ResponseEntity.ok(envios);
    }

    // Método que busca por id.
    @GetMapping ("/{id}")
    public ResponseEntity<Envio> buscarPorId(@PathVariable int id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por run y guardamos el envio en una variable.
            Envio s = es.buscarPorId(id);
            // Retornamos una respuesta que contiene el carrito.
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para guardar.
    @PostMapping ("/{id}")
    public ResponseEntity<Envio> guardar (@RequestBody Envio e){
        // Creamos una variable que contiene el nuevo envio y lo guardamos al mismo tiempo.
        Envio E = es.guardar(e);
        // Retornamos una respuesta que contiene el envio.
        return ResponseEntity.ok(E);
    }

    // Método que borra.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Como acá no guardamos usamos el service directamente.
            es.borrar(id);
            // Retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            // Cualquier error tambien retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método que actualiza.
    // Los argumentos que recibe la funcion es un id para buscar el inventario a editar y un inventario nuevo, 
    // los atributos de este inventario nuevo van a reemplazar los atributos del inventario encontrado.
    @PutMapping ("/{id}")
    public ResponseEntity<Envio> actualizar (@PathVariable long id, @RequestBody Envio en){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos el envio a editar y lo guardamos en una variable.
            Envio EA = es.buscarPorId(id);
            // Reemplazamos los atributos.
            EA.setId_envio(id);
            EA.setCliente(en.getCliente());
            EA.setCarrito(en.getCarrito());
            EA.setDireccion(en.getDireccion());
            EA.setFecha(en.getFecha());
            // Guardamos los cambios.
            es.guardar(EA);
            // Retornamos la respuesta con el envio actualizado.
            return ResponseEntity.ok(EA);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para buscar estado del envio por id.
    @GetMapping ("/revisar_estado/{id}")
    public ResponseEntity<String>estadoPorId(@PathVariable int id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por id y guardamos el pedido en una variable.
            Envio e = es.buscarPorId(id);
            // Guardamos el estado en una variable.
            String E = e.getEstado();
            // Retornamos una respuesta que contiene el estado.
            return ResponseEntity.ok(E);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }
    // Método para editar el estado del pedido por id.
    @PutMapping ("/editar_estado/{id}")
    public ResponseEntity<Envio> editarEstado(@PathVariable int id, String estado){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por id y guardamos el pedido en una variable.
            Envio e = es.buscarPorId(id);
            // Cambiamos el estado del pedido.
            e.setEstado(estado);
            // Guardamos los cambios hechos.
            es.guardar(e);
            // Retornamos una respuesta que contiene el pedido con el estado actualizado.
            return ResponseEntity.ok(e);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }
}
