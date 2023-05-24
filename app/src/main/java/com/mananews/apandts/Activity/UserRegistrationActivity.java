package com.mananews.apandts.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mananews.apandts.R;
import com.mananews.apandts.utils.SPmanager;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "UserRegistration";
    private EditText edtName, edtEmail, edtPassword;
    private TextView txtRegister, txtLogin, txtTitCreateAc, txtTitRegister, txtTitName, txtTitMail, txtTitPass, txtTitAlreadyHaveAc;
    private String name, email, password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String status, loginSuccessfull, registeredSuccessfull;
    private LinearLayout linTop, linMain, linRegister;
    private ImageView imgClose, imgUser, imgEmail, imgPass;
    private View view1, view2, view3;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        getSupportActionBar().hide();
        getPreference();
        init();
        getThemes();
    }

    private void getPreference() {
        loginSuccessfull = SPmanager.getPreference(UserRegistrationActivity.this, "loginSuccessfull");
    }

    private void init() {
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        txtRegister = findViewById(R.id.txtRegister);
        txtLogin = findViewById(R.id.txtLogin);
        txtTitCreateAc = findViewById(R.id.txtTitCreateAc);
        txtTitRegister = findViewById(R.id.txtTitRegister);
        txtTitName = findViewById(R.id.txtTitName);
        txtTitMail = findViewById(R.id.txtTitMail);
        txtTitPass = findViewById(R.id.txtTitPass);
        txtTitAlreadyHaveAc = findViewById(R.id.txtTitAlreadyHaveAc);
        linTop = findViewById(R.id.linTop);
        linRegister = findViewById(R.id.linRegister);
        linMain = findViewById(R.id.linMain);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        imgClose = findViewById(R.id.imgClose);
        imgUser = findViewById(R.id.imgUser);
        imgEmail = findViewById(R.id.imgEmail);
        imgPass = findViewById(R.id.imgPass);

        if (loginSuccessfull != null) {
            if (loginSuccessfull.equals("yes")) {
                txtRegister.setEnabled(false);
                edtEmail.setEnabled(false);
                edtName.setEnabled(false);
                edtPassword.setEnabled(false);
                Toast.makeText(this, "You have already registered", Toast.LENGTH_SHORT).show();
            } else if (loginSuccessfull.equals("no")) {
                txtRegister.setEnabled(false);
                edtEmail.setEnabled(false);
                edtName.setEnabled(false);
                edtPassword.setEnabled(false);
            }
        }

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtName.getText().toString().isEmpty()) {
                } else {
                    name = edtName.getText().toString();
                    SPmanager.saveValue(UserRegistrationActivity.this, "name", name);
                }
                if (edtPassword.getText().toString().isEmpty()) {
                } else {
                    password = edtPassword.getText().toString();
                    SPmanager.saveValue(UserRegistrationActivity.this, "name", password);
                }
                if (edtEmail.getText().toString().isEmpty()) {
                } else {
                    if (edtEmail.getText().toString().trim().matches(emailPattern)) {
                        email = edtEmail.getText().toString();
                        SPmanager.saveValue(UserRegistrationActivity.this, "email", email);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }
                if ((name != null) && (email != null) && (password != null)) {
                    getData(name, email, password);
                } else {
                    Toast.makeText(UserRegistrationActivity.this, "Please fill the all field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getThemes() {
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                linTop.setBackgroundColor(getResources().getColor(R.color.footercolor));
                linMain.setBackgroundColor(getResources().getColor(R.color.white));
                imgClose.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgUser.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgEmail.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgPass.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                txtRegister.setTextColor(getResources().getColor(R.color.white));
                txtLogin.setTextColor(getResources().getColor(R.color.darkDray));
                txtTitAlreadyHaveAc.setTextColor(getResources().getColor(R.color.darkDray));
                txtTitCreateAc.setTextColor(getResources().getColor(R.color.darkDray));
                txtTitMail.setTextColor(getResources().getColor(R.color.darkDray));
                txtTitPass.setTextColor(getResources().getColor(R.color.darkDray));
                txtTitName.setTextColor(getResources().getColor(R.color.darkDray));
                txtTitRegister.setTextColor(getResources().getColor(R.color.darkDray));
                linRegister.setBackgroundResource(R.drawable.btn_login_darkgrey);
                edtName.setHintTextColor(getResources().getColor(R.color.darkDray));
                edtEmail.setHintTextColor(getResources().getColor(R.color.darkDray));
                edtPassword.setHintTextColor(getResources().getColor(R.color.darkDray));
                edtName.setTextColor(getResources().getColor(R.color.darkDray));
                edtEmail.setTextColor(getResources().getColor(R.color.darkDray));
                edtPassword.setTextColor(getResources().getColor(R.color.darkDray));
                view1.setBackgroundColor(getResources().getColor(R.color.darkDray));
                view2.setBackgroundColor(getResources().getColor(R.color.darkDray));
                view3.setBackgroundColor(getResources().getColor(R.color.darkDray));

            } else if (MainActivity.themeKEY.equals("0")) {

                linTop.setBackgroundColor(getResources().getColor(R.color.yellow));
                linMain.setBackgroundColor(getResources().getColor(R.color.darkDray));
                imgClose.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgUser.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgEmail.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgPass.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                txtTitRegister.setTextColor(getResources().getColor(R.color.yellow));
                txtTitPass.setTextColor(getResources().getColor(R.color.white));
                txtRegister.setTextColor(getResources().getColor(R.color.darkDray));
                txtTitName.setTextColor(getResources().getColor(R.color.white));
                txtTitMail.setTextColor(getResources().getColor(R.color.white));
                txtTitCreateAc.setTextColor(getResources().getColor(R.color.white));
                txtTitAlreadyHaveAc.setTextColor(getResources().getColor(R.color.color_gray));
                txtLogin.setTextColor(getResources().getColor(R.color.yellow));
                linRegister.setBackgroundResource(R.drawable.btn_login_yellow);
                edtName.setHintTextColor(getResources().getColor(R.color.color_gray));
                edtEmail.setHintTextColor(getResources().getColor(R.color.color_gray));
                edtPassword.setHintTextColor(getResources().getColor(R.color.color_gray));
                edtName.setTextColor(getResources().getColor(R.color.white));
                edtEmail.setTextColor(getResources().getColor(R.color.white));
                edtPassword.setTextColor(getResources().getColor(R.color.white));
                view1.setBackgroundColor(getResources().getColor(R.color.yellow));
                view2.setBackgroundColor(getResources().getColor(R.color.yellow));
                view3.setBackgroundColor(getResources().getColor(R.color.yellow));

            }
        } else {
            linTop.setBackgroundColor(getResources().getColor(R.color.yellow));
            linMain.setBackgroundColor(getResources().getColor(R.color.darkDray));
            imgClose.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
            imgUser.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            imgEmail.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            imgPass.setColorFilter(ContextCompat.getColor(UserRegistrationActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            txtTitRegister.setTextColor(getResources().getColor(R.color.yellow));
            txtTitPass.setTextColor(getResources().getColor(R.color.white));
            txtRegister.setTextColor(getResources().getColor(R.color.darkDray));
            txtTitName.setTextColor(getResources().getColor(R.color.white));
            txtTitMail.setTextColor(getResources().getColor(R.color.white));
            txtTitCreateAc.setTextColor(getResources().getColor(R.color.white));
            txtTitAlreadyHaveAc.setTextColor(getResources().getColor(R.color.color_gray));
            txtLogin.setTextColor(getResources().getColor(R.color.yellow));
            linRegister.setBackgroundResource(R.drawable.btn_login_yellow);
            edtName.setHintTextColor(getResources().getColor(R.color.color_gray));
            edtEmail.setHintTextColor(getResources().getColor(R.color.color_gray));
            edtPassword.setHintTextColor(getResources().getColor(R.color.color_gray));
            edtName.setTextColor(getResources().getColor(R.color.white));
            edtEmail.setTextColor(getResources().getColor(R.color.white));
            edtPassword.setTextColor(getResources().getColor(R.color.white));
            view1.setBackgroundColor(getResources().getColor(R.color.yellow));
            view2.setBackgroundColor(getResources().getColor(R.color.yellow));
            view3.setBackgroundColor(getResources().getColor(R.color.yellow));

        }
    }

    private void getData(String name, String email, String password) {

        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                progressDialog = new ProgressDialog(UserRegistrationActivity.this, R.style.MyAlertDialogStyleLight);
            } else {
                progressDialog = new ProgressDialog(UserRegistrationActivity.this, R.style.MyAlertDialogStyle);
            }
        } else {
            progressDialog = new ProgressDialog(UserRegistrationActivity.this, R.style.MyAlertDialogStyle);
        }

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Wait a moment...!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String url = getString(R.string.server_url) + "webservices/user-create.php?username=" + name + "&email=" + email + "&password=" + password;
        Log.e(TAG, "getData: " + url);

        AndroidNetworking.post(getString(R.string.server_url) + "webservices/user-create.php?username=" + name + "&email=" + email + "&password=" + password)
                .addBodyParameter("username", name)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: " + response);
                        try {
                            status = response.getString("status");
                            progressDialog.dismiss();
                            if (status.equals("200")) {

                                txtLogin.setEnabled(false);
                                JSONObject object = response.getJSONObject("response");
                                JSONArray data = object.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {

                                    JSONObject object1 = data.getJSONObject(i);
                                    String id = object1.getString("id");
                                    String username = object1.getString("username");
                                    String email = object1.getString("email");
                                    String password = object1.getString("password");
                                    String role = object1.getString("role");

                                    SPmanager.saveValue(UserRegistrationActivity.this, "userid", id);

                                    SPmanager.saveValue(UserRegistrationActivity.this, "username", username);
                                    SPmanager.saveValue(UserRegistrationActivity.this, "email", email);
                                    SPmanager.saveValue(UserRegistrationActivity.this, "password", password);
                                    SPmanager.saveValue(UserRegistrationActivity.this, "role", role);
                                    SPmanager.saveValueBoolean(UserRegistrationActivity.this, "isLogin", true);
                                    Log.e(TAG, "onResponse: " + status + username + email + password);



                                    registeredSuccessfull = "yes";
                                    SPmanager.saveValue(UserRegistrationActivity.this, "registeredSuccessfull", registeredSuccessfull);
                                    Toast.makeText(UserRegistrationActivity.this, "Your account created succesfully ", Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(UserRegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                android.os.Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                }, 1000);
                            } else if (status.equals("409")) {
                                registeredSuccessfull = "no";
                                SPmanager.saveValue(UserRegistrationActivity.this, "registeredSuccessfull", registeredSuccessfull);
                                String error = response.getString("error");
                                Toast.makeText(UserRegistrationActivity.this, error, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                txtLogin.setEnabled(false);
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, "onError: " + anError.getMessage());
                    }
                });
    }
}