package com.mananews.apandts.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mananews.apandts.Model_Class.Model_Cat;
import com.mananews.apandts.Model_Class.ReporterModel;
import com.mananews.apandts.R;
import com.mananews.apandts.api.ApiService;
import com.mananews.apandts.utils.CountingFileRequestBody;
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
    RadioButton radioVideo, radioImage;
    ArrayList<Model_Cat> catList = new ArrayList<>();
    CardView cardVV;
    private ProgressBar mainProgress, progressDark;
    ArrayList<String> strings = new ArrayList<>();
    private Spinner spinner;
    String catId = null;
    int i = 0;
    File VIDEO;
    private static final int REQUEST_PERMISSIONS = 1234;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    ApiService apiService;
    private ProgressDialog dialog;
    TextView tvCategory, tvTitle, tvTag, tvDescription, tvFullDescription, tvChooseFile;
    ImageView iv_image;
    VideoView vv;
    public static String themeKEY;
    Button btn;
    private int GALLERY = 1;
    private int REQUEST_GET_SINGLE_FILE = 123;
    EditText etTitle, etTag, etDescription, etFullDescription;
    RelativeLayout rLayout, rlTop;
    LinearLayout lLayout, lSpinner;
    static String TAG = ReporterActivity.class.getName();
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporter);

        radioVideo = findViewById(R.id.radioVideo);
        radioImage = findViewById(R.id.radioImage);
        cardVV = findViewById(R.id.cardVV);
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
        dialog.setIndeterminate(false);
        dialog.setProgress(0);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isStoragePermissionGranted()) {
                    requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
                } else {
                    imageUploadAPI();
                }
            }
        });

        radioVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radioVideo.isChecked()) {
                    cardVV.setVisibility(View.VISIBLE);
                    vv.setVideoURI(null);
                    vv.setVisibility(View.GONE);
                    iv_image.setImageResource(0);
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, GALLERY);
                }
            }
        });


        radioImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radioImage.isChecked()) {
                    cardVV.setVisibility(View.GONE);
                    iv_image.setImageResource(0);
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_SINGLE_FILE);

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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
            }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY) {
            Log.e(TAG, "GALLERY");
            if (resultCode == RESULT_OK) {

                selectedImageUri = data.getData();
                String path = getPath(selectedImageUri);
                if (path != null) {
                    File f = new File(path);
                    selectedImageUri = Uri.fromFile(f);
                }

                Log.e(TAG, path);
                if (radioVideo.isChecked()) {
                    vv.setVideoURI(selectedImageUri);
                    vv.requestFocus();
                    vv.start();
                    vv.setVisibility(View.VISIBLE);
                    assert path != null;
                    VIDEO = new File(path);
                }

                iv_image.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext())
                        .load(selectedImageUri)
                        .skipMemoryCache(false)
                        .into(iv_image);
            }
        }

        Log.e(TAG, "resultCode ==>" + resultCode +
                "+\n requestCode ==> " + requestCode +
                "+\n getData ==> " + data.getData() +
                "+\n selectedImageUri ==> " + selectedImageUri);

        try {
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    private boolean isStoragePermissionGranted() {
        for (String permissions : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissions) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

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
        File uploadFile = null;
        try {
            iv_image.invalidate();
            Uri uri = null;
            if (iv_image.getDrawable() != null) {
                BitmapDrawable drawable = (BitmapDrawable) iv_image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                uri = getImageUri(getApplicationContext(), bitmap);
                uploadFile = getFile(getApplicationContext(), selectedImageUri);
            }

            Log.e(TAG, "IOException ==> \n" +
                    "uri = " + uri +
                    "uploadFile = " + uploadFile +
                    "selectedImageUri = " + selectedImageUri);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Please select again", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "IOException ==> \n e = " + e.getMessage());
        }
        File uploadFileVideo = VIDEO;

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
        } else if (radioVideo.isChecked() && VIDEO == null) {
            Toast.makeText(getApplicationContext(), "Please select video!", Toast.LENGTH_SHORT).show();
        } else if (uploadFile == null) {
            Toast.makeText(getApplicationContext(), "Please select Video/Image!", Toast.LENGTH_SHORT).show();
        } else {
            if (SPmanager.isConnected(ReporterActivity.this)) {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                String id = SPmanager.getPreference(getApplicationContext(), "userid");
                builder.addFormDataPart("reporter_id", id);
                builder.addFormDataPart("cat_id", catId);
                builder.addFormDataPart("title", etTitle.getText().toString());
                builder.addFormDataPart("tag", etTag.getText().toString());
                builder.addFormDataPart("description", etDescription.getText().toString());
                builder.addFormDataPart("long_description", etFullDescription.getText().toString());
                builder.addFormDataPart("thumbnail", uploadFile.getName(), RequestBody.create(MediaType.parse("image/*"), uploadFile));

                if (radioVideo.isChecked()) {
                    builder.addFormDataPart("media", uploadFileVideo.getName(), RequestBody.create(MediaType.parse("video/*"), uploadFileVideo));
                    builder.addFormDataPart("type", "0");
                } else {
                    builder.addFormDataPart("type", "1");
                }

                Log.e(TAG, "imageUploadAPI ==> try if \n " +
                        "\n reporter_id ==>" + id +
                        "\n catId ==>" + catId +
                        "\n title ==>" + etTitle.getText().toString() +
                        "\n tag ==>" + etTag.getText().toString() +
                        "\n description ==>" + etDescription.getText().toString() +
                        "\n long_description ==>" + etFullDescription.getText().toString());

                MultipartBody requestBody = builder.build();
                dialog.show();
                CountingFileRequestBody requestBody1 = new CountingFileRequestBody(requestBody, "files", new CountingFileRequestBody.ProgressListener() {
                    @Override
                    public void transferred(String key, int num) {
                        Log.e(TAG, "transferred ==> \n" + num + "    ");
                        if (num == 100) {
                        } else {
                            i = num;
                        }
                        dialog.setProgress(i);
                    }
                });

                Call<ReporterModel> call = apiService.imageUpload(requestBody1);
                call.enqueue(new Callback<ReporterModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ReporterModel> call, @NonNull retrofit2.Response<ReporterModel> response) {
                        dialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
//                                JSONObject obj = new JSONObject(response.body().string());
//                                String message = obj.getString("message");
                                Log.e(TAG, "imageUploadAPI ==> try if \n" + "message");
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
//                                Log.w(TAG,"onResponse ==> \n"+ new GsonBuilder().setPrettyPrinting().create().toJson(response));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "imageUploadAPI ==> catch \n" + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ReporterModel> call, Throwable t) {
                        dialog.dismiss();
                        Log.e(TAG, "imageUploadAPI ==> onFailure getMessage \n" + t.getMessage());
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