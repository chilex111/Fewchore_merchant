package merchant.com.our.merchant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.List;

import merchant.com.our.merchant.R;
import merchant.com.our.merchant.model.Faq;


public class FAQAdapter extends Adapter<FAQViewHolder> {
    private List<Faq> FaqList;

    public FAQAdapter(Context context, List<Faq> FaqList) {
        this.FaqList = FaqList;
    }

    @NonNull
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_faq, parent, false);

        return new FAQViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final FAQViewHolder holder, int position) {
        final Faq Faqs = FaqList.get(position);
        holder.faqTitle.setText(Html.fromHtml(Faqs.getFaqQ()));
        Log.i("ANSW", "" + Faqs.getFaqA());
        holder.parentHolder.setOnClickListener(new OnClickListener() {
            private boolean isVisible = false;

            public void onClick(View view) {
                if (this.isVisible) {
                    holder.imageButtonVisible.setVisibility(View.GONE);
                    holder.imageButtonNotVisible.setVisibility(View.VISIBLE);
                    holder.answerTextView.setVisibility(View.GONE);
                    this.isVisible = false;
                    return;
                }
                holder.imageButtonNotVisible.setVisibility(View.GONE);
                holder.imageButtonVisible.setVisibility(View.VISIBLE);
                holder.answerTextView.setVisibility(View.VISIBLE);
                holder.answerTextView.setText(Faqs.getFaqA());
                this.isVisible = true;
            }
        });
    }

    public int getItemCount() {
        return this.FaqList.size();
    }
}
