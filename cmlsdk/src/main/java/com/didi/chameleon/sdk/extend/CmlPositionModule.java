package com.didi.chameleon.sdk.extend;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlJoin;
import com.didi.chameleon.sdk.module.CmlMethod;

import org.json.JSONException;
import org.json.JSONObject;

@CmlJoin(name = "cml")
public class CmlPositionModule {

    @CmlMethod(alias = "getLocationInfo")
    public void getLocationInfo(Context context, CmlCallback<JSONObject> callback) {
        JSONObject object = new JSONObject();
        try {
            Location location = getLastKnownLocation(context);
            if (location != null) {
                object.put("lat", location.getLatitude());
                object.put("lng", location.getLongitude());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        callback.onCallback(object);
    }

    private Location getLastKnownLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return null;
        }
        if (locationManager != null) {
            return locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        } else {
            return null;
        }

    }

}
