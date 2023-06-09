package com.musicentertainment.asyncTask;

import android.os.AsyncTask;

import com.musicentertainment.interfaces.HomeListener;
import com.musicentertainment.item.ItemAlbums;
import com.musicentertainment.item.ItemApps;
import com.musicentertainment.item.ItemArtist;
import com.musicentertainment.item.ItemCountry;
import com.musicentertainment.item.ItemGenres;
import com.musicentertainment.item.ItemHomeBanner;
import com.musicentertainment.item.ItemServerPlayList;
import com.musicentertainment.item.ItemSong;
import com.musicentertainment.utils.Constant;
import com.musicentertainment.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadHome extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private HomeListener homeListener;
    private ArrayList<ItemHomeBanner> arrayListBanner = new ArrayList<>();
    private ArrayList<ItemApps> arrayListApps = new ArrayList<>();
    private ArrayList<ItemAlbums> arrayListAlbums = new ArrayList<>();
    private ArrayList<ItemArtist> arrayListArtist = new ArrayList<>();
    private ArrayList<ItemCountry> arrayListCountry = new ArrayList<>();
    private ArrayList<ItemGenres> arrayListGenres = new ArrayList<>();
    private ArrayList<ItemSong> arrayListSongs = new ArrayList<>();
    private ArrayList<ItemServerPlayList> arrayListPlaylist = new ArrayList<>();

    public LoadHome(HomeListener homeListener, RequestBody requestBody) {
        this.homeListener = homeListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        homeListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {

            String json = JsonUtils.okhttpPost(Constant.SERVER_URL, requestBody);

            JSONObject mainJson = new JSONObject(json);
            JSONObject jsonObject = mainJson.getJSONObject(Constant.TAG_ROOT);

            JSONArray jsonArrayBanner = jsonObject.getJSONArray("home_banner");

            for (int i = 0; i < jsonArrayBanner.length(); i++) {
                JSONObject objJsonBanner = jsonArrayBanner.getJSONObject(i);

                String banner_id = objJsonBanner.getString(Constant.TAG_BID);
                String banner_title = objJsonBanner.getString(Constant.TAG_BANNER_TITLE);
                String banner_desc = objJsonBanner.getString(Constant.TAG_BANNER_DESC);
                String banner_image = objJsonBanner.getString(Constant.TAG_BANNER_IMAGE);
                String banner_total = objJsonBanner.getString(Constant.TAG_BANNER_TOTAL);

                JSONArray jABannerSongs = objJsonBanner.getJSONArray("songs_list");
                ArrayList<ItemSong> arrayListBannerSongs = new ArrayList<>();
                for (int j = 0; j < jABannerSongs.length(); j++) {
                    JSONObject objJson = jABannerSongs.getJSONObject(j);

                    String id = objJson.getString(Constant.TAG_ID);
                    String cid = objJson.getString(Constant.TAG_CAT_ID);
                    String cname = objJson.getString(Constant.TAG_CAT_NAME);
                    String artist = objJson.getString(Constant.TAG_ARTIST);
                    String name = objJson.getString(Constant.TAG_SONG_NAME);
                    String url = objJson.getString(Constant.TAG_MP3_URL);
                    String desc = objJson.getString(Constant.TAG_DESC);
                    String thumb = objJson.getString(Constant.TAG_THUMB_B).replace(" ", "%20");
                    String thumb_small = objJson.getString(Constant.TAG_THUMB_S).replace(" ", "%20");
                    String total_rate = objJson.getString(Constant.TAG_TOTAL_RATE);
                    String avg_rate = objJson.getString(Constant.TAG_AVG_RATE);
                    String views = objJson.getString(Constant.TAG_VIEWS);
                    String downloads = objJson.getString(Constant.TAG_DOWNLOADS);
                    String lyrics = objJson.getString(Constant.TAG_LYRICS);
                    Boolean isFav = objJson.getBoolean(Constant.TAG_IS_FAV);

                    ItemSong objItem = new ItemSong(id, cid, cname, artist, url, thumb, thumb_small, name, desc, total_rate, avg_rate, views, downloads, lyrics, isFav);
                    arrayListBannerSongs.add(objItem);
                }

                arrayListBanner.add(new ItemHomeBanner(banner_id, banner_title, banner_image, banner_desc, banner_total, arrayListBannerSongs));
            }

            JSONArray jsonArrayGenres = jsonObject.getJSONArray("genres");
            for (int i = 0; i < jsonArrayGenres.length(); i++) {
                JSONObject objJson = jsonArrayGenres.getJSONObject(i);

                String id = objJson.getString(Constant.TAG_GENRES_ID);
                String name = objJson.getString(Constant.TAG_GENRES_NAME);
                String image = objJson.getString(Constant.TAG_GENRES_IMAGE);

                ItemGenres objItem = new ItemGenres(id, name, image);
                arrayListGenres.add(objItem);
            }

            JSONArray jsonArrayCountry = jsonObject.getJSONArray("country_list");
            for (int i = 0; i < jsonArrayCountry.length(); i++) {
                JSONObject objJson = jsonArrayCountry.getJSONObject(i);

                String id = objJson.getString(Constant.TAG_COUNTRY_ID);
                String name = objJson.getString(Constant.TAG_COUNTRY_NAME);
                String image = objJson.getString(Constant.TAG_COUNTRY_IMAGE);

                ItemCountry objItem = new ItemCountry(id, name, image);
                arrayListCountry.add(objItem);
            }

            JSONArray jsonArrayArtist = jsonObject.getJSONArray("latest_artist");
            for (int i = 0; i < jsonArrayArtist.length(); i++) {
                JSONObject objJson = jsonArrayArtist.getJSONObject(i);

                String id = objJson.getString(Constant.TAG_ID);
                String name = objJson.getString(Constant.TAG_ARTIST_NAME);
                String image = objJson.getString(Constant.TAG_ARTIST_IMAGE);
                String thumb = objJson.getString(Constant.TAG_ARTIST_THUMB);

                ItemArtist objItem = new ItemArtist(id, name, image, thumb);
                arrayListArtist.add(objItem);
            }

            JSONArray jsonArrayPlaylist = jsonObject.getJSONArray("playlist");
            for (int i = 0; i < jsonArrayPlaylist.length(); i++) {
                JSONObject objJson = jsonArrayPlaylist.getJSONObject(i);

                String id = objJson.getString(Constant.TAG_PID);
                String name = objJson.getString(Constant.TAG_PLAYLIST_NAME);
                String image = objJson.getString(Constant.TAG_PLAYLIST_IMAGE);
                String thumb = objJson.getString(Constant.TAG_PLAYLIST_THUMB);

                ItemServerPlayList objItem = new ItemServerPlayList(id, name, image, thumb);
                arrayListPlaylist.add(objItem);
            }

            JSONArray jsonArrayApps = jsonObject.getJSONArray("more_apps");
            for (int i = 0; i < jsonArrayApps.length(); i++) {
                JSONObject objJson = jsonArrayApps.getJSONObject(i);

                String id = objJson.getString(Constant.TAG_APP_ID);
                String name = objJson.getString(Constant.TAG_APP_TITLE);
                String url = objJson.getString(Constant.TAG_APP_URL);
                String image = objJson.getString(Constant.TAG_APP_IMAGE);
                String thumb = objJson.getString(Constant.TAG_APP_THUMB);

                ItemApps objItem = new ItemApps(id, name, url, image, thumb);
                arrayListApps.add(objItem);
            }

            JSONArray jsonArrayAlbums = jsonObject.getJSONArray("latest_album");
            for (int i = 0; i < jsonArrayAlbums.length(); i++) {
                JSONObject objJson = jsonArrayAlbums.getJSONObject(i);

                String id = objJson.getString(Constant.TAG_AID);
                String name = objJson.getString(Constant.TAG_ALBUM_NAME);
                String image = objJson.getString(Constant.TAG_ALBUM_IMAGE);
                String thumb = objJson.getString(Constant.TAG_ALBUM_THUMB);

                ItemAlbums objItem = new ItemAlbums(id, name, image, thumb);
                arrayListAlbums.add(objItem);
            }

            JSONArray jsonArraySongs = jsonObject.getJSONArray("trending_songs");
            for (int i = 0; i < jsonArraySongs.length(); i++) {
                JSONObject objJson = jsonArraySongs.getJSONObject(i);

                String id = objJson.getString(Constant.TAG_ID);
                String cid = objJson.getString(Constant.TAG_CAT_ID);
                String cname = objJson.getString(Constant.TAG_CAT_NAME);
                String artist = objJson.getString(Constant.TAG_ARTIST);
                String name = objJson.getString(Constant.TAG_SONG_NAME);
                String url = objJson.getString(Constant.TAG_MP3_URL);
                String desc = objJson.getString(Constant.TAG_DESC);
                String thumb = objJson.getString(Constant.TAG_THUMB_B).replace(" ", "%20");
                String thumb_small = objJson.getString(Constant.TAG_THUMB_S).replace(" ", "%20");
                String total_rate = objJson.getString(Constant.TAG_TOTAL_RATE);
                String avg_rate = objJson.getString(Constant.TAG_AVG_RATE);
                String views = objJson.getString(Constant.TAG_VIEWS);
                String downloads = objJson.getString(Constant.TAG_DOWNLOADS);
                String lyrics = objJson.getString(Constant.TAG_LYRICS);
                Boolean isFav = objJson.getBoolean(Constant.TAG_IS_FAV);

                ItemSong objItem = new ItemSong(id, cid, cname, artist, url, thumb, thumb_small, name, desc, total_rate, avg_rate, views, downloads, lyrics, isFav);
                arrayListSongs.add(objItem);
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        homeListener.onEnd(s, arrayListBanner, arrayListAlbums, arrayListArtist, arrayListPlaylist, arrayListSongs, arrayListCountry, arrayListGenres, arrayListApps);
        super.onPostExecute(s);
    }
}