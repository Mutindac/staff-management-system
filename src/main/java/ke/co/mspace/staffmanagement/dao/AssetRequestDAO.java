package ke.co.mspace.staffmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ke.co.mspace.staffmanagement.model.AssetRequest;

public class AssetRequestDAO {
    private Connection connection;

    public AssetRequestDAO(Connection connection) {
        this.connection = connection;
    }

    public void addAssetRequest(AssetRequest req) throws SQLException {
        String sql = "INSERT INTO asset_requests (staff_id, asset_id, days_requested, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, req.getStaffId());
            stmt.setInt(2, req.getAssetId());
            stmt.setInt(3, req.getDaysRequested());
            stmt.setString(4, req.getStatus());
            stmt.executeUpdate();
        }
    }

    public void updateAssetRequestStatus(int requestId, String status, Timestamp startDate, Timestamp endDate) throws SQLException {
        String sql = "UPDATE asset_requests SET status=?, start_date=?, end_date=? WHERE request_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setTimestamp(2, startDate);
            stmt.setTimestamp(3, endDate);
            stmt.setInt(4, requestId);
            stmt.executeUpdate();
        }
    }

    public void processReturn(int requestId, String conditionNotes) throws SQLException {
        String sql = "UPDATE asset_requests SET status='RETURNED', condition_notes=? WHERE request_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, conditionNotes);
            stmt.setInt(2, requestId);
            stmt.executeUpdate();
        }
    }

    public List<AssetRequest> getRequestsByStaffId(int staffId) throws SQLException {
        List<AssetRequest> requests = new ArrayList<>();
        String sql = "SELECT r.*, a.name as asset_name FROM asset_requests r JOIN assets a ON r.asset_id = a.asset_id WHERE r.staff_id=? ORDER BY r.request_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRowToRequest(rs, true, false));
                }
            }
        }
        return requests;
    }

    public List<AssetRequest> getAllRequests() throws SQLException {
        List<AssetRequest> requests = new ArrayList<>();
        String sql = "SELECT r.*, a.name as asset_name, s.firstName, s.lastName FROM asset_requests r " +
                     "JOIN assets a ON r.asset_id = a.asset_id " +
                     "JOIN staff s ON r.staff_id = s.staffId " +
                     "ORDER BY r.request_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                requests.add(mapRowToRequest(rs, true, true));
            }
        }
        return requests;
    }

    public List<AssetRequest> getRequestsByStatus(String status) throws SQLException {
        List<AssetRequest> requests = new ArrayList<>();
        String sql = "SELECT r.*, a.name as asset_name, s.firstName, s.lastName FROM asset_requests r " +
                     "JOIN assets a ON r.asset_id = a.asset_id " +
                     "JOIN staff s ON r.staff_id = s.staffId " +
                     "WHERE r.status = ? ORDER BY r.request_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRowToRequest(rs, true, true));
                }
            }
        }
        return requests;
    }

    private AssetRequest mapRowToRequest(ResultSet rs, boolean includeAssetName, boolean includeStaffName) throws SQLException {
        AssetRequest req = new AssetRequest();
        req.setRequestId(rs.getInt("request_id"));
        req.setStaffId(rs.getInt("staff_id"));
        req.setAssetId(rs.getInt("asset_id"));
        req.setRequestDate(rs.getTimestamp("request_date"));
        req.setDaysRequested(rs.getInt("days_requested"));
        req.setStartDate(rs.getTimestamp("start_date"));
        req.setEndDate(rs.getTimestamp("end_date"));
        req.setStatus(rs.getString("status"));
        req.setConditionNotes(rs.getString("condition_notes"));
        
        if (includeAssetName) {
            req.setAssetName(rs.getString("asset_name"));
        }
        if (includeStaffName) {
            req.setStaffName(rs.getString("firstName") + " " + rs.getString("lastName"));
        }
        return req;
    }
}
