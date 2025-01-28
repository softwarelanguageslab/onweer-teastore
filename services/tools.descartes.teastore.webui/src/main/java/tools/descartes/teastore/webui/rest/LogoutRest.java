package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;

@Path("logout")
@Produces({ "application/json" })
public class LogoutRest {
    @POST
    public Response logout(@CookieParam("sessionBlob") Cookie cookie) {
        return Response.ok().cookie(RestHelpers.deleteCookie(cookie)).build();
    }
}
