package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints;
import play.libs.Json;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Tipo extends Model {
    @Id
    long id;

    @Constraints.Required(message = "El nombre del tipo es obligatorio")
    @Column(unique = true)
    @Constraints.MinLength(message = "El nombre debe tener 2 carácteres o mas",value = 2)
    String nombre_tipo;
    @Constraints.Required(message = "La descripcion del tipo es obligatoria")
    String descripcion;

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
    public static Integer findNumeroDeTipos(){
        return find.query().findCount();
    }
    public static List<Tipo> findAll(){return find.query().findList(); }
    public long getId() {
        return id;
    }
    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
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
