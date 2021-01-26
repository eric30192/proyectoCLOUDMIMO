package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Ingrediente;
import models.Receta;
import org.w3c.dom.Document;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;
import play.data.FormFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class RecetasController extends Controller {
    @Inject
    FormFactory formFactory;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result create(String nombre, String descripcion, Http.Request request) {
        Receta receta_aux = new Receta();
        Form<Receta> form = formFactory.form(Receta.class);

        //COMPROBAMOS CONTENT TYPE NE LA CABECERA
        if (request.contentType().get().equals("application/json")) {
            form = formFactory.form(Receta.class).bindFromRequest(request);
            if (form.hasErrors()) {
                System.out.println(form.errorsAsJson());
                return Results.notAcceptable(form.errorsAsJson());
            } else {
                receta_aux = form.get();
            }

        } else if (request.contentType().get().equals("application/xml")) {
            Document doc = request.body().asXml();

            doc.getChildNodes().item(0).getFirstChild().getTextContent();
            receta_aux.setNombre(doc.getFirstChild().getChildNodes().item(1).getTextContent());
            receta_aux.setDescripcion(doc.getFirstChild().getChildNodes().item(3).getTextContent());
            //form.fill(receta_aux);
            form = formFactory.form(Receta.class).fill(receta_aux);
            if (form.hasErrors()) {
                return Results.notAcceptable(form.errorsAsJson());
            } else {
                receta_aux = form.get();
            }
        } else {
            //jugamos con parametros directamente
            receta_aux.setNombre(nombre.toString());
            receta_aux.setDescripcion(descripcion.toString());
            form.fill(receta_aux);

        }


        //PROCESAMOS LA RESPUESTA
        if (request.accepts("application/json")) {


            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (receta_aux.findByName(receta_aux.getNombre()) == null) {
                try {
                    //System.out.println(receta_aux.getNombre() + " " + receta_aux.getAutor() + " " + receta_aux.getTipo());

                    Receta receta = new Receta();
                    receta = receta_aux;
                    receta.save();

                    succes.put("success", true);
                    info.put("info", "Receta insertada con éxito!");

                } catch (Exception e) {
                    succes.put("success", false);
                    info.put("info", "Fallo al insertar la receta!");
                }

            } else {
                succes.put("success", false);
                info.put("info", "Ya existe una receta con ese nombre!");
            }
            respuesta.add(succes);
            respuesta.add(info);
            return Results.ok(respuesta).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (receta_aux.findByName(receta_aux.getNombre()) == null) {
                try {
                    Receta receta = new Receta();
                    receta = receta_aux;
                    receta.save();
                    succes_aux = "true";
                    mensaje_aux = "Receta insertada con éxito!";
                } catch (Exception e) {
                    succes_aux = "false";
                    mensaje_aux = "No se ha podido insetar la receta";
                }
            } else {
                succes_aux = "false";
                mensaje_aux = "Ya existe una receta con ese nombre!";
            }
            Content content = views.xml.receta.render(succes_aux, mensaje_aux);

            return Results.ok(content).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

        } else {
            return badRequest("Unsupported format");
        }

    }

    public Result update(String nombre, String nombre_nuevo, String descripcion_nuevo, String nombre_autor_nuevo, String nombre_tipo_nuevo, String ingrediente_nuevo, Http.Request request) {
        Receta receta_aux = new Receta();
        receta_aux = receta_aux.findByName(nombre);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (receta_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ninguna receta con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

            } else {
                if (nombre_nuevo != "") {
                    receta_aux.setNombre(nombre_nuevo);
                }
                if (descripcion_nuevo != "") {
                    receta_aux.setDescripcion(descripcion_nuevo);
                }
                if (nombre_autor_nuevo != "" && receta_aux.autor.findByName(nombre_autor_nuevo) != null) {
                    receta_aux.setAutor(receta_aux.autor.findByName(nombre_autor_nuevo));
                }
                if (nombre_tipo_nuevo != "" && receta_aux.tipo.findByName(nombre_tipo_nuevo) != null) {

                    receta_aux.setTipo(receta_aux.tipo.findByName(nombre_tipo_nuevo));
                }
                //Definimos un ingrediente auxiliar para poder buscar ingredientes por nombre en el caso de qe la receta no tenga ningun ingrediente previamente
                Ingrediente ingrediente_aux = new Ingrediente();
                if (ingrediente_nuevo != "" && ingrediente_aux.findByName(ingrediente_nuevo) != null && (receta_aux.getIngredientes().indexOf(ingrediente_aux.findByName(ingrediente_nuevo))) == -1) {
                    receta_aux.addIngrediente(ingrediente_aux.findByName(ingrediente_nuevo));
                }
                receta_aux.update();
                succes.put("success", true);
                info.put("info", "Receta modificado con éxito!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());
            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (receta_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ninguna receta con ese nombre!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

            } else {
                if (nombre_nuevo != "") {
                    receta_aux.setNombre(nombre_nuevo);
                }
                if (descripcion_nuevo != "") {
                    receta_aux.setDescripcion(descripcion_nuevo);
                }
                if (nombre_autor_nuevo != "" && receta_aux.autor.findByName(nombre_autor_nuevo) != null) {
                    receta_aux.setAutor(receta_aux.autor.findByName(nombre_autor_nuevo));
                }
                if (nombre_tipo_nuevo != "" && receta_aux.tipo.findByName(nombre_tipo_nuevo) != null) {
                    receta_aux.setTipo(receta_aux.tipo.findByName(nombre_tipo_nuevo));
                }
                //Definimos un ingrediente auxiliar para poder buscar ingredientes por nombre en el caso de qe la receta no tenga ningun ingrediente previamente
                Ingrediente ingrediente_aux = new Ingrediente();
                if (ingrediente_nuevo != "" && ingrediente_aux.findByName(ingrediente_nuevo) != null && (receta_aux.getIngredientes().indexOf(ingrediente_aux.findByName(ingrediente_nuevo))) == -1) {
                    receta_aux.addIngrediente(ingrediente_aux.findByName(ingrediente_nuevo));
                }
                receta_aux.update();
                succes_aux = "true";
                mensaje_aux = "Receta modificado con éxito!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.ok(content).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }

    }

    public Result delete(String nombre, Http.Request request) {
        Receta receta_aux = new Receta();
        receta_aux = receta_aux.findByName(nombre);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (receta_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ninguna receta con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

            } else {
                receta_aux.delete();
                succes.put("success", true);
                info.put("info", "Receta eliminado con éxito!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (receta_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ninguna receta con ese nombre!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

            } else {
                receta_aux.delete();
                succes_aux = "true";
                mensaje_aux = "Receta modificado con éxito!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.ok(content).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }

    public Result devolver(String nombre, Http.Request request) {
        Receta receta_aux = new Receta();
        receta_aux = receta_aux.findByName(nombre);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (receta_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ninguna receta con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

            } else {
                succes.put("success", true);
                //Descarto este metodo porque no me gusta el resultado y prefiero crear mi json a mano para conseguir un mejor resultado
                /*ObjectMapper mapper = new ObjectMapper();
                try{
                String dtoAsString = mapper.writeValueAsString(receta_aux);
                info.set("receta", Json.toJson(dtoAsString));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }*/

                String receta = receta_aux.toJson();
                info.set("receta", Json.parse(receta.replace("/\\/g", "")));
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (receta_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ninguna receta con ese nombre!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

            } else {
                Content content = views.xml.devolverReceta.render(receta_aux,1);
                return Results.ok(content).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }

    public Result listar(Http.Request request) {

        Receta receta_aux = new Receta();
        List<Receta> recetas = receta_aux.findAll();

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (recetas.size()== 0) {
                succes.put("success", false);
                info.put("info", "No existe ninguna receta!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

            } else {
                succes.put("success", true);
                for (int i=0;i<recetas.size();i++)
                {
                    String receta = recetas.get(i).toJson();
                    info.set("receta"+(i+1), Json.parse(receta.replace("/\\/g", "")));
                }
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());
            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (recetas.size()== 0) {
                succes_aux = "false";
                mensaje_aux = "No existe ninguna receta!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());

            } else {
                Content content = views.xml.devolverRecetas.render(recetas);
                return Results.ok(content).withHeader("X-User-Count", receta_aux.findNumeroDeRecetas().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }
}
