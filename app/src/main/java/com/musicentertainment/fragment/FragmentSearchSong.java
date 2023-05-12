package com.musicentertainment.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.musicentertainment.adapter.AdapterAllSongList;
import com.musicentertainment.asyncTask.LoadSong;
import com.musicentertainment.interfaces.ClickListenerPlayList;
import com.musicentertainment.interfaces.SongListener;
import com.musicentertainment.item.ItemAlbums;
import com.musicentertainment.item.ItemMyPlayList;
import com.musicentertainment.item.ItemSong;
import com.musicentertainment.countrymusic.MainActivity;
import com.musicentertainment.countrymusic.PlayerService;
import com.musicentertainment.countrymusic.R;
import com.musicentertainment.utils.Constant;
import com.musicentertainment.utils.EndlessRecyclerViewScrollListener;
import com.musicentertainment.utils.GlobalBus;
import com.musicentertainment.utils.Methods;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class FragmentSearchSong extends Fragment {


    private Methods methods;
    private RecyclerView rv;
    private AdapterAllSongList adapter;
    private ArrayList<ItemSong> arrayList_songs, arrayListTemp;
    private CircularProgressBar progressBar;
    private FrameLayout frameLayout;
    private String addedFrom = "searchsong";

    private String errr_msg;
    private int page = 1;
    private Boolean isOver = false, isScroll = false, isLoading = false, isNewAdded = true, isAllNew = false;
    private NativeAdsManager mNativeAdsManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_song_by_cat, container, false);

        methods = new Methods(getActivity());
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.search_songs));

        arrayList_songs = new ArrayList<>();
        arrayListTemp = new ArrayList<>();

        frameLayout = rootView.findViewById(R.id.fl_empty);
        progressBar = rootView.findViewById(R.id.pb_song_by_cat);
        rv = rootView.findViewById(R.id.rv_song_by_cat);
        LinearLayoutManager llm_banner = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm_banner);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(llm_banner) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if (!isOver) {
                    if (!isLoading) {
                        isLoading = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isScroll = true;
                                loadSongs();
                            }
                        }, 0);
                    }
                }
            }
        });

        loadSongs();

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            if (methods.isNetworkAvailable()) {
                page = 1;
                isScroll = false;
                Constant.search_item = s.replace(" ", "%20");
                arrayList_songs.clear();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                loadSongs();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    private void loadSongs() {
        try {
            if (methods.isNetworkAvailable()) {
                LoadSong loadSong = new LoadSong(new SongListener() {
                    @Override
                    public void onStart() {
                        if (arrayList_songs.size() == 0) {
                            arrayList_songs.clear();
                            frameLayout.setVisibility(View.GONE);
                            rv.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemSong> arrayListSong, int total_records) {
                        if (getActivity() != null) {
                            if (success.equals("1")) {
                                if (!verifyStatus.equals("-1")) {
                                    if (arrayListSong.size() == 0) {
                                        isOver = true;
                                        errr_msg = getString(R.string.err_no_songs_found);
                                        setEmpty();
                                    } else {

                                        if(Constant.addedFrom.equals(addedFrom) ) {
                                            if (isNewAdded) {
                                                for (int i = 0; i < arrayListSong.size(); i++) {
                                                    if (!methods.isContains(arrayListSong.get(i).getId())) {
                                                        Constant.arrayList_play.add(i + (arrayList_songs.size()), arrayListSong.get(i));
                                                        Constant.playPos = Constant.playPos + 1;
                                                    } else {
                                                        isNewAdded = false;
                                                        break;
                                                    }
                                                }
                                                addNewDataToArrayList(arrayListSong, total_records);
                                                try {
                                                    GlobalBus.getBus().postSticky(new ItemMyPlayList("", "", null));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                addNewDataToArrayList(arrayListSong, total_records);
                                                if (Constant.arrayList_play.size() <= arrayList_songs.size()) {
//                                        Constant.arrayList_play.clear();
//                                                Constant.arrayList_play.addAll(arrayListSong);

                                                    if(!isAllNew) {
                                                        for (int i = 0; i < arrayListSong.size(); i++) {
                                                            if (!methods.isContains(arrayListSong.get(i).getId())) {
                                                                Constant.arrayList_play.add(arrayListSong.get(i));
                                                                isAllNew = true;
                                                            }
                                                        }
                                                    }  else {
                                                        Constant.arrayList_play.addAll(arrayListSong);
                                                    }

                                                    try {
                                                        GlobalBus.getBus().postSticky(new ItemMyPlayList("", "", null));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        } else {
                                            isNewAdded = false;
                                            addNewDataToArrayList(arrayListSong, total_records);
                                        }

                                        page = page + 1;
                                        setAdapter();
                                    }
                                } else {
                                    methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                                }
                            } else {
                                errr_msg = getString(R.string.err_server);
                                setEmpty();
                            }
                            progressBar.setVisibility(View.GONE);
                            isLoading = false;
                        }
                    }
                }, methods.getAPIRequest(Constant.METHOD_SEARCH, page, "", "", Constant.search_item, "songs", "", "", "", "", "", "", "", "", "", Constant.itemUser.getId(), "", null));
                loadSong.execute();

            } else {
                errr_msg = getString(R.string.err_internet_not_conn);
                setEmpty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        if (!isScroll) {
            adapter = new AdapterAllSongList(getActivity(), arrayList_songs, new ClickListenerPlayList() {
                @Override
                public void onClick(int position) {
                    int real_pos = adapter.getRealPos(position, arrayListTemp);

                    Constant.isOnline = true;
//                    if (!Constant.addedFrom.equals(addedFrom)) {
                        Constant.arrayList_play.clear();
                        Constant.arrayList_play.addAll(arrayListTemp);
//                        Constant.addedFrom = addedFrom;
//                        Constant.isNewAdded = true;
//                    }
                    Constant.playPos = real_pos;
                    Constant.isNewAdded = true;

                    Intent intent = new Intent(getActivity(), PlayerService.class);
                    intent.setAction(PlayerService.ACTION_PLAY);
                    getActivity().startService(intent);
                }

                @Override
                public void onItemZero() {

                }
            }, "online");

            loadNativeAds();
            rv.setAdapter(adapter);
            setEmpty();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void loadNativeAds() {
        if (Constant.isNativeAd) {
            if (Constant.natveAdType.equals("admob")) {
                AdLoader.Builder builder = new AdLoader.Builder(getActivity(), Constant.ad_native_id);
                AdLoader adLoader = builder.forUnifiedNativeAd(
                        new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                            @Override
                            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                                // A native ad loaded successfully, check if the ad loader has finished loading
                                // and if so, insert the ads into the list.

                                adapter.addAds(unifiedNativeAd);

                            }
                        }).withAdListener(
                        new AdListener() {
                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                            }
                        }).build();

                // Load the Native Express ad.
                adLoader.loadAds(new AdRequest.Builder().build(), 5);
            } else {
                mNativeAdsManager = new NativeAdsManager(getActivity(), Constant.ad_native_id, 5);
                mNativeAdsManager.setListener(new NativeAdsManager.Listener() {
                    @Override
                    public void onAdsLoaded() {
                        adapter.setFBNativeAdManager(mNativeAdsManager);
                    }

                    @Override
                    public void onAdError(AdError adError) {

                    }
                });
                mNativeAdsManager.loadAds();
            }
        }
    }

    private void addNewDataToArrayList(ArrayList<ItemSong> arrayListLatest, int total_records) {
        arrayListTemp.addAll(arrayListLatest);
        for (int i = 0; i < arrayListLatest.size(); i++) {
            arrayList_songs.add(arrayListLatest.get(i));

            if (Constant.isNativeAd) {
                int abc = arrayList_songs.lastIndexOf(null);
                if (((arrayList_songs.size() - (abc + 1)) % Constant.adNativeDisplay == 0) && (arrayListLatest.size() - 1 != i || arrayListTemp.size() != total_records)) {
                    arrayList_songs.add(null);
                }
            }
        }
    }

    public void setEmpty() {
        if (arrayList_songs.size() > 0) {
            rv.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            frameLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = null;
            if (errr_msg.equals(getString(R.string.err_no_songs_found))) {
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            } else if (errr_msg.equals(getString(R.string.err_internet_not_conn))) {
                myView = inflater.inflate(R.layout.layout_err_internet, null);
            } else if (errr_msg.equals(getString(R.string.err_server))) {
                myView = inflater.inflate(R.layout.layout_err_server, null);
            }

            TextView textView = myView.findViewById(R.id.tv_empty_msg);
            textView.setText(errr_msg);

            myView.findViewById(R.id.btn_empty_try).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadSongs();
                }
            });
            frameLayout.addView(myView);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEquilizerChange(ItemAlbums itemAlbums) {
        adapter.notifyDataSetChanged();
        GlobalBus.getBus().removeStickyEvent(itemAlbums);
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        GlobalBus.getBus().unregister(this);
        super.onStop();
    }
}