package com.ventasWeb.cl.ventasWeb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.service.ProductoService;


@RestController
@RequestMapping ("/api/v1/producto")
public class ProductoController {

    // Creamos la variable que contiene la funcionalidad del service.
    @Autowired
    private ProductoService Pservice;

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los productos.
    @GetMapping ("/listar")
    public ResponseEntity<List<Producto>> buscarTodos(){
        // Buscamos los productos y los guardamos en una variable.
        List<Producto> productos = Pservice.buscarTodos();
        // Si no encontramos productos retornamos una respuesta vacía.
        if (productos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Si tenemos productos los retornamos dentro de una respuesta.
        return ResponseEntity.ok(productos);
    }

    // Método para buscar por id.
    @GetMapping ("/{id}")
    public ResponseEntity<Producto> buscarPorRun(@PathVariable int id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por run y guardamos el producto en una variable.
            Producto p = Pservice.buscarPorId(id);
            // Retornamos una respuesta que contiene el producto.
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para buscar por nombre.
    @GetMapping ("/buscar/{nombre}")
    public ResponseEntity<Producto> buscarPorNombre(@PathVariable String nombre){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por run y guardamos el producto en una variable.
            Producto p = Pservice.buscarPorNombre(nombre);
            // Retornamos una respuesta que contiene el producto.
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método que guarda.
    @PostMapping ("/agregar")
    public ResponseEntity<Producto> guardar (@RequestBody Producto p){
        // Creamos una variable que contiene el nuevo producto y lo guardamos al mismo tiempo.
        Producto P = Pservice.guardar(p);
        // Retornamos una respuesta que contiene el producto.
        return ResponseEntity.ok(P);
    }

    // Método que borra.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Como acá no guardamos usamos el service directamente.
            Pservice.borrar(id);
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
    public ResponseEntity<Producto> actualizar (@PathVariable long id, @RequestBody Producto p){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos el producto a editar y lo guardamos en una variable.
            Producto Pactualizado = Pservice.buscarPorId(id);
            // Reemplazamos los atributos.
            Pactualizado.setNombre(p.getNombre());
            Pactualizado.setCantidad(p.getCantidad());
            Pactualizado.setFecha_vencimiento(p.getFecha_vencimiento());
            Pactualizado.setMaterial(p.getMaterial());
            Pactualizado.setPrecio(p.getPrecio());
            Pactualizado.setRefrigerar(p.isRefrigerar());
            Pactualizado.setTalla(p.getTalla());
            // Guardamos los cambios.
            Pservice.guardar(Pactualizado);
            // Retornamos la respuesta con el inventario actualizado.
            return ResponseEntity.ok(Pactualizado);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }
}
