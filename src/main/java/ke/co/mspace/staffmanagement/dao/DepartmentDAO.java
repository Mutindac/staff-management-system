package ke.co.mspace.staffmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import ke.co.mspace.staffmanagement.model.Department;

public class DepartmentDAO {
    private Connection connection;

    public DepartmentDAO(Connection connection) {
        this.connection = connection;
    }
    
    public void saveDepartment(Department department){
        String sql = "INSERT INTO department(name,description) VALUES(?,?)"; 
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, department.getName());
            stmt.setString(2, department.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public Department getDepartmentById(int departmentId){
        Department department = new Department();
        String sql = "SELECT * FROM department WHERE departmentId = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                department.setDepartmentId(rs.getInt("departmentId"));
                department.setName(rs.getString("name"));
                department.setDescription(rs.getString("description"));

            } 
        } catch(SQLException e){
            e.printStackTrace();
        }
        return department;
    }
    
    public void updateDepartment(Department department){
        String sql = "UPDATE department SET name =?,description =? WHERE departmentId = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, department.getName());
            stmt.setString(2, department.getDescription());
            stmt.setInt(3, department.getDepartmentId());
            stmt.executeUpdate();
            
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void deleteDepartment(int departmentId){
        String sql = "DELETE FROM department WHERE departmentId = ?";
        try(PreparedStatement stmt  = connection.prepareStatement(sql)){
            stmt.setInt(1, departmentId);
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public List<Department> getAllDepartments(){
        List<Department> DepartmentList = new ArrayList<>();
        
        String sql = "SELECT * FROM  department";
        try(Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                Department department = new Department();
                department.setDepartmentId(rs.getInt("departmentId"));
                department.setName(rs.getString("name"));
                department.setDescription(rs.getString("description"));  
                DepartmentList.add(department);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return DepartmentList;
    }
}
