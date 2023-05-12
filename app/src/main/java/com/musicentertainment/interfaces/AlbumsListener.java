package com.musicentertainment.interfaces;

import com.musicentertainment.item.ItemAlbums;

import java.util.ArrayList;

public interface AlbumsListener {
    void onStart();

    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemAlbums> arrayList, int total_records);
}