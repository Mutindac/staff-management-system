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

            if (isLoginRequest || isResourceRequest) {
                // Let the request proceed without authentication
                chain.doFilter(request, response);
            } else if (isLoggedIn) {
                String role = loggedInUser.getRole();
                boolean isAdminPage = requestURI.contains("staffList.xhtml") || 
                                      requestURI.contains("departmentList.xhtml") ||
                                      requestURI.contains("roleList.xhtml") ||
                                      requestURI.contains("useraccountList.xhtml") ||
                                      requestURI.contains("attendanceList.xhtml") ||
                                      requestURI.contains("leaveRequests.xhtml") ||
                                      requestURI.contains("manageAssets.xhtml") ||
                                      requestURI.contains("assetRequests.xhtml") ||
                                      requestURI.contains("notificationList.xhtml") ||
                                      requestURI.contains("index.xhtml");

                if ("Staff".equalsIgnoreCase(role) && isAdminPage) {
                    // Redirect staff away from admin pages
                    res.sendRedirect(req.getContextPath() + "/staffProfile.xhtml");
                } else {
                    // Let the request proceed
                    chain.doFilter(request, response);
                }
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
