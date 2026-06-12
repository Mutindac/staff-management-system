/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.controller;
import ke.co.mspace.staffmanagement.dao.DepartmentDAO;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import ke.co.mspace.staffmanagement.model.Department;
import ke.co.mspace.staffmanagement.util.DButil;
import java.sql.Connection;
/**
 *
 * @author server
 */
@Named("departmentBean")
@SessionScoped
public class DepartmentBean implements Serializable{
    private DepartmentDAO departmentDAO;
    private Department department = new Department();
    private List<Department> departmentList;
    
    
    public DepartmentBean(){
        try {
            Connection conn = DButil.getConnection();
            departmentDAO = new DepartmentDAO(conn);
            departmentList = departmentDAO.getAllDepartments();
            System.out.println("Departments loaded");
        } catch (Exception e) {
             System.out.println("CONSTRUCTOR FAILED");
            e.printStackTrace();
        }
    }
    
    //add new department
    public void addDepartment(Department department){
        departmentDAO.saveDepartment(department);
        departmentList = departmentDAO.getAllDepartments();
        this.department = new Department();
    }
    
    //prepare add new department
    public void prepareAddDepartment(){
        this.department = new Department();
    }

    //prepare update department
    public void prepareUpdateDepartment(Department department){
        this.department = department;
    }

    //update department
    public String updateDepartment(Department department){
        departmentDAO.updateDepartment(department);
        departmentList = departmentDAO.getAllDepartments();
        return "departmentList.xhtml?faces-redirect=true";
    }
    
    //delete department
    public String deleteDepartment(int departmentId){
        departmentDAO.deleteDepartment(departmentId);
        departmentList = departmentDAO.getAllDepartments();
        return "departmentList.xhtml?faces-redirect=true";
    }
    
    //get department by id
    public Department getDepartmentById(int departmentId){
        return departmentDAO.getDepartmentById(departmentId);
    }

    //getters and setters
    public DepartmentDAO getDepartmentDAO() {
        return departmentDAO;
    }

    public Department getDepartment() {
        return department;
    }

    
    public List<Department> getDepartmentList() {
        if (departmentList == null) {
            if (departmentDAO == null) {
                System.out.println("departmentDAO is null, initializing...");
                try {
                    Connection conn = DButil.getConnection();
                    departmentDAO = new DepartmentDAO(conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            departmentList = departmentDAO.getAllDepartments();
        }
        return departmentList;
    }
    public void setDepartmentDAO(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }
    
    
}
