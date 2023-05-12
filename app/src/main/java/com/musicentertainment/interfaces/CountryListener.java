package com.musicentertainment.interfaces;

import com.musicentertainment.item.ItemCountry;

import java.util.ArrayList;

public interface CountryListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemCountry> arrayList, int total_records);
}