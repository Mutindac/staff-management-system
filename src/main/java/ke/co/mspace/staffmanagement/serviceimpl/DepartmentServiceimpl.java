/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.serviceimpl;
import ke.co.mspace.staffmanagement.service.DepartmentService;
import ke.co.mspace.staffmanagement.dao.DepartmentDAO;
import ke.co.mspace.staffmanagement.model.Department;
import java.util.List;
/**
 *
 * @author server
 */
public class DepartmentServiceimpl implements DepartmentService{
    private DepartmentDAO departmentDAO;

    public DepartmentServiceimpl(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }
    
    @Override
    public void addDepartment(Department department){
        departmentDAO.saveDepartment(department);
    }
    
    @Override
    public Department getDepartmentById(int departmentId){
        return departmentDAO.getDepartmentById(departmentId);
    }
    
    @Override
    public List<Department> getAllDepartments(){
        return departmentDAO.getAllDepartments();
    }
    
    @Override
    public void updateDepartment(Department department){
        departmentDAO.updateDepartment(department);
    }
    
    @Override
    public void deleteDepartment(int departmentId){
        departmentDAO.deleteDepartment(departmentId);
    }
}
