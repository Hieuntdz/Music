package com.musicentertainment.asyncTask;

import android.os.AsyncTask;

import com.musicentertainment.interfaces.SongListener;
import com.musicentertainment.item.ItemSong;
import com.musicentertainment.utils.Constant;
import com.musicentertainment.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadSong extends AsyncTask<String, String, String> {

    private SongListener songListener;
    private RequestBody requestBody;
    private ArrayList<ItemSong> arrayList = new ArrayList<>();
    private String verifyStatus = "0", message = "";
    private int total_records = -1;

    public LoadSong(SongListener songListener, RequestBody requestBody) {
        this.songListener = songListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        songListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JsonUtils.okhttpPost(Constant.SERVER_URL, requestBody);

            JSONObject mainJson = new JSONObject(json);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.TAG_ROOT);

            if (jsonArray.length() > 0 && jsonArray.getJSONObject(0).has("songs_list")) {
                jsonArray = jsonArray.getJSONObject(0).getJSONArray("songs_list");
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objJson = jsonArray.getJSONObject(i);

                if(objJson.has("total_records")) {
                    total_records = Integer.parseInt(objJson.getString("total_records"));
                }

                try {
                    if (!objJson.has(Constant.TAG_SUCCESS)) {
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
                        arrayList.add(objItem);
                    } else {
                        verifyStatus = objJson.getString(Constant.TAG_SUCCESS);
                        message = objJson.getString(Constant.TAG_MSG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        songListener.onEnd(s, verifyStatus, message, arrayList, total_records);
        super.onPostExecute(s);
    }
}