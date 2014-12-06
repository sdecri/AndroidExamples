/**
 * AccountInfo.java
 */
package com.example.androidexamples.contacts;

import android.graphics.drawable.Drawable;


/**
 * @author Simone
 * 06/dic/2014
 */
public class AccountInfo {
    
    private String name;
    private String type;
    private Drawable icon;
    
    
    /**
     * @param name
     * @param type
     * @param icon
     */
    public AccountInfo(String name, String type, Drawable icon) {

        super();
        this.name = name;
        this.type = type;
        this.icon = icon;
    }


    /**
    * {@inheritDoc}
    */
    @Override
    public String toString() {
    
        return "ACCOUNT: Name = " + name + "; Type: " + type;
    }
    
    /**
     * @return the name
     */
    public String getName() {
    
        return name;
    }


    
    /**
     * @return the type
     */
    public String getType() {
    
        return type;
    }


    
    /**
     * @return the icon
     */
    public Drawable getIcon() {
    
        return icon;
    }
    

}
