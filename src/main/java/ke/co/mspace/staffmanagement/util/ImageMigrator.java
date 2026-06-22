package ke.co.mspace.staffmanagement.util;

import java.sql.*;
import java.util.Base64;
import java.nio.file.*;
import java.io.File;

public class ImageMigrator {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/staffdb";
        String user = "staff";
        String password = "password";
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Migrate assets
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT asset_id, image FROM assets WHERE image IS NOT NULL AND image NOT LIKE '/assets/%'");
            while(rs.next()) {
                int id = rs.getInt("asset_id");
                String img = rs.getString("image");
                if (img.length() > 100) {
                    byte[] bytes = Base64.getDecoder().decode(img);
                    String filename = System.currentTimeMillis() + "_" + id + ".jpg";
                    Path path = Paths.get("/home/server/uploads/staff-management-system/assets", filename);
                    Files.write(path, bytes);
                    
                    PreparedStatement update = conn.prepareStatement("UPDATE assets SET image = ? WHERE asset_id = ?");
                    update.setString(1, "/assets/" + filename);
                    update.setInt(2, id);
                    update.executeUpdate();
                    System.out.println("Migrated asset " + id);
                }
            }
            
            // Migrate profiles
            rs = stmt.executeQuery("SELECT account_id, profile_image FROM user_account WHERE profile_image IS NOT NULL AND profile_image NOT LIKE '/profiles/%'");
            while(rs.next()) {
                int id = rs.getInt("account_id");
                String img = rs.getString("profile_image");
                if (img.length() > 100) {
                    byte[] bytes = Base64.getDecoder().decode(img);
                    String filename = System.currentTimeMillis() + "_" + id + ".jpg";
                    Path path = Paths.get("/home/server/uploads/staff-management-system/profiles", filename);
                    Files.write(path, bytes);
                    
                    PreparedStatement update = conn.prepareStatement("UPDATE user_account SET profile_image = ? WHERE account_id = ?");
                    update.setString(1, "/profiles/" + filename);
                    update.setInt(2, id);
                    update.executeUpdate();
                    System.out.println("Migrated profile " + id);
                }
            }
            System.out.println("Migration completed successfully.");
        }
    }
}
