package com.ventasWeb.cl.ventasWeb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;
import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.repository.DetalleCarritoRepository;


@Service
@Transactional
public class DetalleCarritoService {

    // LLamamos al repositorio de la clase.    
    @Autowired
    private DetalleCarritoRepository DCR;
    // LLamamos al repositorio de producto.
    @Autowired
    private ProductoService PS;

    // Métodos.
    // Buscar todos.
    public List<DetalleCarrito> buscarTodos(){
        return DCR.findAll();
    }
    // Buscar por id.
    public DetalleCarrito buscarPorId(long id){
        return DCR.findById(id).get();
    }
    // Guardar.
    public DetalleCarrito guardar(DetalleCarrito p){
        return DCR.save(p);
    }
    // Borrar.
    public void borrar(long id){
        DCR.deleteById(id);
    }

    // Calcular valor.
    public int valor(long id){
        // Buscamos el detalleCarrito por id y luego lo guardamos en una variable.
        // Para buscar por id tenemos que convertir int a long.
        DetalleCarrito DC = DCR.findById(id).get();
        // Tendiendo el detalleCarrito creamos una variable producto y guardamos el producto que tiene.
        Producto producto = PS.buscarPorId(DC.getProducto().getId_producto());
        // Teniendo el pedido creamos una variable int y guardamos la cantidad.
        int cantidad = DC.getCantidadSolicitada();
        // Tendiendo el producto creamos una variable int y guardamos el precio.
        int precio = producto.getPrecio();
        // Retornamos el valor del precio por la cantidad.
        return precio * cantidad;
    }

    // Agregar producto a un detalleCarrito nuevo.
    public DetalleCarrito agregarProducto(int id_producto, int cantidad){
        // Buscamos el producto y lo guardamos en una variable.
        Producto p = PS.buscarPorId(id_producto);
        // Creamos un detalleCarrito.
        DetalleCarrito dc = new DetalleCarrito();
        // Asignamos el producto y la cantidad al dc.
        dc.setProducto(p);
        dc.setCantidadSolicitada(cantidad);
        // Lo guardamos y retornamos.
        return DCR.save(dc);
    }

    // Cambiar la cantidad de un producto en el detalleCarrito.
    public DetalleCarrito cambiarCantidad(int id_carrito, int cantidad){
        // Buscamos por id e ingresamos la nueva cantidad.
        DCR.findById(Long.valueOf(id_carrito)).get().setCantidadSolicitada(cantidad);
        // Retornamos el detalle editado.
        return DCR.findById(Long.valueOf(id_carrito)).get();
    }
}
