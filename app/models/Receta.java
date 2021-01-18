package models;


import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;
import org.checkerframework.common.aliasing.qual.Unique;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.Constraint;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Receta extends Model {
    @Id long id;
    @Constraints.Required(message = "El nombre de la receta es obligatorio")
    @Unique
            @Constraints.MinLength(message = "El nombre debe tener 3 carácteres o mas",value = 3)
    String nombre;

    String descripcion;
    @OneToOne(cascade = CascadeType.ALL)
    public Tipo tipo;

    @ManyToOne
    public Autor autor;
    @ManyToMany(cascade = CascadeType.ALL)
    public List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
    @CreatedTimestamp
    Timestamp fecha_creacion;
    @UpdatedTimestamp
    Timestamp ultima_actualizacion;

    public static final Finder<Long,Receta> find = new Finder<>(Receta.class);
    //METODOS BASE DE DATOS
    public static Integer findNumeroDeRecetas(){
        return find.query().findCount();
    }
    /*public Receta(String nombre, String descripcion,long autor, long tipo)
    {
        this.nombre = nombre;
        this.descripcion = descripcion;
        Autor autor_aux = new Autor();
        this.autor = autor_aux.findAutorById(autor);
        Tipo tipo_aux = new Tipo();
        this.tipo = tipo_aux.findTipoById(tipo);
    }*/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Timestamp getUltima_actualizacion() {
        return ultima_actualizacion;
    }

    public void setUltima_actualizacion(Timestamp ultima_actualizacion) {
        this.ultima_actualizacion = ultima_actualizacion;
    }

}
