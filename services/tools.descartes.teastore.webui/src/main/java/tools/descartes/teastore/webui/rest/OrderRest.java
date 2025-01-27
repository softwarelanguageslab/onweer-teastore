package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import tools.descartes.teastore.entities.OrderItem;
import tools.descartes.teastore.entities.message.SessionBlob;
import tools.descartes.teastore.registryclient.rest.LoadBalancedStoreOperations;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Path("order")
@Produces({ "application/json" })
public class OrderRest {

    @Path("confirm")
    @POST
    public Response confirm(@CookieParam("sessionBlob") Cookie cookie, @QueryParam("name") String name,
                            @QueryParam("address") String address, @QueryParam("cardtype") String cardtype,
                            @QueryParam("cardnumber") String cardnumber, @QueryParam("expiry") String expiry) {
        SessionBlob session = RestHelpers.parseSessionCookie(cookie);

        if (!LoadBalancedStoreOperations.isLoggedIn(session)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        long price = 0;
        for (OrderItem item : session.getOrderItems()) {
            price += item.getQuantity() * item.getUnitPriceInCents();
        }

        SessionBlob newsession = LoadBalancedStoreOperations.placeOrder(session, name, address, "", cardtype,
                YearMonth.parse(expiry, DateTimeFormatter.ofPattern("MM/yyyy")).atDay(1).format(DateTimeFormatter.ISO_LOCAL_DATE),
                price, cardnumber);

        return Response.ok().cookie(RestHelpers.encodeSessionCookie(newsession)).build();
    }
}
