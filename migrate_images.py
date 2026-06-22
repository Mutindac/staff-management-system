import mysql.connector
import base64
import time
import os

try:
    conn = mysql.connector.connect(
        host="localhost",
        user="staff",
        password="password",
        database="staffdb"
    )
    cursor = conn.cursor()
    
    # Migrate assets
    cursor.execute("SELECT asset_id, image FROM assets WHERE image IS NOT NULL AND image NOT LIKE '/assets/%'")
    assets = cursor.fetchall()
    for asset_id, image_data in assets:
        if len(image_data) > 100: # It's base64
            filename = f"{int(time.time()*1000)}_{asset_id}.jpg"
            filepath = f"/home/server/uploads/staff-management-system/assets/{filename}"
            with open(filepath, "wb") as fh:
                fh.write(base64.b64decode(image_data))
            cursor.execute("UPDATE assets SET image = %s WHERE asset_id = %s", (f"/assets/{filename}", asset_id))
            print(f"Migrated asset {asset_id}")
    
    # Migrate profiles
    cursor.execute("SELECT account_id, profile_image FROM user_account WHERE profile_image IS NOT NULL AND profile_image NOT LIKE '/profiles/%'")
    profiles = cursor.fetchall()
    for account_id, image_data in profiles:
        if len(image_data) > 100: # It's base64
            filename = f"{int(time.time()*1000)}_{account_id}.jpg"
            filepath = f"/home/server/uploads/staff-management-system/profiles/{filename}"
            with open(filepath, "wb") as fh:
                fh.write(base64.b64decode(image_data))
            cursor.execute("UPDATE user_account SET profile_image = %s WHERE account_id = %s", (f"/profiles/{filename}", account_id))
            print(f"Migrated profile {account_id}")

    conn.commit()
    cursor.close()
    conn.close()
    print("Migration complete.")
except Exception as e:
    print("Error:", e)
