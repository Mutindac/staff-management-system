/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.model;
import java.util.Date;
import java.io.Serializable;
/**
 *
 * @author server
 */
public class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    //database fields
    private int staffId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date hireDate;
    private int departmentId;
    private int roleId;
    private String status;
    
    //default constructor
    public Staff(){
        
    }
    
    //parameterized constructor
    public Staff(int staffId, String firstName,String lastName,String email,String phone,Date hireDate,int roleId,int departmentId,String status){
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.hireDate = hireDate;
        this.departmentId = departmentId;
        this.roleId = roleId;
        this.status = status;
    }
    
    //getters and setters
    
    public int getStaffId(){
        return staffId;
    }
    public void setStaffId(int staffId){
        this.staffId = staffId;
    }
    
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    
    public Date getHireDate(){
        return hireDate;
    }
    public void setHireDate(Date hireDate){
        this.hireDate = hireDate;
    }
    
    public int getDepartmentId(){
        return departmentId;
    }
    public void setDepartmentId(int departmentId){
        this.departmentId = departmentId;
    }
    
    public int getRoleId(){
        return roleId;
    }
    public void setRoleId(int roleId){
        this.roleId = roleId;
    }
    
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    
    
    //to string()method
   @Override
    public String toString(){
        return "staff{"+
                "staffId=" + staffId +
                " firstName= " + firstName +
                ", lastName=" + lastName +
                "  ,email=" + email +
                ", phone=" + phone +
                ", hireDate=" + hireDate +
                " ,departmentId=" + departmentId +
                "' roleId=" + roleId +
                ", status=" + status +
                "}";
    }
}
