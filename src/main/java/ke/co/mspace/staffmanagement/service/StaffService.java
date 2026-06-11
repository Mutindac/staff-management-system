/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ke.co.mspace.staffmanagement.service;
import ke.co.mspace.staffmanagement.model.Staff;
import java.util.List;

/**
 *
 * @author server
 */
public interface StaffService {
    void addStaff(Staff staff);
    Staff getStaffById(int staffId);
    List<Staff> getAllStaff();
    void updateStaff(Staff staff);
    void deleteStaff(int staffId);
}
