package com.musicentertainment.countrymusic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.musicentertainment.asyncTask.LoadAbout;
import com.musicentertainment.fragment.FragmentAlbums;
import com.musicentertainment.fragment.FragmentArtist;
import com.musicentertainment.fragment.FragmentCountries;
import com.musicentertainment.fragment.FragmentDashBoard;
import com.musicentertainment.fragment.FragmentDownloads;
import com.musicentertainment.fragment.FragmentFav;
import com.musicentertainment.fragment.FragmentGenres;
import com.musicentertainment.fragment.FragmentMyPlaylist;
import com.musicentertainment.fragment.FragmentServerPlaylist;
import com.musicentertainment.fragment.FragmentSongs;
import com.musicentertainment.fragment.FragmentTrendingSongs;
import com.musicentertainment.interfaces.AboutListener;
import com.musicentertainment.interfaces.AdConsentListener;
import com.musicentertainment.utils.AdConsent;
import com.musicentertainment.utils.Constant;
import com.musicentertainment.utils.Methods;
import com.google.android.material.navigation.NavigationView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.EventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, EventListener {

    Methods methods;
    FragmentManager fm;
    String selectedFragment = "";
    AdConsent adConsent;
    MenuItem menu_login, menu_prof, menu_suggest;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_main, contentFrameLayout);

        //        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    getPackageName(),
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        Menu menu = navigationView.getMenu();
        menu_login = menu.findItem(R.id.nav_login);
        menu_prof = menu.findItem(R.id.nav_profile);
        menu_suggest = menu.findItem(R.id.nav_suggest);

        changeLoginName();

        Constant.isAppOpen = true;
        methods = new Methods(this);
        methods.forceRTLIfSupported(getWindow());

        fm = getSupportFragmentManager();

        navigationView.setNavigationItemSelectedListener(this);

        adConsent = new AdConsent(this, new AdConsentListener() {
            @Override
            public void onConsentUpdate() {
                methods.showSMARTBannerAd(ll_adView_base);
            }
        });


        if (methods.isNetworkAvailable()) {
            loadAboutData();
        } else {
            if (!isFinishing()) {
                adConsent.checkForConsent();
            }
            dbHelper.getAbout();
        }

        loadDashboardFrag();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                FragmentDashBoard f_home = new FragmentDashBoard();
                loadFrag(f_home, getString(R.string.dashboard), fm);
                break;
            case R.id.nav_albums:
                FragmentAlbums f_album = new FragmentAlbums();
                loadFrag(f_album, getString(R.string.albums), fm);
                break;
            case R.id.nav_artist:
                FragmentArtist f_art = new FragmentArtist();
                loadFrag(f_art, getString(R.string.artist), fm);
                break;
            case R.id.nav_genres:
                FragmentGenres f_genres = new FragmentGenres();
                loadFrag(f_genres, getString(R.string.genres), fm);
                break;
            case R.id.nav_country:
                FragmentCountries f_country = new FragmentCountries();
                loadFrag(f_country, getString(R.string.countries), fm);
                break;
            case R.id.nav_allsongs:
                FragmentSongs f_all_songs = new FragmentSongs();
                loadFrag(f_all_songs, getString(R.string.all_songs), fm);
                break;
            case R.id.nav_trending:
                FragmentTrendingSongs f_trend = new FragmentTrendingSongs();
                loadFrag(f_trend, getString(R.string.trending_songs), fm);
                break;
            case R.id.nav_playlist:
                FragmentServerPlaylist f_server_playlist = new FragmentServerPlaylist();
                loadFrag(f_server_playlist, getString(R.string.playlist), fm);
                break;
            case R.id.nav_myplaylist:
                FragmentMyPlaylist f_myplay = new FragmentMyPlaylist();
                loadFrag(f_myplay, getString(R.string.myplaylist), fm);
                break;
            case R.id.nav_music_library:
                Intent intent_music_lib = new Intent(MainActivity.this, OfflineMusicActivity.class);
                startActivity(intent_music_lib);
                break;
            case R.id.nav_downloads:
                if (checkPerDownload()) {
                    FragmentDownloads f_download = new FragmentDownloads();
                    loadFrag(f_download, getString(R.string.downloads), fm);
                }
                break;
            case R.id.nav_favourite:
                FragmentFav f_fav = new FragmentFav();
                loadFrag(f_fav, getString(R.string.favourite), fm);
                break;
            case R.id.nav_settings:
                Intent intent_settings = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent_settings);
                break;
            case R.id.nav_suggest:
                Intent intent_sugg = new Intent(MainActivity.this, SuggestionActivity.class);
                startActivity(intent_sugg);
                break;
            case R.id.nav_profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_login:
                methods.clickLogin();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadDashboardFrag() {
        FragmentDashBoard f1 = new FragmentDashBoard();
        loadFrag(f1, getResources().getString(R.string.dashboard), fm);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        selectedFragment = name;
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStackImmediate();
        }

        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (!name.equals(getString(R.string.dashboard))) {
            ft.hide(fm.getFragments().get(fm.getBackStackEntryCount()));
            ft.add(R.id.fragment, f1, name);
            ft.addToBackStack(name);
        } else {
            ft.replace(R.id.fragment, f1, name);
        }
        ft.commitAllowingStateLoss();

        getSupportActionBar().setTitle(name);

        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    private void exitDialog() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(MainActivity.this, R.style.ThemeDialog);
        } else {
            alert = new AlertDialog.Builder(MainActivity.this);
        }

        alert.setTitle(getString(R.string.exit));
        alert.setMessage(getString(R.string.sure_exit));
        alert.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    public void loadAboutData() {
        LoadAbout loadAbout = new LoadAbout(MainActivity.this, new AboutListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onEnd(String success, String verifyStatus, String message) {
                if (!verifyStatus.equals("-1") && !verifyStatus.equals("-2")) {
                    String version = "";
                    try {
                        PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        version = String.valueOf(pInfo.versionCode);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (Constant.showUpdateDialog && !Constant.appVersion.equals(version)) {
                        methods.showUpdateAlert(Constant.appUpdateMsg);
                    } else {
                        adConsent.checkForConsent();
                        dbHelper.addtoAbout();
                    }
                } else if (verifyStatus.equals("-2")) {
                    methods.getInvalidUserDialog(message);
                } else {
                    methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                }
            }
        });
        loadAbout.execute();
    }

    private void changeLoginName() {
        if (menu_login != null) {
            if (Constant.isLogged) {
                menu_prof.setVisible(true);
                menu_login.setTitle(getResources().getString(R.string.logout));
                menu_login.setIcon(getResources().getDrawable(R.mipmap.logout));
            } else {
                menu_prof.setVisible(false);
                menu_login.setTitle(getResources().getString(R.string.login));
                menu_login.setIcon(getResources().getDrawable(R.mipmap.login));
            }
        }
    }

    @Override
    protected void onDestroy() {
        try {
            Constant.isAppOpenAdShown = false;
            Constant.isAppOpen = false;
            if (PlayerService.exoPlayer != null && !PlayerService.exoPlayer.getPlayWhenReady()) {
                Intent intent = new Intent(getApplicationContext(), PlayerService.class);
                intent.setAction(PlayerService.ACTION_STOP);
                startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (dialog_desc != null && dialog_desc.isShowing()) {
            dialog_desc.dismiss();
        } else if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (fm.getBackStackEntryCount() != 0) {
            String title = fm.getFragments().get(fm.getBackStackEntryCount()).getTag();
            if (title.equals(getString(R.string.dashboard)) || title.equals(getString(R.string.home)) || title.equals(getString(R.string.categories)) || title.equals(getString(R.string.latest))) {
//                title = getString(R.string.home);
                navigationView.setCheckedItem(R.id.nav_home);
            }
            getSupportActionBar().setTitle(title);
            super.onBackPressed();
        } else {
            exitDialog();
        }
    }

//    public Boolean checkPer() {
//        if ((ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)) {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE"}, 1);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean canUseExternalStorage = false;

        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                canUseExternalStorage = true;
                FragmentDownloads f_download = new FragmentDownloads();
                loadFrag(f_download, getString(R.string.downloads), fm);
            }

            if (!canUseExternalStorage) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.err_cannot_use_features), Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        changeLoginName();
        super.onResume();
    }
}