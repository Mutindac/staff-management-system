package ke.co.mspace.staffmanagement.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ke.co.mspace.staffmanagement.dao.AssetDAO;
import ke.co.mspace.staffmanagement.dao.AssetRequestDAO;
import ke.co.mspace.staffmanagement.model.Asset;
import ke.co.mspace.staffmanagement.model.AssetRequest;
import ke.co.mspace.staffmanagement.util.DButil;

@Named("assetStaffBean")
@SessionScoped
public class AssetStaffBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AssetStaffBean.class.getName());

    @Inject
    private StaffProfileBean staffProfileBean;

    private AssetDAO assetDAO;
    private AssetRequestDAO requestDAO;

    private String searchQuery = "";
    private List<Asset> searchResults;
    private List<AssetRequest> myRequests;
    
    private Asset selectedAsset;
    private int daysRequested = 1;

    @PostConstruct
    public void init() {
        try {
            assetDAO = new AssetDAO(DButil.getConnection());
            requestDAO = new AssetRequestDAO(DButil.getConnection());
            loadMyRequests();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize AssetStaffBean", e);
        }
    }

    public void loadMyRequests() {
        if (staffProfileBean != null && staffProfileBean.getCurrentStaff() != null) {
            try {
                myRequests = requestDAO.getRequestsByStaffId(staffProfileBean.getCurrentStaff().getStaffId());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to load staff requests", e);
            }
        }
    }

    public void searchAssets() {
        try {
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                searchResults = assetDAO.searchAvailableAssets(searchQuery);
            } else {
                // if empty, just list all available
                searchResults = assetDAO.searchAvailableAssets("");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to search available assets", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Search Error", "Could not perform search."));
        }
    }

    public void requestAsset() {
        if (selectedAsset == null) return;
        
        if (daysRequested < 1 || daysRequested > 7) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Duration", "You can only request an asset for 1 to 7 days."));
            return;
        }

        try {
            AssetRequest req = new AssetRequest();
            req.setStaffId(staffProfileBean.getCurrentStaff().getStaffId());
            req.setAssetId(selectedAsset.getAssetId());
            req.setDaysRequested(daysRequested);
            req.setStatus("PENDING");
            
            requestDAO.addAssetRequest(req);
            
            loadMyRequests();
            selectedAsset = null;
            daysRequested = 1;
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Asset request submitted for approval."));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to submit asset request", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to submit request."));
        }
    }

    // Getters and Setters
    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }
    public List<Asset> getSearchResults() { return searchResults; }
    public List<AssetRequest> getMyRequests() { return myRequests; }
    
    public Asset getSelectedAsset() { return selectedAsset; }
    public void setSelectedAsset(Asset selectedAsset) { this.selectedAsset = selectedAsset; }
    public int getDaysRequested() { return daysRequested; }
    public void setDaysRequested(int daysRequested) { this.daysRequested = daysRequested; }
}
