package ke.co.mspace.staffmanagement.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import ke.co.mspace.staffmanagement.dao.AttendanceDAO;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.model.Attendance;
import ke.co.mspace.staffmanagement.model.Staff;
import ke.co.mspace.staffmanagement.util.DButil;

@Named("attendanceBean")
@ViewScoped
public class AttendanceBean implements Serializable {
    private List<Attendance> attendanceLogs;
    private java.util.Map<Integer, String> staffNameCache;
    private java.util.Date selectedDate;

    private AttendanceDAO getAttendanceDAO() throws Exception {
        return new AttendanceDAO(DButil.getConnection());
    }

    private StaffDAO getStaffDAO() throws Exception {
        return new StaffDAO(DButil.getConnection());
    }

    @PostConstruct
    public void init() {
        staffNameCache = new java.util.HashMap<>();
        selectedDate = new java.util.Date();
        loadAttendanceLogsForDate();
    }

    public void loadAttendanceLogsForDate() {
        attendanceLogs = new java.util.ArrayList<>();
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
        try {
            StaffDAO staffDAO = getStaffDAO();
            AttendanceDAO attendanceDAO = getAttendanceDAO();
            List<Staff> allStaff = staffDAO.getAllStaff();
            List<Attendance> logsForDate = attendanceDAO.getAttendanceLogsByDate(sqlDate);

            java.util.Map<Integer, Attendance> attendanceMap = new java.util.HashMap<>();
            for (Attendance a : logsForDate) {
                attendanceMap.put(a.getStaffId(), a);
            }

            for (Staff staff : allStaff) {
                Attendance a = attendanceMap.get(staff.getStaffId());
                if (a != null) {
                    attendanceLogs.add(a);
                } else {
                    Attendance dummy = new Attendance();
                    dummy.setStaffId(staff.getStaffId());
                    dummy.setWorkDate(sqlDate);
                    dummy.setStatus("ABSENT");
                    attendanceLogs.add(dummy);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        try {
            Staff s = getStaffDAO().getStaffById(staffId);
            String name = (s != null) ? s.getFirstName() + " " + s.getLastName() : "Unknown";
            staffNameCache.put(staffId, name);
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    // ---------- Attendance Edit Support ----------

    private Attendance selectedAttendance;
    private java.util.Date editCheckIn;
    private java.util.Date editCheckOut;
    private String editStatus;

    public void prepareEdit(Attendance a) {
        this.selectedAttendance = a;
        this.editCheckIn = a.getCheckInTime();
        this.editCheckOut = a.getCheckOutTime();
        this.editStatus = a.getStatus();
    }

    public void saveAttendanceEdit() {
        if (selectedAttendance == null || selectedAttendance.getAttendanceId() == 0) {
            jakarta.faces.context.FacesContext.getCurrentInstance().addMessage(null,
                new jakarta.faces.application.FacesMessage(jakarta.faces.application.FacesMessage.SEVERITY_WARN,
                    "Warning", "No attendance record selected."));
            return;
        }
        try {
            java.sql.Timestamp checkIn = editCheckIn != null ? new java.sql.Timestamp(editCheckIn.getTime()) : null;
            java.sql.Timestamp checkOut = editCheckOut != null ? new java.sql.Timestamp(editCheckOut.getTime()) : null;
            getAttendanceDAO().updateAttendance(selectedAttendance.getAttendanceId(), checkIn, checkOut, editStatus);
            loadAttendanceLogsForDate();
            jakarta.faces.context.FacesContext.getCurrentInstance().addMessage(null,
                new jakarta.faces.application.FacesMessage(jakarta.faces.application.FacesMessage.SEVERITY_INFO,
                    "Success", "Attendance record updated."));
        } catch (Exception e) {
            e.printStackTrace();
            jakarta.faces.context.FacesContext.getCurrentInstance().addMessage(null,
                new jakarta.faces.application.FacesMessage(jakarta.faces.application.FacesMessage.SEVERITY_ERROR,
                    "Error", "Failed to update attendance record."));
        }
    }

    public Attendance getSelectedAttendance() { return selectedAttendance; }
    public void setSelectedAttendance(Attendance selectedAttendance) { this.selectedAttendance = selectedAttendance; }
    public java.util.Date getEditCheckIn() { return editCheckIn; }
    public void setEditCheckIn(java.util.Date editCheckIn) { this.editCheckIn = editCheckIn; }
    public java.util.Date getEditCheckOut() { return editCheckOut; }
    public void setEditCheckOut(java.util.Date editCheckOut) { this.editCheckOut = editCheckOut; }
    public String getEditStatus() { return editStatus; }
    public void setEditStatus(String editStatus) { this.editStatus = editStatus; }
}
