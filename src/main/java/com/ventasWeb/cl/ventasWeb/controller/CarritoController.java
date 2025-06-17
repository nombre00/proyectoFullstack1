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

import com.ventasWeb.cl.ventasWeb.model.Carrito;
import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.service.CarritoService;
import com.ventasWeb.cl.ventasWeb.service.ClienteService;


@RestController
@RequestMapping ("/api/v1/carrito")
public class CarritoController {

    // Creamos una variable que contiene la funcionalidad del service.
    @Autowired
    private CarritoService cs;
    @Autowired
    private ClienteService cls;

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los productos.
    @GetMapping ("/listar")
    public ResponseEntity<List<Carrito>> buscarTodos(){
        // Buscamos los carritos y los guardamos en una variable.
        List<Carrito> carritos = cs.buscarTodos();
        // Si no encontramos carritos retornamos una respuesta vacía.
        if (carritos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Si tenemos carritos los retornamos dentro de una respuesta.
        return ResponseEntity.ok(carritos);
    }

    // Método que busca por id.
    @GetMapping ("/{id}")
    public ResponseEntity<Carrito> buscarPorRun(@PathVariable int id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por run y guardamos el carrito en una variable.
            Carrito c = cs.buscarPorId(id);
            // Retornamos una respuesta que contiene el carrito.
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para guardar.
    @PostMapping("/agregar")
    public ResponseEntity<Carrito> guardar (@RequestBody Carrito c){
        // Creamos una variable que contiene el nuevo carrito y lo guardamos al mismo tiempo.
        Carrito C = cs.guardar(c);
        // Retornamos una respuesta que contiene el carrito.
        return ResponseEntity.ok(C);
    }

    // Método que borra.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Como acá no guardamos usamos el service directamente.
            cs.borrar(id);
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
    public ResponseEntity<Carrito> actualizar (@PathVariable long id, @RequestBody Carrito c){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos el producto a editar y lo guardamos en una variable.
            Carrito CA = cs.buscarPorId(id);
            // Reemplazamos los atributos.
            CA.setId_carrito(id); 
            CA.setDetallesCarrito(c.getDetallesCarrito());
            CA.setPagado(c.isPagado());
            // Guardamos los cambios.
            cs.guardar(CA);
            // Retornamos la respuesta con el inventario actualizado.
            return ResponseEntity.ok(CA);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    } 

    // Agregar producto al carrito.
    @PostMapping("/agregar-producto/{id_producto}/{run_cliente}/{cantidad}")
    public ResponseEntity<Carrito> agregarProductoCarrito(@PathVariable long id_producto, @PathVariable long run_cliente, @PathVariable int cantidad) {
        // LLamamos a la funcion que hace la pega.
        cs.agregarProductoCarrito(id_producto, run_cliente, cantidad);
        Cliente cliente = cls.buscarPorId(run_cliente);
        Carrito carrito = cliente.getCarrito();
        // Retornamos el carrito actualizado.
        return ResponseEntity.ok(carrito);
    }
    
    
}
