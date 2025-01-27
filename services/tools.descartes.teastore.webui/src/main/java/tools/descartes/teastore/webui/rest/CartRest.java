package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import tools.descartes.teastore.entities.Category;
import tools.descartes.teastore.entities.ImageSizePreset;
import tools.descartes.teastore.entities.OrderItem;
import tools.descartes.teastore.entities.Product;
import tools.descartes.teastore.entities.message.SessionBlob;
import tools.descartes.teastore.registryclient.Service;
import tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedImageOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedRecommenderOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedStoreOperations;

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
        // Not useful, included for parity with webui
        payload.put("storeIcon", LoadBalancedImageOperations.getWebImage("icon", ImageSizePreset.ICON.getSize()));
        payload.put("title", "TeaStore Cart");
        payload.put("categories", LoadBalancedCRUDOperations.getEntities(Service.PERSISTENCE, "categories", Category.class, -1, -1));

        payload.put("orderItems", orderItems);
        payload.put("products", products);
        payload.put("login", LoadBalancedStoreOperations.isLoggedIn(session));
        payload.put("advertisement", ads);
        payload.put("productImages", LoadBalancedImageOperations.getProductPreviewImages(ads));

        return Response.ok().entity(payload).build();
    }
}
