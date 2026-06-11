/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.model;
import java.io.Serializable;
/**
 *
 * @author server
 */
public class Role implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private int roleId;
    private String title;
    private String description;
    
    //default constructor
    public Role(){
        
    }
    
    //paramaterized constructor
    public Role(int roleId, String title, String description){
        this.roleId = roleId;
        this.title = title;
        this.description = description;
    }
    
    //getters and setters
    public int getRoleId(){
        return roleId;
    }
    public void setRoleId(int roleId){
        this.roleId = roleId;
    }
    
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    
    //to string 
    @Override
    public String toString(){
        return "role{"+ roleId +
                " ,title=" + title +
                " ,description=" + description+
                "}";
    }
}
