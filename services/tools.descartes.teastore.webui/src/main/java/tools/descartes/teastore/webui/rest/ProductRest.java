package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import tools.descartes.teastore.entities.ImageSizePreset;
import tools.descartes.teastore.entities.OrderItem;
import tools.descartes.teastore.entities.Product;
import tools.descartes.teastore.entities.message.SessionBlob;
import tools.descartes.teastore.registryclient.Service;
import tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedImageOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedRecommenderOperations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Path("product")
@Produces({ "application/json" })
public class ProductRest {
    @GET
    public static Response get(@CookieParam("sessionBlob") Cookie cookie, @QueryParam("product") Long pid)
            throws NotFoundException {
        if (pid != null) {
            HashMap<String, Object> payload = new HashMap<>();

            Product p = LoadBalancedCRUDOperations.getEntity(Service.PERSISTENCE, "products", Product.class, pid);
            payload.put("product", p);
            payload.put("productImage", LoadBalancedImageOperations.getProductImage(p));

            if(cookie != null) {
                SessionBlob session = RestHelpers.parseSessionCookie(cookie);

                List<OrderItem> items = new LinkedList<>();
                OrderItem oi = new OrderItem();
                oi.setProductId(pid);
                oi.setQuantity(1);
                items.add(oi);
                items.addAll(session.getOrderItems());
                List<Long> productIds = LoadBalancedRecommenderOperations.getRecommendations(items, session.getUID());
                List<Product> ads = new LinkedList<>();
                for (Long productId : productIds) {
                    ads.add(LoadBalancedCRUDOperations.getEntity(Service.PERSISTENCE, "products", Product.class, productId));
                }

                if (ads.size() > 3) {
                    ads.subList(3, ads.size()).clear();
                }

                payload.put("ads", ads);
                payload.put("adImages", LoadBalancedImageOperations.getProductImages(ads, ImageSizePreset.RECOMMENDATION.getSize()));
            }

            return Response.ok().entity(payload).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}