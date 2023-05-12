package com.musicentertainment.interfaces;

import com.musicentertainment.item.ItemServerPlayList;

import java.util.ArrayList;

public interface ServerPlaylistListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemServerPlayList> arrayList, int total_records);
}