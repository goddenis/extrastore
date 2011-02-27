package ru.extrastore;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.jboss.seam.web.AbstractResource;
import ru.extrastore.dao.ProductDAO;
import ru.extrastore.model.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jboss.seam.ScopeType.APPLICATION;


/**
 * http://extrastore.ru
 * Created by Catalyst on 27.02.11 at 13:25
 */
@Scope(APPLICATION)
@Name("productImageResource")
@BypassInterceptors
public class ProductImageResource extends AbstractResource {
    // Resources URIs end with /<productAlias>/<l|s>
    public static Pattern RESOURCE_PATTERN = Pattern.compile("^/([a-zA-Z0-9]+)\\-([ls]{1})\\.jpg$");

    public static final String REGISTER_SEAM_RESOURCE = "/productImage";

    private Log log = Logging.getLog(ProductImageResource.class);

    @Override
    public String getResourcePath() {
        return REGISTER_SEAM_RESOURCE;
    }

    @Override
    public void getResource(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        // Wrap this, we need an ApplicationContext
        new ContextualHttpServletRequest(request) {
            @Override
            public void process() throws IOException {
                doWork(request, response);
            }
        }.run();

    }

    public void doWork(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String pathInfo = request.getPathInfo().substring(getResourcePath().length());

        String productAlias = null;
        String imageSize = null;
        Matcher matcher = RESOURCE_PATTERN.matcher(pathInfo);
        if (matcher.find()) {
            productAlias = matcher.group(1);
            imageSize = matcher.group(2);
            log.debug("request for product image: " + productAlias + ", image size: " + imageSize);
        }

        if (productAlias == null || imageSize == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path, use: /productImage/[a-zA-Z0-9]+\\-(l|s)\\.jpg");
            return;
        }

        ProductDAO productDAO = (ProductDAO) Component.getInstance(ProductDAO.class);
        Product product = productDAO.findProductByAlias(productAlias);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product alias '" + productAlias + "' is not defined." );
            return;
        }

        response.addHeader("Cache-Control", "max-age=10"); // 10 minutes freshness in browser cache

        byte[] image = imageSize.equals("s") ? product.getImageSmall() : product.getImageLarge();
        response.setContentType("image/jpeg");
        response.setContentLength(image.length);
        response.getOutputStream().write(image);
        response.getOutputStream().flush();
    }
}
