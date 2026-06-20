/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.staffmanagement.controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.List;

import ke.co.mspace.staffmanagement.dao.NotificationDAO;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.dao.StaffNotificationDAO;

import ke.co.mspace.staffmanagement.model.Notifications;
import ke.co.mspace.staffmanagement.model.Staff;
import ke.co.mspace.staffmanagement.model.UserAccount;
import ke.co.mspace.staffmanagement.util.DButil;

@Named
@ViewScoped
public class NotificationController
implements Serializable {

    private Notifications notification =
            new Notifications();

    private boolean sendToAll;

    private List<Integer> selectedStaff =
            new ArrayList<>();

    private List<Staff> allStaff =
            new ArrayList<>();



    public NotificationController(){
        try (Connection connection = DButil.getConnection()){
            StaffDAO dao = new StaffDAO(connection);
            allStaff = dao.getAllStaff();
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    public void sendNotification() {
    System.out.println("send clicked");
    try {
        FacesContext ctx = FacesContext.getCurrentInstance();
        UserAccount loggedInUser = (UserAccount) ctx.getExternalContext()
                .getSessionMap()
                .get("loggedInUser");

        if (loggedInUser == null) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "You must be logged in to send notifications."));
            return;
        }

        notification.setCreatedBy(loggedInUser.getStaffId());

        try (Connection connection = DButil.getConnection()) {
            NotificationDAO notificationDAO = new NotificationDAO(connection);
            StaffNotificationDAO staffDAO = new StaffNotificationDAO(connection);

            int notificationId = notificationDAO.saveNotification(notification);

            if (notificationId == 0) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "Failed to save notification."));
                return;
            }

            int recipientCount = 0;
            if (sendToAll) {
                for (Staff staff : allStaff) {
                    staffDAO.sendToStaff(staff.getStaffId(), notificationId);
                    recipientCount++;
                }
            } else {
                for (Integer id : selectedStaff) {
                    staffDAO.sendToStaff(id, notificationId);
                    recipientCount++;
                }
            }

            notification = new Notifications();
            selectedStaff.clear();
            sendToAll = false;

            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Success", "Notification sent to " + recipientCount + " recipient(s)."));
        }

    } catch (Exception e) {
        e.printStackTrace();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "An unexpected error occurred while sending the notification."));
    }
}



    public Notifications
        getNotification(){

        return notification;
    }

    public void setNotification(
            Notifications notification){

        this.notification =
                notification;
    }



    public boolean isSendToAll(){

        return sendToAll;
    }

    public void setSendToAll(
            boolean sendToAll){

        this.sendToAll =
                sendToAll;
    }



    public List<Integer>
        getSelectedStaff(){

        return selectedStaff;
    }

    public void setSelectedStaff(
            List<Integer> selectedStaff){

        this.selectedStaff =
                selectedStaff;
    }



    public List<Staff>
        getAllStaff(){

        return allStaff;
    }

}