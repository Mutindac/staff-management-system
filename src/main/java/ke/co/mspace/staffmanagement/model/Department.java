/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.model;

/**
 *
 * @author server
 */
import java.io.Serializable;

public class Department implements Serializable{
    private static final long serialVersionUID = 1L;
    //database fields
    private int departmentId;
    private String name;
    private String description;
    
    
    //default constructor
    public Department(){
        
    }
    
    //parameterized constructor
    
    public Department(int departmentId, String name,String description){
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
    }
    
    //getters and setters
    public int getDepartmentId(){
        return departmentId;
    }
    public void  setDepartmentId(int departmentId){
        this.departmentId = departmentId;
    }
    
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    
    //convert to string()
    
    @Override
    public String toString(){
        return "Department{" + departmentId +
                " ,name= " + name +
                " ,description=" + description+
                "}";
    }
    
}
