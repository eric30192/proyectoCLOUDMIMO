package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;
import play.libs.Json;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.sql.Timestamp;
import java.util.Set;

@Entity
public class Ingrediente extends Model {
    @Id
    long id;

    String nombre;
    String familia;
    @JsonBackReference
    @ManyToMany(mappedBy = "ingredientes")
    public Set<Receta> recetas;
    @CreatedTimestamp
    Timestamp fecha_creacion;
    @UpdatedTimestamp
    Timestamp ultima_actualizacion;


    public static final Finder<Long,Ingrediente> find = new Finder<>(Ingrediente.class);
    //METODOS BASE DE DATOS
    public static Ingrediente findTipoById(long id){
        return find.byId(id);
    }
    public static Ingrediente findByName(String nombre_buscado){
        return find.query().where().eq("NOMBRE",nombre_buscado).findOne();
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

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
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

    public String toJson(){
        ArrayNode respuesta = Json.newArray();
        ObjectNode ingrediente = Json.newObject();

        ingrediente.set("nombre", Json.toJson(this.nombre));
        ingrediente.set("descripcion",Json.toJson(this.familia));

        respuesta.add(ingrediente);

        return respuesta.toString();
    }

}
