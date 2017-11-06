package voiceofamerica.voiceofamerica;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
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
public class MainActivity extends AppCompatActivity {

    private final static int FAVORITE = 11;

    private DbHelper db;
    private ListView listView;
    private CustomCursorAdapter customAdapter;
    private EditText inputSearch;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_to_left_out_cur, R.anim.slide_to_left_cur_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_mainAcitivity));
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

        title = (TextView)findViewById(R.id.title);
        more = findViewById(R.id.more);
        playStop = findViewById(R.id.playStop);
        tvWebradioName = (TextView) findViewById(R.id.tvWebradioName);
        Font.RALEWAY_MEDIUM.apply(this, tvWebradioName);

        listView = (ListView) findViewById(R.id.webradioList);
        listView.setTextFilterEnabled(true);
        customAdapter = new CustomCursorAdapter(MainActivity.this, db.getAllWebradios());
        listView.setAdapter(customAdapter);
        customAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                String strItemCode = constraint.toString();
                return db.getAllWebradios(strItemCode);
            }
        });

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
                }else{
                    if (connection.isConnectingToInternet()) {
                        SharedPreferences settings = getSharedPreferences(getString(R.string.prefs), 0);
                        RadioName = settings.getString(getString(R.string.prefs_radio_name), null);
                        if (RadioName != null && !RadioName.isEmpty()) {
                            ctx.startService(new Intent(ctx, BackgroundMusicService.class));
                            ((ImageButton) playStop).setImageResource(R.mipmap.ic_action_pause_over_video);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.no_radio, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        showNetDialog(getText(R.string.connection_failed).toString());
                    }
                }
            }
        });

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");
        inputSearch.setTypeface(tf);
        inputSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable arg0) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                customAdapter.getFilter().filter(cs.toString());
                customAdapter.notifyDataSetChanged();
            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setStation();
        updatePlay();
        checkNetworkConn();
    }

    private void updatePlay(){
        if (isMyServiceRunning(BackgroundMusicService.class)) {
            ((ImageButton) playStop).setImageResource(R.mipmap.ic_action_pause_over_video);
        }else
            ((ImageButton) playStop).setImageResource(R.mipmap.ic_play_arrow_black_48dp);
    }

    private void setStation(){
        SharedPreferences settings = getSharedPreferences(getString(R.string.prefs), 0);
        RadioName  = settings.getString(getString(R.string.prefs_radio_name), null);
        if(RadioName != null && !RadioName.isEmpty()){
            tvWebradioName.setText(RadioName);
        }else{
            tvWebradioName.setText(R.string.no_radio);
        }
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
            setTimer();
        }else {
            showNetDialog(getText(R.string.connection_failed).toString());
        }
    }

    private void setTimer(){
        title.setText("");
        if(tiTimer!=null) tiTimer.cancel();
        tiTimer = new Timer();
        tiTimer.scheduleAtFixedRate(new TitleTimer(),0, 1000*20);
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
                }catch (Exception e){}
            }
        }.start();
    }

    private void showNetDialog(String message){
        final Dialog dialog = new Dialog(MainActivity.this);
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
                    startActivityForResult(new Intent(getApplicationContext(), FavoriteActivity.class), FAVORITE);
                }

//                @Override
//                public void onTimer() {
//                    Toast.makeText(getApplicationContext(), "timer", Toast.LENGTH_SHORT).show();
//                }

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
    public void onActivityResult(int req, int res, Intent data){
        switch (req){
            case FAVORITE:
                customAdapter.swapCursor(db.getAllWebradios());
                setStation();
                if(data!=null && data.hasExtra(FavoriteActivity.RADIO_URL))
                    RadioURL = data.getStringExtra(FavoriteActivity.RADIO_URL);
                setTimer();
                updatePlay();
                break;
        }
        super.onActivityResult(req, res, data);
    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //==============================================
    class TitleTimer extends TimerTask{

        //===========================
        @Override
        public void run() {
            getSongName();
        }
    }



//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        MainActivity.this.finish();
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }


}
