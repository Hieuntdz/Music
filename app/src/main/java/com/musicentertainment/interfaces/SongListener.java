package com.musicentertainment.interfaces;

import com.musicentertainment.item.ItemSong;

import java.util.ArrayList;

public interface SongListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemSong> arrayList, int total_records);
}