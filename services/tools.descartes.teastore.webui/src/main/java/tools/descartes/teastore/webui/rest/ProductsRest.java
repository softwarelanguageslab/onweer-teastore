package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import tools.descartes.teastore.entities.Product;
import tools.descartes.teastore.registryclient.Service;
import tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedImageOperations;

import java.util.HashMap;
import java.util.List;

@Path("products")
@Produces({ "application/json" })
public class ProductsRest {
    @GET
    public static Response get(@QueryParam("category") Long cid)
            throws NotFoundException {
        if(cid != null) {
            List<Product> productlist = LoadBalancedCRUDOperations.getEntities(Service.PERSISTENCE,
                    "products", Product.class, "category", cid, 0, 25);

            HashMap<String, Object> payload = new HashMap<>();
            payload.put("products", productlist);
            payload.put("productImages", LoadBalancedImageOperations.getProductPreviewImages(productlist));
            return Response.ok().entity(payload).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}