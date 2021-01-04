package models;

import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;

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

    @OneToMany(cascade= CascadeType.ALL,mappedBy = "autor")
    public List<Receta> recetas_del_autor;
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
}
