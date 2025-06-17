/* 
Comentamos esta prueba ya que al ser una prueba de integración nesesita conectarse a la base de datos cada vez y eso
dificulta mucho hacer push en git, solo vamos a hacer pruebas unitarias.


package com.ventasWeb.cl.ventasWeb.Model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionSystemException;

import static org.assertj.core.api.Assertions.assertThat;

import com.ventasWeb.cl.ventasWeb.model.Producto;


// Esta excepción es para manejar la falta de conección, en particular es para cuando hago un push en github y que no me de error 
// El pipeline en el paso "build with maven", así seguimos realizando los otros test cuando hacemos un push.
class DatabaseConnectionException extends RuntimeException {
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}


// Esta anotación sirve para probar solo la persistencia de los datos (Test de integración).
@DataJpaTest
// Le dice a JpaTest que use la base de datos actual en vez de crear una base de datos para test dentro de la base de datos actual.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductoTest {

    // TestEntityManager es una versión simplificada de EntityManager y se usa para probar entidades JPA.
    // Utilidad: simula operaciones de persistencia, aísla el contexto, garantiza consistencia.
    // Funcionalidad: contiene métodos que permiten manipular entidades en entornos de pruebas.
    @Autowired
    private TestEntityManager tem;

    // Métodos. 
    // Guardar con dato válidos.
    // Los métodos con anotación @Test siempre son públicos, por eso no se pone abajo, sería redundante.
    @Test
    void guardarBien(){
        // Primero se generan los datos que vamos a dar de argumentos.
        Producto p = new Producto();
        p.setNombre("galletas integrales");
        p.setPrecio(1200);
        p.setCantidad(40);
        p.setMaterial("cartón");
        p.setFecha_vencimiento("12/08/2025");
        p.setRefrigerar(false);

        // Encerramos esta parte en un try/catch para manejar cuando no hay conección.
        try {
            // Ejecutamos la acción.
            Producto pGuardado = tem.persistAndFlush(p);

            // Recuperamos el producto por id y verificar los datos.
            Producto pRecuperado = tem.find(Producto.class, pGuardado.getId_producto());
            // Revisamos que los valores sean los que corresponden.
            // assertThat: Confirma que...
            assertThat(pRecuperado).isNotNull();
            assertThat(pRecuperado.getNombre().equals("galletas integrales"));
            assertThat(pRecuperado.getPrecio().equals(1200));
            assertThat(pRecuperado.getCantidad().equals(40));
            assertThat(pRecuperado.getMaterial().equals("cartón"));
            assertThat(pRecuperado.getFecha_vencimiento().equals("12/08/2025"));
            assertThat(pRecuperado.isRefrigerar() == false);
        } catch (TransactionSystemException | DataAccessException e){
            // Captura excepciones relacionadas con transacciones o acceso a datos
            throw new DatabaseConnectionException("No se pudo conectar a la base de datos: " + e.getMessage(), e);
        }
    }
} */
