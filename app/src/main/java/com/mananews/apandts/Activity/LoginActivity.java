package com.mananews.apandts.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ImageView imgClose, imgEmail, imgPass;
    private EditText edtEmail, edtPassword;
    private TextView txtLogin, txtCreateNewOne, txtproceed, txtTitLogin, txtTitmail, txtTitPass, txtDontHaveAccount;
    private String registeredSuccessfull, status;
    public static String loginSuccessfull = "no";
    private LinearLayout linTop, linMain, linLogin;
    private View view1, view2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        getPreferenceData();
        init();
        getThemes();
    }

    private void getPreferenceData() {
        loginSuccessfull = SPmanager.getPreference(LoginActivity.this, "loginSuccessfull");
        registeredSuccessfull = SPmanager.getPreference(LoginActivity.this, "registeredSuccessfull");
    }

    private void init() {
        linTop = findViewById(R.id.linTop);
        linMain = findViewById(R.id.linMain);
        linLogin = findViewById(R.id.linLogin);
        imgClose = findViewById(R.id.imgClose);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        txtLogin = findViewById(R.id.txtLogin_);
        txtCreateNewOne = findViewById(R.id.txtCreateNewOne);
        txtproceed = findViewById(R.id.txtproceed);
        txtTitLogin = findViewById(R.id.txtTitLogin);
        txtTitmail = findViewById(R.id.txtTitmail);
        imgEmail = findViewById(R.id.imgEmail);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        txtTitPass = findViewById(R.id.txtTitPass);
        imgPass = findViewById(R.id.imgPass);
        txtDontHaveAccount = findViewById(R.id.txtDontHaveAccount);

        if (registeredSuccessfull != null) {
            if (registeredSuccessfull.equals("yes")) {
                txtCreateNewOne.setEnabled(false);
            }
        }
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

/*
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (email != null) {
                        if (!email.isEmpty()) {
                            String strpsw = edtPassword.getText().toString();
                            String stremail = edtEmail.getText().toString();
                            if ((password.equals(strpsw)) & (email.equals(stremail))) {
                                loginUser(email,password);
                           } else {
                                loginSuccessfull = "no";
                                Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "loginonClick: " + e.getMessage());
                }
            }
        });
*/

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String strpsw = edtPassword.getText().toString();
                    String stremail = edtEmail.getText().toString();

                    if (!stremail.isEmpty() && !strpsw.isEmpty()) {
                        loginUser(stremail, strpsw);
                    } else if (stremail.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    } else if (strpsw.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter email & password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.getMessage();
                    Log.e(TAG, "loginonClick: " + e.getMessage());
                }
            }
        });


        txtCreateNewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UserRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser(String stremail, String strpassword) {

        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyleLight);
            } else {
                progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
            }
        } else {
            progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
        }

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Wait a moment...!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        AndroidNetworking.post(getString(R.string.server_url) + "webservices/login-user.php")
                .addBodyParameter("email", stremail)
                .addBodyParameter("password", strpassword)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: " + response);
                        try {
                            status = response.getString("status");
                            if (status.equals("200")) {

                                JSONObject object = response.getJSONObject("response");
                                JSONArray data = object.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {

                                    JSONObject object1 = data.getJSONObject(i);
                                    String id = object1.getString("id");
                                    String username = object1.getString("username");
                                    String email = object1.getString("email");
                                    String profile = object1.getString("profile");
                                    String role = object1.getString("role");

                                    loginSuccessfull = "yes";
                                    SPmanager.saveValue(LoginActivity.this, "loginSuccessfull", loginSuccessfull);
                                    SPmanager.saveValue(LoginActivity.this, "userid", id);
                                    SPmanager.saveValue(LoginActivity.this, "username", username);
                                    SPmanager.saveValue(LoginActivity.this, "email", email);
                                    SPmanager.saveValue(LoginActivity.this, "role", role);

                                    SPmanager.saveValueBoolean(LoginActivity.this, "isLogin", true);
                                    if (profile.length() != 0 || !profile.isEmpty()){
                                        SPmanager.saveValue(LoginActivity.this, "profilePic", profile);
                                    }

                                }
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getMessage());
                    }
                });
    }

    private void getThemes() {
        if (MainActivity.themeKEY != null) {
            if (MainActivity.themeKEY.equals("1")) {
                linTop.setBackgroundColor(getResources().getColor(R.color.footercolor));
                linMain.setBackgroundColor(getResources().getColor(R.color.white));
                imgClose.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgEmail.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgPass.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                txtCreateNewOne.setTextColor(getResources().getColor(R.color.darkDray));
                txtproceed.setTextColor(getResources().getColor(R.color.darkDray));
                txtLogin.setTextColor(getResources().getColor(R.color.white));
                txtTitLogin.setTextColor(getResources().getColor(R.color.darkDray));
                txtDontHaveAccount.setTextColor(getResources().getColor(R.color.darkDray));
                txtTitmail.setTextColor(getResources().getColor(R.color.darkDray));
                txtTitPass.setTextColor(getResources().getColor(R.color.darkDray));
                view1.setBackgroundColor(getResources().getColor(R.color.darkDray));
                view2.setBackgroundColor(getResources().getColor(R.color.darkDray));
                linLogin.setBackgroundResource(R.drawable.btn_login_darkgrey);
                edtEmail.setHintTextColor(getResources().getColor(R.color.darkDray));
                edtPassword.setHintTextColor(getResources().getColor(R.color.darkDray));
                edtEmail.setTextColor(getResources().getColor(R.color.darkDray));
                edtPassword.setTextColor(getResources().getColor(R.color.darkDray));

            } else if (MainActivity.themeKEY.equals("0")) {

                linTop.setBackgroundColor(getResources().getColor(R.color.yellow));
                linMain.setBackgroundColor(getResources().getColor(R.color.darkDray));
                imgClose.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgEmail.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgPass.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                txtCreateNewOne.setTextColor(getResources().getColor(R.color.yellow));
                txtproceed.setTextColor(getResources().getColor(R.color.white));
                txtLogin.setTextColor(getResources().getColor(R.color.darkDray));
                txtTitLogin.setTextColor(getResources().getColor(R.color.yellow));
                txtDontHaveAccount.setTextColor(getResources().getColor(R.color.color_gray));
                txtTitmail.setTextColor(getResources().getColor(R.color.white));
                txtTitPass.setTextColor(getResources().getColor(R.color.white));
                view1.setBackgroundColor(getResources().getColor(R.color.yellow));
                view2.setBackgroundColor(getResources().getColor(R.color.yellow));
                linLogin.setBackgroundResource(R.drawable.btn_login_yellow);
                edtEmail.setHintTextColor(getResources().getColor(R.color.color_gray));
                edtPassword.setHintTextColor(getResources().getColor(R.color.color_gray));
                edtEmail.setTextColor(getResources().getColor(R.color.white));
                edtPassword.setTextColor(getResources().getColor(R.color.white));

            }
        } else {
            linTop.setBackgroundColor(getResources().getColor(R.color.yellow));
            linMain.setBackgroundColor(getResources().getColor(R.color.darkDray));
            imgClose.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.darkDray), android.graphics.PorterDuff.Mode.MULTIPLY);
            imgEmail.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            imgPass.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
            txtCreateNewOne.setTextColor(getResources().getColor(R.color.yellow));
            txtproceed.setTextColor(getResources().getColor(R.color.white));
            txtLogin.setTextColor(getResources().getColor(R.color.darkDray));
            txtTitLogin.setTextColor(getResources().getColor(R.color.yellow));
            txtDontHaveAccount.setTextColor(getResources().getColor(R.color.color_gray));
            txtTitmail.setTextColor(getResources().getColor(R.color.white));
            txtTitPass.setTextColor(getResources().getColor(R.color.white));
            view1.setBackgroundColor(getResources().getColor(R.color.yellow));
            view2.setBackgroundColor(getResources().getColor(R.color.yellow));
            linLogin.setBackgroundResource(R.drawable.btn_login_yellow);
            edtEmail.setHintTextColor(getResources().getColor(R.color.color_gray));
            edtPassword.setHintTextColor(getResources().getColor(R.color.color_gray));
            edtEmail.setTextColor(getResources().getColor(R.color.white));
            edtPassword.setTextColor(getResources().getColor(R.color.white));

        }
    }

}