package voiceofamerica.voiceofamerica.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import voiceofamerica.voiceofamerica.util.DbHelper;
import voiceofamerica.voiceofamerica.util.Font;

import java.io.InputStream;

/**
 * Created by mwongela on 5/12/16.
 */
public class CustomCursorAdapter extends CursorAdapter {

    private Context ctx;
    ImageView imageFav;
    private DbHelper db;

    @SuppressWarnings("deprecation")
	public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
        ctx = context;
        db = new DbHelper(ctx);
    }
 
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(voiceofamerica.voiceofamerica.R.layout.webradio_item, parent, false);
        return retView;
    }
 
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
 
        TextView tvWebradioName = (TextView) view.findViewById(voiceofamerica.voiceofamerica.R.id.tvWebradioName);
        String radio = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
        tvWebradioName.setText(radio);
        Font.RALEWAY_MEDIUM.apply(context, tvWebradioName);

        TextView tvGenre = (TextView) view.findViewById(voiceofamerica.voiceofamerica.R.id.tvGenre);
        tvGenre.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
        Font.RALEWAY_LIGHT.apply(context, tvGenre);

        imageFav = (ImageView) view.findViewById(voiceofamerica.voiceofamerica.R.id.imageFav);

        int radioFavStatus = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(4)));
        if(radioFavStatus==0){
            imageFav.setImageDrawable(ActivityCompat.getDrawable(context, voiceofamerica.voiceofamerica.R.mipmap.heart_outline_white));
        }
        if(radioFavStatus==1){
            imageFav.setImageDrawable(ActivityCompat.getDrawable(context, voiceofamerica.voiceofamerica.R.mipmap.heart_red_solid));
        }

        ImageView icon = (ImageView)view.findViewById(voiceofamerica.voiceofamerica.R.id.icon);
        setImage(radio, icon);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = super.getView(position, convertView, parent);
        final ImageView imageFav = (ImageView)  v.findViewById(voiceofamerica.voiceofamerica.R.id.imageFav);
        final TextView tvWebradioName = (TextView)  v.findViewById(voiceofamerica.voiceofamerica.R.id.tvWebradioName);

        final String radioName = tvWebradioName.getText().toString();

        final int radioId = db.getRadioId(radioName);
        final int radioFavStatus = db.getRadioFavStatus(Integer.toString(radioId));

        if(radioFavStatus==0){
            imageFav.setImageDrawable(ActivityCompat.getDrawable(ctx, voiceofamerica.voiceofamerica.R.mipmap.heart_outline_white));
        }
        if(radioFavStatus==1){
            imageFav.setImageDrawable(ActivityCompat.getDrawable(ctx, voiceofamerica.voiceofamerica.R.mipmap.heart_red_solid));
        }

        imageFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ObjectAnimator scdx = ObjectAnimator.ofFloat(v, "scaleX", 0).setDuration(300);
                ObjectAnimator scux = ObjectAnimator.ofFloat(v, "scaleX", 1).setDuration(300);
                scdx.start();
                scux.setStartDelay(300);
                scux.start();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       updateFav(radioId, (ImageView)v);
                    }
                },300);
            }
        });

        ImageView icon = (ImageView)v.findViewById(voiceofamerica.voiceofamerica.R.id.icon);
        setImage(radioName, icon);

        return(v);
    }

    //===========================
    private void updateFav(int radioId, ImageView iv){
        final int radioFavStatus = db.getRadioFavStatus(Integer.toString(radioId));
        if(radioFavStatus==0){
            iv.setImageDrawable(ActivityCompat.getDrawable(ctx, voiceofamerica.voiceofamerica.R.mipmap.heart_red_solid));
            db.updateRadioFavStatus(Integer.toString(radioId), Integer.toString(1));
        }
        if(radioFavStatus==1){
            iv.setImageDrawable(ActivityCompat.getDrawable(ctx, voiceofamerica.voiceofamerica.R.mipmap.heart_outline_white));
            db.updateRadioFavStatus(Integer.toString(radioId), Integer.toString(0));
        }
    }

    //===========================
    private void setImage(String radio, ImageView img){
        try{
            radio = radio.toLowerCase().replaceAll("[ ]","_");
            int l = radio.length();
            String tRadio = "";
            for(int i=0;i<l; i++){
                char c = radio.charAt(i);
                if((c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9') || c=='.' || c=='_')
                    tRadio += c;
                else
                    tRadio +="_";
            }
            InputStream is = ctx.getAssets().open("icon/"+tRadio+".png");
            Bitmap b = BitmapFactory.decodeStream(is);
            if(b==null)
                img.setImageResource(voiceofamerica.voiceofamerica.R.mipmap.ic_launcher);
            else
                img.setImageBitmap(b);
        }catch (Exception e){img.setImageResource(voiceofamerica.voiceofamerica.R.mipmap.ic_launcher);}
    }
}