package ke.co.mspace.staffmanagement.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class AssetRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private int requestId;
    private int staffId;
    private int assetId;
    private Timestamp requestDate;
    private int daysRequested;
    private Timestamp startDate;
    private Timestamp endDate;
    private String status;
    private String conditionNotes;
    
    // Virtual fields for display
    private String assetName;
    private String staffName;

    public AssetRequest() {}

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public int getAssetId() { return assetId; }
    public void setAssetId(int assetId) { this.assetId = assetId; }

    public Timestamp getRequestDate() { return requestDate; }
    public void setRequestDate(Timestamp requestDate) { this.requestDate = requestDate; }

    public int getDaysRequested() { return daysRequested; }
    public void setDaysRequested(int daysRequested) { this.daysRequested = daysRequested; }

    public Timestamp getStartDate() { return startDate; }
    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }

    public Timestamp getEndDate() { return endDate; }
    public void setEndDate(Timestamp endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getConditionNotes() { return conditionNotes; }
    public void setConditionNotes(String conditionNotes) { this.conditionNotes = conditionNotes; }

    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }

    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    public Integer getDaysRemaining() {
        if (!"APPROVED".equalsIgnoreCase(status) || endDate == null) {
            return null;
        }
        long now = System.currentTimeMillis();
        if (now > endDate.getTime()) {
            return -1; // Overdue
        }
        
        long diff;
        if (startDate != null && now < startDate.getTime()) {
            diff = endDate.getTime() - startDate.getTime();
        } else {
            diff = endDate.getTime() - now;
        }
        
        return (int) Math.ceil((double) diff / (1000 * 60 * 60 * 24));
    }
}
