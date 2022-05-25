package com.vma.vmaradioconnection;

public class BleHolder implements Comparable{
    String name;
    String type;
    String id;
    int rssi =0;

    @Override
    public int compareTo(Object o) {
        return ((BleHolder) o).rssi - rssi ;
    }
}
