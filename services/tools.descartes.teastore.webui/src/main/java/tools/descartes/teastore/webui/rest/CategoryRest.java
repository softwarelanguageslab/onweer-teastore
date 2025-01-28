package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import tools.descartes.teastore.entities.Category;
import tools.descartes.teastore.entities.Product;
import tools.descartes.teastore.registryclient.Service;
import tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedImageOperations;

import java.util.HashMap;
import java.util.List;

@Path("category")
@Produces({ "application/json" })
public class CategoryRest {
    @GET
    public static Response get(@QueryParam("category") Long cid) {
        if(cid != null) {
            List<Product> productlist = LoadBalancedCRUDOperations.getEntities(Service.PERSISTENCE,
                    "products", Product.class, "category", cid, 0, 25);

            // TODO: pagination?

            HashMap<String, Object> payload = new HashMap<>();
            payload.put("products", productlist);
            payload.put("productImages", LoadBalancedImageOperations.getProductPreviewImages(productlist));
            return Response.ok().entity(payload).build();
        } else {
            return Response.ok().entity(LoadBalancedCRUDOperations.getEntities(Service.PERSISTENCE, "categories", Category.class, -1, -1)).build();
        }
    }
}
