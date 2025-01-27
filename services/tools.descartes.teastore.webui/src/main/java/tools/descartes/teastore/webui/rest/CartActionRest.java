package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import tools.descartes.teastore.entities.message.SessionBlob;
import tools.descartes.teastore.registryclient.rest.LoadBalancedStoreOperations;
import tools.descartes.teastore.webui.rest.entities.UpdateQuantity;

import java.util.List;

@Path("cartAction")
@Produces({ "application/json" })
public class CartActionRest {

    @Path("addToCart")
    @POST
    public Response addToCart(@CookieParam("sessionBlob") Cookie cookie, @QueryParam("productId") long productId) {
        SessionBlob session = RestHelpers.parseSessionCookie(cookie);
        SessionBlob newSession = LoadBalancedStoreOperations.addProductToCart(session, productId);
        return Response.ok().cookie(RestHelpers.encodeSessionCookie(newSession)).build();
    }

    @Path("removeProduct")
    @POST
    public Response removeProduct(@CookieParam("sessionBlob") Cookie cookie, @QueryParam("productId") long productId) {
        SessionBlob session = RestHelpers.parseSessionCookie(cookie);
        SessionBlob newSession = LoadBalancedStoreOperations.removeProductFromCart(session, productId);
        return Response.ok().cookie(RestHelpers.encodeSessionCookie(newSession)).build();
    }

    @Path("updateCartQuantities")
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
