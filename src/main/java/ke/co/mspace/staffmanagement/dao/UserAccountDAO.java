package ke.co.mspace.staffmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import ke.co.mspace.staffmanagement.model.UserAccount;

public class UserAccountDAO {
    private Connection connection;

    public UserAccountDAO(Connection connection) {
        this.connection = connection;
    }
    
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
    
    private UserAccount mapRowToUserAccount(ResultSet rs) throws SQLException {
        UserAccount useraccount = new UserAccount();
        useraccount.setUserId(rs.getInt("userId"));
        useraccount.setStaffId(rs.getInt("staffId"));
        useraccount.setUsername(rs.getString("username"));
        useraccount.setPasswordHash(rs.getString("passwordHash"));
        useraccount.setRole(rs.getString("role"));
        return useraccount;
    }
    
    public UserAccount getUserAccountById(int userId){
        UserAccount useraccount = new UserAccount();
        String sql = "SELECT * FROM useraccount WHERE userId  = ?";
        try(PreparedStatement stmt  = connection.prepareStatement(sql)){
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                useraccount = mapRowToUserAccount(rs);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return useraccount;
    }
    
    public void updateUserAccount(UserAccount useraccount){
        String sql = "UPDATE useraccount SET staffId = ?, username = ?, passwordHash = ?, role = ? WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, useraccount.getStaffId());
            stmt.setString(2, useraccount.getUsername());
            stmt.setString(3, useraccount.getPasswordHash());
            stmt.setString(4, useraccount.getRole());
            stmt.setInt(5, useraccount.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void deleteUserAccount(int userId){
        String sql = "DELETE FROM useraccount WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public UserAccount getUserAccountByUsername(String username){
        UserAccount useraccount = null;
        String sql = "SELECT * FROM useraccount WHERE username  = ?";
        try(PreparedStatement stmt  = connection.prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                useraccount = mapRowToUserAccount(rs);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return useraccount;
    }
    
    public List <UserAccount> getAllUserAccounts(){
        List<UserAccount> UserAccountList = new ArrayList<>();
        
        String sql = "SELECT * FROM useraccount";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                UserAccountList.add(mapRowToUserAccount(rs));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return UserAccountList;
    }
}
