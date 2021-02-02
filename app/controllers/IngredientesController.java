package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Ingrediente;
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
public class IngredientesController extends Controller {
    @Inject
    FormFactory formFactory;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result create(String nombre, String familia, Http.Request request) {
        Ingrediente ingrediente_aux = new Ingrediente();
        Form<Ingrediente> form = formFactory.form(Ingrediente.class);

        //COMPROBAMOS CONTENT TYPE NE LA CABECERA
        if (request.contentType().get().equals("application/json")) {
            form = formFactory.form(Ingrediente.class).bindFromRequest(request);
            if (form.hasErrors()) {
                return Results.notAcceptable(form.errorsAsJson());
            } else {
                ingrediente_aux = form.get();
            }

        } else if (request.contentType().get().equals("application/xml")) {
            Document doc = request.body().asXml();

            doc.getChildNodes().item(0).getFirstChild().getTextContent();
            ingrediente_aux.setNombre(doc.getFirstChild().getChildNodes().item(1).getTextContent());
            ingrediente_aux.setFamilia(doc.getFirstChild().getChildNodes().item(3).getTextContent());

            form = formFactory.form(Ingrediente.class).fill(ingrediente_aux);
            if (form.hasErrors()) {
                return Results.notAcceptable(form.errorsAsJson());
            } else {
                ingrediente_aux = form.get();
            }
        } else {
            //jugamos con parametros directamente
            ingrediente_aux.setNombre(nombre.toString());
            ingrediente_aux.setFamilia(familia.toString());
            form = formFactory.form(Ingrediente.class).fill(ingrediente_aux);
            if (form.hasErrors()) {
                return Results.notAcceptable(form.errorsAsJson());
            } else {
                ingrediente_aux = form.get();
            }

        }


        //PROCESAMOS LA RESPUESTA
        if (request.accepts("application/json")) {


            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (ingrediente_aux.findByName(ingrediente_aux.getNombre()) == null) {
                try {
                    Ingrediente ingrediente = new Ingrediente();
                    ingrediente = ingrediente_aux;
                    ingrediente.save();

                    succes.put("success", true);
                    info.put("info", "Ingrediente insertado con éxito!");

                } catch (Exception e) {
                    succes.put("success", false);
                    info.put("info", "Fallo al insertar el ingrediente!");
                }

            } else {
                succes.put("success", false);
                info.put("info", "Ya existe un ingrediente con ese nombre!");
            }
            respuesta.add(succes);
            respuesta.add(info);
            return Results.ok(respuesta).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (ingrediente_aux.findByName(ingrediente_aux.getNombre()) == null) {
                try {
                    Ingrediente ingrediente = new Ingrediente();
                    ingrediente = ingrediente_aux;
                    ingrediente.save();
                    succes_aux = "true";
                    mensaje_aux = "Ingrediente insertado con éxito!";
                } catch (Exception e) {
                    succes_aux = "false";
                    mensaje_aux = "No se ha podido insetar el ingrediente";
                }
            } else {
                succes_aux = "false";
                mensaje_aux = "Ya existe un ingrediente con ese nombre!";
            }
            Content content = views.xml.receta.render(succes_aux, mensaje_aux);

            return Results.ok(content).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

        } else {
            return badRequest("Unsupported format");
        }

    }

    public Result update(String nombre, String nombre_nuevo, String familia_nuevo, Http.Request request) {
        Ingrediente ingrediente_aux = new Ingrediente();
        ingrediente_aux = ingrediente_aux.findByName(nombre);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (ingrediente_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningun ingrediente con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            } else {
                if (nombre_nuevo != "") {
                    ingrediente_aux.setNombre(nombre_nuevo);
                }
                if (familia_nuevo != "") {
                    ingrediente_aux.setFamilia(familia_nuevo);
                }

                ingrediente_aux.update();
                succes.put("success", true);
                info.put("info", "Ingrediente modificado con éxito!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());
            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (ingrediente_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun ingrediente con ese nombre!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            } else {
                if (nombre_nuevo != "") {
                    ingrediente_aux.setNombre(nombre_nuevo);
                }
                if (familia_nuevo != "") {
                    ingrediente_aux.setFamilia(familia_nuevo);
                }

                ingrediente_aux.update();
                succes_aux = "true";
                mensaje_aux = "Ingrediente modificado con éxito!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.ok(content).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }

    }

    public Result delete(String nombre, Http.Request request) {
        Ingrediente ingrediente_aux = new Ingrediente();
        ingrediente_aux = ingrediente_aux.findByName(nombre);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (ingrediente_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningun ingrediente con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            } else {
                ingrediente_aux.delete();
                succes.put("success", true);
                info.put("info", "Ingrediente eliminado con éxito!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (ingrediente_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun ingrediente con ese nombre!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            } else {
                ingrediente_aux.delete();
                succes_aux = "true";
                mensaje_aux = "Tipo ingrediente con éxito!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.ok(content).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }


    public Result devolver(String nombre, Http.Request request) {
        Ingrediente ingrediente_aux = new Ingrediente();
        ingrediente_aux = ingrediente_aux.findByName(nombre);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (ingrediente_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningún ingrediente con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            } else {
                succes.put("success", true);
                String tipo = ingrediente_aux.toJson();
                info.set("tipo", Json.parse(tipo.replace("/\\/g", "")));
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (ingrediente_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningún ingrediente con ese nombre!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            } else {
                Content content = views.xml._ingrediente.render(ingrediente_aux,2);
                return Results.ok(content).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }


    public Result devolverById(Integer id, Http.Request request) {
        Ingrediente ingrediente_aux = new Ingrediente();
        ingrediente_aux = ingrediente_aux.findTipoById(id);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (ingrediente_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningún ingrediente con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            } else {
                succes.put("success", true);
                String tipo = ingrediente_aux.toJson();
                info.set("tipo", Json.parse(tipo.replace("/\\/g", "")));
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (ingrediente_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningún ingrediente con ese nombre!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            } else {
                Content content = views.xml._ingrediente.render(ingrediente_aux,2);
                return Results.ok(content).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }
    public Result listar(Http.Request request) {

        Ingrediente ingrediente_aux = new Ingrediente();
        List<Ingrediente> ingredientes = ingrediente_aux.findAll();

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (ingredientes.size()== 0) {
                succes.put("success", false);
                info.put("info", "No existe ningun ingrediente!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            } else {
                succes.put("success", true);
                for (int i=0;i<ingredientes.size();i++)
                {
                    String autor = ingredientes.get(i).toJson();
                    info.set("ingrediente"+(i+1), Json.parse(autor.replace("/\\/g", "")));
                }
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());
            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (ingredientes.size()== 0) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun tipo!";
                Content content = views.xml.receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());

            } else {
                Content content = views.xml.ingredientes.render(ingredientes);
                return Results.ok(content).withHeader("X-User-Count", ingrediente_aux.findNumeroDeIngredientes().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }
}
