package ke.co.mspace.staffmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import ke.co.mspace.staffmanagement.model.LeaveRequest;

public class LeaveRequestDAO {
    private Connection connection;

    public LeaveRequestDAO(Connection connection) {
        this.connection = connection;
    }

    public int saveLeaveRequest(LeaveRequest leaveRequest) {
        int generatedId = 0;
        String sql = "INSERT INTO leaverequest(staffId, startDate, endDate, reason, status) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, leaveRequest.getStaffId());
            stmt.setDate(2, new java.sql.Date(leaveRequest.getStartDate().getTime()));
            stmt.setDate(3, new java.sql.Date(leaveRequest.getEndDate().getTime()));
            stmt.setString(4, leaveRequest.getReason());
            stmt.setString(5, leaveRequest.getStatus() != null ? leaveRequest.getStatus() : "PENDING");
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public LeaveRequest getLeaveRequestById(int leaveId) {
        LeaveRequest leaveRequest = null;
        String sql = "SELECT * FROM leaverequest WHERE leaveId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leaveId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    leaveRequest = mapResultSetToLeaveRequest(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaveRequest;
    }

    public void updateLeaveStatus(int leaveId, String status) {
        String sql = "UPDATE leaverequest SET status = ? WHERE leaveId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, leaveId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LeaveRequest> getLeaveRequestsByStaff(int staffId) {
        List<LeaveRequest> leaveList = new ArrayList<>();
        String sql = "SELECT * FROM leaverequest WHERE staffId = ? ORDER BY requestDate DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    leaveList.add(mapResultSetToLeaveRequest(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaveList;
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> leaveList = new ArrayList<>();
        String sql = "SELECT * FROM leaverequest ORDER BY requestDate DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                leaveList.add(mapResultSetToLeaveRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaveList;
    }

    private LeaveRequest mapResultSetToLeaveRequest(ResultSet rs) throws SQLException {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setLeaveId(rs.getInt("leaveId"));
        leaveRequest.setStaffId(rs.getInt("staffId"));
        leaveRequest.setStartDate(rs.getDate("startDate"));
        leaveRequest.setEndDate(rs.getDate("endDate"));
        leaveRequest.setReason(rs.getString("reason"));
        leaveRequest.setStatus(rs.getString("status"));
        leaveRequest.setRequestDate(rs.getTimestamp("requestDate"));
        return leaveRequest;
    }
}
