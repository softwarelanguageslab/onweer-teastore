package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import tools.descartes.teastore.entities.message.SessionBlob;
import tools.descartes.teastore.registryclient.rest.LoadBalancedStoreOperations;

import java.util.Map;

@Path("login")
@Produces({ "application/json" })
public class LoginRest {
    @POST
    @Consumes({ "application/json" })
    public Response login(Map<String, String> request, @CookieParam("sessionBlob") Cookie cookie) {
        if (request.get("username") != null && request.get("password") != null) {
            SessionBlob newsession = LoadBalancedStoreOperations.login(RestHelpers.parseSessionCookie(cookie), request.get("username"), request.get("password"));

            if (newsession != null && newsession.getSID() != null) {
                return Response.ok().cookie(RestHelpers.encodeSessionCookie(newsession)).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
