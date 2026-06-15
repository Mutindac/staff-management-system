package ke.co.mspace.staffmanagement.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import ke.co.mspace.staffmanagement.dao.AttendanceDAO;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.model.Attendance;
import ke.co.mspace.staffmanagement.model.Staff;
import ke.co.mspace.staffmanagement.util.DButil;

@Named("attendanceBean")
@ViewScoped
public class AttendanceBean implements Serializable {
    private AttendanceDAO attendanceDAO;
    private StaffDAO staffDAO;
    private List<Attendance> attendanceLogs;
    private java.util.Map<Integer, String> staffNameCache;
    private java.util.Date selectedDate;

    public AttendanceBean() {
        try {
            Connection conn = DButil.getConnection();
            attendanceDAO = new AttendanceDAO(conn);
            staffDAO = new StaffDAO(conn);
            staffNameCache = new java.util.HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        selectedDate = new java.util.Date(); // Default to today
        loadAttendanceLogsForDate();
    }

    public void loadAttendanceLogsForDate() {
        attendanceLogs = new java.util.ArrayList<>();
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
        
        List<Staff> allStaff = staffDAO.getAllStaff();
        List<Attendance> logsForDate = attendanceDAO.getAttendanceLogsByDate(sqlDate);
        
        // Create a map for quick lookup
        java.util.Map<Integer, Attendance> attendanceMap = new java.util.HashMap<>();
        for (Attendance a : logsForDate) {
            attendanceMap.put(a.getStaffId(), a);
        }
        
        for (Staff staff : allStaff) {
            Attendance a = attendanceMap.get(staff.getStaffId());
            if (a != null) {
                attendanceLogs.add(a);
            } else {
                // Create dummy attendance for ABSENT
                Attendance dummy = new Attendance();
                dummy.setStaffId(staff.getStaffId());
                dummy.setWorkDate(sqlDate);
                dummy.setStatus("ABSENT");
                attendanceLogs.add(dummy);
            }
        }
    }

    public List<Attendance> getAttendanceLogs() {
        return attendanceLogs;
    }

    public java.util.Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(java.util.Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void testMorningReminders() {
        ke.co.mspace.staffmanagement.util.AttendanceScheduler.checkMissingCheckIns();
        jakarta.faces.context.FacesContext.getCurrentInstance().addMessage(null, 
            new jakarta.faces.application.FacesMessage("Morning Reminders Sent"));
    }

    public void testEveningReminders() {
        ke.co.mspace.staffmanagement.util.AttendanceScheduler.checkMissingCheckOuts();
        jakarta.faces.context.FacesContext.getCurrentInstance().addMessage(null, 
            new jakarta.faces.application.FacesMessage("Evening Reminders Sent"));
    }
    public String getStaffName(int staffId) {
        if (staffNameCache.containsKey(staffId)) {
            return staffNameCache.get(staffId);
        }
        Staff s = staffDAO.getStaffById(staffId);
        String name = (s != null) ? s.getFirstName() + " " + s.getLastName() : "Unknown";
        staffNameCache.put(staffId, name);
        return name;
    }
}
