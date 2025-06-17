package com.ventasWeb.cl.ventasWeb.service;

import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.repository.ProductoRepository;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional; //  Sirve para las consultas
import org.springframework.beans.factory.annotation.Autowired; // Para no instanciar nuevos objetos si ya existen. 
import org.springframework.stereotype.Service; //  Las llamadas de los servicios ocupan esto.


@Service
@Transactional
public class ProductoService {

    // LLamamos al repositorio de la clase.
    @Autowired
    private ProductoRepository PR;
 
    // Métodos.
    // Buscar todos.
    public List<Producto> buscarTodos(){
        return PR.findAll();
    }
    // Buscar por id.
    public Producto buscarPorId(long id){
        return PR.findById(id).get();
    }
    // Guardar.
    public Producto guardar(Producto p){
        return PR.save(p);
    }
    // Borrar.
    public void borrar(long id){
        PR.deleteById(id);
    }

    // Buscar por nombre.
    // Esta función puede retornar un producto vacío.
    public Producto buscarPorNombre(String nombre){
        // Primero buscamos los productos y los guardamos en una lista.
        List<Producto> productos = PR.findAll();
        // Luego creamos una variable que va a guardar el producto que buscamos.
        Producto produ = new Producto();
        // Luego iteramos por los productos.
        for (Producto p : productos){
            // Comparamos los nombres.
            if (p.getNombre().equals(nombre)){
                produ = p;
            }
        }
        // Retornamos el producto.
        return produ;
    }
    // Buscar todos los ids.
    public List<Long> buscarTodosIds(){
        // Primero buscamos todos los productos y los guardamos en una lista.
        List<Producto> productos = PR.findAll();
        // Luego vamos a crear un arreglo de Integer donde vamos a guardar los ids, recuera que listas no se pueden instanciar vacías.
        ArrayList<Long> ids = new ArrayList<>();
        // Iteramos por la lista de productos y agregamos los ids al arreglo.
        for (Producto p : productos){
            ids.add(p.getId_producto());
        }
        // Creamos una lista de ids y la llenamos con el contenido del arreglo.
        List<Long> IDs = ids;
        // Retornamos la lista.
        return IDs;
    }
}
