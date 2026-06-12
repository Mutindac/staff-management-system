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
        attendanceLogs = attendanceDAO.getAllAttendanceLogs();
    }

    public List<Attendance> getAttendanceLogs() {
        return attendanceLogs;
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
