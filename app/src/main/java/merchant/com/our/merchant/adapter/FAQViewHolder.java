package merchant.com.our.merchant.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import merchant.com.our.merchant.R;


class FAQViewHolder extends ViewHolder {
    public TextView answerTextView ;
    public TextView faqTitle;
    public FrameLayout frameLayoutButtons ;
    public ImageButton imageButtonNotVisible;
    public ImageButton imageButtonVisible;
    public LinearLayout parentHolder;

    public FAQViewHolder(View view) {
        super(view);
        answerTextView = view.findViewById(R.id.textViewAnswer);
        faqTitle = view.findViewById(R.id.faqTitle);
        frameLayoutButtons = view.findViewById(R.id.frameExpand);
        imageButtonNotVisible = view.findViewById(R.id.noDetails);
        imageButtonVisible = view.findViewById(R.id.moreDetails);
        parentHolder = view.findViewById(R.id.parentView);
    }
}
