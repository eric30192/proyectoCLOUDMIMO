package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Tipo extends Model {
    @Id
    long id;

    String nombre;
    String descripcion;

    @OneToOne(mappedBy = "tipo")
    public Receta receta;

    public static final Finder<Long,Tipo> find = new Finder<>(Tipo.class);
    //METODOS BASE DE DATOS
    public static Tipo findTipoById(long id){
        return find.byId(id);
    }
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


}
