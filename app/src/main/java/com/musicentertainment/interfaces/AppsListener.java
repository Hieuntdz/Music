package com.musicentertainment.interfaces;

import com.musicentertainment.item.ItemApps;

import java.util.ArrayList;

public interface AppsListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemApps> arrayList, int total_records);
}