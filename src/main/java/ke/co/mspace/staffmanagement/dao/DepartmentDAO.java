/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ke.co.mspace.staffmanagement.dao;

/**
 *
 * @author server
 */
import ke.co.mspace.staffmanagement.model.Department;
import java.util.List;

public interface DepartmentDAO {
    void saveDepartment(Department department);
    Department getDepartmentById(int departmentId);
    List<Department> getAllDepartments();
    void updateDepartment(Department department);
    void deleteDepartment(int departmentId);
}
