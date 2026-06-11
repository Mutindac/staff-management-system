/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ke.co.mspace.staffmanagement.service;
import ke.co.mspace.staffmanagement.model.Role;
import java.util.List;

/**
 *
 * @author server
 */
public interface RoleService {
    void addRole(Role role);
    Role getRoleById(int roleId);
    List<Role> getAllRoles();
    void updateRole(Role role);
    void deleteRole(int roleId);
}
