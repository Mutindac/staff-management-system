/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.controller;
import ke.co.mspace.staffmanagement.model.UserAccount;
import ke.co.mspace.staffmanagement.service.UserAccountService;
import ke.co.mspace.staffmanagement.dao.UserAccountDAO;
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
@Named("userraccountBean")
@SessionScoped
public class UserAccountBean implements Serializable{
    private UserAccountService useraccountService;
    private UserAccount userAccount = new UserAccount();
    private List<UserAccount> useraccountList;

    public UserAccountBean(){
        try {
            Connection conn = DButil.getConnection();
            UserAccountDAO useraccountDAO = new UserAccountDAO(conn);
            useraccountService = new UserAccountService(useraccountDAO);
            useraccountList = useraccountService.getAllUserAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    //add new UserAccount
    public String addUserAccount(UserAccount useraccount){
        useraccountService.addUserAccount(useraccount);
        useraccountList = useraccountService.getAllUserAccounts();
        useraccount = new UserAccount();
        return "userAccountList.xhtml?faces-redirect=true";
    }
    
    //update userAccount
    public String updateUserAccount (UserAccount useraccount){
        useraccountService.updateUserAccount(useraccount);
        useraccountList = useraccountService.getAllUserAccounts();
        return "userAccountList.xhtml?faces-redirect=true";
    }
    
    //delete userAccount
    public String deleteUserAccount(int userId){
        useraccountService.deleteUserAccount(userId);
        useraccountList = useraccountService.getAllUserAccounts();
        return "userAccountList.xhtml?faces-redirect=true";
    }
    
    //get userAccount by id
    public UserAccount getUserAccountById (int userId){
        return useraccountService.getUserAccountById(userId);
    }
}
