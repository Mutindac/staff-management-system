/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

public class StaffNotificationDAO {

    private Connection connection;

    public StaffNotificationDAO(
            Connection connection){

        this.connection =
                connection;
    }


    //Send to one employee

    public void sendToStaff(
            int staffId,
            int notificationId){

        String sql =
        "INSERT INTO staff_notifications(staff_id,notification_id) VALUES(?,?)";

        try(
            PreparedStatement stmt =
                connection.prepareStatement(sql)
        ){

            stmt.setInt(
                    1,
                    staffId);

            stmt.setInt(
                    2,
                    notificationId);

            stmt.executeUpdate();

        }catch(SQLException e){

            e.printStackTrace();
        }

    }



    //Send to all employees

    public void sendToAll(
            List<Integer> staffIds,
            int notificationId){

        for(Integer id : staffIds){

            sendToStaff(
                    id,
                    notificationId);
        }

    }



    //Get notifications for staff

    public List<Integer>
        getNotificationIds(
                int staffId){

        List<Integer> ids =
                new ArrayList<>();

        String sql =
        "SELECT notification_id FROM staff_notifications WHERE staff_id=?";

        try(
            PreparedStatement stmt =
                connection.prepareStatement(sql)
        ){

            stmt.setInt(
                    1,
                    staffId);

            ResultSet rs =
                    stmt.executeQuery();

            while(rs.next()){

                ids.add(
                    rs.getInt(
                        "notification_id"));
            }

        }catch(SQLException e){

            e.printStackTrace();
        }

        return ids;
    }


    // Get full notifications for a staff member
public List<ke.co.mspace.staffmanagement.model.Notifications> getNotificationsForStaff(int staffId) {
    List<ke.co.mspace.staffmanagement.model.Notifications> list = new ArrayList<>();
    String sql = "SELECT n.notification_id, n.title, n.message, n.created_by, n.created_at, " +
                 "sn.is_read, sn.staff_notification_id " +
                 "FROM notifucations n " +
                 "JOIN staff_notifications sn ON n.notification_id = sn.notification_id " +
                 "WHERE sn.staff_id = ? " +
                 "ORDER BY n.created_at DESC";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, staffId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            ke.co.mspace.staffmanagement.model.Notifications notif = 
                new ke.co.mspace.staffmanagement.model.Notifications();
            notif.setNotificationId(rs.getInt("notification_id"));
            notif.setTitle(rs.getString("title"));
            notif.setMessage(rs.getString("message"));
            notif.setCreatedBy(rs.getInt("created_by"));
            notif.setCreatedAt(rs.getTimestamp("created_at"));
            notif.setIsRead(rs.getBoolean("is_read"));
            notif.setStaffNotificationId(rs.getInt("staff_notification_id"));
            list.add(notif);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

// Mark all as read for a staff member
public void markAllReadForStaff(int staffId) {
    String sql = "UPDATE staff_notifications SET is_read = 1 WHERE staff_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, staffId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    //Mark read

    public void markAsRead(
            int staffNotificationId){

        String sql =
        "UPDATE staff_notifications SET is_read=1 WHERE staff_notification_id=?";

        try(
            PreparedStatement stmt =
                connection.prepareStatement(sql)
        ){

            stmt.setInt(
                    1,
                    staffNotificationId);

            stmt.executeUpdate();

        }catch(SQLException e){

            e.printStackTrace();
        }

    }

    public void deleteNotification(int staffNotificationId) {
        String sql = "DELETE FROM staff_notifications WHERE staff_notification_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, staffNotificationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}