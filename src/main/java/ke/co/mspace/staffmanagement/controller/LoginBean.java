package ke.co.mspace.staffmanagement.controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import ke.co.mspace.staffmanagement.dao.UserAccountDAO;
import ke.co.mspace.staffmanagement.daoimplementation.UserAccountDAOimpl;
import ke.co.mspace.staffmanagement.model.UserAccount;
import ke.co.mspace.staffmanagement.service.UserAccountService;
import ke.co.mspace.staffmanagement.serviceimpl.UserAccountServiceimpl;
import ke.co.mspace.staffmanagement.util.DButil;

import java.io.Serializable;
import java.sql.Connection;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private String username;
    private String password;
    private UserAccount loggedInUser;
    private UserAccountService useraccountService;

    public LoginBean() {
        try {
            Connection conn = DButil.getConnection();
            UserAccountDAO useraccountDAO = new UserAccountDAOimpl(conn);
            useraccountService = new UserAccountServiceimpl(useraccountDAO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserAccount user = useraccountService.getUserAccountByUsername(username);

        if (user != null && user.getPasswordHash() != null && user.getPasswordHash().equals(password)) {
            // Verify if user is an Admin. Depending on requirements, we can allow other roles too,
            // but the request is specifically for "admin" login.
            if ("Admin".equalsIgnoreCase(user.getRole())) {
                loggedInUser = user;
                ExternalContext externalContext = context.getExternalContext();
                externalContext.getSessionMap().put("loggedInUser", loggedInUser);
                return "index.xhtml?faces-redirect=true";
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Access Denied: Admin privileges required", null));
                return null;
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null));
            return null; // Stay on same page
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        loggedInUser = null;
        return "login.xhtml?faces-redirect=true";
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserAccount getLoggedInUser() { return loggedInUser; }
    public void setLoggedInUser(UserAccount loggedInUser) { this.loggedInUser = loggedInUser; }
}
