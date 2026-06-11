/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.controller;
import ke.co.mspace.staffmanagement.service.DepartmentService;
import ke.co.mspace.staffmanagement.serviceimpl.DepartmentServiceimpl;
import ke.co.mspace.staffmanagement.dao.DepartmentDAO;
import ke.co.mspace.staffmanagement.daoimplementation.DepartmentDAOimpl;
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
    private DepartmentService departmentService;
    private Department department = new Department();
    private List<Department> departmentList;
    
    
    public DepartmentBean(){
        try {
            Connection conn = DButil.getConnection();
            DepartmentDAO departmentDAO = new DepartmentDAOimpl(conn);
            departmentService = new DepartmentServiceimpl(departmentDAO);           
            departmentList = departmentService.getAllDepartments();
            System.out.println("Departments loaded");
        } catch (Exception e) {
             System.out.println("CONSTRUCTOR FAILED");
            e.printStackTrace();
        }
    }
    
    //add new department
    public void addDepartment(Department department){
        departmentService.addDepartment(department);
        departmentList = departmentService.getAllDepartments();
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
        departmentService.updateDepartment(department);
        departmentList = departmentService.getAllDepartments();
        return "departmentList.xhtml?faces-redirect=true";
    }
    
    //delete department
    public String deleteDepartment(int departmentId){
        departmentService.deleteDepartment(departmentId);
        departmentList = departmentService.getAllDepartments();
        return "departmentList.xhtml?faces-redirect=true";
    }
    
    //get department by id
    public Department getDepartmentById(int departmentId){
        return departmentService.getDepartmentById(departmentId);
    }

    //getters and setters
    public DepartmentService getDepartmentService() {
        return departmentService;
    }

    public Department getDepartment() {
        return department;
    }

    
    public List<Department> getDepartmentList() {
        if (departmentList == null) {
            if (departmentService == null) {
                System.out.println("departmentService is null, initializing...");
                try {
                    Connection conn = DButil.getConnection();
                    DepartmentDAO departmentDAO = new DepartmentDAOimpl(conn);
                    departmentService = new DepartmentServiceimpl(departmentDAO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            departmentList = departmentService.getAllDepartments();
        }
        return departmentList;
    }
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }
    
    
}
