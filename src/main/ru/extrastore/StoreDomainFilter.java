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
import ru.extrastore.auxiliary.BooleanWrapper;
import ru.extrastore.model.AliasDomain;
import ru.extrastore.model.Store;

import javax.persistence.EntityManager;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
                    String domain = request.getServerName();    // User getServerName() here, cuz it returns server name free of port.
                    if (domain == null || isIPAddress(domain)) {
                        domain = "localhost";
                    }
                    if (domain.startsWith("www.")) {
                        // Remove "www.", redirect to the short name
                        domain = domain.substring(domain.indexOf(".")+1, domain.length());
                        httpResponse.setStatus(301);    // Moved permanently.
                        httpResponse.setHeader("Location", "http://" + domain + httpRequest.getRequestURI()
                                + (httpRequest.getQueryString() != null ? "?" + httpRequest.getQueryString() : ""));
                        continueChain.setValue(false);
                        return;
                    }


                    List storesList = em.createQuery("from Store s where s.domain = :domainName").setParameter("domainName", domain).getResultList();

                    if (storesList.size() == 0) {   // No store with such primary domain found — look in domain aliases
                        List domainsList = em.createQuery("from AliasDomain ad where ad.domain = :domainName").setParameter("domainName", domain).getResultList();
                        if (domainsList.size() == 0) {
                            throw new StoreNotFoundException("No store found for domain " + domain);
                        } else if (domainsList.size() > 1) {
                            throw new IllegalStateException("More than one domain alias for domain '" + domain + "'.");
                        } else { // Redirect to the primary domain
                            AliasDomain aliasDomain = (AliasDomain)domainsList.get(0);
                            httpResponse.setStatus(301);
                            httpResponse.setHeader("Location", "http://" + aliasDomain.getStore().getDomain() + httpRequest.getRequestURI()
                                    + (httpRequest.getQueryString() != null ? "?" + httpRequest.getQueryString() : ""));
                            continueChain.setValue(false);
                            return;
                        }

                    } else if (storesList.size() > 1) { //Error: more than one store for a domain
                        throw new IllegalStateException("More than one store with domain name '" + domain + "'.");

                    } else {    // One store found
                        assert storesList.get(0) instanceof Store;
                        Store store = (Store)storesList.get(0);
                        Contexts.getSessionContext().set("store", store);
                        System.out.println("Store domain set to " + store.getDomain() + ", store(" + store.hashCode() + "), session: " + httpRequest.getSession().getId());
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
