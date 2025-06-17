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

import com.ventasWeb.cl.ventasWeb.model.Carrito;
import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.service.CarritoService;
import com.ventasWeb.cl.ventasWeb.service.ClienteService;


@RestController
@RequestMapping ("/api/v1/cliente")
public class ClienteController {

    // Creamos la variable que contiene la funcionalidad del service.
    @Autowired
    private ClienteService cs;
    @Autowired
    private CarritoService carS;

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.
    // Método que lista los clientes.
    @GetMapping ("/listar")
    public ResponseEntity<List<Cliente>> buscarTodos(){
        // Buscamos los clientes y los guardamos en una variable.
        List<Cliente> clientes = cs.buscarTodos();
        // Si no encontramos clientes retornamos una respuesta vacía.
        if (clientes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Si tenemos productos los retornamos dentro de una respuesta.
        return ResponseEntity.ok(clientes);
    }

    // Método para buscar por id.
    @GetMapping ("/{id}")
    public ResponseEntity<Cliente> buscarPorRun(@PathVariable int id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por run y guardamos el producto en una variable.
            Cliente c = cs.buscarPorId(id);
            // Retornamos una respuesta que contiene el producto.
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método que guarda.
    @PostMapping ("/agregar")
    public ResponseEntity<Cliente> guardar (@RequestBody Cliente c){
        // Creamos una variable que contiene el nuevo producto y y revisamos si incluye un carrito, sino, lo creamos.
        Cliente C = c;
        if (C.getCarrito() == null){
            Carrito carrito = new Carrito();
            // Guardamos el carrito.
            carS.guardar(carrito);
            C.setCarrito(carrito);
        }
        // Guardamos el cliente nuevo con su carrito.
        cs.guardar(C);
        // Retornamos una respuesta que contiene el producto.
        return ResponseEntity.ok(C);
    }

    // Método que borra.
    @DeleteMapping("/borrar/{id}")
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
    // Los argumentos que recibe la funcion es un id para buscar el cliente a editar y un cliente nuevo,
    // los atributos de este cliente nuevo van a reemplazar los atributos del cliente encontrado.
    @PutMapping ("/{id}")
    public ResponseEntity<Cliente> actualizar (@PathVariable long id, @RequestBody Cliente c){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos el producto a editar y lo guardamos en una variable.
            Cliente Cactualizado = cs.buscarPorId(id);
            // Reemplazamos los atributos.
            Cactualizado.setRun_cliente(id);
            Cactualizado.setCarrito(c.getCarrito());
            Cactualizado.setDetallesCompras(c.getDetallesCompras());
            Cactualizado.setDireccion(c.getDireccion());
            Cactualizado.setDireccion(c.getDireccion());
            Cactualizado.setDv(c.getDv());
            Cactualizado.setHistorial_comprasWeb(c.getHistorial_comprasWeb());
            Cactualizado.setMail(c.getMail());
            Cactualizado.setNombre_completo(c.getNombre_completo());
            Cactualizado.setClave(c.getClave());
            // Guardamos los cambios.
            cs.guardar(Cactualizado);
            // Retornamos la respuesta con el inventario actualizado.
            return ResponseEntity.ok(Cactualizado);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para ingresar a la cuenta.
    @GetMapping("/ingresar_cuenta/{nombre}/{clave}")
    public ResponseEntity<String> ingresar(@PathVariable String nombre, @PathVariable String clave){
        // Creamos un string.
        String sentencia = "";
        // Le pasamos los argumentos a la función que revisa la clave e is.
        if (cs.ingresar(nombre, clave)){
            sentencia = "Ingresando a la página, bienvenido.";
            // Retornamos la sentencia dentro de la respuesta.
            return ResponseEntity.ok(sentencia);
        }
        // Si no, retornamos una respuesta vacía.
        return ResponseEntity.noContent().build();
    }

    // Método que actualiza el carro. 
    @PutMapping("/actualizarCarro/{id}")
    public ResponseEntity<Cliente> actualizarCarro(@PathVariable long id){
        // Buscamos el cliente y lo guardamos en una variable.
        Cliente cliente = cs.buscarPorId(id);
        // Buscamos el carrito viejo y lo guardamos en una variable.
        Carrito carritoViejo = cliente.getCarrito();

        // Creamos un nuevo carrito.
        Carrito carrito = new Carrito();
        // Seteamos el cliente del carrito.
        carrito.setCliente(cliente);
        // Guardamos el carrito nuevo.
        carS.guardar(carrito);

        // Movemos el carrito viejo a la lista de carritos viejos.
        List<Carrito> listaCarrito = cliente.getCarritosViejos();
        listaCarrito.add(carritoViejo);
        cliente.setCarritosViejos(listaCarrito);

        // Ahora pasamos el carrito nuevo como carrito actual del cliente.
        cliente.setCarrito(carrito);
        // Guardamos el cliente editado.
        cs.guardar(cliente);
        return ResponseEntity.ok(cliente);
    }
}
