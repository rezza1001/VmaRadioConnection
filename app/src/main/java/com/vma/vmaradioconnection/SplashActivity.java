package com.vma.vmaradioconnection;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PermissionHandler.requestBluetooth(this);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            showInfo("Device doesn't support Bluetooth");
            return;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                showInfo("Device Bluetooth not Granted");
                return;
            }
        }

        if (!bluetoothAdapter.enable()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 12);
        } else {
            showInfo("Bluetooth Active");
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            },500);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAGRZ","DATA "+ requestCode + " = "+resultCode);
    }


    private void showInfo(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
