package ke.co.mspace.staffmanagement.service;

import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.model.Staff;
import java.util.List;

public class StaffService {
    private StaffDAO staffDAO;

    public StaffService(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }
    
    public void addStaff(Staff staff){
        if(staff.getDepartmentId() == 0){
            throw new IllegalArgumentException("Staff must have a department!");
        }
        staffDAO.saveStaff(staff);
    }
    
    public Staff getStaffById(int staffId){
        return staffDAO.getStaffById(staffId);
    }
    
    public List<Staff> getAllStaff(){
        return staffDAO.getAllStaff();
    }
    
    public void updateStaff(Staff staff){
        staffDAO.updateStaff(staff);
    }
    
    public void deleteStaff(int staffId){
        staffDAO.deleteStaff(staffId);
    }
}
