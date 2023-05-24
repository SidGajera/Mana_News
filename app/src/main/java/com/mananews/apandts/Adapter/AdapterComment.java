package com.mananews.apandts.Adapter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mananews.apandts.Activity.CommentActivity;
import com.mananews.apandts.Activity.MainActivity;
import com.mananews.apandts.Model_Class.ModelComment;
import com.mananews.apandts.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder> {

    Activity activity;
    LayoutInflater inflater;
    ArrayList<ModelComment> commentList;

     private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int totalItemCount;
    private int pastVisibleItems;
    private int lastVisibleItem;
    private static final int TYPE_AD = 1;
    private static final int TYPE_ITEM = 2;
    public static final int ITEMS_PER_AD = 6;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void addItem(ArrayList<ModelComment> item, int position) {
        if (item.size() != 0) {
            commentList.addAll(item);
            notifyItemInserted(position);
        }
    }

    public AdapterComment(RecyclerView recycleComment, CommentActivity commentActivity, ArrayList<ModelComment> commentList) {
        activity = commentActivity;
        this.commentList = commentList;
        inflater = LayoutInflater.from(commentActivity);

        recycleComment.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert linearLayoutManager != null;
                visibleThreshold = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                final int lastItem = lastVisibleItem + visibleThreshold;
//                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                if (totalItemCount >= CommentActivity.noOfrecords) {
                    if (!isLoading && totalItemCount <= lastItem) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cell_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imgPath = activity.getString(R.string.server_url) + "uploads/user_image/";
        holder.txtCommentDesc.setText(commentList.get(position).getComment());
        holder.txtCommentTime.setText(commentList.get(position).getComment_time());
        holder.txtUserName.setText(commentList.get(position).getUsername());

        String userName = holder.txtUserName.getText().toString();
        String strFirstChar = userName.substring(0, 1).toUpperCase();
        try {
            if (commentList.get(position).getProfile().toString().equals("")) {
                holder.imgUserProfile.setVisibility(View.GONE);
                holder.txtImg.setVisibility(View.VISIBLE);
                holder.txtImg.setText(strFirstChar);
            } else {
                holder.imgUserProfile.setVisibility(View.VISIBLE);
                holder.txtImg.setVisibility(View.GONE);
                Glide.with(activity).load(imgPath + commentList.get(position).getProfile()).into(holder.imgUserProfile);
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "onBindViewHolder: " + e.getMessage());
        }

        Animation animFadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        holder.imgUserProfile.startAnimation(animFadeIn);

        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                holder.txtCommentDesc.setTextColor(activity.getResources().getColor(R.color.darkDray));
                holder.txtCommentTime.setTextColor(activity.getResources().getColor(R.color.darkDray));
                holder.txtUserName.setTextColor(activity.getResources().getColor(R.color.darkDray));
                holder.txtImg.setBackgroundResource(R.drawable.rounded_corner_light);
                holder.txtImg.setTextColor(activity.getResources().getColor(R.color.white));
            } else if (MainActivity.themeKEY.equals("0")) {
                holder.txtCommentDesc.setTextColor(activity.getResources().getColor(R.color.white));
                holder.txtCommentTime.setTextColor(activity.getResources().getColor(R.color.white));
                holder.txtUserName.setTextColor(activity.getResources().getColor(R.color.yellow));
                holder.txtImg.setBackgroundResource(R.drawable.rounded_corner);
                holder.txtImg.setTextColor(activity.getResources().getColor(R.color.darkDray));
            }
        } else {
            holder.txtCommentDesc.setTextColor(activity.getResources().getColor(R.color.white));
            holder.txtCommentTime.setTextColor(activity.getResources().getColor(R.color.white));
            holder.txtUserName.setTextColor(activity.getResources().getColor(R.color.yellow));
            holder.txtImg.setBackgroundResource(R.drawable.rounded_corner);
            holder.txtImg.setTextColor(activity.getResources().getColor(R.color.darkDray));
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCommentDesc, txtUserName, txtCommentTime, txtImg;
        //        ImageView imgUserProfile;
        CircleImageView imgUserProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCommentDesc = itemView.findViewById(R.id.txtCommentDesc);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtCommentTime = itemView.findViewById(R.id.txtCommentTime);
            txtImg = itemView.findViewById(R.id.txtImg);
            imgUserProfile = itemView.findViewById(R.id.imgUserProfile);
        }
    }
}
