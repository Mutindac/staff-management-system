package ke.co.mspace.staffmanagement.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import ke.co.mspace.staffmanagement.dao.AssetDAO;
import ke.co.mspace.staffmanagement.dao.AssetRequestDAO;
import ke.co.mspace.staffmanagement.model.Asset;
import ke.co.mspace.staffmanagement.model.AssetRequest;
import ke.co.mspace.staffmanagement.util.DButil;

@Named("assetAdminBean")
@SessionScoped
public class AssetAdminBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AssetAdminBean.class.getName());

    private AssetDAO assetDAO;
    private AssetRequestDAO requestDAO;

    private List<Asset> allAssets;
    private Asset currentAsset;
    private Asset newAsset;

    private List<AssetRequest> allRequests;
    private AssetRequest selectedRequest;
    private String conditionNotes;

    @PostConstruct
    public void init() {
        try {
            assetDAO = new AssetDAO(DButil.getConnection());
            requestDAO = new AssetRequestDAO(DButil.getConnection());
            newAsset = new Asset();
            loadData();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize AssetAdminBean", e);
        }
    }

    public void loadData() {
        try {
            allAssets = assetDAO.getAllAssets();
            allRequests = requestDAO.getAllRequests();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load asset data", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load asset data. Please try again or contact IT."));
        }
    }

    // Asset Image Uploads
    public void handleNewAssetImageUpload(FileUploadEvent event) {
        try {
            byte[] bytes = event.getFile().getContent();
            String base64Image = java.util.Base64.getEncoder().encodeToString(bytes);
            newAsset.setImage(base64Image);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Image uploaded successfully."));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to process image upload", e);
        }
    }

    public void handleEditAssetImageUpload(FileUploadEvent event) {
        try {
            byte[] bytes = event.getFile().getContent();
            String base64Image = java.util.Base64.getEncoder().encodeToString(bytes);
            if (currentAsset != null) {
                currentAsset.setImage(base64Image);
            }
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Image updated successfully."));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to process image upload", e);
        }
    }

    // Asset CRUD
    public void saveNewAsset() {
        try {
            newAsset.setAvailableQuantity(newAsset.getTotalQuantity()); // Initially all are available
            assetDAO.addAsset(newAsset);
            newAsset = new Asset(); // Reset
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Asset added successfully."));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to add new asset", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add asset."));
        }
    }

    public void updateAsset() {
        try {
            assetDAO.updateAsset(currentAsset);
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Asset updated successfully."));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update asset", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update asset."));
        }
    }

    public void deleteAsset(Asset asset) {
        try {
            assetDAO.deleteAsset(asset.getAssetId());
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Asset deleted successfully."));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete asset", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete asset. Ensure no active requests."));
        }
    }

    // Request Management
    public void approveRequest(AssetRequest req) {
        try {
            // Check availability again just in case
            Asset asset = assetDAO.getAssetById(req.getAssetId());
            if (asset != null && asset.getAvailableQuantity() > 0) {
                // Update availability
                asset.setAvailableQuantity(asset.getAvailableQuantity() - 1);
                assetDAO.updateAsset(asset);

                // Set dates
                long now = System.currentTimeMillis();
                Timestamp startDate = new Timestamp(now);
                // end date = now + (days_requested * 24hrs)
                Timestamp endDate = new Timestamp(now + ((long) req.getDaysRequested() * 24 * 60 * 60 * 1000));
                
                requestDAO.updateAssetRequestStatus(req.getRequestId(), "APPROVED", startDate, endDate);
                loadData();
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Request approved."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Asset is out of stock. Cannot approve."));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to approve asset request", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to approve request. Please try again."));
        }
    }

    public void declineRequest(AssetRequest req) {
        try {
            requestDAO.updateAssetRequestStatus(req.getRequestId(), "DECLINED", null, null);
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Request declined."));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to decline asset request", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to decline request."));
        }
    }

    public void processReturn() {
        if (selectedRequest == null) return;
        try {
            requestDAO.processReturn(selectedRequest.getRequestId(), conditionNotes);
            
            // Restore inventory
            Asset asset = assetDAO.getAssetById(selectedRequest.getAssetId());
            if (asset != null) {
                asset.setAvailableQuantity(asset.getAvailableQuantity() + 1);
                assetDAO.updateAsset(asset);
            }
            
            conditionNotes = null;
            selectedRequest = null;
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Asset marked as returned."));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to process asset return", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to process return."));
        }
    }

    // Getters and Setters
    public List<Asset> getAllAssets() { return allAssets; }
    public Asset getCurrentAsset() { return currentAsset; }
    public void setCurrentAsset(Asset currentAsset) { this.currentAsset = currentAsset; }
    public Asset getNewAsset() { return newAsset; }
    public void setNewAsset(Asset newAsset) { this.newAsset = newAsset; }
    
    public List<AssetRequest> getAllRequests() { return allRequests; }
    public AssetRequest getSelectedRequest() { return selectedRequest; }
    public void setSelectedRequest(AssetRequest selectedRequest) { this.selectedRequest = selectedRequest; }
    public String getConditionNotes() { return conditionNotes; }
    public void setConditionNotes(String conditionNotes) { this.conditionNotes = conditionNotes; }
}
