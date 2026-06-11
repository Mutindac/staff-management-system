/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ke.co.mspace.staffmanagement.dao;

/**
 *
 * @author server
 */
import ke.co.mspace.staffmanagement.model.UserAccount;
import java.util.List;

public interface UserAccountDAO {
    void saveUserAccount(UserAccount userAccount);
    UserAccount getUserAccountById(int userId);
    List <UserAccount> getAllUserAccounts();
    void updateUserAccount(UserAccount userAccount);
    void deleteUserAccount (int userId);
    UserAccount getUserAccountByUsername(String username);
}
