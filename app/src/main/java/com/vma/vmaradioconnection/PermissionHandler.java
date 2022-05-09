package com.vma.vmaradioconnection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.Objects;

public class PermissionHandler {

    public static boolean requestLocation(Activity activity){
        String[] PERMISSIONS =  new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        return checkPermission(activity,PERMISSIONS);
    }
    public static boolean requestBluetooth(Activity activity){
        String[] PERMISSIONS;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PERMISSIONS = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_PRIVILEGED,
            };
        }
        else {
            PERMISSIONS = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_PRIVILEGED,
            };
        }
        return hasPermission(activity,PERMISSIONS);
    }

    public static boolean hasPermission(Activity activity, String[] permissions){
        if(!hasPermissions(activity, permissions)){
            ActivityCompat.requestPermissions(Objects.requireNonNull(activity), permissions, 32);
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean checkPermission(Activity activity, String[] permissions){
        return hasPermissions(activity, permissions);
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("PermissionHandler",permission+ "= false");
                    return false;
                }
                Log.d("PermissionHandler","permission "+permission+" true");
            }
        }
        return true;
    }

}
