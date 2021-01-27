package models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;
import org.checkerframework.common.aliasing.qual.Unique;
import org.h2.util.json.JSONObject;
import play.data.validation.Constraints;
import play.libs.Json;

import javax.persistence.*;
import javax.validation.Constraint;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Receta extends Model {
    @Id long id;
    @Constraints.Required(message = "El nombre de la receta es obligatorio")
    @Column(unique = true)
    @Constraints.MinLength(message = "El nombre debe tener 3 carácteres o mas",value = 3)
    String nombre;
    @Constraints.Required(message = "La descripcion de la receta es obligatoria")
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
    public static Receta findByName(String nombre){
        return find.query().where().eq("NOMBRE",nombre).findOne();
    }
    public static List<Receta> findAll(){return find.query().findList(); }
    public static Receta findById(Integer id){
        return find.query().where().eq("id",id).findOne();
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
    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }
    public void addIngrediente(Ingrediente ingrediente_nuevo) {
        this.ingredientes.add(ingrediente_nuevo);
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

    //parametro modo para controlar cuando se muestran los demas modelos que contienen receta, procurando que no se efectue un bucle infinito al formar el json
    //modo = 3 venimos de el modelo Autor
    //modo = 2 al venir del controlador pasamos este parametro para indicar al metodo del autor que No debe mostrar recetas
    public String toJson(int modo){
        ArrayNode respuesta = Json.newArray();
        ObjectNode receta = Json.newObject();
        ObjectNode ingredientes = Json.newObject();


        receta.set("nombre", Json.toJson(this.nombre));
        receta.set("descripcion",Json.toJson(this.descripcion));

        if(modo != 3)
        {
            if(this.autor != null){
                receta.set("autor", Json.parse(this.autor.toJson(modo).replace("/\\/g", "")));
            }
        }

        if(this.tipo != null){
            receta.set("tipo", Json.parse(this.tipo.toJson().replace("/\\/g", "")));
        }
        if(this.ingredientes.size() > 0){
            for(int i =0;i<this.ingredientes.size();i++)
            {
                ingredientes.set("ingrediente"+(i+1), Json.parse(this.ingredientes.get(i).toJson().replace("/\\/g", "")));
            }
            receta.set("ingredientes", ingredientes);
        }

        respuesta.add(receta);
        return respuesta.toString();
    }

}
