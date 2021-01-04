package models;


import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Receta extends Model {
    @Id long id;

    String nombre;
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

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
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
