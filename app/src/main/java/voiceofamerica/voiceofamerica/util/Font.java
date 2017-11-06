package voiceofamerica.voiceofamerica.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by mwongela on 5/12/16.
 */
public class Font {
    public static final Font  RALEWAY_LIGHT    = new Font("fonts/Raleway-Light.ttf");
    public static final Font  RALEWAY_MEDIUM = new Font("fonts/Raleway-Medium.ttf");
    private final String      assetName;
    private volatile Typeface typeface;

    private Font(String assetName) {
        this.assetName = assetName;
    }

    public void apply(Context context, TextView textView) {
        if (typeface == null) {
            synchronized (this) {
                if (typeface == null) {
                    typeface = Typeface.createFromAsset(context.getAssets(), assetName);
                }
            }
        }
        textView.setTypeface(typeface);
    }
}
