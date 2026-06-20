package ke.co.mspace.staffmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import ke.co.mspace.staffmanagement.model.Asset;

public class AssetDAO {
    private Connection connection;

    public AssetDAO(Connection connection) {
        this.connection = connection;
    }

    public void addAsset(Asset asset) throws SQLException {
        String sql = "INSERT INTO assets (name, description, total_quantity, available_quantity, image) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, asset.getName());
            stmt.setString(2, asset.getDescription());
            stmt.setInt(3, asset.getTotalQuantity());
            stmt.setInt(4, asset.getAvailableQuantity());
            stmt.setString(5, asset.getImage());
            stmt.executeUpdate();
        }
    }

    public void updateAsset(Asset asset) throws SQLException {
        String sql = "UPDATE assets SET name=?, description=?, total_quantity=?, available_quantity=?, image=? WHERE asset_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, asset.getName());
            stmt.setString(2, asset.getDescription());
            stmt.setInt(3, asset.getTotalQuantity());
            stmt.setInt(4, asset.getAvailableQuantity());
            stmt.setString(5, asset.getImage());
            stmt.setInt(6, asset.getAssetId());
            stmt.executeUpdate();
        }
    }

    public void deleteAsset(int assetId) throws SQLException {
        String sql = "DELETE FROM assets WHERE asset_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, assetId);
            stmt.executeUpdate();
        }
    }

    public Asset getAssetById(int assetId) throws SQLException {
        String sql = "SELECT * FROM assets WHERE asset_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, assetId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToAsset(rs);
                }
            }
        }
        return null;
    }

    public List<Asset> getAllAssets() throws SQLException {
        List<Asset> assets = new ArrayList<>();
        String sql = "SELECT * FROM assets ORDER BY name ASC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                assets.add(mapRowToAsset(rs));
            }
        }
        return assets;
    }

    public List<Asset> searchAvailableAssets(String keyword) throws SQLException {
        List<Asset> assets = new ArrayList<>();
        String sql = "SELECT * FROM assets WHERE available_quantity > 0 AND (name LIKE ? OR description LIKE ?) ORDER BY name ASC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            stmt.setString(1, kw);
            stmt.setString(2, kw);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    assets.add(mapRowToAsset(rs));
                }
            }
        }
        return assets;
    }

    private Asset mapRowToAsset(ResultSet rs) throws SQLException {
        Asset asset = new Asset();
        asset.setAssetId(rs.getInt("asset_id"));
        asset.setName(rs.getString("name"));
        asset.setDescription(rs.getString("description"));
        asset.setTotalQuantity(rs.getInt("total_quantity"));
        asset.setAvailableQuantity(rs.getInt("available_quantity"));
        asset.setImage(rs.getString("image"));
        asset.setCreatedAt(rs.getTimestamp("created_at"));
        return asset;
    }
}
