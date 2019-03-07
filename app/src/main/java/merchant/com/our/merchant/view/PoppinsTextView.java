package merchant.com.our.merchant.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import merchant.com.our.merchant.R;


/**
 * PoppinsTextView by Ekerete, created on 29/01/2018
 **/
public class PoppinsTextView extends AppCompatTextView {

//	private static final String TAG = "EditText";

    private Typeface typeface;

    public PoppinsTextView(Context context) {
        super(context);
    }

    public PoppinsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public PoppinsTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.PoppinsTextView);
        String customFont = a.getString(R.styleable.PoppinsTextView_PoppinsTextView);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    private boolean setCustomFont(Context ctx, String asset) {
        try {
            if (typeface == null) {
                // Log.i(TAG, "asset:: " + "fonts/" + asset);
                typeface = Typeface.createFromAsset(ctx.getAssets(),
                        "Poppins-Regular.ttf");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }
}
