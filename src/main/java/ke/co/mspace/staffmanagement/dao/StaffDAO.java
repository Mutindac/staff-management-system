package ke.co.mspace.staffmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import ke.co.mspace.staffmanagement.model.Staff;

public class StaffDAO {
    private Connection connection;
    
    public StaffDAO(Connection connection){
        this.connection = connection;
    }
    
    public void saveStaff(Staff staff){
        String sql = "INSERT INTO staff(firstName,LastName,email,phone,hireDate,departmentId,roleId,status) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, staff.getFirstName());
            stmt.setString(2, staff.getLastName());
            stmt.setString(3, staff.getEmail());
            stmt.setString(4, staff.getPhone());
            stmt.setDate(5, new java.sql.Date(staff.getHireDate().getTime()));
            stmt.setInt(6, staff.getDepartmentId());
            stmt.setInt(7, staff.getRoleId());
            stmt.setString(8, staff.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public Staff getStaffById(int staffId){
        Staff staff = new Staff();
        String sql = "SELECT * FROM staff WHERE staffId = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                staff.setStaffId(rs.getInt("staffId"));
                staff.setFirstName(rs.getString("firstName"));
                staff.setLastName(rs.getString("lastName"));
                staff.setEmail(rs.getString("email"));
                staff.setPhone(rs.getString("phone"));
                staff.setHireDate(rs.getDate("hireDate"));
                staff.setDepartmentId(rs.getInt("departmentId"));
                staff.setRoleId(rs.getInt("roleId"));
                staff.setStatus(rs.getString("status"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return staff;
    }
    
    public void updateStaff(Staff staff){
        String sql = "UPDATE staff SET firstName =?,LastName =?,email =?,phone =?,hireDate =?,departmentId =?,roleId =?,status =? WHERE staffId =?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, staff.getFirstName());
            stmt.setString(2, staff.getLastName());
            stmt.setString(3, staff.getEmail());
            stmt.setString(4, staff.getPhone());
            stmt.setDate(5, new java.sql.Date(staff.getHireDate().getTime()));
            stmt.setInt(6, staff.getDepartmentId());
            stmt.setInt(7, staff.getRoleId());
            stmt.setString(8, staff.getStatus());
            stmt.setInt(9, staff.getStaffId());
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void deleteStaff(int staffId){
        String sql = "DELETE FROM staff WHERE staffId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, staffId);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public List <Staff> getAllStaff(){
        List<Staff> StaffList = new ArrayList<>();
        
        String sql = "SELECT * FROM staff";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                Staff staff = new Staff();
                staff.setStaffId(rs.getInt("staffId"));
                staff.setFirstName(rs.getString("firstName"));
                staff.setLastName(rs.getString("lastName"));
                staff.setEmail(rs.getString("email"));
                staff.setPhone(rs.getString("phone"));
                staff.setHireDate(rs.getDate("hireDate"));
                staff.setDepartmentId(rs.getInt("departmentId"));
                staff.setRoleId(rs.getInt("roleId"));
                staff.setStatus(rs.getString("status"));
                StaffList.add(staff);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return StaffList;
    }
}
