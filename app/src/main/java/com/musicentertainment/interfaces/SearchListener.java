package com.musicentertainment.interfaces;

import com.musicentertainment.item.ItemAlbums;
import com.musicentertainment.item.ItemArtist;
import com.musicentertainment.item.ItemSong;

import java.util.ArrayList;

public interface SearchListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemSong> arrayListSong, ArrayList<ItemArtist> arrayListArtist, ArrayList<ItemAlbums> arrayListAlbums);
}
