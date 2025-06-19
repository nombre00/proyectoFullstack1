package com.ventasWeb.cl.ventasWeb.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import com.ventasWeb.cl.ventasWeb.model.Carrito;
import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;
import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.repository.CarritoRepository;

@Service
@Transactional
public class CarritoService {

    // LLamamos al repositorio de la clase y al service del pedido, producto y detalleProducto ya que las clases colaboran.    
    @Autowired
    private CarritoRepository cr;
    @Autowired
    private DetalleCarritoService dcs;
    @Autowired
    private ProductoService ps;
    @Autowired
    private ClienteService cs;

    // Métodos.

    // Buscar todos.
    public List<Carrito> buscarTodos(){
        return cr.findAll();
    }
    // Buscar por id.
    public Carrito buscarPorId(long id){
        return cr.findById(id).get();
    }
    // Guardar.
    public Carrito guardar(Carrito c){
        return cr.save(c);
    }
    // Borrar.
    public void borrar(long id){
        cr.deleteById(id);
    }

    // Calcular valor carrito. 
    public int valor(long id_carrito){
        // Buscamos el carrito por id y lo guardamos en una variable.
        Carrito carrito = cr.findById(id_carrito).get();
        // Creamos una lista de Integer que guarda los id de los detalle del carrito.
        List<DetalleCarrito> detalles = carrito.getDetallesCarrito();
        // Teniendo los detalles del carrito ahora podemos calcular el precio.
        int total = 0;
        // Iteramos los detalles y sumamos los costos.
        for (DetalleCarrito d : detalles){
            total += dcs.valor(d.getId_detalleCarrito());
        }
        // Retornamos el valor total del carrito.
        return total;
    }

    // Agregar detalle al carrito.
    @Transactional
    public Carrito agregarDetalleCarrito(long id_detalle, long id_carrito){
        // Buscamos el carrito y lo guardamos en una variable.
        Carrito carrito = cr.findById(id_carrito).get();
        // Buscamos el detalle y lo guardamos en una variable.
        carrito.getDetallesCarrito().add(dcs.buscarPorId(id_detalle));
        // Retornamos el carrito y lo guardamos.
        return cr.save(carrito);
    }

    // Agregar producto al carrito.
    @Transactional
    public Carrito agregarProductoCarrito(long id_producto, long run_cliente, int cantidad){
        // Buscamos el carrito y lo guardamos en una variable.
        Carrito carrito = cs.buscarPorId(run_cliente).getCarrito();
        // Buscamos el producto.
        Producto producto = ps.buscarPorId(id_producto);
        // Creamos un detalle nuevo.
        DetalleCarrito detalleNuevo = new DetalleCarrito();
        detalleNuevo.setCantidadSolicitada(cantidad);
        detalleNuevo.setCarrito(carrito);
        detalleNuevo.setProducto(producto);
        // Guardamos el detalle nuevo. 
        dcs.guardar(detalleNuevo);
        // Agregamos el detalle al carrito.
        carrito.getDetallesCarrito().add(detalleNuevo);
        // Guardamos el carrito actualizado.
        cr.save(carrito);
        // Guardamos el cliente.
        cs.guardar(cs.buscarPorId(run_cliente));
        return cr.save(carrito);
    }

    // Borrar un detalle de carrito.
    // Cuando hay colaboración de clases se usa @Transaccional.
    @Transactional
    public Carrito eliminarDetalle(long id_detalle, long id_carrito){
        // Buscamos el carrito y lo guardamos en una variable.
        Carrito carrito = cr.findById(id_carrito).get();
        // Iteramos los detalleCarrito del carrito.
        // Usamos Iterator para recorrer ids_detalleCarritos de forma segura.
        Iterator<DetalleCarrito> iterator = carrito.getDetallesCarrito().iterator();
        while (iterator.hasNext()) {
            // Creamos una variable para comparar las ids con id_detalle
            DetalleCarrito detalle = iterator.next();
            // Si las ids coinciden
            if (detalle.getId_detalleCarrito() == id_detalle) { 
                // Borramos el id de la lista.
                iterator.remove();
                // Borramos el detalle de la tabla de detalles
                dcs.borrar(detalle.getId_detalleCarrito());
                break; 
            }
        }
        // Retornamos el carrito actualizado.
        return cr.save(carrito);
    }

    // Buscar todos los id_producto del carrito.
    @Transactional
    public List<Long> buscarIDsProductos(long id_carrito){
        // Primero buscamos los detallesCarrito del carrito y los guardamos en una variable.
        List<DetalleCarrito> detalllesCarrito = cr.findById(id_carrito).get().getDetallesCarrito();
        // Luego creamos un arreglo de Integers donde guardar los ids, recuerda que no podemos instanciar listas vacías.
        ArrayList<Long> ids_productos = new ArrayList<>();
        // Luego iteramos por los detalles del carrito para sacar los ids.
        for (DetalleCarrito detCar : detalllesCarrito){
            // Agregamos los ids al arreglo.
            ids_productos.add(detCar.getProducto().getId_producto());
        }
        // Creamos una lista de Integers donde guardar los ids.
        List<Long> IDs = ids_productos;
        // Retornamos la lista.
        return IDs;
    }

    // Buscar detalles de carrito.
    @Transactional
    public List<DetalleCarrito> buscarDetalles(long id_carrito){
        // Creamos una lista de arreglos de detalleCarrito, no nos deja instanciar una lista vacía.
        List<DetalleCarrito> detallesC = cr.findById(id_carrito).get().getDetallesCarrito();
        // Retornamos la lista.
        return detallesC;
    }

    // Buscar los productos del carrito, revisa que existan en la tabla productos.
    @Transactional
    public boolean revisarStock(long id_carrito){
        // Primero buscamos los detallesCarrito del carrito y los guardamos en una variable.
        List<DetalleCarrito> detalllesCarrito = cr.findById(id_carrito).get().getDetallesCarrito();
        // Luego creamos un arreglo de Integers donde guardar los ids, recuerda que no podemos instanciar listas vacías.
        ArrayList<Long> ids_productosCarrito = new ArrayList<>();
        // Luego iteramos por los detalles del carrito para sacar los ids.
        for (DetalleCarrito detCar : detalllesCarrito){
            // Agregamos los ids al arreglo.
            ids_productosCarrito.add(detCar.getProducto().getId_producto());
        }
        // Creamos una lista de Integers donde guardar los ids de los productos de la base de datos.
        List<Long> IDs = ids_productosCarrito;
        // Buscamos todos los ids de los productos y los guardamos en una lista.
        List<Long> ids_productos = ps.buscarTodosIds();
        // Revisamos que todos los ids de productos existan.
        boolean existen = ids_productos.containsAll(IDs);
        // Retornamos el valor booleano.
        return existen;
    }

    // Revisamos el estado del carrito, si está pagado borramos los detalles para volver a ser usado.
    @Transactional
    public Carrito revisarPago(Carrito c){
        Carrito car = cr.findById(c.getId_carrito()).get();
        // Revisamos si el carrito está pagado.
        if (car.isPagado()){
            // Si lo está borramos los detalles.
            car.setDetallesCarrito(null);
            return cr.save(car);
        }else {
            return car;
        }
    }
}
