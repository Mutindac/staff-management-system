/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.controller;
import ke.co.mspace.staffmanagement.model.Role;
import ke.co.mspace.staffmanagement.service.RoleService;
import ke.co.mspace.staffmanagement.serviceimpl.RoleServiceimpl;
import ke.co.mspace.staffmanagement.dao.RoleDAO;
import ke.co.mspace.staffmanagement.daoimplementation.RoleDAOimpl;
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
    private RoleService roleService;
    private Role role = new Role();
    private List<Role> roleList;
    
    public RoleBean(){
        try {
            Connection conn = DButil.getConnection();
            RoleDAO roleDAO = new RoleDAOimpl(conn);
            roleService = new RoleServiceimpl(roleDAO);
            roleList = roleService.getAllRoles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //add new role
    public void addRole(Role role){
        roleService.addRole(role);
        roleList = roleService.getAllRoles();
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
        roleService.updateRole(role);
        roleList = roleService.getAllRoles();
        return "roleList.xhtml?faces-redirect=true";
    }
    
    //delete role
    public String deleteRole(int roleId){
        roleService.deleteRole(roleId);
        roleList = roleService.getAllRoles();
        return "roleList.xhtml?faces-redirect=true";
    }
    
    //get role by id
    public Role getRoleById(int roleId){
        return roleService.getRoleById(roleId);
    }

    //getters and setters
    
    public RoleService getRoleService() {
        return roleService;
    }

    public Role getRole() {
        return role;
    }

    public List<Role> getRoleList() {
        if (roleList == null) {
            if(roleService == null){
                System.out.println(" Role is null....");
                try{
                    Connection conn = DButil.getConnection();
                    RoleDAO roleDAO = new RoleDAOimpl(conn);
                    roleService = new RoleServiceimpl(roleDAO);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            roleList = roleService.getAllRoles();
        }
        return roleList;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
    
    
}
