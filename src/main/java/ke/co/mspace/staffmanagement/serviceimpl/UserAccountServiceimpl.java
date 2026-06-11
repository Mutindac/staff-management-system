/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.serviceimpl;
import ke.co.mspace.staffmanagement.dao.UserAccountDAO;
import ke.co.mspace.staffmanagement.model.UserAccount;
import ke.co.mspace.staffmanagement.service.UserAccountService;
import java.util.List;
/**
 *
 * @author server
 */
public class UserAccountServiceimpl implements UserAccountService{
    private UserAccountDAO useraccountDAO;

    public UserAccountServiceimpl(UserAccountDAO useraccountDAO) {
        this.useraccountDAO = useraccountDAO;
    }
    
    @Override
    public void addUserAccount(UserAccount useraccount){
        //rules
        //userAccount must have staff
        if(useraccount.getStaffId() == 0){
            throw new IllegalArgumentException("UserAccount must have staff!");
        }
        useraccountDAO.saveUserAccount(useraccount);
    }
    
    @Override
    public UserAccount getUserAccountById (int userId){
        return useraccountDAO.getUserAccountById(userId);
    }
    
    @Override
    public List<UserAccount> getAllUserAccounts(){
        return useraccountDAO.getAllUserAccounts();
    }
    
    @Override
    public void updateUserAccount (UserAccount useraccount){
        useraccountDAO.updateUserAccount(useraccount);
    }
    
    @Override
    public void deleteUserAccount(int userId){
        useraccountDAO.deleteUserAccount(userId);
    }
    
    @Override
    public UserAccount getUserAccountByUsername(String username) {
        return useraccountDAO.getUserAccountByUsername(username);
    }
}
