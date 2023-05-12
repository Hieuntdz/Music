package com.musicentertainment.interfaces;

import com.musicentertainment.item.ItemAlbums;
import com.musicentertainment.item.ItemApps;
import com.musicentertainment.item.ItemArtist;
import com.musicentertainment.item.ItemCountry;
import com.musicentertainment.item.ItemGenres;
import com.musicentertainment.item.ItemHomeBanner;
import com.musicentertainment.item.ItemServerPlayList;
import com.musicentertainment.item.ItemSong;

import java.util.ArrayList;

public interface HomeListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemHomeBanner> arrayListBanner, ArrayList<ItemAlbums> arrayListAlbums, ArrayList<ItemArtist> arrayListArtist, ArrayList<ItemServerPlayList> arrayListPlaylist, ArrayList<ItemSong> arrayListSongs, ArrayList<ItemCountry> arrayListCountry, ArrayList<ItemGenres> arrayListGenres, ArrayList<ItemApps> arrayListApps);
}
