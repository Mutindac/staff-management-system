/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.daoimplementation;
import ke.co.mspace.staffmanagement.dao.RoleDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import ke.co.mspace.staffmanagement.model.Role;

/**
 *
 * @author server
 */
public class RoleDAOimpl implements RoleDAO {
    private Connection connection;

    public RoleDAOimpl(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public void saveRole(Role role){
        String sql = "INSERT INTO roles(title,description) VALUES(?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, role.getTitle());
            stmt.setString(2, role.getDescription());
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public Role getRoleById(int roleId){
        Role role = new Role();
        String sql = "SELECT * FROM roles WHERE roleId  = ?";
        try(PreparedStatement stmt  = connection.prepareStatement(sql)){
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                role.setRoleId(rs.getInt("roleId"));
                role.setTitle(rs.getString("title"));
                role.setDescription(rs.getString("description"));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return role;
    }
    
    @Override
    public void updateRole(Role role){
        String sql = "UPDATE roles SET title =?,description =? WHERE roleId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, role.getTitle());
            stmt.setString(2, role.getDescription());
            stmt.setInt(3, role.getRoleId());
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteRole(int roleId){
        String sql = "DELETE FROM roles WHERE roleId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, roleId);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public List <Role> getAllRoles(){
        List<Role> RoleList = new ArrayList<>();
        
        String sql = "SELECT * FROM roles";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                Role role = new Role();
                role.setRoleId(rs.getInt("roleId"));
                role.setTitle(rs.getString("title"));
                role.setDescription(rs.getString("description"));
                RoleList.add(role);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return RoleList;
    }
    
}
