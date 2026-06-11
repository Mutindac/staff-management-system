/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ke.co.mspace.staffmanagement.dao;

/**
 *
 * @author server
 */
import ke.co.mspace.staffmanagement.model.Staff;
import java.util.List;

public interface StaffDAO {
    void saveStaff(Staff staff);
    Staff getStaffById(int staffId);
    List <Staff> getAllStaff();
    void updateStaff(Staff staff);
    void deleteStaff(int staffId);
}
