/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.controller;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import ke.co.mspace.staffmanagement.dao.RoleDAO;
import ke.co.mspace.staffmanagement.dao.DepartmentDAO;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import java.sql.Connection;
import ke.co.mspace.staffmanagement.util.DButil;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import ke.co.mspace.staffmanagement.model.Staff;
import ke.co.mspace.staffmanagement.model.Department;
import ke.co.mspace.staffmanagement.model.Role;
/**
 *
 * @author server
 */
@Named("dashboardBean")
@SessionScoped
public class DashboardBean implements Serializable{
    
    private String departmentChartModel;
    private String statusChartModel;
    private String roleChartModel;
    
    public DashboardBean(){
        initCharts();
    }
    
    private StaffDAO getStaffDAO() throws Exception {
        return new StaffDAO(DButil.getConnection());
    }
    
    private DepartmentDAO getDepartmentDAO() throws Exception {
        return new DepartmentDAO(DButil.getConnection());
    }
    
    private RoleDAO getRoleDAO() throws Exception {
        return new RoleDAO(DButil.getConnection());
    }
    
    private void initCharts() {
        createDepartmentChart();
        createStatusChart();
        createRoleChart();
    }
    
    private void createDepartmentChart() {
        String[] colors = {"#3b82f6", "#e94560", "#10b981", "#f59e0b", "#8b5cf6", "#ec4899"};
        Map<Integer, Integer> deptCounts = new HashMap<>();
        try {
            StaffDAO staffDAO = getStaffDAO();
            DepartmentDAO departmentDAO = getDepartmentDAO();
            for (Staff s : staffDAO.getAllStaff()) {
                if (s.getDepartmentId() > 0) {
                    deptCounts.put(s.getDepartmentId(), deptCounts.getOrDefault(s.getDepartmentId(), 0) + 1);
                }
            }
        
            StringBuilder labels = new StringBuilder("[");
            StringBuilder data = new StringBuilder("[");
            StringBuilder bgColors = new StringBuilder("[");
            
            int colorIdx = 0;
            boolean first = true;
            for (Map.Entry<Integer, Integer> entry : deptCounts.entrySet()) {
                Department d = departmentDAO.getDepartmentById(entry.getKey());
                if (d != null) {
                    if (!first) {
                        labels.append(",");
                        data.append(",");
                        bgColors.append(",");
                    }
                    labels.append("\"").append(d.getName().replace("\"", "\\\"")).append("\"");
                    data.append(entry.getValue());
                    bgColors.append("\"").append(colors[colorIdx % colors.length]).append("\"");
                    first = false;
                    colorIdx++;
                }
            }
            labels.append("]");
            data.append("]");
            bgColors.append("]");
            
            departmentChartModel = "{"
                + "\"type\": \"doughnut\","
                + "\"data\": {"
                + "  \"labels\": " + labels.toString() + ","
                + "  \"datasets\": [{"
                + "    \"data\": " + data.toString() + ","
                + "    \"backgroundColor\": " + bgColors.toString()
                + "  }]"
                + "},"
                + "\"options\": {"
                + "  \"maintainAspectRatio\": false,"
                + "  \"plugins\": {"
                + "    \"legend\": {\"position\": \"bottom\"}"
                + "  }"
                + "}"
                + "}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void createStatusChart() {
        int active = getActiveStaff();
        int inactive = getInactiveStaff();
        statusChartModel = "{"
            + "\"type\": \"pie\","
            + "\"data\": {"
            + "  \"labels\": [\"Active\", \"Inactive\"],"
            + "  \"datasets\": [{"
            + "    \"data\": [" + active + ", " + inactive + "],"
            + "    \"backgroundColor\": [\"#10b981\", \"#ef4444\"]"
            + "  }]"
            + "},"
            + "\"options\": {"
            + "  \"maintainAspectRatio\": false,"
            + "  \"plugins\": {"
            + "    \"legend\": {\"position\": \"bottom\"}"
            + "  }"
            + "}"
            + "}";
    }
    
    private void createRoleChart() {
        Map<Integer, Integer> roleCounts = new HashMap<>();
        try {
            StaffDAO staffDAO = getStaffDAO();
            RoleDAO roleDAO = getRoleDAO();
            for (Staff s : staffDAO.getAllStaff()) {
                if (s.getRoleId() > 0) {
                    roleCounts.put(s.getRoleId(), roleCounts.getOrDefault(s.getRoleId(), 0) + 1);
                }
            }
        
            StringBuilder labels = new StringBuilder("[");
            StringBuilder data = new StringBuilder("[");
            
            boolean first = true;
            for (Map.Entry<Integer, Integer> entry : roleCounts.entrySet()) {
                Role r = roleDAO.getRoleById(entry.getKey());
                if (r != null) {
                    if (!first) {
                        labels.append(",");
                        data.append(",");
                    }
                    labels.append("\"").append(r.getTitle().replace("\"", "\\\"")).append("\"");
                    data.append(entry.getValue());
                    first = false;
                }
            }
            labels.append("]");
            data.append("]");
            
            roleChartModel = "{"
                + "\"type\": \"bar\","
                + "\"data\": {"
                + "  \"labels\": " + labels.toString() + ","
                + "  \"datasets\": [{"
                + "    \"label\": \"Headcount per role\","
                + "    \"data\": " + data.toString() + ","
                + "    \"backgroundColor\": \"#3b82f6\""
                + "  }]"
                + "},"
                + "\"options\": {"
                + "  \"maintainAspectRatio\": false,"
                + "  \"indexAxis\": \"y\","
                + "  \"plugins\": {"
                + "    \"legend\": {\"display\": false}"
                + "  },"
                + "  \"scales\": {"
                + "    \"x\": {\"ticks\": {\"stepSize\": 1}}"
                + "  }"
                + "}"
                + "}";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getDepartmentChartModel() { 
        return departmentChartModel; 
    }
    
    public String getStatusChartModel() { 
        return statusChartModel; 
    }
    
    public String getRoleChartModel() { 
        return roleChartModel; 
    }
    
    /** Call this to force a fresh chart rebuild (e.g. after a staff change). */
    public void refreshCharts() {
        initCharts();
    }
    
    //method for return total number of staff
    public int getTotalStaff(){
        try {
            return getStaffDAO().getAllStaff().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    //method for getting total number of departmenta
    public int getAllDepartments(){
        try {
            return getDepartmentDAO().getAllDepartments().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    //method for getting total number or roles
    public int getAllRoles(){
        try {
            return getRoleDAO().getAllRoles().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    //method for getting active staff
    public int getActiveStaff() {
        try {
            return (int) getStaffDAO().getAllStaff().stream()
                    .filter(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()))
                    .count();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    //method for getting inactive staff
    public int getInactiveStaff() {
        try {
            return (int) getStaffDAO().getAllStaff().stream()
                    .filter(s -> "INACTIVE".equalsIgnoreCase(s.getStatus()))
                    .count();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
