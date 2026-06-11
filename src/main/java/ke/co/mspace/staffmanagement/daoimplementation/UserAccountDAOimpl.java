/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.daoimplementation;
import ke.co.mspace.staffmanagement.dao.UserAccountDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import ke.co.mspace.staffmanagement.model.UserAccount;
/**
 *
 * @author server
 */
public class UserAccountDAOimpl implements UserAccountDAO{
    private Connection connection;

    public UserAccountDAOimpl(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public void saveUserAccount(UserAccount userAccount){
        String sql = "INSERT INTO useraccount(staffId,username,passwordHash,role) VALUES(?,?,?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, userAccount.getStaffId());
            stmt.setString(2, userAccount.getUsername());
            stmt.setString(3, userAccount.getPasswordHash());
            stmt.setString(4, userAccount.getRole());
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public UserAccount getUserAccountById(int userId){
        UserAccount useraccount = new UserAccount();
        String sql = "SELECT * FROM useraccount WHERE userId  = ?";
        try(PreparedStatement stmt  = connection.prepareStatement(sql)){
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                useraccount.setUserId(rs.getInt("userId"));
                useraccount.setStaffId(rs.getInt("staffId"));
                useraccount.setUsername(rs.getString("username"));
                useraccount.setPasswordHash(rs.getString("passwordHash"));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return useraccount;
    }
    
    @Override
    public void updateUserAccount(UserAccount useraccount){
        String sql = "UPDATE useraccount SET staffId = ?,username = ?, passwordHash = ?, role = ? WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(2, useraccount.getStaffId());
            stmt.setString(3, useraccount.getUsername());
            stmt.setString(4, useraccount.getPasswordHash());
            stmt.setString(5, useraccount.getRole());
            stmt.setInt(1, useraccount.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteUserAccount(int userId){
        String sql = "DELETE FROM useraccount WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public UserAccount getUserAccountByUsername(String username){
        UserAccount useraccount = null;
        String sql = "SELECT * FROM useraccount WHERE username  = ?";
        try(PreparedStatement stmt  = connection.prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                useraccount = new UserAccount();
                useraccount.setUserId(rs.getInt("userId"));
                useraccount.setStaffId(rs.getInt("staffId"));
                useraccount.setUsername(rs.getString("username"));
                useraccount.setPasswordHash(rs.getString("passwordHash"));
                useraccount.setRole(rs.getString("role"));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return useraccount;
    }
    
    @Override
    public List <UserAccount> getAllUserAccounts(){
        List<UserAccount> UserAccountList = new ArrayList<>();
        
        String sql = "SELECT * FROM useraccount";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                UserAccount useraccount = new UserAccount();
                useraccount.setUserId(rs.getInt("userId"));
                useraccount.setStaffId(rs.getInt("staffId"));
                useraccount.setUsername(rs.getString("username"));
                useraccount.setPasswordHash(rs.getString("passwordHash"));
                useraccount.setRole(rs.getString("role"));
                useraccount.setStaffId(rs.getInt("staffId"));
                UserAccountList.add(useraccount);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return UserAccountList;
    }
}
