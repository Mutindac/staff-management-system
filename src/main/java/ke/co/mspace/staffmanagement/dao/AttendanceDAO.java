package ke.co.mspace.staffmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import ke.co.mspace.staffmanagement.model.Attendance;

public class AttendanceDAO {
    private Connection conn;

    public AttendanceDAO(Connection conn) {
        this.conn = conn;
    }

    public Attendance getTodayAttendance(int staffId, Date workDate) {
        String sql = "SELECT * FROM attendance WHERE staffId = ? AND workDate = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            stmt.setDate(2, workDate);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Attendance a = new Attendance();
                    a.setAttendanceId(rs.getInt("attendanceId"));
                    a.setStaffId(rs.getInt("staffId"));
                    a.setWorkDate(rs.getDate("workDate"));
                    a.setCheckInTime(rs.getTimestamp("checkInTime"));
                    a.setCheckOutTime(rs.getTimestamp("checkOutTime"));
                    a.setStatus(rs.getString("status"));
                    return a;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void checkIn(int staffId, Date workDate, Timestamp checkInTime) {
        String sql = "INSERT INTO attendance (staffId, workDate, checkInTime, status) VALUES (?, ?, ?, 'PRESENT')";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            stmt.setDate(2, workDate);
            stmt.setTimestamp(3, checkInTime);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkOut(int attendanceId, Timestamp checkOutTime) {
        String sql = "UPDATE attendance SET checkOutTime = ? WHERE attendanceId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, checkOutTime);
            stmt.setInt(2, attendanceId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public java.util.List<Attendance> getAllAttendanceLogs() {
        java.util.List<Attendance> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM attendance ORDER BY workDate DESC, checkInTime DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Attendance a = new Attendance();
                a.setAttendanceId(rs.getInt("attendanceId"));
                a.setStaffId(rs.getInt("staffId"));
                a.setWorkDate(rs.getDate("workDate"));
                a.setCheckInTime(rs.getTimestamp("checkInTime"));
                a.setCheckOutTime(rs.getTimestamp("checkOutTime"));
                a.setStatus(rs.getString("status"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public java.util.List<Attendance> getAttendanceLogsByDate(Date workDate) {
        java.util.List<Attendance> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE workDate = ? ORDER BY checkInTime DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, workDate);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Attendance a = new Attendance();
                    a.setAttendanceId(rs.getInt("attendanceId"));
                    a.setStaffId(rs.getInt("staffId"));
                    a.setWorkDate(rs.getDate("workDate"));
                    a.setCheckInTime(rs.getTimestamp("checkInTime"));
                    a.setCheckOutTime(rs.getTimestamp("checkOutTime"));
                    a.setStatus(rs.getString("status"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public java.util.List<Attendance> getAttendanceLogsByStaff(int staffId) {
        java.util.List<Attendance> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE staffId = ? ORDER BY workDate DESC, checkInTime DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Attendance a = new Attendance();
                    a.setAttendanceId(rs.getInt("attendanceId"));
                    a.setStaffId(rs.getInt("staffId"));
                    a.setWorkDate(rs.getDate("workDate"));
                    a.setCheckInTime(rs.getTimestamp("checkInTime"));
                    a.setCheckOutTime(rs.getTimestamp("checkOutTime"));
                    a.setStatus(rs.getString("status"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public java.util.List<Attendance> getAttendanceLogsByStaffAndDateRange(int staffId, java.util.Date startDate, java.util.Date endDate) {
        java.util.List<Attendance> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE staffId = ? AND workDate >= ? AND workDate <= ? ORDER BY workDate DESC, checkInTime DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            stmt.setDate(2, new java.sql.Date(startDate.getTime()));
            stmt.setDate(3, new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Attendance a = new Attendance();
                    a.setAttendanceId(rs.getInt("attendanceId"));
                    a.setStaffId(rs.getInt("staffId"));
                    a.setWorkDate(rs.getDate("workDate"));
                    a.setCheckInTime(rs.getTimestamp("checkInTime"));
                    a.setCheckOutTime(rs.getTimestamp("checkOutTime"));
                    a.setStatus(rs.getString("status"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteAttendanceByStaffId(int staffId) {
        String sql = "DELETE FROM attendance WHERE staffId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
