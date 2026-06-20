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

    public Integer getDaysRemaining() {
        if (!"APPROVED".equalsIgnoreCase(status) || endDate == null) {
            return null;
        }
        long now = System.currentTimeMillis();
        // If the leave hasn't started yet, calculate from start to end
        // Or calculate from now to end? "days left before your leave expires"
        // implies calculating from today to the end date if currently on leave,
        // or from start to end if it hasn't started.
        // For simplicity, let's just do endDate - today if today is before endDate.
        if (now > endDate.getTime()) {
            return 0; // Expired
        }
        
        long diff;
        if (now < startDate.getTime()) {
            // Hasn't started yet, full duration
            diff = endDate.getTime() - startDate.getTime();
        } else {
            // Currently on leave
            diff = endDate.getTime() - now;
        }
        
        // Convert to days
        return (int) Math.ceil((double) diff / (1000 * 60 * 60 * 24));
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
