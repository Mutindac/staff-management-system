/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.serviceimpl;
import ke.co.mspace.staffmanagement.dao.RoleDAO;
import ke.co.mspace.staffmanagement.model.Role;
import ke.co.mspace.staffmanagement.service.RoleService;
import java.util.List;
/**
 *
 * @author server
 */
public class RoleServiceimpl implements RoleService{
    private RoleDAO roleDAO;

    public RoleServiceimpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }
    
    @Override
    public void addRole(Role role){
        roleDAO.saveRole(role);
    }
    
    @Override
    public Role getRoleById(int roleId){
        return roleDAO.getRoleById(roleId);
    }
    
    @Override
    public List<Role> getAllRoles(){
        return roleDAO.getAllRoles();
    }
    
    @Override
    public void updateRole(Role role){
        roleDAO.updateRole(role);
    }
    
    @Override
    public void deleteRole(int roleId){
        roleDAO.deleteRole(roleId);
    }
}
