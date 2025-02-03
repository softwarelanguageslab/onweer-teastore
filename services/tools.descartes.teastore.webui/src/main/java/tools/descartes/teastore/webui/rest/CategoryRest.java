package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import tools.descartes.teastore.entities.Category;
import tools.descartes.teastore.registryclient.Service;
import tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations;

@Path("category")
@Produces({ "application/json" })
public class CategoryRest {
    @GET
    public static Response get() {
        return Response.ok().entity(LoadBalancedCRUDOperations.getEntities(Service.PERSISTENCE, "categories", Category.class, -1, -1)).build();
    }
}
