package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Autor;
import models.Ingrediente;
import models.Receta;
import org.w3c.dom.Document;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;
import views.xml._autor;
import views.xml.autores;
import views.xml.receta;

import javax.inject.Inject;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class AutoresController extends Controller {
    @Inject
    FormFactory formFactory;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result create(String nombre, String apellidos, String ciudad_natal, Http.Request request) {
        Autor autor_aux = new Autor();
        Form<Autor> form = formFactory.form(Autor.class);

        //COMPROBAMOS CONTENT TYPE NE LA CABECERA
        if (request.contentType().get().equals("application/json")) {
            form = formFactory.form(Autor.class).bindFromRequest(request);
            if (form.hasErrors()) {
                System.out.println(form.errorsAsJson());
                return Results.notAcceptable(form.errorsAsJson());
            } else {
                autor_aux = form.get();
            }

        } else if (request.contentType().get().equals("application/xml")) {
            Document doc = request.body().asXml();

            doc.getChildNodes().item(0).getFirstChild().getTextContent();
            autor_aux.setNombre(doc.getFirstChild().getChildNodes().item(1).getTextContent());
            autor_aux.setApellidos(doc.getFirstChild().getChildNodes().item(3).getTextContent());
            autor_aux.setCiudad_natal(doc.getFirstChild().getChildNodes().item(5).getTextContent());
            //form.fill(receta_aux);
            form = formFactory.form(Autor.class).fill(autor_aux);
            if (form.hasErrors()) {
                return Results.notAcceptable(form.errorsAsJson());
            } else {
                autor_aux = form.get();
            }
        } else {
            //jugamos con parametros directamente
            autor_aux.setNombre(nombre.toString());
            autor_aux.setApellidos(apellidos.toString());
            autor_aux.setCiudad_natal(ciudad_natal.toString());
            form.fill(autor_aux);

        }


        //PROCESAMOS LA RESPUESTA
        if (request.accepts("application/json")) {


            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (autor_aux.findByName(autor_aux.getNombre()) == null) {
                try {
                    //System.out.println(receta_aux.getNombre() + " " + receta_aux.getAutor() + " " + receta_aux.getTipo());

                    Autor autor = new Autor();
                    autor = autor_aux;
                    autor.save();

                    succes.put("success", true);
                    info.put("info", "Autor insertado con éxito!");

                } catch (Exception e) {
                    succes.put("success", false);
                    info.put("info", "Fallo al insertar el autor!");
                }

            } else {
                succes.put("success", false);
                info.put("info", "Ya existe un autor con ese nombre!");
            }
            respuesta.add(succes);
            respuesta.add(info);
            return Results.ok(respuesta).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (autor_aux.findByName(autor_aux.getNombre()) == null) {
                try {
                    Autor autor = new Autor();
                    autor = autor_aux;
                    autor.save();
                    succes_aux = "true";
                    mensaje_aux = "Autor insertado con éxito!";
                } catch (Exception e) {
                    succes_aux = "false";
                    mensaje_aux = "No se ha podido insetar el autor";
                }
            } else {
                succes_aux = "false";
                mensaje_aux = "Ya existe un autor con ese nombre!";
            }
            Content content = receta.render(succes_aux, mensaje_aux);

            return Results.ok(content).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

        } else {
            return badRequest("Unsupported format");
        }

    }

    public Result update(String nombre, String nombre_nuevo, String apellido_nuevo, String ciudad_natal_nuevo, String nombre_receta_nuevo, Http.Request request) {
        Autor autor_aux = new Autor();
        autor_aux = autor_aux.findByName(nombre);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (autor_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningun autor con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

            } else {
                if (nombre_nuevo != "") {
                    autor_aux.setNombre(nombre_nuevo);
                }
                if (apellido_nuevo != "") {
                    autor_aux.setApellidos(apellido_nuevo);
                }
                if (ciudad_natal_nuevo != "") {
                    autor_aux.setCiudad_natal(ciudad_natal_nuevo);
                }
                if (nombre_receta_nuevo != "" && autor_aux.getRecetas_del_autor().get(0).findByName(nombre_receta_nuevo) != null && autor_aux.getRecetas_del_autor().indexOf(autor_aux.getRecetas_del_autor().get(0).findByName(nombre_receta_nuevo)) == -1) {
                    autor_aux.addRecetas_del_autor(autor_aux.getRecetas_del_autor().get(0).findByName(nombre_receta_nuevo));
                }

                autor_aux.update();
                succes.put("success", true);
                info.put("info", "Autor modificado con éxito!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());
            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (autor_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun autor con ese nombre!";
                Content content = receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

            } else {
                if (nombre_nuevo != "") {
                    autor_aux.setNombre(nombre_nuevo);
                }
                if (apellido_nuevo != "") {
                    autor_aux.setApellidos(apellido_nuevo);
                }
                if (ciudad_natal_nuevo != "") {
                    autor_aux.setCiudad_natal(ciudad_natal_nuevo);
                }
                if (nombre_receta_nuevo != "" && autor_aux.getRecetas_del_autor().get(0).findByName(nombre_receta_nuevo) != null && autor_aux.getRecetas_del_autor().indexOf(autor_aux.getRecetas_del_autor().get(0).findByName(nombre_receta_nuevo)) == -1) {
                    autor_aux.addRecetas_del_autor(autor_aux.getRecetas_del_autor().get(0).findByName(nombre_receta_nuevo));
                }
                autor_aux.update();
                succes_aux = "true";
                mensaje_aux = "Autor modificado con éxito!";
                Content content = receta.render(succes_aux, mensaje_aux);
                return Results.ok(content).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }

    }

    public Result delete(String nombre, Http.Request request) {
        Autor autor_aux = new Autor();
        autor_aux = autor_aux.findByName(nombre);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (autor_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningun autor con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

            } else {
                autor_aux.delete();
                succes.put("success", true);
                info.put("info", "Autor eliminado con éxito!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (autor_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun autor con ese nombre!";
                Content content = receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

            } else {
                autor_aux.delete();
                succes_aux = "true";
                mensaje_aux = "Autor eliminado con éxito!";
                Content content = receta.render(succes_aux, mensaje_aux);
                return Results.ok(content).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }

    public Result devolver(String nombre, Http.Request request) {
        Autor autor_aux = new Autor();
        autor_aux = autor_aux.findByName(nombre);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (autor_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningun autor con ese nombre!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

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

                String autor = autor_aux.toJson(2);
                info.set("autor", Json.parse(autor.replace("/\\/g", "")));
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (autor_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun autor con ese nombre!";
                Content content = receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

            } else {
                Content content = _autor.render(autor_aux,2);
                return Results.ok(content).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }

    public Result devolverById(Integer id, Http.Request request) {
        Autor autor_aux = new Autor();
        autor_aux = autor_aux.findAutorById(id);

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (autor_aux == null) {
                succes.put("success", false);
                info.put("info", "No existe ningun autor con ese identificador!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

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

                String autor = autor_aux.toJson(2);
                info.set("autor", Json.parse(autor.replace("/\\/g", "")));
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (autor_aux == null) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun autor con ese identificador!";
                Content content = receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

            } else {
                Content content = _autor.render(autor_aux,2);
                return Results.ok(content).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }

    public Result listar(Http.Request request) {

        Autor autor_aux = new Autor();
        List<Autor> autores = autor_aux.findAll();

        if (request.accepts("application/json")) {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            if (autores.size()== 0) {
                succes.put("success", false);
                info.put("info", "No existe ningun autr!");
                respuesta.add(succes);
                respuesta.add(info);
                return Results.notFound(respuesta).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

            } else {
                succes.put("success", true);
                for (int i=0;i<autores.size();i++)
                {
                    String autor = autores.get(i).toJson(2);
                    info.set("autor"+(i+1), Json.parse(autor.replace("/\\/g", "")));
                }
                respuesta.add(succes);
                respuesta.add(info);
                return Results.ok(respuesta).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());
            }
        } else if (request.accepts("application/xml")) {
            String succes_aux = "";
            String mensaje_aux = "";
            if (autores.size()== 0) {
                succes_aux = "false";
                mensaje_aux = "No existe ningun autor!";
                Content content = receta.render(succes_aux, mensaje_aux);
                return Results.notFound(content).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());

            } else {
                Content content = views.xml.autores.render(autores);
                return Results.ok(content).withHeader("X-User-Count", autor_aux.findNumeroDeAutores().toString());
            }
        } else {
            return badRequest("Unsupported format");
        }
    }

}
