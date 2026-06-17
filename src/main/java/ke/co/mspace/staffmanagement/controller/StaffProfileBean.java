package ke.co.mspace.staffmanagement.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ke.co.mspace.staffmanagement.dao.DepartmentDAO;
import ke.co.mspace.staffmanagement.dao.RoleDAO;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.dao.UserAccountDAO;
import ke.co.mspace.staffmanagement.model.Department;
import ke.co.mspace.staffmanagement.model.Role;
import ke.co.mspace.staffmanagement.model.Staff;
import ke.co.mspace.staffmanagement.model.UserAccount;
import ke.co.mspace.staffmanagement.dao.DepartmentDAO;
import ke.co.mspace.staffmanagement.dao.RoleDAO;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.dao.UserAccountDAO;
import ke.co.mspace.staffmanagement.dao.AttendanceDAO;
import ke.co.mspace.staffmanagement.util.DButil;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Base64;
import java.util.stream.Collectors;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

@Named("staffProfileBean")
@SessionScoped
public class StaffProfileBean implements Serializable {

    private Staff currentStaff;
    private UserAccount currentUserAccount;
    
    private String departmentName;
    private String roleName;
    
    private String newPassword;
    
    private int totalActiveStaff;
    private int departmentSize;
    private List<Staff> colleagues;
    private String departmentRoleChartModel;
    
    private java.util.Date filterStartDate;
    private java.util.Date filterEndDate;

    private StaffDAO staffDAO;
    private UserAccountDAO userAccountDAO;
    private DepartmentDAO departmentDAO;
    private RoleDAO roleDAO;
    private AttendanceDAO attendanceDAO;
    private ke.co.mspace.staffmanagement.model.Attendance todayAttendance;
    private List<ke.co.mspace.staffmanagement.model.Attendance> myAttendanceHistory;

    public StaffProfileBean() {
        try {
            Connection conn = DButil.getConnection();
            staffDAO = new StaffDAO(conn);
            userAccountDAO = new UserAccountDAO(conn);
            departmentDAO = new DepartmentDAO(conn);
            roleDAO = new RoleDAO(conn);
            attendanceDAO = new AttendanceDAO(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        loadUserProfile();
    }

    public void loadUserProfile() {
        FacesContext context = FacesContext.getCurrentInstance();
        currentUserAccount = (UserAccount) context.getExternalContext().getSessionMap().get("loggedInUser");

        if (currentUserAccount != null && currentUserAccount.getStaffId() > 0) {
            currentStaff = staffDAO.getStaffById(currentUserAccount.getStaffId());
            
            if (currentStaff != null) {
                Department dept = departmentDAO.getDepartmentById(currentStaff.getDepartmentId());
                departmentName = (dept != null) ? dept.getName() : "N/A";
                
                Role role = roleDAO.getRoleById(currentStaff.getRoleId());
                roleName = (role != null) ? role.getTitle() : "N/A";
                
                List<Staff> allStaff = staffDAO.getAllStaff();
                
                totalActiveStaff = (int) allStaff.stream()
                        .filter(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()))
                        .count();
                        
                colleagues = allStaff.stream()
                        .filter(s -> s.getDepartmentId() == currentStaff.getDepartmentId() && s.getStaffId() != currentStaff.getStaffId())
                        .collect(Collectors.toList());
                        
                departmentSize = colleagues.size() + 1; // Including the current user
                
                loadTodayAttendance();
                myAttendanceHistory = attendanceDAO.getAttendanceLogsByStaff(currentStaff.getStaffId());
                generateRoleChart();
            }
        }
    }
    
    public void loadTodayAttendance() {
        if (currentStaff != null) {
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            todayAttendance = attendanceDAO.getTodayAttendance(currentStaff.getStaffId(), today);
        }
    }
    
    public void checkIn() {
        if (todayAttendance == null) {
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
            attendanceDAO.checkIn(currentStaff.getStaffId(), today, now);
            loadTodayAttendance();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Checked in successfully"));
        }
    }

    public void checkOut() {
        if (todayAttendance != null && todayAttendance.getCheckOutTime() == null) {
            java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
            attendanceDAO.checkOut(todayAttendance.getAttendanceId(), now);
            loadTodayAttendance();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Checked out successfully"));
        }
    }

    public void updateProfile() {
        try {
            staffDAO.updateStaff(currentStaff);
            
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                currentUserAccount.setPasswordHash(ke.co.mspace.staffmanagement.util.PasswordUtil.hashPassword(newPassword));
                userAccountDAO.updateUserAccount(currentUserAccount);
                newPassword = null; 
            }
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Profile updated successfully"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update profile"));
            e.printStackTrace();
        }
    }
    
    public void handleImageUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            if (file != null && file.getContent() != null && file.getContent().length > 0) {
                byte[] bytes = file.getContent();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                currentUserAccount.setProfileImage(base64Image);
                userAccountDAO.updateUserAccount(currentUserAccount);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Profile image updated successfully."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to upload image."));
            e.printStackTrace();
        }
    }
    
    public void filterAttendanceHistory() {
        if (currentStaff != null) {
            if (filterStartDate != null && filterEndDate != null) {
                myAttendanceHistory = attendanceDAO.getAttendanceLogsByStaffAndDateRange(currentStaff.getStaffId(), filterStartDate, filterEndDate);
            } else {
                myAttendanceHistory = attendanceDAO.getAttendanceLogsByStaff(currentStaff.getStaffId());
            }
        }
    }

    // Getters and Setters
    public Staff getCurrentStaff() { return currentStaff; }
    public void setCurrentStaff(Staff currentStaff) { this.currentStaff = currentStaff; }

    public UserAccount getCurrentUserAccount() { return currentUserAccount; }
    public void setCurrentUserAccount(UserAccount currentUserAccount) { this.currentUserAccount = currentUserAccount; }

    public String getDepartmentName() { return departmentName; }
    public String getRoleName() { return roleName; }
    
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    
    public int getTotalActiveStaff() { return totalActiveStaff; }
    public int getDepartmentSize() { return departmentSize; }
    public List<Staff> getColleagues() { return colleagues; }
    public String getDepartmentRoleChartModel() { return departmentRoleChartModel; }
    
    public java.util.Date getFilterStartDate() { return filterStartDate; }
    public void setFilterStartDate(java.util.Date filterStartDate) { this.filterStartDate = filterStartDate; }
    
    public java.util.Date getFilterEndDate() { return filterEndDate; }
    public void setFilterEndDate(java.util.Date filterEndDate) { this.filterEndDate = filterEndDate; }
    
    public ke.co.mspace.staffmanagement.model.Attendance getTodayAttendance() { return todayAttendance; }
    public void setTodayAttendance(ke.co.mspace.staffmanagement.model.Attendance todayAttendance) { this.todayAttendance = todayAttendance; }
    
    public List<ke.co.mspace.staffmanagement.model.Attendance> getMyAttendanceHistory() { return myAttendanceHistory; }
    public void setMyAttendanceHistory(List<ke.co.mspace.staffmanagement.model.Attendance> myAttendanceHistory) { this.myAttendanceHistory = myAttendanceHistory; }
    
    public String getRoleTitleForStaff(Staff staff) {
        Role r = roleDAO.getRoleById(staff.getRoleId());
        return r != null ? r.getTitle() : "N/A";
    }

    private void generateRoleChart() {
        java.util.Map<Integer, Integer> roleCounts = new java.util.HashMap<>();
        roleCounts.put(currentStaff.getRoleId(), 1); // Include self
        
        for (Staff s : colleagues) {
            if (s.getRoleId() > 0) {
                roleCounts.put(s.getRoleId(), roleCounts.getOrDefault(s.getRoleId(), 0) + 1);
            }
        }
        
        StringBuilder labels = new StringBuilder("[");
        StringBuilder data = new StringBuilder("[");
        StringBuilder bgColors = new StringBuilder("[");
        String[] colors = {"#3b82f6", "#e94560", "#10b981", "#f59e0b", "#8b5cf6", "#ec4899"};
        
        boolean first = true;
        int colorIdx = 0;
        for (java.util.Map.Entry<Integer, Integer> entry : roleCounts.entrySet()) {
            Role r = roleDAO.getRoleById(entry.getKey());
            if (r != null) {
                if (!first) {
                    labels.append(",");
                    data.append(",");
                    bgColors.append(",");
                }
                labels.append("\"").append(r.getTitle().replace("\"", "\\\"")).append("\"");
                data.append(entry.getValue());
                bgColors.append("\"").append(colors[colorIdx % colors.length]).append("\"");
                first = false;
                colorIdx++;
            }
        }
        labels.append("]");
        data.append("]");
        bgColors.append("]");
        
        departmentRoleChartModel = "{"
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
    }
}
