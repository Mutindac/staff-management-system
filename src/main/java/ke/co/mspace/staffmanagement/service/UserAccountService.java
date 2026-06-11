package ke.co.mspace.staffmanagement.service;

import ke.co.mspace.staffmanagement.dao.UserAccountDAO;
import ke.co.mspace.staffmanagement.model.UserAccount;
import java.util.List;

public class UserAccountService {
    private UserAccountDAO useraccountDAO;

    public UserAccountService(UserAccountDAO useraccountDAO) {
        this.useraccountDAO = useraccountDAO;
    }
    
    public void addUserAccount(UserAccount useraccount){
        if(useraccount.getStaffId() == 0){
            throw new IllegalArgumentException("UserAccount must have staff!");
        }
        useraccountDAO.saveUserAccount(useraccount);
    }
    
    public UserAccount getUserAccountById (int userId){
        return useraccountDAO.getUserAccountById(userId);
    }
    
    public List<UserAccount> getAllUserAccounts(){
        return useraccountDAO.getAllUserAccounts();
    }
    
    public void updateUserAccount (UserAccount useraccount){
        useraccountDAO.updateUserAccount(useraccount);
    }
    
    public void deleteUserAccount(int userId){
        useraccountDAO.deleteUserAccount(userId);
    }
    
    public UserAccount getUserAccountByUsername(String username) {
        return useraccountDAO.getUserAccountByUsername(username);
    }
}
