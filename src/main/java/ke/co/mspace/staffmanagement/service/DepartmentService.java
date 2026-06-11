package ke.co.mspace.staffmanagement.service;

import ke.co.mspace.staffmanagement.dao.DepartmentDAO;
import ke.co.mspace.staffmanagement.model.Department;
import java.util.List;

public class DepartmentService {
    private DepartmentDAO departmentDAO;

    public DepartmentService(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }
    
    public void addDepartment(Department department){
        departmentDAO.saveDepartment(department);
    }
    
    public Department getDepartmentById(int departmentId){
        return departmentDAO.getDepartmentById(departmentId);
    }
    
    public List<Department> getAllDepartments(){
        return departmentDAO.getAllDepartments();
    }
    
    public void updateDepartment(Department department){
        departmentDAO.updateDepartment(department);
    }
    
    public void deleteDepartment(int departmentId){
        departmentDAO.deleteDepartment(departmentId);
    }
}
