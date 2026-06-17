package ke.co.mspace.staffmanagement.model;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

public class LeaveRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private int leaveId;
    private int staffId;
    private Date startDate;
    private Date endDate;
    private String reason;
    private String status;
    private Timestamp requestDate;

    // Default constructor
    public LeaveRequest() {
    }

    // Parameterized constructor
    public LeaveRequest(int leaveId, int staffId, Date startDate, Date endDate, String reason, String status, Timestamp requestDate) {
        this.leaveId = leaveId;
        this.staffId = staffId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.requestDate = requestDate;
    }

    // Getters and Setters
    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "leaveId=" + leaveId +
                ", staffId=" + staffId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", requestDate=" + requestDate +
                '}';
    }
}
