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

import java.util.ArrayList;
import java.util.List;

import ke.co.mspace.staffmanagement.model.Notifications;

public class NotificationDAO {

    private Connection connection;

    public NotificationDAO(Connection connection){
        this.connection = connection;
    }

    //Create notification

    public int saveNotification(
            Notifications notification){

        int generatedId = 0;

        String sql =
        "INSERT INTO notifucations(title,message,created_by) VALUES(?,?,?)";

        try(
            PreparedStatement stmt =
                connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS)
        ){

            stmt.setString(
                    1,
                    notification.getTitle());

            stmt.setString(
                    2,
                    notification.getMessage());

            stmt.setInt(
                    3,
                    notification.getCreatedBy());

            stmt.executeUpdate();

            ResultSet rs =
                    stmt.getGeneratedKeys();

            if(rs.next()){

                generatedId =
                        rs.getInt(1);
            }

        }catch(SQLException e){

            e.printStackTrace();
        }

        return generatedId;
    }



    //Get notification

    public Notifications getNotificationById(
            int notificationId){

        Notifications notification =
                new Notifications();

        String sql =
        "SELECT * FROM notifucations WHERE notification_id=?";

        try(
            PreparedStatement stmt =
                connection.prepareStatement(sql)
        ){

            stmt.setInt(
                    1,
                    notificationId);

            ResultSet rs =
                    stmt.executeQuery();

            if(rs.next()){

                notification.setNotificationId(
                        rs.getInt(
                            "notification_id"));

                notification.setTitle(
                        rs.getString(
                            "title"));

                notification.setMessage(
                        rs.getString(
                            "message"));

                notification.setCreatedBy(
                        rs.getInt(
                            "created_by"));

                notification.setCreatedAt(
                        rs.getTimestamp(
                            "created_at"));
            }

        }catch(SQLException e){

            e.printStackTrace();
        }

        return notification;
    }



    //Get all notifications

    public List<Notifications>
        getAllNotifications(){

        List<Notifications> list =
                new ArrayList<>();

        String sql =
                "SELECT * FROM notifucations ORDER BY created_at DESC";

        try(
            Statement stmt =
                    connection.createStatement();

            ResultSet rs =
                    stmt.executeQuery(sql)
        ){

            while(rs.next()){

                Notifications n =
                        new Notifications();

                n.setNotificationId(
                        rs.getInt(
                            "notification_id"));

                n.setTitle(
                        rs.getString(
                            "title"));

                n.setMessage(
                        rs.getString(
                            "message"));

                n.setCreatedBy(
                        rs.getInt(
                            "created_by"));

                n.setCreatedAt(
                        rs.getTimestamp(
                            "created_at"));

                list.add(n);
            }

        }catch(SQLException e){

            e.printStackTrace();
        }

        return list;
    }

}