package ke.co.mspace.staffmanagement.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import ke.co.mspace.staffmanagement.dao.LeaveRequestDAO;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.model.LeaveRequest;
import ke.co.mspace.staffmanagement.model.Staff;
import ke.co.mspace.staffmanagement.util.DButil;
import ke.co.mspace.staffmanagement.util.EmailUtil;

@Named("leaveRequestBean")
@SessionScoped
public class LeaveRequestBean implements Serializable {
    private LeaveRequestDAO leaveRequestDAO;
    private StaffDAO staffDAO;
    private List<LeaveRequest> allLeaveRequests;
    private List<LeaveRequest> myLeaveRequests;
    private LeaveRequest newLeaveRequest;

    @Inject
    private LoginBean loginBean;

    public LeaveRequestBean() {
        try {
            Connection conn = DButil.getConnection();
            leaveRequestDAO = new LeaveRequestDAO(conn);
            staffDAO = new StaffDAO(conn);
            newLeaveRequest = new LeaveRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        loadData();
    }

    public void loadData() {
        if (loginBean != null && loginBean.getLoggedInUser() != null) {
            if ("Admin".equalsIgnoreCase(loginBean.getLoggedInUser().getRole())) {
                allLeaveRequests = leaveRequestDAO.getAllLeaveRequests();
            } else if ("Staff".equalsIgnoreCase(loginBean.getLoggedInUser().getRole())) {
                myLeaveRequests = leaveRequestDAO.getLeaveRequestsByStaff(loginBean.getLoggedInUser().getStaffId());
            }
        }
    }

    public void submitLeaveRequest() {
        if (loginBean.getLoggedInUser() != null) {
            // Check for existing active requests
            if (myLeaveRequests != null) {
                for (LeaveRequest lr : myLeaveRequests) {
                    if ("PENDING".equals(lr.getStatus()) || "APPROVED".equals(lr.getStatus())) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "You already have an active (" + lr.getStatus() + ") leave request."));
                        return; // Block submission
                    }
                }
            }

            newLeaveRequest.setStaffId(loginBean.getLoggedInUser().getStaffId());
            newLeaveRequest.setStatus("PENDING");
            int id = leaveRequestDAO.saveLeaveRequest(newLeaveRequest);
            if (id > 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Leave request submitted successfully."));
                newLeaveRequest = new LeaveRequest(); // reset
                loadData(); // refresh list
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to submit leave request."));
            }
        }
    }

    public void approveLeave(LeaveRequest request) {
        leaveRequestDAO.updateLeaveStatus(request.getLeaveId(), "APPROVED");
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Leave request approved."));
        // Notify the staff member
        Staff staff = staffDAO.getStaffById(request.getStaffId());
        if (staff != null && staff.getEmail() != null) {
            String subject = "Your Leave Request Has Been Approved";
            String message = "Hello " + staff.getFirstName() + ",\n\n"
                    + "Your leave request from " + request.getStartDate() + " to " + request.getEndDate()
                    + " has been APPROVED.\n\nHave a great time!\n\nHR Team";
            EmailUtil.sendReminderEmail(staff.getEmail(), subject, message);
        }
        loadData();
    }

    public void declineLeave(LeaveRequest request) {
        leaveRequestDAO.updateLeaveStatus(request.getLeaveId(), "DECLINED");
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Leave request declined."));
        // Notify the staff member
        Staff staff = staffDAO.getStaffById(request.getStaffId());
        if (staff != null && staff.getEmail() != null) {
            String subject = "Your Leave Request Has Been Declined";
            String message = "Hello " + staff.getFirstName() + ",\n\n"
                    + "Your leave request from " + request.getStartDate() + " to " + request.getEndDate()
                    + " has been DECLINED.\n\nPlease contact HR for more information.\n\nHR Team";
            EmailUtil.sendReminderEmail(staff.getEmail(), subject, message);
        }
        loadData();
    }

    public String getStaffName(int staffId) {
        Staff staff = staffDAO.getStaffById(staffId);
        if (staff != null) {
            return staff.getFirstName() + " " + staff.getLastName();
        }
        return "Unknown";
    }

    // Getters and Setters
    public List<LeaveRequest> getAllLeaveRequests() {
        return allLeaveRequests;
    }

    public void setAllLeaveRequests(List<LeaveRequest> allLeaveRequests) {
        this.allLeaveRequests = allLeaveRequests;
    }

    public List<LeaveRequest> getMyLeaveRequests() {
        return myLeaveRequests;
    }

    public void setMyLeaveRequests(List<LeaveRequest> myLeaveRequests) {
        this.myLeaveRequests = myLeaveRequests;
    }

    public LeaveRequest getNewLeaveRequest() {
        return newLeaveRequest;
    }

    public void setNewLeaveRequest(LeaveRequest newLeaveRequest) {
        this.newLeaveRequest = newLeaveRequest;
    }
}
