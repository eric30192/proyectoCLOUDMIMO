package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        if(request.contentType().get().equals("application/json")){
            form = formFactory.form(Receta.class).bindFromRequest(request);
            if(form.hasErrors()){
                System.out.println(form.errorsAsJson());
                return Results.notAcceptable(form.errorsAsJson());
            }else{
                receta_aux = form.get();
            }

        }else if(request.contentType().get().equals("application/xml")){
            Document doc = request.body().asXml();
            System.out.println("NOMBRE: " + doc.getFirstChild().getChildNodes().item(1).getTextContent());
            System.out.println("DESCRIPCION: " + doc.getFirstChild().getChildNodes().item(3).getTextContent());

            doc.getChildNodes().item(0).getFirstChild().getTextContent();
            receta_aux.setNombre(doc.getFirstChild().getChildNodes().item(1).getTextContent());
            receta_aux.setDescripcion(doc.getFirstChild().getChildNodes().item(3).getTextContent());
            //form.fill(receta_aux);
            form = formFactory.form(Receta.class).fill(receta_aux);
            if(form.hasErrors()){
                return Results.notAcceptable(form.errorsAsJson());
            }else{
                receta_aux = form.get();
                System.out.println("sdad");
            }
        }else{
            //jugamos con parametros directamente
            receta_aux.setNombre(nombre.toString());
            receta_aux.setDescripcion(descripcion.toString());
            form.fill(receta_aux);

        }



        if (request.accepts("application/json"))
        {
            ArrayNode respuesta = Json.newArray();
            ObjectNode succes = Json.newObject();
            ObjectNode info = Json.newObject();
            try {
                //System.out.println(receta_aux.getNombre() + " " + receta_aux.getAutor() + " " + receta_aux.getTipo());

                   Receta receta = new Receta();
                   receta = receta_aux;
                   receta.save();

                   succes.put("success", true);
                   info.put("info", "Receta insertada con éxito!");

            }catch (Exception e){
                succes.put("success", false);
                info.put("info", "Fallo al insertar la receta!");
            }
            respuesta.add(succes);
            respuesta.add(info);
            return Results.ok(respuesta).withHeader("X-User-Count",receta_aux.findNumeroDeRecetas().toString());

        }else if (request.accepts("application/xml")) {
            String succes_aux="";
            String mensaje_aux="";

            try {
                    Receta receta = new Receta();
                    receta = receta_aux;
                    receta.save();
                    succes_aux = "true";
                    mensaje_aux = "Receta insertada con éxito!";
            }catch (Exception e){
                succes_aux = "false";
                mensaje_aux = "No se ha podido insetar la receta";
            }

            Content content = views.xml.receta.render(succes_aux,mensaje_aux);

            return Results.ok(content).withHeader("X-User-Count",receta_aux.findNumeroDeRecetas().toString());

        }
        else {
            return badRequest("Unsupported format");
        }

    }

}
