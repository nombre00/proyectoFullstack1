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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping ("/api/v1/cliente")
// @Tag es para la documentación.
@Tag(name = "Cliente", description = "Son las operaciones relacionadas con los clientes.")
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
    @Operation(summary = "Obtener todos los clientes.", description = "Obtiene una lista con todos los clientes.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontraron clientes."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<List<Cliente>> buscarTodos(){
        // Buscamos los clientes y los guardamos en una variable.
        List<Cliente> clientes = cs.buscarTodos();
        System.out.println("clientes retornados por el service.");
        System.out.println(clientes);
        // Si no encontramos clientes retornamos una respuesta vacía.
        if (clientes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Si tenemos productos los retornamos dentro de una respuesta.
        return ResponseEntity.ok(clientes);
    }

    // Método para buscar por id.
    @GetMapping ("/{id}")
    @Operation(summary = "Obtener un cliente por su id.", description = "Obtiene un cliente.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el cliente."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    // Agregamos : @Parameter(description = "ID del cliente a buscar", required = true).
    // Esto es para agregar una explicación de que hace esa variable y señalar que es obligatoria.
    // No es necesaria para que swagger reconozca el argumento en la URL.
    public ResponseEntity<Cliente> buscarPorRun(@Parameter(description = "ID del cliente a buscar", required = true)
        @PathVariable int id){
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
    @Operation(summary = "Agrega un cliente nuevo.", description = "Agrega un cliente.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Cliente agregado exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<Cliente> guardar (
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo cliente",
            required = true,
            content = @Content(schema = @Schema(implementation = Cliente.class))
        )
        @RequestBody Cliente c){
        // Creamos una variable que contiene el nuevo cliente y y revisamos si incluye un carrito, sino, lo creamos.
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
    @Operation(summary = "Elimina un cliente por su id.", description = "Elimina un cliente.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Eliminación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el cliente."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    // Agregamos : @Parameter(description = "ID del cliente a eliminar", required = true).
    // Esto es para agregar una explicación de que hace esa variable y señalar que es obligatoria.
    // No es necesaria para que swagger reconozca el argumento en la URL.
    public ResponseEntity<?> borrar(@Parameter(description = "ID del cliente a eliminar", required = true)
        @PathVariable Long id){
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
    @Operation(summary = "Actualiza un cliente por su id.", description = "Actualiza un cliente.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Actualización ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el cliente."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<Cliente> actualizar (
        // Parameter es para el swagger.
        @Parameter(description = "ID del cliente a actualizar", required = true)
        @PathVariable long id, 
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nuevos datos del cliente",
            required = true,
            content = @Content(schema = @Schema(implementation = Cliente.class))
        )
        @RequestBody Cliente c){
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
    @Operation(summary = "Simula un ingreso a la cuenta de un cliente", description = "Ingreso de sesión de un cliente.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Ingreso ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el cliente."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<String> ingresar(
        // Parameter es para el swagger.
        @Parameter(description = "Nombre del cliente", required = true)@PathVariable String nombre, 
        @Parameter(description = "Clave del cliente", required = true)@PathVariable String clave){
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

    // Método que actualiza el carro del cliente, pasa un carrito que se pagó a la lista de carritos pagados,
    // y le pasa al cliente un nuevo carrito para comprar.
    @PutMapping("/actualizarCarro/{id}")
    @Operation(summary = "Actualiza el carrito de un cliente por su id.", 
    description = "Obtiene un cliente, pasa su carrito pagado a la lista de carritos pagados y lo reempaza por un carrito nuevo.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Reemplazo ejecutado exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el cliente."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<Cliente> actualizarCarro(@Parameter(description = "Id del cliente", required = true)
    @PathVariable long id){
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
