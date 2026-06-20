/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.model;

/**
 *
 * @author server
 */
public class StaffNotification {
    private int staffNotificationId;
    private int staffId;
    private int notificationId;
    private Boolean isRead;
    private String recievedAt;

    public StaffNotification() {
    }

    public StaffNotification(int staffNotificationId, int staffId, int notificationId, Boolean isRead, String recievedAt) {
        this.staffNotificationId = staffNotificationId;
        this.staffId = staffId;
        this.notificationId = notificationId;
        this.isRead = isRead;
        this.recievedAt = recievedAt;
    }

    public int getStaffNotificationId() {
        return staffNotificationId;
    }

    public void setStaffNotificationId(int staffNotificationId) {
        this.staffNotificationId = staffNotificationId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getRecievedAt() {
        return recievedAt;
    }

    public void setRecievedAt(String recievedAt) {
        this.recievedAt = recievedAt;
    }

    
    
}
