package voiceofamerica.voiceofamerica;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mwongela on 5/12/16.
 */
public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000-1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        TextView logoTxt = (TextView) findViewById(R.id.logoTxt);

        ImageView logo = (ImageView) findViewById(R.id.imgLogo);
        logo.getDrawable().setAlpha(200);

        logo.setScaleX(0f);
        logo.setScaleY(0f);

        AnimatorSet as = new AnimatorSet();
        as.playTogether(ObjectAnimator.ofFloat(logo, "scaleX", 1f), ObjectAnimator.ofFloat(logo, "scaleY", 1f));
        as.setStartDelay(400);
        as.start();

        View text = findViewById(R.id.logoTxt);
        TranslateAnimation ta = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 1, TranslateAnimation.RELATIVE_TO_PARENT, 0, TranslateAnimation.RELATIVE_TO_PARENT, 0, TranslateAnimation.RELATIVE_TO_PARENT, 0);
        ta.setDuration(800);
        ta.setStartOffset(1000);
        text.startAnimation(ta);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
