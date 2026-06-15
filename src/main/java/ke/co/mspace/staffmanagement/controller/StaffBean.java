/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.controller;
import ke.co.mspace.staffmanagement.model.Staff;
//import ke.co.mspace.staffmanagement.model.Department;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.util.DButil;
import ke.co.mspace.staffmanagement.dao.RoleDAO;
import ke.co.mspace.staffmanagement.dao.DepartmentDAO;
import ke.co.mspace.staffmanagement.model.UserAccount;
import ke.co.mspace.staffmanagement.dao.UserAccountDAO;
import ke.co.mspace.staffmanagement.dao.AttendanceDAO;
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
    private StaffDAO staffDAO;
    private Staff staff = new Staff();
    private List<Staff> staffList;
    private RoleDAO roleDAO;
    private DepartmentDAO departmentDAO;
    private UserAccountDAO userAccountDAO;
    private AttendanceDAO attendanceDAO;
    private UserAccount userAccount = new UserAccount();
    private boolean createAccount;
    private String newPassword;

    public StaffBean(){
        try {
            Connection conn = DButil.getConnection();
            staffDAO = new StaffDAO(conn);
            roleDAO = new RoleDAO(conn);
            departmentDAO = new DepartmentDAO(conn);
            userAccountDAO = new UserAccountDAO(conn);
            attendanceDAO = new AttendanceDAO(conn);
            staffList = staffDAO.getAllStaff();
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
        int newStaffId = staffDAO.saveStaff(staff);
        
        if (createAccount && newStaffId > 0) {
            userAccount.setStaffId(newStaffId);
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                userAccount.setPasswordHash(ke.co.mspace.staffmanagement.util.PasswordUtil.hashPassword(newPassword));
            }
            userAccountDAO.saveUserAccount(userAccount);
        }
        
        staffList = staffDAO.getAllStaff();
        this.staff = new Staff();
        this.userAccount = new UserAccount();
        this.newPassword = null;
        this.createAccount = false;
    }
    
    //prepare add new staff
    public void prepareAddStaff(){
        this.staff = new Staff();
        this.userAccount = new UserAccount();
        this.newPassword = null;
        this.createAccount = false;
    }

    //update staff
    public void updateStaff(Staff staff){
        staffDAO.updateStaff(staff);
        System.out.println("Updating staff: " + staff);
        staffList = staffDAO.getAllStaff();
    }
    
    //delete staff
    public String deleteStaff(int staffId){
        attendanceDAO.deleteAttendanceByStaffId(staffId);
        userAccountDAO.deleteUserAccountByStaffId(staffId);
        staffDAO.deleteStaff(staffId);
        staffList = staffDAO.getAllStaff();
        return "staffList.xhtml?faces-redirect=true";
    }
    
    //get staff by id
    public Staff getStaffById(int staffId){
        return staffDAO.getStaffById(staffId);
    }
    
    //getters and setters

    public StaffDAO getStaffDAO() {
        return staffDAO;
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

    public void setStaffDAO(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }
    
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public boolean isCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(boolean createAccount) {
        this.createAccount = createAccount;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    
    //method for getting role title
    public String getRoleTitle(Staff staff){
        return roleDAO.getRoleById(staff.getRoleId()).getTitle();
    }
    
    //method for getting department name
    public String getDepartmentName(Staff staff){
        return departmentDAO.getDepartmentById(staff.getDepartmentId()).getName();
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
