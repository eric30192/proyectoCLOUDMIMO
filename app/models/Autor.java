package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints;
import play.libs.Json;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Autor extends Model {
    @Id
    long id;
    @Constraints.Required(message = "El nombre del autor es obligatorio")
    @Column(unique = true)
    @Constraints.MinLength(message = "El nombre debe tener 2 carácteres o mas",value = 2)
    String nombre;
    String apellidos;
    @Constraints.Required(message = "La ciudad natal del autor es obligatorio")
    String ciudad_natal;


    @OneToMany(cascade= CascadeType.ALL,mappedBy = "autor")
    public List<Receta> recetas_del_autor;
    @CreatedTimestamp
    Timestamp fecha_creacion;
    @UpdatedTimestamp
    Timestamp ultima_actualizacion;

    public static final Finder<Long,Autor> find = new Finder<>(Autor.class);
    public static Autor findByName(String nombre){
        return find.query().where().eq("NOMBRE",nombre).findOne();
    }
    public static Integer findNumeroDeAutores(){
        return find.query().findCount();
    }
    public static List<Autor> findAll(){return find.query().findList(); }
    //METODOS BASE DE DATOS
    public static Autor findAutorById(long id){
        return find.byId(id);
    }
    public long getId() {
        return id;
    }

    public List<Receta> getRecetas_del_autor() {
        return recetas_del_autor;
    }

    public void setRecetas_del_autor(List<Receta> recetas_del_autor) {
        this.recetas_del_autor = recetas_del_autor;
    }
    public void addRecetas_del_autor(Receta receta_nueva) {
        this.recetas_del_autor.add(receta_nueva);
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

    public String toJson(Integer modo){
        ArrayNode respuesta = Json.newArray();
        ObjectNode autor = Json.newObject();
        ObjectNode recetas = Json.newObject();

        autor.set("nombre", Json.toJson(this.nombre));
        autor.set("apellidos",Json.toJson(this.apellidos));
        autor.set("ciudad_natal",Json.toJson(this.ciudad_natal));
        if(modo == 2)
        {

            if(this.recetas_del_autor.size() > 0){
                for(int i =0;i<this.recetas_del_autor.size();i++)
                {
                    recetas.set("receta"+(i+1), Json.parse(this.recetas_del_autor.get(i).toJson(3).replace("/\\/g", "")));
                }
                autor.set("recetas", recetas);
            }
        }
        respuesta.add(autor);
        return respuesta.toString();
    }
}
