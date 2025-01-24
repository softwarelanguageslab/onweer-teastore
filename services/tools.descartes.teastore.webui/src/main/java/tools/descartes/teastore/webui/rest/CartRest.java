package tools.descartes.teastore.webui.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Path("cart")
@Produces({ "application/json" })
public class CartRest {

    @GET
    public Response get(@CookieParam("sessionBlob") Cookie cookie) {
        SessionBlob session = parseSessionCookie(cookie);

        List<OrderItem> orderItems = session.getOrderItems();
        ArrayList<Long> ids = new ArrayList<Long>();
        for (OrderItem orderItem : orderItems) {
            ids.add(orderItem.getProductId());
        }

        HashMap<Long, Product> products = new HashMap<Long, Product>();
        for (Long id : ids) {
            Product product = LoadBalancedCRUDOperations.getEntity(Service.PERSISTENCE, "products",
                    Product.class, id);
            products.put(product.getId(), product);
        }

        request.setAttribute("storeIcon",
                LoadBalancedImageOperations.getWebImage("icon", ImageSizePreset.ICON.getSize()));
        request.setAttribute("title", "TeaStore Cart");
        request.setAttribute("CategoryList", LoadBalancedCRUDOperations.getEntities(Service.PERSISTENCE,
                "categories", Category.class, -1, -1));
        request.setAttribute("OrderItems", orderItems);
        request.setAttribute("Products", products);
        request.setAttribute("login", LoadBalancedStoreOperations.isLoggedIn(getSessionBlob(request)));

        List<Long> productIds = LoadBalancedRecommenderOperations
                .getRecommendations(blob.getOrderItems(), blob.getUID());
        List<Product> ads = new LinkedList<Product>();
        for (Long productId : productIds) {
            ads.add(LoadBalancedCRUDOperations.getEntity(Service.PERSISTENCE, "products", Product.class,
                    productId));
        }

        if (ads.size() > 3) {
            ads.subList(3, ads.size()).clear();
        }
        request.setAttribute("Advertisment", ads);

        request.setAttribute("productImages", LoadBalancedImageOperations.getProductPreviewImages(ads));

        return null;
    }

    private SessionBlob parseSessionCookie(Cookie cookie) {
        ObjectMapper o = new ObjectMapper();
        try {
            SessionBlob blob = o.readValue(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8), SessionBlob.class);
            if (blob != null) {
                return blob;
            }
        } catch (
                IOException e) {
            throw new IllegalStateException("Cookie corrupted!");
        }
    }

}
