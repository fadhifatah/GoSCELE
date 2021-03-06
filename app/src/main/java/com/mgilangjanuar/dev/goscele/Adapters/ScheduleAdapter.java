package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.CourseDetailActivity;
import com.mgilangjanuar.dev.goscele.Helpers.WebViewContentHelper;
import com.mgilangjanuar.dev.goscele.InAppBrowserActivity;
import com.mgilangjanuar.dev.goscele.Models.ScheduleModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by muhammadgilangjanuar on 5/17/17.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private Context context;
    private List<ScheduleModel> list;

    public ScheduleAdapter(Context context, List<ScheduleModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(final ScheduleViewHolder holder, int position) {
        final ScheduleModel scheduleModel = list.get(position);
        holder.title.setText(scheduleModel.title);
        holder.subTitle.setText(scheduleModel.courseModel.name);
        holder.time.setText(scheduleModel.time);

        if (scheduleModel.description.equals("")) {
            holder.description.setVisibility(TextView.GONE);
        } else {
            holder.description.setVisibility(TextView.VISIBLE);
            WebViewContentHelper.setWebView(holder.description, scheduleModel.description);
        }

        holder.layout.setOnClickListener(v -> context.startActivity((new Intent(context, InAppBrowserActivity.class)).putExtra("url", scheduleModel.url)));

        holder.share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, scheduleModel.courseModel.name + "\n\n" + scheduleModel.title + "\n" + "(" + scheduleModel.date + " " + scheduleModel.time + ")" + "\n\n" + scheduleModel.url);
            holder.itemView.getContext().startActivity(Intent.createChooser(shareIntent, "Share"));
        });

        holder.course.setOnClickListener(v -> context.startActivity((new Intent(context, CourseDetailActivity.class)).putExtra("url", scheduleModel.courseModel.url)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_schedule)
        public TextView title;
        @BindView(R.id.subtitle_schedule)
        public TextView subTitle;
        @BindView(R.id.description_schedule)
        public WebView description;
        @BindView(R.id.time_schedule)
        public TextView time;
        @BindView(R.id.button_share_schedule)
        public Button share;
        @BindView(R.id.button_view_course_in_schedule)
        public Button course;
        @BindView(R.id.title_card)
        public LinearLayout layout;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
