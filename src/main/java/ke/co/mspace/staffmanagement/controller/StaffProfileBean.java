package ke.co.mspace.staffmanagement.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ke.co.mspace.staffmanagement.dao.DepartmentDAO;
import ke.co.mspace.staffmanagement.dao.RoleDAO;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.dao.UserAccountDAO;
import ke.co.mspace.staffmanagement.model.Department;
import ke.co.mspace.staffmanagement.model.Role;
import ke.co.mspace.staffmanagement.model.Staff;
import ke.co.mspace.staffmanagement.model.UserAccount;
import ke.co.mspace.staffmanagement.service.DepartmentService;
import ke.co.mspace.staffmanagement.service.RoleService;
import ke.co.mspace.staffmanagement.service.StaffService;
import ke.co.mspace.staffmanagement.service.UserAccountService;
import ke.co.mspace.staffmanagement.util.DButil;

import java.io.Serializable;
import java.sql.Connection;

@Named("staffProfileBean")
@SessionScoped
public class StaffProfileBean implements Serializable {

    private Staff currentStaff;
    private UserAccount currentUserAccount;
    
    private String departmentName;
    private String roleName;
    
    private String newPassword;

    private StaffService staffService;
    private UserAccountService userAccountService;
    private DepartmentService departmentService;
    private RoleService roleService;

    public StaffProfileBean() {
        try {
            Connection conn = DButil.getConnection();
            staffService = new StaffService(new StaffDAO(conn));
            userAccountService = new UserAccountService(new UserAccountDAO(conn));
            departmentService = new DepartmentService(new DepartmentDAO(conn));
            roleService = new RoleService(new RoleDAO(conn));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        loadUserProfile();
    }

    public void loadUserProfile() {
        FacesContext context = FacesContext.getCurrentInstance();
        currentUserAccount = (UserAccount) context.getExternalContext().getSessionMap().get("loggedInUser");

        if (currentUserAccount != null && currentUserAccount.getStaffId() > 0) {
            currentStaff = staffService.getStaffById(currentUserAccount.getStaffId());
            
            if (currentStaff != null) {
                Department dept = departmentService.getDepartmentById(currentStaff.getDepartmentId());
                departmentName = (dept != null) ? dept.getName() : "N/A";
                
                Role role = roleService.getRoleById(currentStaff.getRoleId());
                roleName = (role != null) ? role.getTitle() : "N/A";
            }
        }
    }

    public void updateProfile() {
        try {
            staffService.updateStaff(currentStaff);
            
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                currentUserAccount.setPasswordHash(newPassword);
                userAccountService.updateUserAccount(currentUserAccount);
                newPassword = null; // Clear after update
            }
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Profile updated successfully"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update profile"));
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public Staff getCurrentStaff() { return currentStaff; }
    public void setCurrentStaff(Staff currentStaff) { this.currentStaff = currentStaff; }

    public UserAccount getCurrentUserAccount() { return currentUserAccount; }
    public void setCurrentUserAccount(UserAccount currentUserAccount) { this.currentUserAccount = currentUserAccount; }

    public String getDepartmentName() { return departmentName; }
    public String getRoleName() { return roleName; }
    
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
