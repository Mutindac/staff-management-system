/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.controller;
import ke.co.mspace.staffmanagement.model.Role;
import ke.co.mspace.staffmanagement.dao.RoleDAO;
import ke.co.mspace.staffmanagement.util.DButil;
import java.sql.Connection;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
/**
 *
 * @author server
 */
@Named("roleBean")
@SessionScoped
public class RoleBean implements Serializable {
    private RoleDAO roleDAO;
    private Role role = new Role();
    private List<Role> roleList;
    
    public RoleBean(){
        try {
            Connection conn = DButil.getConnection();
            roleDAO = new RoleDAO(conn);
            roleList = roleDAO.getAllRoles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //add new role
    public void addRole(Role role){
        roleDAO.saveRole(role);
        roleList = roleDAO.getAllRoles();
        this.role = new Role();
    }
    
    //prepare add new role
    public void prepareAddRole(){
        this.role = new Role();
    }

    //prepare update role
    public void prepareUpdateRole(Role role){
        this.role = role;
    }

    //update role
    public String updateRole(Role role){
        roleDAO.updateRole(role);
        roleList = roleDAO.getAllRoles();
        return "roleList.xhtml?faces-redirect=true";
    }
    
    //delete role
    public String deleteRole(int roleId){
        roleDAO.deleteRole(roleId);
        roleList = roleDAO.getAllRoles();
        return "roleList.xhtml?faces-redirect=true";
    }
    
    //get role by id
    public Role getRoleById(int roleId){
        return roleDAO.getRoleById(roleId);
    }

    //getters and setters
    
    public RoleDAO getRoleDAO() {
        return roleDAO;
    }

    public Role getRole() {
        return role;
    }

    public List<Role> getRoleList() {
        if (roleList == null) {
            if(roleDAO == null){
                System.out.println(" Role is null....");
                try {
                    Connection conn = DButil.getConnection();
                    roleDAO = new RoleDAO(conn);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            roleList = roleDAO.getAllRoles();
        }
        return roleList;
    }

    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
    
    
}
