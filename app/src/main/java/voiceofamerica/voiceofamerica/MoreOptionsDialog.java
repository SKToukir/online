package voiceofamerica.voiceofamerica;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by vivek on 14/5/16.
 */
//==============================================
public class MoreOptionsDialog extends PopupWindow {

    private MoreOptionsListener list;

    //===========================
    public MoreOptionsDialog(Context ctx){
        super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View v = LayoutInflater.from(ctx).inflate(R.layout.more_options, null);
        setContentView(v);
        setAnimationStyle(R.style.MoreDialogAnim);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        View fav = v.findViewById(R.id.fav);
//        View timer = v.findViewById(R.id.timer);
//        View feedback = v.findViewById(R.id.feedback);
//        View review = v.findViewById(R.id.review);
        View share = v.findViewById(R.id.share);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list!=null) list.onFavorite();
            }
        });
//        timer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(list!=null) list.onTimer();
//            }
//        });
//        feedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(list!=null) list.onFeedback();
//            }
//        });
//        review.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(list!=null) list.onReview();
//            }
//        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list!=null) list.onShare();
            }
        });
    }

    //===========================
    public void setMoreOptionsListener(MoreOptionsListener list){this.list = list;}

    //==============================================
    public interface MoreOptionsListener{
        public void onFavorite();
//        public void onTimer();
//        public void onFeedback();
//        public void onReview();
        public void onShare();
    }
}
