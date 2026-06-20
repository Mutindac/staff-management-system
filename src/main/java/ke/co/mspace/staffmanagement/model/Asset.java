package ke.co.mspace.staffmanagement.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Asset implements Serializable {
    private static final long serialVersionUID = 1L;

    private int assetId;
    private String name;
    private String description;
    private int totalQuantity;
    private int availableQuantity;
    private String image;
    private Timestamp createdAt;

    public Asset() {}

    public int getAssetId() { return assetId; }
    public void setAssetId(int assetId) { this.assetId = assetId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
