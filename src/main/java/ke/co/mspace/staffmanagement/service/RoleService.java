package ke.co.mspace.staffmanagement.service;

import ke.co.mspace.staffmanagement.dao.RoleDAO;
import ke.co.mspace.staffmanagement.model.Role;
import java.util.List;

public class RoleService {
    private RoleDAO roleDAO;

    public RoleService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }
    
    public void addRole(Role role){
        roleDAO.saveRole(role);
    }
    
    public Role getRoleById(int roleId){
        return roleDAO.getRoleById(roleId);
    }
    
    public List<Role> getAllRoles(){
        return roleDAO.getAllRoles();
    }
    
    public void updateRole(Role role){
        roleDAO.updateRole(role);
    }
    
    public void deleteRole(int roleId){
        roleDAO.deleteRole(roleId);
    }
}
