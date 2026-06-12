/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.controller;
import ke.co.mspace.staffmanagement.model.UserAccount;
import ke.co.mspace.staffmanagement.service.UserAccountService;
import ke.co.mspace.staffmanagement.dao.UserAccountDAO;
import ke.co.mspace.staffmanagement.model.Staff;
import ke.co.mspace.staffmanagement.service.StaffService;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.util.DButil;
import java.sql.Connection;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
/**
 *
 * @author server
 */
@Named("userAccountBean")
@SessionScoped
public class UserAccountBean implements Serializable{
    private UserAccountService useraccountService;
    private StaffService staffService;
    private UserAccount userAccount = new UserAccount();
    private List<UserAccount> useraccountList;
    private List<Staff> availableStaffList;
    private String newPassword;

    public UserAccountBean(){
        try {
            Connection conn = DButil.getConnection();
            UserAccountDAO useraccountDAO = new UserAccountDAO(conn);
            useraccountService = new UserAccountService(useraccountDAO);
            
            StaffDAO staffDAO = new StaffDAO(conn);
            staffService = new StaffService(staffDAO);
            
            useraccountList = useraccountService.getAllUserAccounts();
            availableStaffList = staffService.getAllStaff();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    //prepare add
    public void prepareAdd() {
        this.userAccount = new UserAccount();
        this.newPassword = null;
    }
    
    //prepare edit
    public void prepareEdit(UserAccount account) {
        this.userAccount = account;
        this.newPassword = null;
    }
    
    //save userAccount (Add or Update)
    public String saveAccount(){
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            userAccount.setPasswordHash(newPassword);
        }
        
        if (userAccount.getUserId() > 0) {
            useraccountService.updateUserAccount(userAccount);
        } else {
            useraccountService.addUserAccount(userAccount);
        }
        
        useraccountList = useraccountService.getAllUserAccounts();
        return null;
    }
    
    //delete userAccount
    public String deleteUserAccount(int userId){
        useraccountService.deleteUserAccount(userId);
        useraccountList = useraccountService.getAllUserAccounts();
        return null;
    }
    
    //get userAccount by id
    public UserAccount getUserAccountById (int userId){
        return useraccountService.getUserAccountById(userId);
    }
    
    // Getters and Setters
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public List<UserAccount> getUseraccountList() {
        if (useraccountList == null) {
            useraccountList = useraccountService.getAllUserAccounts();
        }
        return useraccountList;
    }

    public List<Staff> getAvailableStaffList() {
        if (availableStaffList == null && staffService != null) {
            availableStaffList = staffService.getAllStaff();
        }
        return availableStaffList;
    }
    
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
