package com.mananews.apandts.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mananews.apandts.Model_Class.Model_Cat;
import com.mananews.apandts.Model_Class.ReporterModel;
import com.mananews.apandts.R;
import com.mananews.apandts.api.ApiService;
import com.mananews.apandts.utils.FileUtils;
import com.mananews.apandts.utils.SPmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ReporterActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 5;
    RadioButton radioVideo, radioImage;
    ArrayList<Model_Cat> catList = new ArrayList<>();
    CardView cardVV;
    private ProgressBar mainProgress, progressDark;
    ArrayList<String> strings = new ArrayList<>();
    private Spinner spinner;
    String catId = null;
    int i = 0;
    File VIDEO;
    //    private static final int REQUEST_PERMISSIONS = 1234;
    private static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ApiService apiService;
    private ProgressDialog dialog;
    TextView tvCategory, tvTitle, tvTag, tvDescription, tvFullDescription, tvChooseFile;
    ImageView iv_image;
    VideoView vv;
    public static String themeKEY;
    Button btn;
    private int GALLERY_VIDEO = 1;
    private int REQUEST_GET_SINGLE_FILE = 123;
    EditText etTitle, etTag, etDescription, etFullDescription;
    RelativeLayout rLayout, rlTop;
    LinearLayout lLayout, lSpinner;
    static String TAG = ReporterActivity.class.getName();
    Uri selectedImageUri;
    private File image_file;
    private String MEME_TYPE;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporter);

        radioVideo = findViewById(R.id.radioVideo);
        radioImage = findViewById(R.id.radioImage);
        cardVV = findViewById(R.id.cardVV);
        radioGroup = findViewById(R.id.rdGroup);
        mainProgress = findViewById(R.id.mainProgress);
        progressDark = findViewById(R.id.progressDark);
        spinner = findViewById(R.id.spinner);
        vv = findViewById(R.id.vv);
        iv_image = findViewById(R.id.iv_image);
        btn = findViewById(R.id.btn);
        etTitle = findViewById(R.id.etTitle);
        etTag = findViewById(R.id.etTag);
        etDescription = findViewById(R.id.etDescription);
        etFullDescription = findViewById(R.id.etFullDescription);
        rLayout = findViewById(R.id.rLayout);
        lLayout = findViewById(R.id.lLayout);
        lSpinner = findViewById(R.id.lSpinner);
        tvCategory = findViewById(R.id.tvCategory);
        tvTitle = findViewById(R.id.tvTitle);
        tvTag = findViewById(R.id.tvTag);
        tvChooseFile = findViewById(R.id.tvChooseFile);
        tvDescription = findViewById(R.id.tvDescription);
        tvFullDescription = findViewById(R.id.tvFullDescription);
        rlTop = findViewById(R.id.rlTop);

        themeKEY = SPmanager.getPreference(getApplicationContext(), "themeKEY");
        Log.e(TAG, "onCreate: " + themeKEY);

        if (themeKEY == null) {
            if (getApplicationContext().getResources().getString(R.string.isLight).equalsIgnoreCase("1")) {
                themeKEY = "1";
            } else {
                themeKEY = "0";
            }
        }
        if (MainActivity.themeKEY != null) {
            if (themeKEY.equals("1")) {
                tvTitle.setTextColor(getResources().getColor(R.color.darkDray));
                tvTag.setTextColor(getResources().getColor(R.color.darkDray));
                tvChooseFile.setTextColor(getResources().getColor(R.color.darkDray));
                tvFullDescription.setTextColor(getResources().getColor(R.color.darkDray));
                tvDescription.setTextColor(getResources().getColor(R.color.darkDray));
                tvCategory.setTextColor(getResources().getColor(R.color.darkDray));
                radioImage.setTextColor(getResources().getColor(R.color.darkDray));
                radioVideo.setTextColor(getResources().getColor(R.color.darkDray));
                rLayout.setBackgroundColor(getResources().getColor(R.color.white));
                lLayout.setBackgroundColor(getResources().getColor(R.color.white));
                rlTop.setBackgroundColor(getResources().getColor(R.color.white));
                etTitle.setBackgroundResource(R.drawable.button_no_light);
                etTag.setBackgroundResource(R.drawable.button_no_light);
                etDescription.setBackgroundResource(R.drawable.button_no_light);
                etFullDescription.setBackgroundResource(R.drawable.button_no_light);
                lSpinner.setBackgroundResource(R.drawable.button_no_light);
                etTitle.setTextColor(getResources().getColor(R.color.black));
                etTag.setTextColor(getResources().getColor(R.color.black));
                etDescription.setTextColor(getResources().getColor(R.color.black));
                etFullDescription.setTextColor(getResources().getColor(R.color.black));
                spinner.setBackgroundTintList(getResources().getColorStateList(R.color.darkDray));
            } else {
                tvTitle.setTextColor(getResources().getColor(R.color.white));
                tvTag.setTextColor(getResources().getColor(R.color.white));
                tvChooseFile.setTextColor(getResources().getColor(R.color.white));
                tvFullDescription.setTextColor(getResources().getColor(R.color.white));
                radioImage.setTextColor(getResources().getColor(R.color.white));
                radioVideo.setTextColor(getResources().getColor(R.color.white));
                tvDescription.setTextColor(getResources().getColor(R.color.white));
                tvCategory.setTextColor(getResources().getColor(R.color.white));
                rLayout.setBackgroundColor(getResources().getColor(R.color.darkDray));
                lLayout.setBackgroundColor(getResources().getColor(R.color.darkDray));
                rlTop.setBackgroundColor(getResources().getColor(R.color.yellow));
                etTitle.setBackgroundResource(R.drawable.border);
                etTag.setBackgroundResource(R.drawable.border);
                etDescription.setBackgroundResource(R.drawable.border);
                etFullDescription.setBackgroundResource(R.drawable.border);
                lSpinner.setBackgroundResource(R.drawable.border);
                spinner.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                etTitle.setTextColor(getResources().getColor(R.color.white));
                etTag.setTextColor(getResources().getColor(R.color.white));
                etDescription.setTextColor(getResources().getColor(R.color.white));
                etFullDescription.setTextColor(getResources().getColor(R.color.white));
            }
        }

        apiService = SPmanager.getUserService(getApplicationContext());

        dialog = new ProgressDialog(ReporterActivity.this);
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        btn.setOnClickListener(v -> imageUploadAPI());

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (ContextCompat.checkSelfPermission(ReporterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ReporterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ReporterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                if (checkedId == R.id.radioVideo) {
                    if (!radioImage.isChecked() && radioVideo.isChecked() && ContextCompat.checkSelfPermission(ReporterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ReporterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("FATZ", "Video Picker");
                        cardVV.setVisibility(View.VISIBLE);
                        vv.setVideoURI(null);
                        vv.setVisibility(View.GONE);
                        iv_image.setImageResource(0);
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("video/*");
                        startActivityForResult(intent, GALLERY_VIDEO);
                    } else {
                        ActivityCompat.requestPermissions(ReporterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                } else {
                    if (!radioVideo.isChecked() && radioImage.isChecked() && ContextCompat.checkSelfPermission(ReporterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ReporterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("FATZ", "Image Picker");
                        cardVV.setVisibility(View.GONE);
                        iv_image.setImageResource(0);
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_SINGLE_FILE);
                    } else {
                        ActivityCompat.requestPermissions(ReporterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                }
            }
        });

        String catimgPath = "webservices/category.php";
        String urlCatImg = getString(R.string.server_url) + catimgPath;
        Log.e(TAG, "getData: " + urlCatImg);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlCatImg, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: " + response);
                catList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        JSONObject resp = jsonObject.getJSONObject("response");
                        JSONObject data = resp.getJSONObject("data");
                        JSONArray category = data.getJSONArray("category");
                        for (int i = 0; i < category.length(); i++) {
                            Model_Cat model_cat = new Model_Cat();
                            JSONObject jsonObject1 = category.getJSONObject(i);
                            String cate_id = jsonObject1.getString("id");
                            String category_name = jsonObject1.getString("category_name");
                            String image = jsonObject1.getString("image");
                            model_cat.setCat_id(cate_id);
                            model_cat.setCategory_name(category_name);
                            model_cat.setCatImg(image);
                            catList.add(model_cat);
                            strings.add(category_name);
                        }
                        mainProgress.setVisibility(View.GONE);
                        progressDark.setVisibility(View.GONE);

                        ArrayAdapter aa = null;
                        if (MainActivity.themeKEY != null) {
                            if (themeKEY.equals("1")) {
                                aa = new ArrayAdapter(getApplicationContext(), R.layout.spinner_light, strings);
                            } else {
                                aa = new ArrayAdapter(getApplicationContext(), R.layout.spinner, strings);
                            }
                        } else {
                            aa = new ArrayAdapter(getApplicationContext(), R.layout.spinner, strings);
                        }
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(aa);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }
            }
        }, error -> {
            error.getMessage();
            Log.e(TAG, "onErrorResponse: " + error.getMessage());
        });
        requestQueue.add(stringRequest);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < catList.size(); i++) {
                    if (catList.get(i).getCategory_name().equalsIgnoreCase(String.valueOf(spinner.getSelectedItem()))) {
                        catId = catList.get(i).getCat_id();
                        break;
                    }
                }
                Log.e(TAG, "spinner ==> \n" + spinner.getSelectedItem() + "  " + catId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("FATZ", "Here: " + requestCode + " | " + resultCode + " | " + (data != null));

        if (requestCode == GALLERY_VIDEO && resultCode == RESULT_OK && data != null) {
            Log.d("FATZ", "VIDEo Select");

            Uri uri = data.getData();
            image_file = FileUtils.getFile(ReporterActivity.this, uri);
            MEME_TYPE = FileUtils.getMimeTypeFromFileUri(this, uri);

//            selectedImageUri = data.getData();
//            String path = getPath(selectedImageUri);
//            if (path != null) {
//                File f = new File(path);
//                selectedImageUri = Uri.fromFile(f);
//            }
//
//            Log.e(TAG, path);
//            if (radioVideo.isChecked()) {
//                vv.setVideoURI(selectedImageUri);
//                vv.requestFocus();
//                vv.start();
//                vv.setVisibility(View.VISIBLE);
//                assert path != null;
//                VIDEO = new File(path);
//            }
//            iv_image.setVisibility(View.VISIBLE);
//            Glide.with(getApplicationContext()).load(selectedImageUri).skipMemoryCache(false).into(iv_image);
        } else if (requestCode == REQUEST_GET_SINGLE_FILE && resultCode == RESULT_OK) {
            Log.d("FATZ", "Here Images");
            Uri uri = data.getData();
            image_file = FileUtils.getFile(ReporterActivity.this, uri);
            MEME_TYPE = FileUtils.getMimeTypeFromFileUri(this, uri);

            Log.d("FATZ", "Selected File Path: " + image_file.getPath() + " | \n" + MEME_TYPE);
        } else {
            radioImage.setSelected(false);
            radioVideo.setSelected(false);
        }
    }

//    private boolean isStoragePermissionGranted() {
//        for (String permissions : PERMISSIONS) {
//            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissions) != PackageManager.PERMISSION_GRANTED) {
//                return true;
//            }
//        }
//        return false;
//    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "image", null);
        Log.e(TAG, "getImageUri: path ==>" + path);
        if (path != null) {
            return Uri.parse(path);
        } else {
            return Uri.parse("");
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Log.e(TAG, "queryName: uri ==> " + uri);
        Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public static File getFile(Context context, Uri uri) throws IOException {
//        Log.e(TAG, "getFile queryName ==> " + queryName(context, uri));
//        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        File destinationFilename = new File(uri.getPath());
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e(TAG, "getFile = " + ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }


    private void imageUploadAPI() {
        System.setProperty("http.keepAlive", "false");

        if (catId == null) {
            Toast.makeText(getApplicationContext(), "Please select category!", Toast.LENGTH_SHORT).show();
        } else if (etTitle.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter title!", Toast.LENGTH_SHORT).show();
        } else if (etTag.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter tag!", Toast.LENGTH_SHORT).show();
        } else if (etDescription.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please description title!", Toast.LENGTH_SHORT).show();
        } else if (etFullDescription.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please full-description title!", Toast.LENGTH_SHORT).show();
        } else if (radioVideo.isChecked() && image_file == null) {
            Toast.makeText(getApplicationContext(), "Please select video!", Toast.LENGTH_SHORT).show();
        } else if (image_file == null && radioImage.isSelected()) {
            Toast.makeText(getApplicationContext(), "Please select Image!", Toast.LENGTH_SHORT).show();
        } else if (image_file == null) {
            Toast.makeText(getApplicationContext(), "Please select File!", Toast.LENGTH_SHORT).show();
        } else {
            if (SPmanager.isConnected(ReporterActivity.this)) {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                String id = SPmanager.getPreference(getApplicationContext(), "userid");
                builder.addFormDataPart("title", etTitle.getText().toString());
                builder.addFormDataPart("tag", etTag.getText().toString());
                builder.addFormDataPart("cat_id", catId);
                builder.addFormDataPart("type", radioVideo.isChecked() ? "0" : "1");
                builder.addFormDataPart("description", etDescription.getText().toString());
                builder.addFormDataPart("reporter_id", id);
                builder.addFormDataPart("long_description", etFullDescription.getText().toString());
                builder.addFormDataPart("thumbnail", image_file.getName(), RequestBody.create(MediaType.parse(MEME_TYPE), image_file));
                builder.addFormDataPart("media", image_file.getName(), RequestBody.create(MediaType.parse(MEME_TYPE), image_file));

                Log.d("FATZ", " Meme: " + MEME_TYPE + " \n isVideo" + (radioVideo.isChecked()) + "\n Video: " + image_file.getPath());
                Log.d("FATZ", "imageUploadAPI ==> try if \n " + "\n reporter_id ==>" + id + "\n catId ==>" + catId + "\n title ==>" + etTitle.getText().toString() + "\n tag ==>" + etTag.getText().toString() + "\n description ==>" + etDescription.getText().toString() + "\n long_description ==>" + etFullDescription.getText().toString());

                MultipartBody requestBody = builder.build();
                dialog.show();

//                CountingFileRequestBody requestBody1 = new CountingFileRequestBody(requestBody, "files", (key, num) -> {
//                    Log.e(TAG, "transferred ==> \n" + num + "    ");
//                    if (num == 100) {
//                    } else {
//                        i = num;
//                    }
//                    dialog.setProgress(i);
//                });

                Call<ReporterModel> call = apiService.imageUpload(requestBody);
                call.enqueue(new Callback<ReporterModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ReporterModel> call, @NonNull retrofit2.Response<ReporterModel> response) {
                        image_file = null;
                        dialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
//                                JSONObject obj = new JSONObject(response.body().string());
//                                String message = obj.getString("message");
                                assert response.body() != null;
                                Log.d("FATZ", "Upload Success: " + response.body().getMessage());
                                vv.setVideoURI(null);
                                VIDEO = null;
                                iv_image.setImageResource(0);
                                vv.setVisibility(View.GONE);
                                iv_image.setVisibility(View.GONE);
                                etTitle.setText("");
                                etTag.setText("");
                                etDescription.setText("");
                                etFullDescription.setText("");
                                startActivity(new Intent(ReporterActivity.this, MainActivity.class));
                                finish();
//                                Log.w(TAG,"onResponse ==> \n"+ new GsonBuilder().setPrettyPrinting().create().toJson(response));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("FATZ", "imageUploadAPI ==> catch \n" + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ReporterModel> call, Throwable t) {
                        dialog.dismiss();
                        image_file = null;
                        i = 0;
                        Log.d("FATZ", "imageUploadAPI ==> onFailure getMessage \n" + t.getMessage());
//                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void btnBack(View view) {
        onBackPressed();
    }
}