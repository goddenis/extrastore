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

        public void doFilter(final ServletRequest request,
                             final ServletResponse response,
                             FilterChain chain)
                throws java.io.IOException, javax.servlet.ServletException {
            if (request instanceof HttpServletRequest) {
                final HttpServletRequest httpRequest = (HttpServletRequest)request;
                final HttpServletResponse httpResponse = (HttpServletResponse)response;

//                if (!(httpRequest.getServletPath().endsWith(".seam"))) {
//                    chain.doFilter(request, response);
//                    return;
//                }

                new ContextualHttpServletRequest((HttpServletRequest)request) {
                    public void process() throws Exception {

                        if (Contexts.getSessionContext().get("store") != null) {
                            return;
                        }

                        EntityManager em = (EntityManager) Component.getInstance("entityManager");
                        String domain = request.getServerName();
                        if (domain == null) {
                            domain = "localhost";
                        }
                        if (domain.startsWith("www.")) {
                            // Remove "www.", redirect to the short name
                            domain = domain.substring(domain.indexOf(".")+1, domain.length());
                        }

                        try {

                            Store store = (Store)em.createQuery("from Store s where s.domain = :domainName").setParameter("domainName", domain).getSingleResult();

                            if (store != null) {
                                Contexts.getSessionContext().set("store", store);
                                System.out.println("Store domain set to " + store.getDomain() + ", session: " + httpRequest.getSession().getId());
                            }

                        } catch (NoResultException e) {
                            e.printStackTrace();
                        }
                    }

                }.run();

            }

            chain.doFilter(request,response);
        }

        public void init(final FilterConfig filterConfig) {
            this.filterConfig = filterConfig;
        }

        public void destroy() {
        }
    }
