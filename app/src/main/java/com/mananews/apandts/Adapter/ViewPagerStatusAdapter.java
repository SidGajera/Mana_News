package com.mananews.apandts.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mananews.apandts.Activity.ProfileActivity;
import com.mananews.apandts.BuildConfig;
import com.mananews.apandts.Model_Class.StatusModel.Datum;
import com.mananews.apandts.OnItemClickListner;
import com.mananews.apandts.R;
import com.mananews.apandts.databinding.ItemStatusCellBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerStatusAdapter extends RecyclerView.Adapter<ViewPagerStatusAdapter.ViewHolder> {

    private static final String TAG = ViewPagerStatusAdapter.class.getName();
    public Activity activity;
    ArrayList<Datum> newsArrayList;
    private OnItemClickListner onItemClickListners;
    String shareImage = "";

    public ViewPagerStatusAdapter(Activity activity, ArrayList<Datum> imageList, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.newsArrayList = imageList;
        this.onItemClickListners = onItemClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemStatusCellBinding.inflate(activity.getLayoutInflater(), parent, false));
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Datum item = newsArrayList.get(position);
        String imagePath = activity.getString(R.string.server_url);
        String image = item.getImage().split("\\.\\.")[1];
        Log.e("FATZ", "Image: " + imagePath + image);
        Glide.with(activity).load(imagePath + image).placeholder(Color.WHITE).into(holder.binding.imageview);
        holder.binding.txtDescription.setText(item.getName());
        holder.binding.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListners.onItemClick(imagePath + image, position);
            }
        });

        holder.binding.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(activity).asBitmap().load(imagePath + image).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (isStoragePermissionGranted()) {
                            long time = System.currentTimeMillis();
                            saveImages(resource, "mana_news_save" + time);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
            }
        });

        holder.binding.imgWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Glide.with(activity).asBitmap().load(imagePath + image).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        isStoragePermissionGranted();
                        long time = System.currentTimeMillis();
                        shareImage = saveImages(resource, "mana_news" + time);
                        Log.e(TAG, "onClick: shareImage ==> " + shareImage);

                        Uri contentUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", new File(shareImage));

                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Mana News");
                        whatsappIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        whatsappIntent.setType("image/jpeg");
                        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        try {
                            activity.startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(activity, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

            }
        });

        holder.binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Glide.with(activity).asBitmap().load(imagePath + image).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        isStoragePermissionGranted();
                        long time = System.currentTimeMillis();
                        shareImage = saveImages(resource, "mana_news" + time);
                        Log.e(TAG, "onClick: shareImage ==> " + shareImage);

                        Uri contentUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", new File(shareImage));

                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Mana News");
                        whatsappIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        whatsappIntent.setType("image/jpeg");
                        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        try {
                            activity.startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(activity, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
            }
        });
    }

    private String saveImages(Bitmap image, String name) {
        Log.e(TAG, "saveImages: Bitmap = " + image + "\nname = " + name);
        String savedImagePath = null;

        String imageFileName = name + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + activity.getString(R.string.app_name));

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "saveImages: " + e.getMessage());
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            if (name.contains("save")) {
                Toast.makeText(activity, "IMAGE SAVED", Toast.LENGTH_LONG).show();
            }

            Log.e(TAG, "saveImages: savedImagePath ==> " + imageFile.getPath());
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }

    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        OutputStream fos;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = activity.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + File.separator + activity.getString(R.string.app_name));
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            Log.e(TAG, "First: " + imageUri + "\n imageUri: " + imageUri.getPath() + "\n filePath: " + getRealPathFromURI(imageUri));
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
            File image = new File(imagesDir, name + ".jpg");
            fos = new FileOutputStream(image);
            Log.e(TAG, "Second: " + image.getAbsolutePath());
        }

        Log.e(TAG, "Final: " + fos);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        Objects.requireNonNull(fos).close();

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    private void saveImage(Bitmap bitmap, @NonNull String name, Activity activity) throws IOException {
        boolean saved;
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = activity.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + activity.getString(R.string.app_name));
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + activity.getString(R.string.app_name);

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);

        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemStatusCellBinding binding;

        public ViewHolder(@NonNull ItemStatusCellBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
