package com.musicentertainment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.musicentertainment.countrymusic.PlayerService;
import com.musicentertainment.countrymusic.R;
import com.musicentertainment.interfaces.ClickListenerPlayList;
import com.musicentertainment.item.ItemMyPlayList;
import com.musicentertainment.item.ItemSong;
import com.musicentertainment.utils.Constant;
import com.musicentertainment.utils.DBHelper;
import com.musicentertainment.utils.GlobalBus;
import com.musicentertainment.utils.Methods;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdsManager;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import es.claucookie.miniequalizerlibrary.EqualizerView;


public class AdapterAllSongList extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<ItemSong> arrayList;
    private ArrayList<ItemSong> filteredArrayList;
    private ClickListenerPlayList recyclerClickListener;
    private NameFilter filter;
    private String type;
    private Methods methods;
    private DBHelper dbHelper;

    private final int VIEW_PROG = -1;

    private Boolean isAdLoaded = false;
    private List<com.google.android.gms.ads.nativead.NativeAd> mNativeAdsAdmob = new ArrayList<>();
    private NativeAdsManager mNativeAdsManager;
    private ArrayList<NativeAd> mNativeAdsFB = new ArrayList<>();

    public AdapterAllSongList(Activity context, ArrayList<ItemSong> arrayList, ClickListenerPlayList recyclerClickListener, String type) {
        this.arrayList = arrayList;
        this.filteredArrayList = arrayList;
        this.context = context;
        this.type = type;
        this.recyclerClickListener = recyclerClickListener;
        methods = new Methods(context);
        dbHelper = new DBHelper(context);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_song, textView_duration, textView_catname, tv_avg_rate, tv_views, tv_download;
        EqualizerView equalizer;
        ImageView imageView, imageView_option, iv_downlaod_icon;
        RelativeLayout rl;
        RatingBar ratingBar;

        MyViewHolder(View view) {
            super(view);
            rl = view.findViewById(R.id.ll_songlist);
            tv_views = view.findViewById(R.id.tv_songlist_views);
            tv_download = view.findViewById(R.id.tv_songlist_downloads);
            textView_song = view.findViewById(R.id.tv_songlist_name);
            textView_duration = view.findViewById(R.id.tv_songlist_duration);
            tv_avg_rate = view.findViewById(R.id.tv_songlist_avg_rate);
            equalizer = view.findViewById(R.id.equalizer_view);
            textView_catname = view.findViewById(R.id.tv_songlist_cat);
            imageView = view.findViewById(R.id.iv_songlist);
            imageView_option = view.findViewById(R.id.iv_songlist_option);
            ratingBar = view.findViewById(R.id.rb_songlist);
            iv_downlaod_icon = view.findViewById(R.id.iv_downlaod_icon);

            if (!Constant.isSongDownload) {
                tv_download.setVisibility(View.GONE);
                iv_downlaod_icon.setVisibility(View.GONE);
            }
        }
    }

    private static class ADViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_native_ad;

        private ADViewHolder(View view) {
            super(view);
            rl_native_ad = view.findViewById(R.id.rl_native_ad);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_PROG) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        } else if (viewType >= 1000) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ads, parent, false);
            return new ADViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recent_songs, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            ((MyViewHolder) holder).tv_views.setText(methods.format(Double.parseDouble(arrayList.get(position).getViews())));
            ((MyViewHolder) holder).tv_download.setText(methods.format(Double.parseDouble(arrayList.get(position).getDownloads())));

            ((MyViewHolder) holder).textView_song.setText(arrayList.get(position).getTitle());
            ((MyViewHolder) holder).textView_duration.setText(arrayList.get(position).getDuration());
            Picasso.get()
                    .load(arrayList.get(position).getImageSmall())
                    .placeholder(R.drawable.placeholder_song)
                    .into(((MyViewHolder) holder).imageView);

            ((MyViewHolder) holder).tv_avg_rate.setTypeface(((MyViewHolder) holder).tv_avg_rate.getTypeface(), Typeface.BOLD);
            ((MyViewHolder) holder).tv_avg_rate.setText(arrayList.get(position).getAverageRating());
            ((MyViewHolder) holder).ratingBar.setRating(Float.parseFloat(arrayList.get(position).getAverageRating()));

            if (PlayerService.getIsPlayling() && Constant.playPos <= holder.getAdapterPosition() && Constant.arrayList_play.get(Constant.playPos).getId().equals(arrayList.get(position).getId())) {
                ((MyViewHolder) holder).imageView.setVisibility(View.GONE);
                ((MyViewHolder) holder).equalizer.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).equalizer.animateBars();
            } else {
                ((MyViewHolder) holder).imageView.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).equalizer.setVisibility(View.GONE);
                ((MyViewHolder) holder).equalizer.stopBars();
            }

            if (arrayList.get(position).getCatName() != null && !arrayList.get(position).getCatName().equals("")) {
                ((MyViewHolder) holder).textView_catname.setText(arrayList.get(position).getCatName());
            } else {
                ((MyViewHolder) holder).textView_catname.setText(arrayList.get(position).getArtist());
            }

            ((MyViewHolder) holder).rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        recyclerClickListener.onClick(holder.getAdapterPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            ((MyViewHolder) holder).imageView_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        openOptionPopUp(((MyViewHolder) holder).imageView_option, holder.getAdapterPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (holder instanceof ADViewHolder) {
            if (isAdLoaded) {
                if (((ADViewHolder) holder).rl_native_ad.getChildCount() == 0) {
                    if (Constant.natveAdType.equals("admob")) {
                        if (mNativeAdsAdmob.size() >= 1) {

                            int i;
                            if (mNativeAdsAdmob.size() > 1) {
                                i = new Random().nextInt(mNativeAdsAdmob.size() - 1);
                            } else {
                                i = 0;
                            }

//                            CardView cardView = (CardView) ((Activity) context).getLayoutInflater().inflate(R.layout.layout_native_ad_admob, null);

                            NativeAdView adView = (NativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.layout_native_ad_admob, null);
                            populateUnifiedNativeAdView(mNativeAdsAdmob.get(i), adView);
                            ((ADViewHolder) holder).rl_native_ad.removeAllViews();
                            ((ADViewHolder) holder).rl_native_ad.addView(adView);

                            ((ADViewHolder) holder).rl_native_ad.setVisibility(View.VISIBLE);
                        }
                    } else {
                        NativeAdLayout fb_native_container = (NativeAdLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.layout_native_ad_fb, null);

                        NativeAd ad;
                        if (mNativeAdsFB.size() >= 5) {
                            ad = mNativeAdsFB.get(new Random().nextInt(5));
                        } else {
                            ad = mNativeAdsManager.nextNativeAd();
                            mNativeAdsFB.add(ad);
                        }

                        LinearLayout adChoicesContainer = fb_native_container.findViewById(R.id.ad_choices_container);
                        AdOptionsView adOptionsView = new AdOptionsView(context, ad, fb_native_container);
                        adChoicesContainer.removeAllViews();
                        adChoicesContainer.addView(adOptionsView, 0);

                        // Create native UI using the ad metadata.
                        com.facebook.ads.MediaView nativeAdIcon = fb_native_container.findViewById(R.id.native_ad_icon);
                        TextView nativeAdTitle = fb_native_container.findViewById(R.id.native_ad_title);
                        com.facebook.ads.MediaView nativeAdMedia = fb_native_container.findViewById(R.id.native_ad_media);
                        TextView nativeAdSocialContext = fb_native_container.findViewById(R.id.native_ad_social_context);
                        TextView nativeAdBody = fb_native_container.findViewById(R.id.native_ad_body);
                        TextView sponsoredLabel = fb_native_container.findViewById(R.id.native_ad_sponsored_label);
                        Button nativeAdCallToAction = fb_native_container.findViewById(R.id.native_ad_call_to_action);

                        // Set the Text.
                        nativeAdTitle.setText(ad.getAdvertiserName());
                        nativeAdBody.setText(ad.getAdBodyText());
                        nativeAdSocialContext.setText(ad.getAdSocialContext());
                        nativeAdCallToAction.setVisibility(ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                        nativeAdCallToAction.setText(ad.getAdCallToAction());
                        sponsoredLabel.setText(ad.getSponsoredTranslation());

                        // Create a list of clickable views
                        List<View> clickableViews = new ArrayList<>();
                        clickableViews.add(nativeAdTitle);
                        clickableViews.add(nativeAdCallToAction);

                        // Register the Title and CTA button to listen for clicks.
                        ad.registerViewForInteraction(fb_native_container, nativeAdMedia, nativeAdIcon, clickableViews);

                        ((ADViewHolder) holder).rl_native_ad.addView(fb_native_container);

                        ((ADViewHolder) holder).rl_native_ad.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            try {
                if (getItemCount() < 10) {
                    ProgressViewHolder.progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    public boolean isHeader(int position) {
        return position == arrayList.size();
    }

    public void hideHeader() {
        try {
            ProgressViewHolder.progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return VIEW_PROG;
        } else if (arrayList.get(position) == null) {
            return 1000 + position;
        } else {
            return position;
        }
    }

    public void addAds(com.google.android.gms.ads.nativead.NativeAd unifiedNativeAd) {
        mNativeAdsAdmob.add(unifiedNativeAd);
        isAdLoaded = true;
    }

    public void setFBNativeAdManager(NativeAdsManager mNativeAdsManager) {
        this.mNativeAdsManager = mNativeAdsManager;
        isAdLoaded = true;
    }

    private void populateUnifiedNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);
    }

    public void destroyNativeAds() {
        try {
            for (int i = 0; i < mNativeAdsAdmob.size(); i++) {
                mNativeAdsAdmob.get(i).destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRealPos(int pos, ArrayList<ItemSong> arrayListTemp) {
        return arrayListTemp.indexOf(arrayList.get(pos));
    }

    private void openOptionPopUp(ImageView imageView, final int pos) {
        PopupMenu popup = new PopupMenu(context, imageView);
        popup.getMenuInflater().inflate(R.menu.popup_song, popup.getMenu());
        if (type.equals("playlist")) {
            popup.getMenu().findItem(R.id.popup_add_song).setTitle(context.getString(R.string.remove));
        }
        if (!Constant.isOnline) {
            popup.getMenu().findItem(R.id.popup_add_queue).setVisible(false);
        }
        if (!methods.isYoutubeAppInstalled()) {
            popup.getMenu().findItem(R.id.popup_youtube).setVisible(false);
        }
        if (!Constant.isSongDownload) {
            popup.getMenu().findItem(R.id.popup_download).setVisible(false);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_add_song:
                        switch (type) {
                            case "playlist":
                                dbHelper.removeFromPlayList(arrayList.get(pos).getId(), true);
                                arrayList.remove(pos);
                                notifyItemRemoved(pos);
                                Toast.makeText(context, context.getString(R.string.remove_from_playlist), Toast.LENGTH_SHORT).show();
                                if (arrayList.size() == 0) {
                                    recyclerClickListener.onItemZero();
                                }
                                break;
                            default:
                                methods.openPlaylists(arrayList.get(pos), true);
                                break;
                        }
                        break;
                    case R.id.popup_add_queue:
                        Constant.arrayList_play.add(arrayList.get(pos));
                        GlobalBus.getBus().postSticky(new ItemMyPlayList("", "", null));
                        Toast.makeText(context, context.getString(R.string.add_to_queue), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.popup_youtube:
                        Intent intent = new Intent(Intent.ACTION_SEARCH);
                        intent.setPackage("com.google.android.youtube");
                        intent.putExtra("query", arrayList.get(pos).getTitle());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case R.id.popup_share:
                        methods.shareSong(arrayList.get(pos), true);
                        break;
                    case R.id.popup_download:
                        methods.download(arrayList.get(pos));
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public Filter getFilter() {
        if (filter == null) {
            filter = new NameFilter();
        }
        return filter;
    }

    private class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<ItemSong> filteredItems = new ArrayList<>();

                for (int i = 0, l = filteredArrayList.size(); i < l; i++) {
                    if(filteredArrayList.get(i) != null) {
                        String nameList = filteredArrayList.get(i).getTitle();
                        if (nameList.toLowerCase().contains(constraint))
                            filteredItems.add(filteredArrayList.get(i));
                    }
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = filteredArrayList;
                    result.count = filteredArrayList.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            arrayList = (ArrayList<ItemSong>) results.values;
            notifyDataSetChanged();
        }
    }
}