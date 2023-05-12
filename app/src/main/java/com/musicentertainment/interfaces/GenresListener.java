package com.musicentertainment.interfaces;

import com.musicentertainment.item.ItemGenres;

import java.util.ArrayList;

public interface GenresListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemGenres> arrayList, int total_records);
}