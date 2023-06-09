package com.musicentertainment.utils;

import android.content.Context;

import com.musicentertainment.item.ItemAbout;
import com.musicentertainment.item.ItemAlbums;
import com.musicentertainment.item.ItemArtist;
import com.musicentertainment.item.ItemSong;
import com.musicentertainment.item.ItemUser;
import com.musicentertainment.countrymusic.BuildConfig;

import java.io.Serializable;
import java.util.ArrayList;

public class Constant implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String SERVER_URL = BuildConfig.SERVER_URL;

    public static final String METHOD_LOGIN = "user_login";
    public static final String METHOD_REGISTER = "user_register";
    public static final String METHOD_FORGOT_PASSWORD = "forgot_pass";
    public static final String METHOD_PROFILE = "user_profile";
    public static final String METHOD_PROFILE_EDIT = "user_profile_update";
    public static final String METHOD_SINGLE_SONG = "single_song";
    public static final String METHOD_LATEST = "latest";
    public static final String METHOD_SEARCH = "song_search";
    public static final String METHOD_FAV = "favourite_post";
    public static final String METHOD_ALL_SONGS = "all_songs";
    public static final String METHOD_SONG_BY_TRENDING = "trending_songs";
    public static final String METHOD_SONG_BY_CAT = "cat_songs";
    public static final String METHOD_SONG_BY_RECENT = "get_recent_songs";
    public static final String METHOD_SONG_BY_FAV = "get_favourite_post";
    public static final String METHOD_SONG_BY_COUNTRY = "country_songs";
    public static final String METHOD_SONG_BY_ALBUMS = "album_songs";
    public static final String METHOD_SONG_BY_ARTIST = "artist_name_songs";
    public static final String METHOD_SONG_BY_PLAYLIST = "playlist_songs";
    public static final String METHOD_ALBUMS = "album_list";
    public static final String METHOD_ALBUMS_BY_ARTIST = "artist_album_list";
    public static final String METHOD_HOME = "home";
    public static final String METHOD_ARTIST = "artist_list";
    public static final String METHOD_ARTIST_BY_GENRES = "genre_artist_list";
    public static final String METHOD_CAT = "cat_list";
    public static final String METHOD_COUNTRY = "country_list";
    public static final String METHOD_GENRES = "genres_list";
    public static final String METHOD_APP_DETAILS = "app_details";
    public static final String METHOD_RATINGS = "song_rating";
    public static final String METHOD_SERVER_PLAYLIST = "playlist";
    public static final String METHOD_REPORT = "song_report";
    public static final String METHOD_SUGGESTION = "song_suggest";
    public static final String METHOD_DOWNLOAD_COUNT = "song_download";
    public static final String METHOD_APPS = "more_app_list";

    public static final String URL_ABOUT_US_LOGO = "images/";

    public static final String TAG_ROOT = "ONLINE_MP3";

    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MSG = "msg";

    public static final String TAG_ID = "id";
    public static final String TAG_CAT_ID = "cat_id";
    public static final String TAG_CAT_NAME = "category_name";
    public static final String TAG_CAT_IMAGE = "category_image";

    public static final String TAG_COUNTRY_ID = "id";
    public static final String TAG_COUNTRY_NAME = "country_name";
    public static final String TAG_COUNTRY_IMAGE = "country_image_thumb";

    public static final String TAG_GENRES_ID = "genre_id";
    public static final String TAG_GENRES_NAME = "genre_title";
    public static final String TAG_GENRES_IMAGE = "genre_image_thumb";

    public static final String TAG_MP3_URL = "mp3_url";
    public static final String TAG_DURATION = "mp3_duration";
    public static final String TAG_SONG_NAME = "mp3_title";
    public static final String TAG_DESC = "mp3_description";
    public static final String TAG_THUMB_B = "mp3_thumbnail_b";
    public static final String TAG_THUMB_S = "mp3_thumbnail_s";
    public static final String TAG_ARTIST = "mp3_artist";
    public static final String TAG_TOTAL_RATE = "total_rate";
    public static final String TAG_AVG_RATE = "rate_avg";
    public static final String TAG_USER_RATE = "user_rate";
    public static final String TAG_VIEWS = "total_views";
    public static final String TAG_DOWNLOADS = "total_download";
    public static final String TAG_LYRICS = "mp3_lyrics";
    public static final String TAG_IS_FAV = "is_favourite";

    public static final String TAG_CID = "cid";
    public static final String TAG_AID = "aid";
    public static final String TAG_PID = "pid";
    public static final String TAG_BID = "bid";

    public static final String TAG_BANNER_TITLE = "banner_title";
    public static final String TAG_BANNER_DESC = "banner_sort_info";
    public static final String TAG_BANNER_IMAGE = "banner_image";
    public static final String TAG_BANNER_TOTAL = "total_songs";

    public static final String TAG_ARTIST_NAME = "artist_name";
    public static final String TAG_ARTIST_IMAGE = "artist_image";
    public static final String TAG_ARTIST_THUMB = "artist_image_thumb";

    public static final String TAG_ALBUM_NAME = "album_name";
    public static final String TAG_ALBUM_IMAGE = "album_image";
    public static final String TAG_ALBUM_THUMB = "album_image_thumb";

    public static final String TAG_APP_ID = "id";
    public static final String TAG_APP_TITLE = "app_title";
    public static final String TAG_APP_URL = "app_url";
    public static final String TAG_APP_IMAGE = "app_image";
    public static final String TAG_APP_THUMB = "app_image_thumb";

    public static final String TAG_PLAYLIST_NAME = "playlist_name";
    public static final String TAG_PLAYLIST_IMAGE = "playlist_image";
    public static final String TAG_PLAYLIST_THUMB = "playlist_image_thumb";

    public static final String LOGIN_TYPE_NORMAL = "normal";
    public static final String LOGIN_TYPE_GOOGLE = "google";
    public static final String LOGIN_TYPE_FB = "facebook";

    public static final String DARK_MODE_ON = "on";
    public static final String DARK_MODE_OFF = "off";
    public static final String DARK_MODE_SYSTEM = "system";

    public static ItemAbout itemAbout;
    public static ItemUser itemUser = new ItemUser("","","","","", Constant.LOGIN_TYPE_NORMAL);
    public static Boolean isLogged = false;

    public static int playPos = 0;
    public static Boolean isNewAdded = true;
    public static String addedFrom = "";
    public static ArrayList<ItemSong> arrayList_play = new ArrayList<>();
    public static ArrayList<ItemSong> arrayListOfflineSongs = new ArrayList<>();
    public static ArrayList<ItemAlbums> arrayListOfflineAlbums = new ArrayList<>();
    public static ArrayList<ItemArtist> arrayListOfflineArtist = new ArrayList<>();

    public static Boolean isRepeat = false, isSuffle = false, isPlayed = false, isFromNoti = false, isFromPush = false, isAppOpen = false, isOnline = true, isDownloaded = false, isBannerAd = true,
            isInterAd = true, isNativeAd = true, isSongDownload = false, isUpdate = false, showUpdateDialog = true, appUpdateCancel = false, isAppOpenAdShown = false;
    public static Context context;
    public static String recentLimit = "30";
    public static String packageName, pushSID = "0", pushCID = "0", pushCName = "", pushArtID = "0", pushArtNAME = "", pushAlbID = "0", pushAlbNAME = "",
            search_item = "", bannerAdType = "admob", interstitialAdType = "admob", natveAdType = "admob", appVersion="1", appUpdateMsg = "", appUpdateURL = "";

    public static int rotateSpeed = 25000; //in milli seconds

    public static int bannerAdShowTime = 200000; //in milli seconds

    public static int dialogCount = 0;
    public static int adCount = 0;
    public static int ad_interstitial_display = 5;
    public static int adNativeDisplay = 6;

    public static String ad_publisher_id = "";
    public static String ad_banner_id = "";
    public static String ad_inter_id = "";
    public static String ad_native_id = "";
}