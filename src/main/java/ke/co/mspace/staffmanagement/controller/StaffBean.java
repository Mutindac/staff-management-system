/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.controller;
import ke.co.mspace.staffmanagement.model.Staff;
//import ke.co.mspace.staffmanagement.model.Department;
import ke.co.mspace.staffmanagement.service.StaffService;
import ke.co.mspace.staffmanagement.service.RoleService;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.util.DButil;
import ke.co.mspace.staffmanagement.dao.RoleDAO;
import ke.co.mspace.staffmanagement.dao.DepartmentDAO;
import ke.co.mspace.staffmanagement.service.DepartmentService;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
/**
 *
 * @author server
 */
@Named("staffBean")
@SessionScoped
public class StaffBean implements Serializable{
    private StaffService staffService;
    private Staff staff = new Staff();
    private List<Staff> staffList;
    private RoleService roleService;
    private DepartmentService departmentService;

    public StaffBean(){
        try {
            Connection conn = DButil.getConnection();
            StaffDAO staffDAO = new StaffDAO(conn);
            RoleDAO roleDAO = new RoleDAO(conn);
            DepartmentDAO departmentDAO = new DepartmentDAO(conn);
            departmentService = new DepartmentService(departmentDAO);
            roleService = new RoleService(roleDAO) ;
            staffService = new StaffService(staffDAO);
            staffList = staffService.getAllStaff();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    //add new staff
    public void addStaff(Staff staff){
        System.out.println("Staff: " + staff);
        if(staff.getStatus() == null){
            staff.setStatus("ACTIVE");
        }
        staffService.addStaff(staff);
        staffList = staffService.getAllStaff();
        this.staff = new Staff();
    }
    
    //prepare add new staff
    public void prepareAddStaff(){
        this.staff = new Staff();
    }

    //update staff
    public void updateStaff(Staff staff){
        staffService.updateStaff(staff);
        System.out.println("Updating staff: " + staff);
        staffList = staffService.getAllStaff();
    }
    
    //delete staff
    public String deleteStaff(int staffId){
        staffService.deleteStaff(staffId);
        staffList = staffService.getAllStaff();
        return "staffList.xhtml?faces-redirect=true";
    }
    
    //get staff by id
    public Staff getStaffById(int staffId){
        return staffService.getStaffById(staffId);
    }
    
    //getters and setters

    public StaffService getStaffService() {
        return staffService;
    }

    public Staff getStaff() {
        if (staff == null) {
            staff = new Staff();
        }
        return staff;
    }

    public List<Staff> getStaffList() {
        
        return staffList;
    }

    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }
    
    
    //method for getting role title
    public String getRoleTitle(Staff staff){
        return roleService.getRoleById(staff.getRoleId()).getTitle();
    }
    
    //method for getting department name
    public String getDepartmentName(Staff staff){
        return departmentService.getDepartmentById(staff.getDepartmentId()).getName();
    }
    
    //method for navigation to the update form when update button is clicked
    public void prepareUpdateStaff(Staff staff){
        this.staff = staff;
    }
    
    //method for navigation to the details page when view button is clicked
    public void prepareViewStaff(Staff staff){
        this.staff = staff;
    }
}
