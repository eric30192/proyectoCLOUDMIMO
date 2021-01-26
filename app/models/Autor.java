package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;
import play.libs.Json;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Autor extends Model {
    @Id
    long id;
    String nombre;
    String apellidos;
    String ciudad_natal;

    @JsonBackReference
    @OneToMany(cascade= CascadeType.ALL,mappedBy = "autor")
    public List<Receta> recetas_del_autor;
    @CreatedTimestamp
    Timestamp fecha_creacion;
    @UpdatedTimestamp
    Timestamp ultima_actualizacion;

    public static Autor findByName(String nombre){
        return find.query().where().eq("NOMBRE",nombre).findOne();
    }
    public static final Finder<Long,Autor> find = new Finder<>(Autor.class);
    //METODOS BASE DE DATOS
    public static Autor findAutorById(long id){
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCiudad_natal() {
        return ciudad_natal;
    }

    public void setCiudad_natal(String ciudad_natal) {
        this.ciudad_natal = ciudad_natal;
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
        ObjectNode autor = Json.newObject();

        autor.set("nombre", Json.toJson(this.nombre));
        autor.set("apellidos",Json.toJson(this.apellidos));
        autor.set("ciudad_natal",Json.toJson(this.ciudad_natal));
        respuesta.add(autor);

        return respuesta.toString();
    }
}
