package com.musicentertainment.interfaces;

import com.musicentertainment.item.ItemArtist;

import java.util.ArrayList;

public interface ArtistListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemArtist> arrayList, int totalRecords);
}