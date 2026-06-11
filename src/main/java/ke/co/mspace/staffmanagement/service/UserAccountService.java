/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ke.co.mspace.staffmanagement.service;
import ke.co.mspace.staffmanagement.model.UserAccount;
import java.util.List;
/**
 *
 * @author server
 */
public interface UserAccountService {
    void addUserAccount(UserAccount useraccount);
    UserAccount getUserAccountById (int userId);
    List<UserAccount> getAllUserAccounts();
    void updateUserAccount (UserAccount useraccount);
    void deleteUserAccount(int userId);
    UserAccount getUserAccountByUsername(String username);
}
