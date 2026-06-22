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
import ke.co.mspace.staffmanagement.dao.StaffNotificationDAO;
import ke.co.mspace.staffmanagement.util.DButil;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;
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
    private String assetRequestsChartModel;
    private String weeklyAttendanceChartModel;
    private String leaveStatusChartModel;
    private int totalAttendanceDays;
    private int pendingLeaveCount;
    private int approvedLeaveCount;
    
    private java.util.Date filterStartDate;
    private java.util.Date filterEndDate;
    
    private List<ke.co.mspace.staffmanagement.model.Notifications> myNotifications;
    private ke.co.mspace.staffmanagement.model.Notifications selectedNotification;
    private int unreadNotificationCount;
    
    private ke.co.mspace.staffmanagement.model.Attendance todayAttendance;
    private List<ke.co.mspace.staffmanagement.model.Attendance> myAttendanceHistory;

    public StaffProfileBean() {
        // Default constructor
    }

    private StaffDAO getStaffDAO() throws Exception { return new StaffDAO(DButil.getConnection()); }
    private UserAccountDAO getUserAccountDAO() throws Exception { return new UserAccountDAO(DButil.getConnection()); }
    private DepartmentDAO getDepartmentDAO() throws Exception { return new DepartmentDAO(DButil.getConnection()); }
    private RoleDAO getRoleDAO() throws Exception { return new RoleDAO(DButil.getConnection()); }
    private AttendanceDAO getAttendanceDAO() throws Exception { return new AttendanceDAO(DButil.getConnection()); }
    private StaffNotificationDAO getStaffNotificationDAO() throws Exception { return new StaffNotificationDAO(DButil.getConnection()); }

    @PostConstruct
    public void init() {
        loadUserProfile();
    }

    public void loadUserProfile() {
        FacesContext context = FacesContext.getCurrentInstance();
        currentUserAccount = (UserAccount) context.getExternalContext().getSessionMap().get("loggedInUser");

        try {
            if (currentUserAccount != null && currentUserAccount.getStaffId() > 0) {
                currentStaff = getStaffDAO().getStaffById(currentUserAccount.getStaffId());
                
                if (currentStaff != null) {
                    Department dept = getDepartmentDAO().getDepartmentById(currentStaff.getDepartmentId());
                    departmentName = (dept != null) ? dept.getName() : "N/A";
                    
                    Role role = getRoleDAO().getRoleById(currentStaff.getRoleId());
                    roleName = (role != null) ? role.getTitle() : "N/A";
                    
                    List<Staff> allStaff = getStaffDAO().getAllStaff();
                    
                    totalActiveStaff = (int) allStaff.stream()
                            .filter(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()))
                            .count();
                            
                    colleagues = allStaff.stream()
                            .filter(s -> s.getDepartmentId() == currentStaff.getDepartmentId() && s.getStaffId() != currentStaff.getStaffId())
                            .collect(Collectors.toList());
                            
                    departmentSize = colleagues.size() + 1; // Including the current user
                    
                    loadTodayAttendance();
                    myAttendanceHistory = getAttendanceDAO().getAttendanceLogsByStaff(currentStaff.getStaffId());
                    generateRoleChart();
                    generateWeeklyAttendanceChart();
                    generateLeaveStatusChart();
                    generateAssetRequestsChart();
                    myNotifications = getStaffNotificationDAO().getNotificationsForStaff(currentStaff.getStaffId());
                    updateUnreadCount();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadTodayAttendance() {
        if (currentStaff != null) {
            try {
                java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
                todayAttendance = getAttendanceDAO().getTodayAttendance(currentStaff.getStaffId(), today);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void checkIn() {
        if (todayAttendance == null) {
            try {
                java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
                java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
                getAttendanceDAO().checkIn(currentStaff.getStaffId(), today, now);
                loadTodayAttendance();
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Checked in successfully"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void checkOut() {
        if (todayAttendance != null && todayAttendance.getCheckOutTime() == null) {
            try {
                java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
                getAttendanceDAO().checkOut(todayAttendance.getAttendanceId(), now);
                loadTodayAttendance();
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Checked out successfully"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateProfile() {
        try {
            getStaffDAO().updateStaff(currentStaff);
            
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                currentUserAccount.setPasswordHash(ke.co.mspace.staffmanagement.util.PasswordUtil.hashPassword(newPassword));
                getUserAccountDAO().updateUserAccount(currentUserAccount);
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
                // Validation: Max size 2MB
                if (file.getSize() > 2 * 1024 * 1024) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "File size exceeds the 2MB limit."));
                    return;
                }
                
                // Validation: Must be an image
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Only image files are allowed."));
                    return;
                }
                
                byte[] bytes = file.getContent();
                String ext = ".jpg";
                if (contentType.equals("image/png")) ext = ".png";
                else if (contentType.equals("image/gif")) ext = ".gif";
                
                String filename = System.currentTimeMillis() + ext;
                java.nio.file.Path path = java.nio.file.Paths.get("/home/server/uploads/staff-management-system/profiles", filename);
                java.nio.file.Files.write(path, bytes);
                
                currentUserAccount.setProfileImage("/profiles/" + filename);
                getUserAccountDAO().updateUserAccount(currentUserAccount);
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
            try {
                if (filterStartDate != null && filterEndDate != null) {
                    myAttendanceHistory = getAttendanceDAO().getAttendanceLogsByStaffAndDateRange(currentStaff.getStaffId(), filterStartDate, filterEndDate);
                } else {
                    myAttendanceHistory = getAttendanceDAO().getAttendanceLogsByStaff(currentStaff.getStaffId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public List<ke.co.mspace.staffmanagement.model.Notifications> getMyNotifications() { return myNotifications; }
    public int getUnreadNotificationCount() { return unreadNotificationCount; }
    
    public ke.co.mspace.staffmanagement.model.Notifications getSelectedNotification() { return selectedNotification; }
    public void setSelectedNotification(ke.co.mspace.staffmanagement.model.Notifications selectedNotification) { this.selectedNotification = selectedNotification; }

    private void updateUnreadCount() {
        if (myNotifications == null) unreadNotificationCount = 0;
        else unreadNotificationCount = (int) myNotifications.stream().filter(n -> !n.isIsRead()).count();
    }

    public void markAllNotificationsRead() {
        try {
            getStaffNotificationDAO().markAllReadForStaff(currentStaff.getStaffId());
            myNotifications = getStaffNotificationDAO().getNotificationsForStaff(currentStaff.getStaffId());
            updateUnreadCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewNotification(ke.co.mspace.staffmanagement.model.Notifications notif) {
        this.selectedNotification = notif;
        if (!notif.isIsRead()) {
            try {
                getStaffNotificationDAO().markAsRead(notif.getStaffNotificationId());
                myNotifications = getStaffNotificationDAO().getNotificationsForStaff(currentStaff.getStaffId());
                updateUnreadCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteNotification(ke.co.mspace.staffmanagement.model.Notifications notif) {
        try {
            getStaffNotificationDAO().deleteNotification(notif.getStaffNotificationId());
            myNotifications = getStaffNotificationDAO().getNotificationsForStaff(currentStaff.getStaffId());
            updateUnreadCount();
            
            // If the deleted notification was the one currently selected, clear it
            if (selectedNotification != null && selectedNotification.getStaffNotificationId() == notif.getStaffNotificationId()) {
                selectedNotification = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public String getAssetRequestsChartModel() { return assetRequestsChartModel; }
    public String getWeeklyAttendanceChartModel() { return weeklyAttendanceChartModel; }
    public String getLeaveStatusChartModel() { return leaveStatusChartModel; }
    public int getTotalAttendanceDays() { return totalAttendanceDays; }
    public int getPendingLeaveCount() { return pendingLeaveCount; }
    public int getApprovedLeaveCount() { return approvedLeaveCount; }
    
    public java.util.Date getFilterStartDate() { return filterStartDate; }
    public void setFilterStartDate(java.util.Date filterStartDate) { this.filterStartDate = filterStartDate; }
    
    public java.util.Date getFilterEndDate() { return filterEndDate; }
    public void setFilterEndDate(java.util.Date filterEndDate) { this.filterEndDate = filterEndDate; }
    
    public ke.co.mspace.staffmanagement.model.Attendance getTodayAttendance() { return todayAttendance; }
    public void setTodayAttendance(ke.co.mspace.staffmanagement.model.Attendance todayAttendance) { this.todayAttendance = todayAttendance; }
    
    public List<ke.co.mspace.staffmanagement.model.Attendance> getMyAttendanceHistory() { return myAttendanceHistory; }
    public void setMyAttendanceHistory(List<ke.co.mspace.staffmanagement.model.Attendance> myAttendanceHistory) { this.myAttendanceHistory = myAttendanceHistory; }
    
    public String getRoleTitleForStaff(Staff staff) {
        try {
            Role r = getRoleDAO().getRoleById(staff.getRoleId());
            return r != null ? r.getTitle() : "N/A";
        } catch (Exception e) {
            e.printStackTrace();
            return "N/A";
        }
    }

    private void generateRoleChart() {
        java.util.Map<Integer, Integer> roleCounts = new java.util.HashMap<>();
        roleCounts.put(currentStaff.getRoleId(), 1);
        for (Staff s : colleagues) {
            if (s.getRoleId() > 0) {
                roleCounts.put(s.getRoleId(), roleCounts.getOrDefault(s.getRoleId(), 0) + 1);
            }
        }
        StringBuilder labels = new StringBuilder("[");
        StringBuilder data = new StringBuilder("[");
        StringBuilder bgColors = new StringBuilder("[");
        String[] colors = {"#3b82f6", "#e94560", "#10b981", "#f59e0b", "#8b5cf6", "#ec4899"};
        try {
            boolean first = true;
            int colorIdx = 0;
            for (java.util.Map.Entry<Integer, Integer> entry : roleCounts.entrySet()) {
                Role r = getRoleDAO().getRoleById(entry.getKey());
                if (r != null) {
                    if (!first) { labels.append(","); data.append(","); bgColors.append(","); }
                    labels.append("\"").append(r.getTitle().replace("\"", "\\\"")).append("\"");
                    data.append(entry.getValue());
                    bgColors.append("\"").append(colors[colorIdx % colors.length]).append("\"");
                    first = false;
                    colorIdx++;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        labels.append("]"); data.append("]"); bgColors.append("]");
        departmentRoleChartModel = "{" +
            "\"type\": \"doughnut\"," +
            "\"data\": {\"labels\": " + labels + ", \"datasets\": [{\"data\": " + data + ", \"backgroundColor\": " + bgColors + "}]}," +
            "\"options\": {\"maintainAspectRatio\": false, \"plugins\": {\"legend\": {\"position\": \"bottom\"}}}" +
            "}";
    }

    private void generateWeeklyAttendanceChart() {
        String[] dayLabels = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        double[] hoursWorked = new double[7];
        totalAttendanceDays = 0;

        try {
            // Get the start of the current week (Monday)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            java.util.Date weekStart = cal.getTime();

            if (myAttendanceHistory != null) {
                for (ke.co.mspace.staffmanagement.model.Attendance att : myAttendanceHistory) {
                    if (att.getWorkDate() != null) {
                        java.util.Calendar attCal = java.util.Calendar.getInstance();
                        attCal.setTime(att.getWorkDate());
                        // Check if in this week
                        if (!attCal.getTime().before(weekStart)) {
                            int dow = attCal.get(java.util.Calendar.DAY_OF_WEEK);
                            // Convert Sunday=1..Saturday=7 to Mon=0..Sun=6
                            int idx = (dow == java.util.Calendar.SUNDAY) ? 6 : dow - java.util.Calendar.MONDAY;
                            if (idx >= 0 && idx < 7) {
                                totalAttendanceDays++; // count as a present day
                                double hours = 0.0;
                                if (att.getCheckInTime() != null) {
                                    long endMillis = att.getCheckInTime().getTime();
                                    
                                    if (att.getCheckOutTime() != null) {
                                        endMillis = att.getCheckOutTime().getTime();
                                    } else {
                                        // If no checkout time, only use current time if the attendance is for TODAY
                                        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
                                        if (att.getWorkDate() != null && att.getWorkDate().toString().equals(today.toString())) {
                                            endMillis = System.currentTimeMillis();
                                        }
                                        // Otherwise, they forgot to check out on a past day, so diff will be 0
                                    }
                                    
                                    long diff = endMillis - att.getCheckInTime().getTime();
                                    if (diff > 0) {
                                        hours = diff / (1000.0 * 60 * 60);
                                        // Cap at 24 hours just in case of weird data
                                        if (hours > 24) hours = 24.0;
                                    }
                                }
                                hoursWorked[idx] += Math.round(hours * 10.0) / 10.0; // Accumulate in case of multiple records per day
                            }
                        }
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        StringBuilder dataStr = new StringBuilder("[");
        StringBuilder labelStr = new StringBuilder("[");
        StringBuilder bgStr = new StringBuilder("[");
        for (int i = 0; i < 7; i++) {
            if (i > 0) { dataStr.append(","); labelStr.append(","); bgStr.append(","); }
            labelStr.append("\"").append(dayLabels[i]).append("\"");
            dataStr.append(hoursWorked[i]);
            // Use blue for hours worked, light gray if no hours
            bgStr.append(hoursWorked[i] > 0 ? "\"#3b82f6\"" : "\"#e2e8f0\"");
        }
        dataStr.append("]"); labelStr.append("]"); bgStr.append("]");

        weeklyAttendanceChartModel = "{" +
            "\"type\": \"bar\"," +
            "\"data\": {\"labels\": " + labelStr + ", \"datasets\": [{\"label\": \"Hours Worked\", \"data\": " + dataStr + ", \"backgroundColor\": " + bgStr + ", \"borderRadius\": 6, \"borderSkipped\": false}]}," +
            "\"options\": {\"maintainAspectRatio\": false, \"scales\": {\"y\": {\"display\": true, \"title\": {\"display\": true, \"text\": \"Hours\", \"font\": {\"size\": 11}}, \"beginAtZero\": true, \"suggestedMax\": 8}, \"x\": {\"grid\": {\"display\": false}}}, \"plugins\": {\"legend\": {\"display\": false}, \"tooltip\": {\"callbacks\": {\"label\": \"function(context) { return context.parsed.y + ' hrs'; }\"}}}}" +
            "}";
    }

    private void generateLeaveStatusChart() {
        pendingLeaveCount = 0;
        approvedLeaveCount = 0;
        int declinedCount = 0;

        try {
            List<?> leaves = new ke.co.mspace.staffmanagement.dao.LeaveRequestDAO(DButil.getConnection())
                    .getLeaveRequestsByStaff(currentStaff.getStaffId());
            if (leaves != null) {
                for (Object obj : leaves) {
                    ke.co.mspace.staffmanagement.model.LeaveRequest lr = (ke.co.mspace.staffmanagement.model.LeaveRequest) obj;
                    if ("APPROVED".equalsIgnoreCase(lr.getStatus())) approvedLeaveCount++;
                    else if ("DECLINED".equalsIgnoreCase(lr.getStatus())) declinedCount++;
                    else pendingLeaveCount++;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        leaveStatusChartModel = "{" +
            "\"type\": \"doughnut\"," +
            "\"data\": {\"labels\": [\"Approved\", \"Pending\", \"Declined\"], \"datasets\": [{\"data\": [" + approvedLeaveCount + "," + pendingLeaveCount + "," + declinedCount + "], \"backgroundColor\": [\"#10b981\", \"#f59e0b\", \"#ef4444\"]}]}," +
            "\"options\": {\"maintainAspectRatio\": false, \"plugins\": {\"legend\": {\"position\": \"bottom\"}}}" +
            "}";
    }

    private void generateAssetRequestsChart() {
        int approvedCount = 0;
        int pendingCount = 0;
        int returnedCount = 0;
        int declinedCount = 0;

        try {
            java.util.List<ke.co.mspace.staffmanagement.model.AssetRequest> reqs = new ke.co.mspace.staffmanagement.dao.AssetRequestDAO(ke.co.mspace.staffmanagement.util.DButil.getConnection())
                    .getRequestsByStaffId(currentStaff.getStaffId());
            if (reqs != null) {
                for (ke.co.mspace.staffmanagement.model.AssetRequest req : reqs) {
                    if ("APPROVED".equalsIgnoreCase(req.getStatus())) approvedCount++;
                    else if ("RETURNED".equalsIgnoreCase(req.getStatus())) returnedCount++;
                    else if ("DECLINED".equalsIgnoreCase(req.getStatus())) declinedCount++;
                    else if ("PENDING".equalsIgnoreCase(req.getStatus())) pendingCount++;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        assetRequestsChartModel = "{" +
            "\"type\": \"doughnut\"," +
            "\"data\": {\"labels\": [\"Approved\", \"Pending\", \"Declined\", \"Returned\"], \"datasets\": [{\"data\": [" + approvedCount + "," + pendingCount + "," + declinedCount + "," + returnedCount + "], \"backgroundColor\": [\"#10b981\", \"#f59e0b\", \"#ef4444\", \"#8b5cf6\"]}]}," +
            "\"options\": {\"maintainAspectRatio\": false, \"plugins\": {\"legend\": {\"position\": \"bottom\"}}}" +
            "}";
    }
}
