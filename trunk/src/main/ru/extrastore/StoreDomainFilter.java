package ru.extrastore;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.web.Filter;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import ru.extrastore.model.Store;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

import static org.jboss.seam.annotations.Install.APPLICATION;

/**
 * File profile
 * Creator: Catalyst
 * Date: 30.01.11
 * Time: 3:38
 */
@Startup
@Scope(ScopeType.APPLICATION)
@Name("storeDomainFilter")
@Install(precedence = APPLICATION)
@BypassInterceptors
@Filter(within="org.jboss.seam.web.ajax4jsfFilter")
public class StoreDomainFilter implements javax.servlet.Filter {

    private class BooleanWrapper {  // Enables us to change boolean value from inline class ContextualHttpServletRequest.
        boolean value;

        private BooleanWrapper(boolean value) {
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }
    }

    FilterConfig filterConfig;

    private static final Pattern IPAddressPattern = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}");

    private static final boolean isIPAddress(String address) {
        return IPAddressPattern.matcher(address).matches();
    }

    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
            throws java.io.IOException, javax.servlet.ServletException {

        final BooleanWrapper continueChain = new BooleanWrapper(true);

        if (request instanceof HttpServletRequest) {

            final HttpServletRequest httpRequest = (HttpServletRequest)request;
            final HttpServletResponse httpResponse = (HttpServletResponse)response;



            new ContextualHttpServletRequest((HttpServletRequest)request) {
                public void process() throws Exception {

                    if (Contexts.getSessionContext().get("store") != null) {
                        return;
                    }

                    EntityManager em = (EntityManager) Component.getInstance("entityManager");
                    String domain = httpRequest.getHeader("Host");
                    if (domain == null || isIPAddress(domain)) {
                        domain = "localhost";
                    }
                    if (domain.startsWith("www.")) {
                        // Remove "www.", redirect to the short name
                        domain = domain.substring(domain.indexOf(".")+1, domain.length());
                        httpResponse.setStatus(301);
                        httpResponse.setHeader("Location", "http://" + domain + httpRequest.getRequestURI()
                                + (httpRequest.getQueryString() != null ? "?" + httpRequest.getQueryString() : ""));
                        continueChain.setValue(false);
                        return;
                    }

                    try {

                        Store store = (Store)em.createQuery("from Store s where s.domain = :domainName").setParameter("domainName", domain).getSingleResult();

                        if (store != null) {
                            Contexts.getSessionContext().set("store", store);
                            System.out.println("Store domain set to " + store.getDomain() + ", store(" + store.hashCode() + "), session: " + httpRequest.getSession().getId());
                        }

                    } catch (NoResultException e) {
                        throw new IllegalArgumentException("Error: No store found for domain " + domain, e);
                    }
                }

            }.run();
        }

        if (continueChain.getValue()) {
            chain.doFilter(request,response);
        }
    }

    public void init(final FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
    }
}
