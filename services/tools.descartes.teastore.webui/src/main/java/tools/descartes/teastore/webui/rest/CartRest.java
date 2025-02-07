package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import tools.descartes.teastore.entities.OrderItem;
import tools.descartes.teastore.entities.Product;
import tools.descartes.teastore.entities.message.SessionBlob;
import tools.descartes.teastore.registryclient.Service;
import tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedImageOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedRecommenderOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedStoreOperations;
import tools.descartes.teastore.webui.rest.entities.UpdateQuantity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Path("cart")
@Produces({ "application/json" })
public class CartRest {
    @GET
    public static Response get(@CookieParam("sessionBlob") Cookie cookie) {
        SessionBlob session = RestHelpers.parseSessionCookie(cookie);

        List<OrderItem> orderItems = session.getOrderItems();
        ArrayList<Long> ids = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            ids.add(orderItem.getProductId());
        }

        HashMap<Long, Product> products = new HashMap<>();
        for (Long id : ids) {
            Product product = LoadBalancedCRUDOperations.getEntity(Service.PERSISTENCE, "products",
                    Product.class, id);
            products.put(product.getId(), product);
        }

        List<Long> productIds = LoadBalancedRecommenderOperations.getRecommendations(orderItems, session.getUID());
        List<Product> ads = new ArrayList<>();
        for (Long productId : productIds) {
            ads.add(LoadBalancedCRUDOperations.getEntity(Service.PERSISTENCE, "products", Product.class, productId));
        }
        if (ads.size() > 3) {
            ads.subList(3, ads.size()).clear();
        }

        HashMap<String, Object> payload = new HashMap<>();

        payload.put("orderItems", orderItems);
        payload.put("products", products);
        payload.put("productImages", LoadBalancedImageOperations.getProductPreviewImages(ads));
        payload.put("ads", ads);

        return Response.ok().entity(payload).build();
    }

    @Path("add")
    @POST
    public Response addToCart(@CookieParam("sessionBlob") Cookie cookie, @QueryParam("productId") Long productId)
        throws NotFoundException {
        if(productId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            SessionBlob session = RestHelpers.parseSessionCookie(cookie);
            SessionBlob newSession = LoadBalancedStoreOperations.addProductToCart(session, productId);
            return Response.ok().cookie(RestHelpers.encodeSessionCookie(newSession)).build();
        }
    }

    @Path("remove")
    @POST
    public Response removeProduct(@CookieParam("sessionBlob") Cookie cookie, @QueryParam("productId") Long productId)
            throws NotFoundException {
        if(productId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            SessionBlob session = RestHelpers.parseSessionCookie(cookie);
            SessionBlob newSession = LoadBalancedStoreOperations.removeProductFromCart(session, productId);
            return Response.ok().cookie(RestHelpers.encodeSessionCookie(newSession)).build();
        }
    }

    @Path("update_quantity")
    @POST
    @Consumes({ "application/json" })
    public Response updateCartQuantities(List<UpdateQuantity> productQuantities, @CookieParam("sessionBlob") Cookie cookie) {
        SessionBlob session = RestHelpers.parseSessionCookie(cookie);

        for (UpdateQuantity entry : productQuantities) {
            if(session.getOrderItems().stream().anyMatch(o -> o.getProductId() == entry.getPid())) {
                session = LoadBalancedStoreOperations.updateQuantity(session, entry.getPid(), entry.getQuantity());
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        return Response.ok().cookie(RestHelpers.encodeSessionCookie(session)).build();
    }
}
