package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Autor;
import models.Tipo;
import org.w3c.dom.Document;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;

import javax.inject.Inject;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class TiposController extends Controller {
    @Inject
    FormFactory formFactory;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result create(String nombre, String descripcion, Http.Request request) {
        Tipo tipo_aux = new Tipo();
        Form<Tipo> form = formFactory.form(Tipo.class);

        //COMPROBAMOS CONTENT TYPE NE LA CABECERA
        if (request.contentType().get().equals("application/json")) {
            form = formFactory.form(Tipo.class).bindFromRequest(request);
            if (form.hasErrors()) {
                return Results.notAcceptable(form.errorsAsJson());
            } else {
                tipo_aux = form.get();
            }

        } else if (request.contentType().get().equals("application/xml")) {
            Document doc = request.body().asXml();

            doc.getChildNodes().item(0).getFirstChild().getTextContent();
            tipo_aux.setNombre_tipo(doc.getFirstChild().getChildNodes().item(1).getTextContent());
            tipo_aux.setDescripcion(doc.getFirstChild().getChildNodes().item(3).getTextContent());

            form = formFactory.form(Tipo.class).fill(tipo_aux);
            if (form.hasErrors()) {
                return Results.notAcceptable(form.errorsAsJson());
            } else {
                tipo_aux = form.get();
            }
        } else {
            //jugamos con parametros directamente
            tipo_aux.setNombre_tipo(nombre.toString());
            tipo_aux.setDescripcion(descripcion.toString());
            form = formFactory.form(Tipo.class).fill(tipo_aux);
            if (form.hasErrors()) {
                return Results.notAcceptable(form.errorsAsJson());
            } else {
                tipo_aux = form.get();
            }

        }


        //PROCESAMOS LA RESPUESTA
        if (request.accepts("application/json")) {


            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (tipo_aux.findByName(tipo_aux.getNombre_tipo()) == null) {
                try {
                    Tipo tipo = new Tipo();
                    tipo = tipo_aux;
                    tipo.save();

                    succes.put("success", true);
                    info.put("info", "Tipo insertado con éxito!");

                } catch (Exception e) {
                    succes.put("success", false);
                    info.put("info", "Fallo al insertar el tipo!");
                }

            } else {
                succes.put("success", false);
                info.put("info", "Ya existe un tipo con ese nombre!");
            }
            respuesta.add(succes);
            respuesta.add(info);
            return Results.ok(respuesta).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (tipo_aux.findByName(tipo_aux.getNombre_tipo()) == null) {
                try {
                    Tipo tipo = new Tipo();
                    tipo = tipo_aux;
                    tipo.save();
                    succes_aux = "true";
                    mensaje_aux = "Tipo insertado con éxito!";
                } catch (Exception e) {
                    succes_aux = "false";
                    mensaje_aux = "No se ha podido insetar el tipo";
                }
            } else {
                succes_aux = "false";
                mensaje_aux = "Ya existe un tipo con ese nombre!";
            }
            Content content = views.xml.receta.render(succes_aux, mensaje_aux);

            return Results.ok(content).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

        } else {
            return badRequest("Unsupported format");
        }

    }

    public Result update(String nombre_tipo, String nombre_tipo_nuevo, String descripcion_nuevo, Http.Request request) {
        Tipo tipo_aux = new Tipo();
        tipo_aux = tipo_aux.findByName(nombre_tipo);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (tipo_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningun tipo con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            } else {
                if (nombre_tipo_nuevo != "") {
                    tipo_aux.setNombre_tipo(nombre_tipo_nuevo);
                }
                if (descripcion_nuevo != "") {
                    tipo_aux.setDescripcion(descripcion_nuevo);
                }

                tipo_aux.update();
                succes.put("success", true);
                info.put("info", "Tipo modificado con éxito!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());
            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (tipo_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun tipo con ese nombre!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            } else {
                if (nombre_tipo_nuevo != "") {
                    tipo_aux.setNombre_tipo(nombre_tipo_nuevo);
                }
                if (descripcion_nuevo != "") {
                    tipo_aux.setDescripcion(descripcion_nuevo);
                }

                tipo_aux.update();
                succes_aux = "true";
                mensaje_aux = "Tipo modificado con éxito!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.ok(content).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }

    }

    public Result delete(String nombre, Http.Request request) {
        Tipo tipo_aux = new Tipo();
        tipo_aux = tipo_aux.findByName(nombre);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (tipo_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningun tipo con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            } else {
                tipo_aux.delete();
                succes.put("success", true);
                info.put("info", "Tipo eliminado con éxito!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (tipo_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun tipo con ese nombre!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            } else {
                tipo_aux.delete();
                succes_aux = "true";
                mensaje_aux = "Tipo eliminado con éxito!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.ok(content).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }


    public Result devolver(String nombre, Http.Request request) {
        Tipo tipo_aux = new Tipo();
        tipo_aux = tipo_aux.findByName(nombre);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (tipo_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningun tipo con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            } else {
                succes.put("success", true);
                String tipo = tipo_aux.toJson();
                info.set("tipo", Json.parse(tipo.replace("/\\/g", "")));
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (tipo_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun autor con ese nombre!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            } else {
                Content content = views.xml._tipo.render(tipo_aux,2);
                return Results.ok(content).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }

    public Result devolverById(Integer id, Http.Request request) {
        Tipo tipo_aux = new Tipo();
        tipo_aux = tipo_aux.findTipoById(id);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (tipo_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningun tipo con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            } else {
                succes.put("success", true);
                String tipo = tipo_aux.toJson();
                info.set("tipo", Json.parse(tipo.replace("/\\/g", "")));
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (tipo_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun autor con ese nombre!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            } else {
                Content content = views.xml._tipo.render(tipo_aux,2);
                return Results.ok(content).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }

    public Result listar(Http.Request request) {

        Tipo tipo_aux = new Tipo();
        List<Tipo> tipos = tipo_aux.findAll();

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (tipos.size()== 0) {
                succes.put("success", false);
                info.put("info", "No existe ningun tipo!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            } else {
                succes.put("success", true);
                for (int i=0;i<tipos.size();i++)
                {
                    String autor = tipos.get(i).toJson();
                    info.set("tipo"+(i+1), Json.parse(autor.replace("/\\/g", "")));
                }
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());
            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (tipos.size()== 0) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun tipo!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());

            } else {
                Content content = views.xml.tipos.render(tipos);
                return Results.ok(content).withHeader("X-User-Count", tipo_aux.findNumeroDeTipos().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }
}
