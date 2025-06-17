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
import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;
import com.ventasWeb.cl.ventasWeb.model.VentaWeb;
import com.ventasWeb.cl.ventasWeb.service.ClienteService;
import com.ventasWeb.cl.ventasWeb.service.VentaWebService;


@RestController
@RequestMapping ("/api/v1/venta_web")
public class VentaWebController {

    // LLamamos el service de la clase.
    @Autowired
    private VentaWebService vws;
    @Autowired
    private ClienteService cs;

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo 
    // que se da cuando se comunica con una página web.

    // Método que lista las ventasWeb.
    @GetMapping("/listar")
    public ResponseEntity<List<VentaWeb>> buscarTodos(){
        // Buscamos las ventasWeb y las guardamos en una variable.
        List<VentaWeb> ventasWeb = vws.buscarTodos();
        // Si no encontramos ventasWeb reornamos una respuesta vacía.
        if (ventasWeb.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Si no, retornamos las ventasWeb en una respuesta.
        return ResponseEntity.ok(ventasWeb);
    }

    // Método que busca por id.
    @GetMapping ("/{id}")
    public ResponseEntity<VentaWeb> BuscaPorId(@PathVariable int id){
        // Encerramos la funcionalidad en un try/catch
        try {
            // Buscamos por id y guardamos la venta en una variable.
            VentaWeb vw = vws.buscarPorId(id);
            // Retornamos una respuesta que contiene la ventaWeb.
            return ResponseEntity.ok(vw);
        } catch (Exception e) {
            // Si algo falla retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para eliminar.
    @DeleteMapping ("/{id}")
    public ResponseEntity<?> borrar(Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Usamos el service directamente.
            vws.borrar(id);
            // Retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para actualizar.
    // Los argumentos que recibe la funcion es un id para buscar la ventaWeb a editar y recibe una ventaWeb nueva, 
    // los atributos de esta ventaWeb nueva van a reemplazar los atributos de la ventaWeb encontrada.
    @PutMapping ("/{id}")
    public ResponseEntity<VentaWeb> actualizar (@PathVariable Integer id, @RequestBody VentaWeb vw){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos la ventaWeb a editar y la guardamos en una variable.
            VentaWeb VW = vws.buscarPorId(id);
            // Reemplazamos los atributos.
            VW.setDireccion(vw.getDireccion());
            VW.setFecha(vw.getFecha());
            VW.setEnvio(vw.getEnvio());
            VW.setCliente(vw.getCliente());
            VW.setTotalPagado(vw.getTotalPagado());
            // Guardamos los cambios.
            vws.guardar(VW);
            // Retornamos la respuesta con la ventaWeb actualizada.
            return ResponseEntity.ok(VW);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    } 

    // Crea una venta
    @PostMapping("/agregar/{run}/{pago}")
    public ResponseEntity<?> comprar2(@PathVariable long run, @PathVariable int pago) {
        Cliente cliente = cs.buscarPorId(run);
        // Creamos una ventaWeb nueva y la guardamos en una variable.
        VentaWeb vw = vws.comprarCarrito(cliente.getRun_cliente(), pago);
        System.out.println("Revisando salida");
        System.out.println(vw.getCliente().getRun_cliente());
        // La guardamos
        vws.guardar(vw);

        // Escribimos una salida que señala los productos comprados:
        int total = vws.valorCarrito(run);
        System.out.println("Costo de la compra: " + total);
        Carrito carrito = cliente.getCarrito();
        List<DetalleCarrito> detalles = carrito.getDetallesCarrito();
        for (DetalleCarrito d : detalles){
            System.out.println("Producto: " + d.getProducto().getNombre() + ",  Cantidad: " + d.getCantidadSolicitada());
        }
        System.out.println("\n");

        // Retornamos una respuesta que contiene la ventaWeb.
        return ResponseEntity.ok(vw);
    }

    // Método para calcular el costo del carrito.
    @GetMapping("/carrito_costo/{id}")
    public ResponseEntity<String>valorCarrito(@PathVariable long id){
        // Calculamos el valor del carrito:
        int costo = vws.valorCarrito(id);
        String sentencia = ("El valor del carrito es: " + costo + "\n");
        // Buscamos el carrito:
        Carrito carrito = cs.buscarPorId(id).getCarrito();
        // Buscamos los detalles del carrito.
        String nombre;
        int cantidad;
        List<DetalleCarrito> detalles = carrito.getDetallesCarrito();
        for (DetalleCarrito d : detalles){
            nombre = d.getProducto().getNombre();
            cantidad = d.getCantidadSolicitada();
            sentencia += ("Producto: " + nombre + "       Cantidad: " + cantidad + "\n");
        }
        return ResponseEntity.ok(sentencia);
    }
}
