/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.serviceimpl;
import ke.co.mspace.staffmanagement.service.StaffService;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.model.Staff;
import java.util.List;
/**
 *
 * @author server
 */
public class StaffServiceimpl implements StaffService{
    private StaffDAO staffDAO;

    public StaffServiceimpl(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }
    
    @Override
    public void addStaff(Staff staff){
        //rules
        //staff have department
        if(staff.getDepartmentId() == 0){
            throw new IllegalArgumentException("Staff must have a department!");
        }
        staffDAO.saveStaff(staff);
    }
    
    @Override
    public Staff getStaffById(int staffId){
        return staffDAO.getStaffById(staffId);
    }
    
    @Override
    public List<Staff> getAllStaff(){
        return staffDAO.getAllStaff();
    }
    
    @Override
    public void updateStaff(Staff staff){
        staffDAO.updateStaff(staff);
    }
    
    @Override
    public void deleteStaff(int staffId){
        staffDAO.deleteStaff(staffId);
    }
    
}
