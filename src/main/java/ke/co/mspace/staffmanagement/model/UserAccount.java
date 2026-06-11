/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.model;
import java.io. Serializable;
/**
 *
 * @author server
 */
public class UserAccount implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private int userId;
    private int staffId;
    private String username;
    private String passwordHash;
    private String role;
    
    //default constructor
    public UserAccount(){
        
    }
    
    //parameterized constructor
    public UserAccount(int userId,int staffId, String username, String passwordHash, String role){
        this.userId = userId;
        this.staffId = staffId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }
    
    //getters and setters
    public int getUserId(){
        return userId;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    public int getStaffId(){
        return staffId;
    }
    public void setStaffId(int staffId){
        this.staffId = staffId;
    }
    
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    
    public String getPasswordHash(){
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }
    
    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role = role;
    }
    
    @Override
    public String toString(){
        return "userAccount{" + userId +
                " ,staffId=" + staffId + 
                " ,username=" + username +
                " ,passwordHash=" + passwordHash +
                " ,role=" + role +
                "}";
    }
}
