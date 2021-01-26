package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Finder;
import io.ebean.Model;
import play.libs.Json;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Tipo extends Model {
    @Id
    long id;

    String nombre_tipo;
    String descripcion;

    @JsonBackReference
    @OneToOne(mappedBy = "tipo")
    public Receta receta;

    public static final Finder<Long,Tipo> find = new Finder<>(Tipo.class);
    //METODOS BASE DE DATOS
    public static Tipo findTipoById(long id){
        return find.byId(id);
    }
    public static Tipo findByName(String nombre_buscado){
        return find.query().where().eq("NOMBRE_TIPO",nombre_buscado).findOne();
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre_tipo() {
        return nombre_tipo;
    }

    public void setNombre_tipo(String nombre_tipo) {
        this.nombre_tipo = nombre_tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String toJson(){
        ArrayNode respuesta = Json.newArray();
        ObjectNode tipo = Json.newObject();

        tipo.set("nombre", Json.toJson(this.nombre_tipo));
        tipo.set("descripcion",Json.toJson(this.descripcion));

        respuesta.add(tipo);

        return respuesta.toString();
    }
}
