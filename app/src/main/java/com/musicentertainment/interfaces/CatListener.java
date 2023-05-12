package com.musicentertainment.interfaces;

import com.musicentertainment.item.ItemCat;

import java.util.ArrayList;

public interface CatListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemCat> arrayList, int total_records);
}
