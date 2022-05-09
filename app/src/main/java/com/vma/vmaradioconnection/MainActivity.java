package com.vma.vmaradioconnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<Bundle> listData = new ArrayList<>();
    MainAdapter mainAdapter;
    HashMap<String, String> mapDevices = new HashMap<>();

    static final int ENABLE_BLUETOOTH_REQUEST_CODE = 1;
    private BluetoothLeScanner bluetoothLeScanner;

    ImageView imvw_scan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rcvw_data = findViewById(R.id.rcvw_data);
        rcvw_data.setLayoutManager(new LinearLayoutManager(this));
        imvw_scan = findViewById(R.id.imvw_scan);

        mainAdapter = new MainAdapter(listData);
        rcvw_data.setAdapter(mainAdapter);

        PermissionHandler.requestBluetooth(this);
        checkSupport();
        setupBluetooth();
        imvw_scan.setOnClickListener(view -> {
            if (imvw_scan.getTag().toString().equals("0")){
                setupBluetooth();
            }
        });
    }

    private void checkSupport() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "bluetooth low energy (BLE) not supported", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupBluetooth() {
        listData.clear();
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
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE);
        } else {
            showInfo("Bluetooth Active");
        }

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(leScanCallback);
        imvw_scan.setTag(1);

        imvw_scan.clearAnimation();
        imvw_scan.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate));

        new Handler().postDelayed(() -> {
            imvw_scan.setTag(0);
            imvw_scan.clearAnimation();
            bluetoothLeScanner.stopScan(leScanCallback);
        },30000);
    }


    private void showInfo(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    showInfo("Device Bluetooth not Granted");
                    return;
                }
            }
            if (result.getDevice().getName() != null){
                if (result.getDevice().getBondState() != BluetoothDevice.BOND_BONDED){
                    BluetoothDevice device = result.getDevice();
                    if (mapDevices.get(device.getAddress()) == null){
                        mapDevices.put(device.getAddress(), device.getName());

                        Bundle bundle = new Bundle();
                        bundle.putString("name",device.getName());
                        if (device.getName().startsWith("3")){
                            bundle.putString("type","Vma Radio");
                            bundle.putString("id",NcsUtill.bleNameToID(device.getName()));
                        }
                        else {
                            bundle.putString("type","Unknown");
                            bundle.putString("id","Undefined VMA id");
                        }
                        listData.add(bundle);
                        mainAdapter.notifyItemInserted(listData.size());
                    }
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

}