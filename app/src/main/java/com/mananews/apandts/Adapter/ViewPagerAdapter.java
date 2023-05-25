package com.mananews.apandts.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mananews.apandts.Activity.Detail_Activity;
import com.mananews.apandts.Activity.FullImageActivity;
import com.mananews.apandts.Activity.MainActivity;
import com.mananews.apandts.Activity.ReferenceActivity;
import com.mananews.apandts.Activity.VideoPlayer_Activity;
import com.mananews.apandts.BuildConfig;
import com.mananews.apandts.Model_Class.Model_News;
import com.mananews.apandts.R;
import com.mananews.apandts.utils.OnSwipeTouchListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    public static Activity activity;
    ArrayList<Model_News> newsArrayList;
    LayoutInflater inflater;
    private ViewPager2 viewPager2;
    private Bitmap bitmap__, wmBitmap;
    private Spanned Text;
    public static boolean isVisible = false;
    private Bitmap watermarkBitmap;
    //    private ImageView imgWaterMark;
    public static String TAG = ViewPagerAdapter.class.getName();

    public ViewPagerAdapter(FragmentActivity activity_, ArrayList<Model_News> imageList, ViewPager2 viewPager2_) {
        try {
            activity = activity_;
            this.newsArrayList = imageList;
            inflater = LayoutInflater.from(activity_);
            viewPager2 = viewPager2_;
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "ViewPagerAdapter: " + e.getMessage());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            final Model_News menuGetset = newsArrayList.get(position);
            String imagePath = activity.getString(R.string.server_url);

            holder.txt_short_desc.setText(Html.fromHtml("<b>"+newsArrayList.get(position).getShort_desc()+"<b>"));
            holder.txt_description.setText(Html.fromHtml(newsArrayList.get(position).getDescription()));
            String reference = menuGetset.getReference();
            String shortName = menuGetset.getShortname();

            Text = Html.fromHtml(menuGetset.getDescription());
            holder.txt_description.setMovementMethod(LinkMovementMethod.getInstance());
            holder.txt_description.setText(Text);
            if (newsArrayList.get(position).getShortname() != null &&
                    !newsArrayList.get(position).getShortname().equalsIgnoreCase("null")) {
                holder.txtRef.setText(newsArrayList.get(position).getShortname());
            } else {
                holder.txtRef.setText("");
            }

            holder.txtNewsDate.setText(newsArrayList.get(position).getNews_date());

            Animation animFadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
            holder.imageview.startAnimation(animFadeIn);


            if (menuGetset.getIs_image().equals("0") && menuGetset.getImage() != null) {

                Log.e(TAG, "ViewPagerAdapter: " + imagePath + "uploads/thumbnail/" + menuGetset.getImage());
                Log.e(TAG, "ViewPagerAdapter: getIs_image ==>" + menuGetset.getIs_image());

//                local video image
                try {
//                    Glide.with(activity).load(imagePath + menuGetset.getImage()).apply(new RequestOptions().placeholder(circularProgressDrawable)).into(holder.imageview);
                    Glide.with(activity).load(imagePath + "uploads/thumbnail/" + menuGetset.getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.imageview);
                    holder.img_play.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onBindViewHolder: " + e.getMessage());
                }
            }

            if (menuGetset.getIs_image().equals("1") && menuGetset.getImage() != null) {

                Log.e(TAG, "ViewPagerAdapter: " + imagePath + "uploads/thumbnail/" + menuGetset.getImage());
                Log.e(TAG, "ViewPagerAdapter: getIs_image ==>" + menuGetset.getIs_image());

//                local image
                try {
                    Glide.with(activity).load(imagePath + "uploads/thumbnail/" + menuGetset.getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.imageview);
                    holder.img_play.setVisibility(View.GONE);
                    holder.img_playurL.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onBindViewHolder: " + e.getMessage());
                }
            }
            if (menuGetset.getIs_image().equals("2") && menuGetset.getImage() != null) {

                Log.e(TAG, "ViewPagerAdapter: " + imagePath + "uploads/thumbnail/" + menuGetset.getImage());
                Log.e(TAG, "ViewPagerAdapter: getIs_image ==>" + menuGetset.getIs_image());

//                Youtube image
                try {
                    Glide.with(activity).load(imagePath + "uploads/thumbnail/" + menuGetset.getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.imageview);
                    holder.img_playurL.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onBindViewHolder: " + e.getMessage());
                }
            }

            holder.txt_short_desc2.setText(Html.fromHtml(newsArrayList.get(position).getShort_desc()));
            holder.txt_description2.setMovementMethod(LinkMovementMethod.getInstance());
            holder.txt_description2.setText(Text);
            if (newsArrayList.get(position).getShortname() != null &&
                    !newsArrayList.get(position).getShortname().equalsIgnoreCase("null")) {
                holder.txtRef2.setText(newsArrayList.get(position).getShortname());
            } else {
                holder.txtRef2.setText("");
            }

            holder.txtNewsDate2.setText(newsArrayList.get(position).getNews_date());

            if (newsArrayList.get(position).getShortname().isEmpty()) {
                holder.linRef.setEnabled(false);
            } else {
                holder.linRef.setEnabled(true);
            }

            if (menuGetset.getIs_image().equals("0") && menuGetset.getImage() != null) {

                Log.e(TAG, "ViewPagerAdapter: " + imagePath + "uploads/thumbnail/" + menuGetset.getImage());
                Log.e(TAG, "ViewPagerAdapter: getIs_image ==>" + menuGetset.getIs_image());

                try {
                    Glide.with(activity).load(imagePath + "uploads/thumbnail/" + menuGetset.getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.imageview2);
                    holder.img_play.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onBindViewHolder: " + e.getMessage());
                }
            }

            if (menuGetset.getIs_image().equals("1") && menuGetset.getImage() != null) {
                Log.e(TAG, "ViewPagerAdapter: " + imagePath + "uploads/thumbnail/" + menuGetset.getImage());
                Log.e(TAG, "ViewPagerAdapter: getIs_image ==>" + menuGetset.getIs_image());

                try {
                    Glide.with(activity).load(imagePath + "uploads/thumbnail/" + menuGetset.getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.imageview2);
                    Log.e(TAG, imagePath + newsArrayList.get(position).getThumbnail());
                    holder.img_play.setVisibility(View.GONE);
                    holder.img_playurL.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onBindViewHolder: " + e.getMessage());
                }
            }
            if (menuGetset.getIs_image().equals("2") && menuGetset.getImage() != null) {

                Log.e(TAG, "ViewPagerAdapter: " + imagePath + "uploads/thumbnail/" + menuGetset.getImage());
                Log.e(TAG, "ViewPagerAdapter: getIs_image ==>" + menuGetset.getIs_image());

                try {
                    Glide.with(activity).load(imagePath + "uploads/thumbnail/" + menuGetset.getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.imageview2);
                    holder.img_playurL.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "onBindViewHolder: " + e.getMessage());
                }
            }
            holder.img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(activity, VideoPlayer_Activity.class);
                    intent.putExtra("localVideo", newsArrayList.get(position).getMedia());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.bottom_up, R.anim.bottom_up);

                }
            });
            holder.img_playurL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(activity, VideoPlayer_Activity.class);
                    intent.putExtra("mediaLink", newsArrayList.get(position).getMediaLink());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.bottom_up, R.anim.bottom_up);

                }
            });

            holder.imgShareM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.imgShareM.getVisibility() == View.VISIBLE) {
                        holder.imgShareM2.setVisibility(View.GONE);
                        if (holder.imgShareM2.getVisibility() == View.GONE) {
                            if (newsArrayList.get(position).getIs_image().equals("0")) {
                                if (holder.img_play.getVisibility() == View.VISIBLE) {
                                    holder.img_play.setVisibility(View.GONE);
                                    screenShot(holder.rel_main2);
                                    holder.img_play.setVisibility(View.VISIBLE);
//                                holder.imgShareM2.setVisibility(View.VISIBLE);
                                }
                            } else if (newsArrayList.get(position).getIs_image().equals("1")) {
                                screenShot(holder.rel_main2);
//                                holder.imgShareM2.setVisibility(View.VISIBLE);

                            } else if (newsArrayList.get(position).getIs_image().equals("2")) {
                                if (holder.img_playurL.getVisibility() == View.VISIBLE) {
                                    holder.img_playurL.setVisibility(View.GONE);
                                    screenShot(holder.rel_main2);
                                    holder.img_playurL.setVisibility(View.VISIBLE);
//                                holder.imgShareM2.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
            });

            if (holder.img_play.getVisibility() == View.VISIBLE || holder.img_playurL.getVisibility() == View.VISIBLE) {
                holder.imageview.setEnabled(false);
            } else {
                holder.imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, FullImageActivity.class);
                        intent.putExtra("image", imagePath + menuGetset.getImage());
                        intent.putExtra("newsTitle", menuGetset.getShort_desc());

                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(activity,
                                        view,
                                        ViewCompat.getTransitionName(holder.imageview));
                        activity.startActivity(intent, options.toBundle());
                    }
                });
            }

            holder.imageview.setOnTouchListener(new OnSwipeTouchListener(activity) {
                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                    Intent intent = new Intent(activity, Detail_Activity.class);
                    intent.putExtra("news_id", newsArrayList.get(position).getId1());
                    intent.putExtra("short_desc", newsArrayList.get(position).getShort_desc());
                    intent.putExtra("long_desc", newsArrayList.get(position).getDescription());
                    intent.putExtra("image", newsArrayList.get(position).getImage());
                    intent.putExtra("media", newsArrayList.get(position).getMedia());
                    intent.putExtra("medialink", newsArrayList.get(position).getMediaLink());
                    intent.putExtra("is_image", newsArrayList.get(position).getIs_image());
                    intent.putExtra("localVideo", newsArrayList.get(position).getMedia());
                    intent.putExtra("news_date", newsArrayList.get(position).getNews_date());
                    intent.putExtra("reference", reference);
                    intent.putExtra("shortname", shortName);
                    activity.startActivity(intent);
                }

                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();

                }

            });
            holder.txt_description.setOnTouchListener(new OnSwipeTouchListener(activity) {
                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                    Intent intent = new Intent(activity, Detail_Activity.class);
                    intent.putExtra("news_id", newsArrayList.get(position).getId1());
                    intent.putExtra("short_desc", newsArrayList.get(position).getShort_desc());
                    intent.putExtra("long_desc", newsArrayList.get(position).getDescription());
                    intent.putExtra("image", newsArrayList.get(position).getImage());
                    intent.putExtra("media", newsArrayList.get(position).getMedia());
                    intent.putExtra("medialink", newsArrayList.get(position).getMediaLink());
                    intent.putExtra("is_image", newsArrayList.get(position).getIs_image());
                    intent.putExtra("localVideo", newsArrayList.get(position).getMedia());
                    intent.putExtra("news_date", newsArrayList.get(position).getNews_date());
                    intent.putExtra("reference", reference);
                    intent.putExtra("shortname", shortName);
                    activity.startActivity(intent);
                }

                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();

                }
            });
            holder.linRef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ReferenceActivity.class);
                    intent.putExtra("strReference", newsArrayList.get(position).getReference());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.bottom_up, R.anim.bottom_up);
                }
            });

        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "onBindViewHolder: " + e.getMessage());
        }

        try {
            if (MainActivity.themeKEY != null) {
                if (MainActivity.themeKEY.equals("0")) {
                    holder.linItem.setBackgroundColor(activity.getResources().getColor(R.color.darkDray));
                    holder.linItem2.setBackgroundColor(activity.getResources().getColor(R.color.darkDray));
                    holder.txt_description.setTextColor(activity.getResources().getColor(R.color.white));
                    holder.txt_description2.setTextColor(activity.getResources().getColor(R.color.white));
                    holder.txt_short_desc.setTextColor(activity.getResources().getColor(R.color.yellow));
                    holder.txt_short_desc2.setTextColor(activity.getResources().getColor(R.color.yellow));
                    holder.imgShareM.setColorFilter(ContextCompat.getColor(activity, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    holder.imgShareM2.setColorFilter(ContextCompat.getColor(activity, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    holder.txtRef.setTextColor(activity.getResources().getColor(R.color.yellow));
                    holder.txtRef2.setTextColor(activity.getResources().getColor(R.color.yellow));
                    holder.txtNewsDate.setTextColor(activity.getResources().getColor(R.color.white));
                    holder.txtNewsDate2.setTextColor(activity.getResources().getColor(R.color.white));
                    holder.txtWaterMark.setTextColor(activity.getResources().getColor(R.color.yellow));

                } else if (MainActivity.themeKEY.equals("1")) {
                    holder.linItem.setBackgroundColor(activity.getResources().getColor(R.color.white));
                    holder.linItem2.setBackgroundColor(activity.getResources().getColor(R.color.white));
                    holder.txt_description.setTextColor(activity.getResources().getColor(R.color.darkDray));
                    holder.txt_description2.setTextColor(activity.getResources().getColor(R.color.darkDray));
                    holder.txt_short_desc.setTextColor(activity.getResources().getColor(R.color.darkDray));
                    holder.txt_short_desc2.setTextColor(activity.getResources().getColor(R.color.darkDray));
                    holder.imgShareM.setColorFilter(ContextCompat.getColor(activity, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                    holder.imgShareM2.setColorFilter(ContextCompat.getColor(activity, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                    holder.txtRef.setTextColor(activity.getResources().getColor(R.color.darkDray));
                    holder.txtRef2.setTextColor(activity.getResources().getColor(R.color.darkDray));
                    holder.txtNewsDate.setTextColor(activity.getResources().getColor(R.color.darkDray));
                    holder.txtNewsDate2.setTextColor(activity.getResources().getColor(R.color.darkDray));
                    holder.txtWaterMark.setTextColor(activity.getResources().getColor(R.color.yellow));
                    holder.txtWaterMark.setTextColor(activity.getResources().getColor(R.color.darkDray));

                }
            } else {
                holder.linItem.setBackgroundColor(activity.getResources().getColor(R.color.darkDray));
                holder.linItem2.setBackgroundColor(activity.getResources().getColor(R.color.darkDray));
                holder.txt_description.setTextColor(activity.getResources().getColor(R.color.white));
                holder.txt_description2.setTextColor(activity.getResources().getColor(R.color.white));
                holder.txt_short_desc.setTextColor(activity.getResources().getColor(R.color.yellow));
                holder.txt_short_desc2.setTextColor(activity.getResources().getColor(R.color.yellow));
                holder.imgShareM.setColorFilter(ContextCompat.getColor(activity, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.imgShareM2.setColorFilter(ContextCompat.getColor(activity, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.txtRef.setTextColor(activity.getResources().getColor(R.color.yellow));
                holder.txtRef2.setTextColor(activity.getResources().getColor(R.color.yellow));
                holder.txtNewsDate.setTextColor(activity.getResources().getColor(R.color.white));
                holder.txtNewsDate2.setTextColor(activity.getResources().getColor(R.color.white));
                holder.txtWaterMark.setTextColor(activity.getResources().getColor(R.color.yellow));

            }
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "onBindViewHolder: " + e.getMessage());
        }
    }

    private void screenShot(RelativeLayout view) {
//        imgWaterMark.setBackground(activity.getResources().getDrawable(R.drawable.borderrr));
        try {
            bitmap__ = Bitmap.createBitmap(view.getWidth(),
                    view.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap__);
            view.draw(canvas);
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "screenShot: " + e.getMessage());
        }

        Uri uri = (Uri) getLocalBitmapUri(bitmap__);
        Intent sharingImage = new Intent(Intent.ACTION_SEND);
        sharingImage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingImage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sharingImage.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        sharingImage.setType("image/png");
        sharingImage.putExtra(Intent.EXTRA_STREAM, uri);
        sharingImage.putExtra(Intent.EXTRA_TEXT, "FAST WAY OF GETTING UPDATE :" + "\n" + "https://play.google.com/store/apps/details?id=" + activity.getPackageName());
        sharingImage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivity(Intent.createChooser(sharingImage, "Share news using"));
        isVisible = false;

    }

    public static Object getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "share_image_"
                            + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
//            bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getLocalBitmapUri: " + e.getMessage());
        }
        return bmpUri;
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    public void addItem(ArrayList<Model_News> item, int size) {
        if (item.size() != 0) {
            newsArrayList.addAll(item);
            notifyDataSetChanged();
        }

    }

    public void removeView(int pos) {
        try {
            newsArrayList.remove(pos);
            notifyDataSetChanged();

        } catch (Exception e) {
            notifyDataSetChanged();
            e.getMessage();
            Log.e(TAG, "removeView: " + e.getMessage());
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_play, img_playurL, imgShareM, imageview, imgAppIcon;
        ImageView imageview2, imgWaterMark2, imgShareM2;

        TextView txt_short_desc, txt_description, txtRef, txtNewsDate;
        TextView txt_short_desc2, txt_description2, txtRef2, txtNewsDate2, txtWaterMark;

        LinearLayout linItem, linRef;
        LinearLayout linItem2, linRef2;
        RelativeLayout rel_main, rel_main2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageview = itemView.findViewById(R.id.imageview);
            imgAppIcon = itemView.findViewById(R.id.imgAppIcon);
            img_play = itemView.findViewById(R.id.img_play);
            img_playurL = itemView.findViewById(R.id.img_playurL);
            imgShareM = itemView.findViewById(R.id.imgShareM);
            txt_short_desc = itemView.findViewById(R.id.txt_short_desc);
            txt_description = itemView.findViewById(R.id.txt_description);
            txtRef = itemView.findViewById(R.id.txtRef);
            txtNewsDate = itemView.findViewById(R.id.txtNewsDate);
            linItem = itemView.findViewById(R.id.linItem);
            linRef = itemView.findViewById(R.id.linRef);
            rel_main = itemView.findViewById(R.id.rel_main);
            rel_main2 = itemView.findViewById(R.id.rel_main2);

            imgShareM2 = itemView.findViewById(R.id.imgShareM2);
            imageview2 = itemView.findViewById(R.id.imageview2);
            imgWaterMark2 = itemView.findViewById(R.id.imgWaterMark2);
            txt_short_desc2 = itemView.findViewById(R.id.txt_short_desc2);
            txt_description2 = itemView.findViewById(R.id.txt_description2);
            txtWaterMark = itemView.findViewById(R.id.txtWaterMark);
            txtRef2 = itemView.findViewById(R.id.txtRef2);
            txtNewsDate2 = itemView.findViewById(R.id.txtNewsDate2);
            linItem2 = itemView.findViewById(R.id.linItem2);
        }
    }
}
