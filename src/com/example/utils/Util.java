/**
 * Util.java
 */
package com.example.utils;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.android.gms.maps.model.LatLng;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Photo;

/**
 * @author Simone
 *         06/dic/2014
 */
public class Util {

    /**
     * Load a contact photo thumbnail and return it as a Bitmap, resizing the
     * image to the provided image dimensions as needed.
     * 
     * @param contentResolver 
     * @param photoData
     *            photo ID Prior to Honeycomb, the contact's _ID value. For
     *            Honeycomb and later, the value of PHOTO_THUMBNAIL_URI.
     * @return A thumbnail Bitmap, sized to the provided width and height.
     *         Returns null if the thumbnail is not found.
     */
    public static Bitmap loadContactPhotoThumbnail(ContentResolver contentResolver,String photoData) {

        if (photoData == null)
            return null;

        Bitmap toReturn = null;

        // Creates an asset file descriptor for the thumbnail file.
        AssetFileDescriptor afd = null;
        // try-catch block for file not found
        try {
            // Creates a holder for the URI.
            Uri thumbUri;
            // If Android 3.0 or later
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                // Sets the URI from the incoming PHOTO_THUMBNAIL_URI
                thumbUri = Uri.parse(photoData);
            }
            else {
                // Prior to Android 3.0, constructs a photo Uri using _ID
                /*
                 * Creates a contact URI from the Contacts content URI incoming
                 * photoData (_ID)
                 */
                final Uri contactUri = Uri.withAppendedPath(Contacts.CONTENT_URI, photoData);
                /*
                 * Creates a photo URI by appending the content URI of
                 * Contacts.Photo.
                 */
                thumbUri = Uri.withAppendedPath(contactUri, Photo.CONTENT_DIRECTORY);
            }

            /*
             * Retrieves an AssetFileDescriptor object for the thumbnail URI
             * using ContentResolver.openAssetFileDescriptor
             */
            afd = contentResolver.openAssetFileDescriptor(thumbUri, "r");
            /*
             * Gets a file descriptor from the asset file descriptor. This
             * object can be used across processes.
             */
            FileDescriptor fileDescriptor = afd.getFileDescriptor();
            // Decode the photo file and return the result as a Bitmap
            // If the file descriptor is valid
            if (fileDescriptor != null) {
                // Decodes the bitmap
                toReturn = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null);
            }
            // If the file isn't found
        }
        catch (FileNotFoundException e) {
            // In all cases, close the asset file descriptor
        }
        finally {
            if (afd != null) {
                try {
                    afd.close();
                }
                catch (IOException e) {}
            }
        }
        return toReturn;
    }

    /**
     * Get the {@link Drawable icon} of the {@link Account} 
     * @param account
     * @param manager
     * @param context
     * @return
     */
    public static Drawable getIconForAccount(Account account, AccountManager manager, Context context) {
        AuthenticatorDescription[] descriptions =  manager.getAuthenticatorTypes();
        for (AuthenticatorDescription description: descriptions) {
            if (description.type.equals(account.type)) {
                PackageManager pm = context.getPackageManager();
                return pm.getDrawable(description.packageName, description.iconId, null); 
            }
        }
        return null;
    }

    /**
     * Returns the approximate distance in meters between 
     * two locations. 
     * Distance is defined using the WGS84 ellipsoid
     * @param l1
     * @param l2
     * @return
     */
    public static float distance(Location l1,Location l2){
        if(l1==null || l2==null) return 0;
        return l1.distanceTo(l2);
    }
    
    /**
     * Returns the approximate distance in meters between 
     * two locations. 
     * Distance is defined using the WGS84 ellipsoid
     * @param point1
     * @param point2
     * @return
     */
    public static float distance(LatLng point1, LatLng point2){
        if(point1==null || point2==null) return 0;
        Location location1=new Location("");
        location1.setLatitude(point1.latitude);
        location1.setLongitude(point1.longitude);
        Location location2=new Location("");
        location2.setLatitude(point2.latitude);
        location2.setLongitude(point2.longitude);
        return location1.distanceTo(location2);
    }
    
}
