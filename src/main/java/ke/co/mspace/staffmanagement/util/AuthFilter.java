package ke.co.mspace.staffmanagement.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ke.co.mspace.staffmanagement.model.UserAccount;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
public class AuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpSession session = req.getSession(false);

            String requestURI = req.getRequestURI();

            boolean isLoginRequest = requestURI.contains("login.xhtml");
            boolean isResourceRequest = requestURI.contains("jakarta.faces.resource");
            
            UserAccount loggedInUser = (session != null) ? (UserAccount) session.getAttribute("loggedInUser") : null;
            boolean isLoggedIn = (loggedInUser != null);

            if (isLoggedIn || isLoginRequest || isResourceRequest) {
                // Let the request proceed
                chain.doFilter(request, response);
            } else {
                // If not logged in, redirect to login page
                res.sendRedirect(req.getContextPath() + "/login.xhtml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
