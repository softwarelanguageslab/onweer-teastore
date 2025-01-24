package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;

@Path("cartAction")
@Produces({ "application/json" })
public class CartActionRest {

    @Path("addToCart")
    @POST
    public Response addToCart(@CookieParam("sessionBlob") Cookie cookie) {
        return null;
    }

    @Path("removeProduct")
    @POST
    public Response removeProduct(@CookieParam("sessionBlob") Cookie cookie) {
        return null;
    }

    @Path("updateCartQuantities")
    @POST
    public Response updateCartQuantities(@CookieParam("sessionBlob") Cookie cookie) {
        return null;
    }
}
