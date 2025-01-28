package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import tools.descartes.teastore.entities.Order;
import tools.descartes.teastore.entities.User;
import tools.descartes.teastore.entities.message.SessionBlob;
import tools.descartes.teastore.registryclient.Service;
import tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedStoreOperations;

import java.util.HashMap;

@Path("profile")
@Produces({ "application/json" })
public class ProfileRest {
    @GET
    public static Response get(@CookieParam("sessionBlob") Cookie cookie) {
        SessionBlob session = RestHelpers.parseSessionCookie(cookie);

        if (!LoadBalancedStoreOperations.isLoggedIn(session)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("user", LoadBalancedCRUDOperations.getEntity(Service.PERSISTENCE, "users", User.class, session.getUID()));
        payload.put("Orders", LoadBalancedCRUDOperations.getEntities(Service.PERSISTENCE, "orders", Order.class, "user", session.getUID(), -1, -1));

        return Response.ok().entity(payload).build();
    }
}
