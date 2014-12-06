/**
 * ContactInfo.java
 */
package com.example.androidexamples.contacts;

import java.util.HashMap;
import java.util.Map.Entry;

import android.accounts.Account;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * @author Simone
 *         06/dic/2014
 */
public class ContactInfo {

    private long id;
    private String lookUpKey;
    private String displayName;
    private Bitmap thumbnail;
    private Uri uri;
    private HashMap<String, AccountInfo> mapAccount;

    /**
     * 
     * @param id
     * @param lookUpKey
     * @param uri
     * @param displayName
     * @param thumbnail
     */
    public ContactInfo(long id, String lookUpKey, Uri uri, AccountInfo[] accounts,
            String displayName, Bitmap thumbnail) {

        super();
        this.id = id;
        this.lookUpKey = lookUpKey;
        this.displayName = displayName;
        this.thumbnail = thumbnail;
        this.uri = uri;
        if (accounts != null) {
            mapAccount = new HashMap<String, AccountInfo>(accounts.length);
            for (int i = 0; i < accounts.length; i++) {
                mapAccount.put(accounts[i].getName() + ";" + accounts[i].getType(), accounts[i]);
            }
        }

    }

    // METHODS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
    * {@inheritDoc}
    */
    @Override
    public String toString() {
    
        StringBuilder sb = new StringBuilder("Contact: " + displayName);
        // accounts
        if(mapAccount!=null){
            sb.append("; ");
            for (Entry<String, AccountInfo> entry : mapAccount.entrySet()) {
                sb.append(entry.getValue()).append(";");
            }
            sb.delete(sb.length()-1, sb.length());
        }
        return sb.toString();
    }
    
    /**
     * Return <code>true</code> if the current {@link ContactInfo} contains
     * at least one of the specified account
     * 
     * @param accounts
     * @return
     */
    public boolean containsAtLeastOneAccount(AccountInfo[] accounts) {

        if (mapAccount != null) {
            for (int i = 0; i < accounts.length; i++) {
                if (mapAccount.containsKey(accounts[i].getName()+";"+accounts[i].getType()))
                    return true;
            }
        }
        return false;
    }

    /**
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * @return the lookUpKey
     */
    public String getLookUpKey() {

        return lookUpKey;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {

        return displayName;
    }

    /**
     * @return the thumbnail
     */
    public Bitmap getThumbnail() {

        return thumbnail;
    }

    /**
     * @return the uri
     */
    public Uri getUri() {

        return uri;
    }

    /**
     * @return the accounts
     */
    public Account[] getAccounts() {

        return (Account[]) mapAccount.values().toArray();
    }

}
