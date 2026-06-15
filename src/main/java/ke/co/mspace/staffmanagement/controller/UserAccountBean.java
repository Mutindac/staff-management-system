/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.controller;
import ke.co.mspace.staffmanagement.model.UserAccount;
import ke.co.mspace.staffmanagement.dao.UserAccountDAO;
import ke.co.mspace.staffmanagement.model.Staff;
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
    private UserAccountDAO useraccountDAO;
    private StaffDAO staffDAO;
    private UserAccount userAccount = new UserAccount();
    private List<UserAccount> useraccountList;
    private List<Staff> availableStaffList;
    private String newPassword;

    public UserAccountBean(){
        try {
            Connection conn = DButil.getConnection();
            useraccountDAO = new UserAccountDAO(conn);
            staffDAO = new StaffDAO(conn);
            
            useraccountList = useraccountDAO.getAllUserAccounts();
            availableStaffList = staffDAO.getAllStaff();
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
            userAccount.setPasswordHash(ke.co.mspace.staffmanagement.util.PasswordUtil.hashPassword(newPassword));
        }
        
        if (userAccount.getUserId() > 0) {
            useraccountDAO.updateUserAccount(userAccount);
        } else {
            useraccountDAO.saveUserAccount(userAccount);
        }
        
        useraccountList = useraccountDAO.getAllUserAccounts();
        return null;
    }
    
    //delete userAccount
    public String deleteUserAccount(int userId){
        useraccountDAO.deleteUserAccount(userId);
        useraccountList = useraccountDAO.getAllUserAccounts();
        return null;
    }
    
    //get userAccount by id
    public UserAccount getUserAccountById (int userId){
        return useraccountDAO.getUserAccountById(userId);
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
            useraccountList = useraccountDAO.getAllUserAccounts();
        }
        return useraccountList;
    }

    public List<Staff> getAvailableStaffList() {
        if (availableStaffList == null && staffDAO != null) {
            availableStaffList = staffDAO.getAllStaff();
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
