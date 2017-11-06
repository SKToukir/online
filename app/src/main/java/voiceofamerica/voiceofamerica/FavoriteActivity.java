package voiceofamerica.voiceofamerica;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import voiceofamerica.voiceofamerica.adapter.CustomCursorAdapter;
import voiceofamerica.voiceofamerica.info.Tag;
import voiceofamerica.voiceofamerica.service.BackgroundMusicService;
import voiceofamerica.voiceofamerica.util.CheckInternet;
import voiceofamerica.voiceofamerica.util.DbHelper;
import voiceofamerica.voiceofamerica.util.Font;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mwongela on 5/12/16.
 */
public class FavoriteActivity extends AppCompatActivity {

    public final static String RADIO_NAME = "rname", RADIO_URL = "rurl";

    private DbHelper db;
    public ListView listView;
    public CustomCursorAdapter customAdapter;
    private AdView mAdView;
    private TextView tvWebradioName, title;
    private String RadioName;
    private String RadioURL;
    private Context ctx;
    private MoreOptionsDialog mod;
    private View more;
    private View playStop;
    private CheckInternet connection;
    private InterstitialAd mInterstitialAd;
    private Timer tiTimer; //=== Titlt timer
    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_to_left_out_cur, R.anim.slide_to_left_cur_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_favoriteActivity));
        AdRequest iAdRequest = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(iAdRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        connection = new CheckInternet(this);

        db = new DbHelper(this);

        ctx = this;

        msg = (TextView)findViewById(R.id.msg);
        title = (TextView)findViewById(R.id.title);
        more = findViewById(R.id.more);
        playStop = findViewById(R.id.playStop);
        tvWebradioName = (TextView) findViewById(R.id.tvWebradioName);
        Font.RALEWAY_MEDIUM.apply(this, tvWebradioName);

        SharedPreferences settings = getSharedPreferences(getString(R.string.prefs), 0);
        RadioName  = settings.getString(getString(R.string.prefs_radio_name), null);
        if(RadioName != null && !RadioName.isEmpty()){
            tvWebradioName.setText(RadioName);
        }else{
            tvWebradioName.setText(R.string.no_radio);
        }

        listView = (ListView) findViewById(R.id.webradioList);
        Cursor favCur = db.getFavoriteWebradios();
        if(!favCur.moveToFirst())
            msg.setText(R.string.no_fav);
        customAdapter = new CustomCursorAdapter(FavoriteActivity.this, favCur);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RadioName = ((TextView) view.findViewById(R.id.tvWebradioName)).getText().toString();
                RadioURL = db.getRadioURL(RadioName);
                tvWebradioName.setText(RadioName);

                SharedPreferences settings = getSharedPreferences(getString(R.string.prefs), 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(getString(R.string.prefs_radio_url), RadioURL);
                editor.putString(getString(R.string.prefs_radio_name), RadioName);
                editor.commit();

                playRadio();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOptions();
            }
        });

        playStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMyServiceRunning(BackgroundMusicService.class)) {
                    ctx.stopService(new Intent(ctx, BackgroundMusicService.class));
                    ((ImageButton) playStop).setImageResource(R.mipmap.ic_play_arrow_black_48dp);
                } else {
                    if (connection.isConnectingToInternet()) {
                        SharedPreferences settings = getSharedPreferences(getString(R.string.prefs), 0);
                        RadioName = settings.getString(getString(R.string.prefs_radio_name), null);
                        if (RadioName != null && !RadioName.isEmpty()) {
                            ctx.startService(new Intent(ctx, BackgroundMusicService.class));
                            ((ImageButton) playStop).setImageResource(R.mipmap.ic_action_pause_over_video);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.no_radio, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showNetDialog(getText(R.string.connection_failed).toString());
                    }
                }
            }
        });



        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (isMyServiceRunning(BackgroundMusicService.class)) {
            ((ImageButton) playStop).setImageResource(R.mipmap.ic_action_pause_over_video);
        }
        checkNetworkConn();
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void checkNetworkConn(){
        if(!connection.isConnectingToInternet()){
            new AlertDialog.Builder(this)
                    .setMessage(R.string.connect_to_internet)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }

    private void playRadio(){

        if (connection.isConnectingToInternet()) {

            if (isMyServiceRunning(BackgroundMusicService.class)) {
                ctx.stopService(new Intent(ctx, BackgroundMusicService.class));
            }
            Intent serviceIntent = new Intent(ctx, BackgroundMusicService.class);
            ctx.startService(serviceIntent);
            ((ImageButton) playStop).setImageResource(R.mipmap.ic_action_pause_over_video);
            title.setText("");
            if(tiTimer!=null) tiTimer.cancel();
            tiTimer = new Timer();
            tiTimer.scheduleAtFixedRate(new TitleTimer(),0, 1000*20);
        }else {
            showNetDialog(getText(R.string.connection_failed).toString());
        }
    }

    private void getSongName(){
        new Thread(){
            public void run(){
                try{
                    final Tag t = Tag.fetch(RadioURL);
                    if(title!=null) {
                        if (t != null) {
                            runOnUiThread(new Runnable() { public void run() { title.setText(t.getTitle()); }});
                        } else
                            runOnUiThread(new Runnable() { public void run() { title.setText(""); }});
                    }else
                        tiTimer.cancel();
                }catch (Exception e){e.printStackTrace();}
            }
        }.start();
    }

    private void showNetDialog(String message){
        final Dialog dialog = new Dialog(FavoriteActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_layout);

        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        Button btnRetry = (Button)dialog.findViewById(R.id.btnRetry);

        Button btnSetting= (Button)dialog.findViewById(R.id.btnSetting);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRadio();
                dialog.dismiss();
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void showMoreOptions(){
        if(mod==null){
            mod = new MoreOptionsDialog(this);
            mod.setMoreOptionsListener(new MoreOptionsDialog.MoreOptionsListener() {
                @Override
                public void onFavorite() {
                    mod.dismiss();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
//
//                @Override
//                public void onTimer() {
//                    Toast.makeText(getApplicationContext(), "timer", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFeedback() {
//                    Toast.makeText(getApplicationContext(), "feedback", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onReview() {
//                    Toast.makeText(getApplicationContext(), "review", Toast.LENGTH_SHORT).show();
//                }

                @Override
                public void onShare() {
                    mod.dismiss();
                    Intent sharingIntent = new Intent();
                    sharingIntent.setAction(Intent.ACTION_SEND);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getText(R.string.share_text_heading));
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getText(R.string.share_text_body));
                    sharingIntent.setType("text/plain");
                    startActivity(sharingIntent);
                }
            });
        }
        if(mod.isShowing())
            mod.dismiss();
        else
            mod.showAtLocation(more, Gravity.BOTTOM|Gravity.RIGHT, 0,(int)(2.5*more.getHeight()));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi){
        switch (mi.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(mi);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        overridePendingTransition(R.anim.slide_to_right_out_cur, R.anim.slide_to_right_cur_out);
        Intent data = new Intent();
        data.putExtra(RADIO_NAME, RadioName);
        data.putExtra(RADIO_URL, RadioURL);
        setResult(RESULT_OK, data);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        title = null;
        super.onDestroy();
    }


    //==============================================
    class TitleTimer extends TimerTask{

        //===========================
        @Override
        public void run() {
            getSongName();
        }
    }
}
