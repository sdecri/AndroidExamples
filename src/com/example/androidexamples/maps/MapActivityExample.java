package com.example.androidexamples.maps;
import android.os.Bundle;

import com.example.androidexamples.R;
import com.google.android.maps.MapActivity;


/**
 * Map.java
 */

/**
 * @author Simone
 * 07/dic/2014
 */
public class MapActivityExample extends MapActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isRouteDisplayed() {

        return false;
    }

    
    /**
    * {@inheritDoc}
    */
    @Override
    protected void onCreate(Bundle arg0) {
    
        super.onCreate(arg0);
        
        setContentView(R.layout.fragment_map);
    }
}
