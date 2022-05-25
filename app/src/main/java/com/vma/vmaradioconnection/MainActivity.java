package com.vma.vmaradioconnection;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<BleHolder> listData = new ArrayList<>();
    MainAdapter mainAdapter;
    HashMap<String, String> mapDevices = new HashMap<>();
    TextView bbtn_scan;

    static final int ENABLE_BLUETOOTH_REQUEST_CODE = 1;
    private BluetoothLeScanner bluetoothLeScanner;

    ImageView imvw_scan;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rcvw_data = findViewById(R.id.rcvw_data);
        rcvw_data.setLayoutManager(new LinearLayoutManager(this));
        imvw_scan = findViewById(R.id.imvw_scan);
        bbtn_scan = findViewById(R.id.bbtn_scan);

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


        bbtn_scan.setOnClickListener(view -> {
            if (imvw_scan.getTag().toString().equals("0")){
                setupBluetooth();
            }
            else {
                imvw_scan.setTag(0);
                bbtn_scan.setBackgroundResource(R.drawable.button_scan);
                bbtn_scan.setText("START SCAN");
                imvw_scan.clearAnimation();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        showInfo("Device Bluetooth not Granted");
                        return;
                    }
                }
                bluetoothLeScanner.stopScan(leScanCallback);
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

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    private void setupBluetooth() {
        listData.clear();
        mapDevices.clear();
        mainAdapter.notifyDataSetChanged();

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
            this.startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE);
        } else {
            showInfo("Bluetooth OK");
        }

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(leScanCallback);
        imvw_scan.setTag(1);
        bbtn_scan.setBackgroundResource(R.drawable.button_stop);
        bbtn_scan.setText("STOP SCAN");

        imvw_scan.clearAnimation();
        imvw_scan.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate));

        new Handler().postDelayed(() -> {
            imvw_scan.setTag(0);
            bbtn_scan.setBackgroundResource(R.drawable.button_scan);
            imvw_scan.clearAnimation();
            bluetoothLeScanner.stopScan(leScanCallback);
            bbtn_scan.setText("START SCAN");
        },30000);
    }


    private void showInfo(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    ScanCallback leScanCallback = new ScanCallback() {
        @SuppressLint("NotifyDataSetChanged")
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

                        BleHolder holder = new BleHolder();
                        holder.name = device.getName();
                        holder.rssi = result.getRssi();
                        if (device.getName().startsWith("3")){
                            holder.type = "Vma Radio";
                            holder.id = NcsUtill.bleNameToID(device.getName());
                        }
                        else {
                            holder.type = "Unknown";
                            holder.id = "Undefined VMA id";
                        }
                        listData.add(holder);

                        Collections.sort(listData);
                        mainAdapter.notifyDataSetChanged();
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