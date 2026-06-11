/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ke.co.mspace.staffmanagement.service;
import ke.co.mspace.staffmanagement.model.Department;
import java.util.List;
/**
 *
 * @author server
 */
public interface DepartmentService {
    void addDepartment(Department department);
    Department getDepartmentById(int departmentId);
    List<Department> getAllDepartments();
    void updateDepartment(Department department);
    void deleteDepartment(int departmentId);
}
