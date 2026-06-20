package ke.co.mspace.staffmanagement.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.co.mspace.staffmanagement.dao.NotificationDAO;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.model.Notifications;
import ke.co.mspace.staffmanagement.model.Staff;
import ke.co.mspace.staffmanagement.util.DButil;

@Named("notificationListBean")
@SessionScoped
public class NotificationListBean implements Serializable {

    private List<Notifications> allNotifications;
    private Map<Integer, Integer> recipientCountCache = new HashMap<>();
    private Map<Integer, String> senderNameCache = new HashMap<>();

    @PostConstruct
    public void init() {
        loadAll();
    }

    public void loadAll() {
        try (Connection conn = DButil.getConnection()) {
            NotificationDAO dao = new NotificationDAO(conn);
            allNotifications = dao.getAllNotifications();
            recipientCountCache.clear();
            senderNameCache.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns how many staff members received the given notification.
     */
    public int getRecipientCount(int notificationId) {
        if (recipientCountCache.containsKey(notificationId)) {
            return recipientCountCache.get(notificationId);
        }
        int count = 0;
        String sql = "SELECT COUNT(*) FROM staff_notifications WHERE notification_id = ?";
        try (Connection conn = DButil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        recipientCountCache.put(notificationId, count);
        return count;
    }

    /**
     * Returns the full name of the staff member who created the notification.
     */
    public String getSenderName(Integer staffId) {
        if (staffId == null || staffId == 0) return "System";
        if (senderNameCache.containsKey(staffId)) {
            return senderNameCache.get(staffId);
        }
        try (Connection conn = DButil.getConnection()) {
            StaffDAO dao = new StaffDAO(conn);
            Staff s = dao.getStaffById(staffId);
            String name = (s != null) ? s.getFirstName() + " " + s.getLastName() : "Unknown";
            senderNameCache.put(staffId, name);
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    public List<Notifications> getAllNotifications() {
        return allNotifications;
    }
}
