/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.model;

import java.sql.Timestamp;

/**
 *
 * @author server
 */
public class Notifications {
    private int staffNotificationId;  // the join table PK
    private boolean isRead;
    private java.sql.Timestamp createdAt;
    private int notificationId;
    private String title;
    private String message;
    private Integer createdBy;
    

    public Notifications() {
    }

    public Notifications(int staffNotificationId, boolean isRead, Timestamp createdAt, int notificationId, String title, String message, Integer createdBy) {
        this.staffNotificationId = staffNotificationId;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.notificationId = notificationId;
        this.title = title;
        this.message = message;
        this.createdBy = createdBy;
    }

    
    
    

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public int getStaffNotificationId() {
        return staffNotificationId;
    }

    public void setStaffNotificationId(int staffNotificationId) {
        this.staffNotificationId = staffNotificationId;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    
    
    
}
