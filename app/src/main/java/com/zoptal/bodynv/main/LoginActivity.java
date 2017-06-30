package com.zoptal.bodynv.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.zoptal.bodynv.R;
import com.zoptal.bodynv.common.AlertDialogMsg;
import com.zoptal.bodynv.common.NetworkConnection;
import com.zoptal.bodynv.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by zotal.102 on 19/04/17.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_name,ed_pass;
    private TextView tv_forgotpw;
    private ImageView img_disable,img_selected;
    private Button btn_login,btn_joinus;
    ProgressDialog loading;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    Boolean keepsigned=false;
    String userpw,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        username = sharedpreferences1.getString("username", "");
        userpw = sharedpreferences1.getString("userpw", "");
        initview();

    }

    public void initview(){

        ed_name=(EditText)findViewById(R.id.ed_name);
        ed_pass=(EditText)findViewById(R.id.ed_pass);
        img_disable=(ImageView)findViewById(R.id.img_disable);
        img_selected=(ImageView)findViewById(R.id.img_selected);
        tv_forgotpw=(TextView) findViewById(R.id.tv_forgotpw);
        btn_login=(Button) findViewById(R.id.btn_login);
        btn_joinus=(Button) findViewById(R.id.btn_joinus);


        ed_name.setText(username);
        ed_pass.setText(userpw);

        btn_login.setOnClickListener(this);
        btn_joinus.setOnClickListener(this);
        tv_forgotpw.setOnClickListener(this);
        img_disable.setOnClickListener(this);
        img_selected.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_login:

                if (NetworkConnection.isConnectedToInternet(LoginActivity.this)) {

                    login();

                }
                else {
                   // Toast.makeText(LoginActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();

                    AlertDialogMsg alertmsg = new
                            AlertDialogMsg(LoginActivity.this, "Please Check your internet connection.");

                    alertmsg.dialog.show();
                    return;

                }


//                Intent intentwillow= new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intentwillow);
//                finish();

                break;

            case R.id.btn_joinus:

                Intent intnt = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intnt);
              finish();
                break;


            case R.id.tv_forgotpw:

                Intent intent = new Intent(LoginActivity.this,ForgotPwActivity.class);
                startActivity(intent);
                break;

            case R.id.img_disable:

                keepsigned=true;

                img_disable.setVisibility(View.GONE);
                img_selected.setVisibility(View.VISIBLE);
                break;
            case R.id.img_selected:

                keepsigned=false;

                img_disable.setVisibility(View.VISIBLE);
                img_selected.setVisibility(View.GONE);
                break;
        }

    }

    private void login(){


        final String username=ed_name.getText().toString().trim();
        final String password=ed_pass.getText().toString().trim();

        if(username.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(LoginActivity.this, "Please enter your Username.");

            alertmsg.dialog.show();
            return;

        }


        if(password.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(LoginActivity.this, "Please enter your Password.");

            alertmsg.dialog.show();
            return;
        }


        loading = new ProgressDialog(LoginActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose login===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String msg =main_obj.getString("message");

                            Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();

                            JSONObject  obj=main_obj.getJSONObject("data");

                            String username =obj.getString("username");
                            String email =obj.getString("email");
                            String access_token =obj.getString("access_token");
                            String membership =obj.getString("membership");

                            if(keepsigned==false){

                                SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                                editor1.putString("username",username);
                                editor1.putString("email",email);
                                editor1.putString("access_token",access_token);
                                editor1.putString("userpw","");
                                editor1.putString("membership",membership);
                                editor1.commit();

                            }

                            if(keepsigned==true) {


                                userpw=password;

                                SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                                editor1.putString("username",username);
                                editor1.putString("email",email);
                                editor1.putString("access_token",access_token);
                                editor1.putString("userpw",userpw);
                                editor1.putString("membership",membership);
                                editor1.commit();
                            }

                            Intent intentwillow = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intentwillow);
                            finish();
//              if(membership.equals("false")) {
//                  Intent intentwillow = new Intent(LoginActivity.this, BannerActivity.class);
//                  startActivity(intentwillow);
//                  finish();
//              }
//                            else{
//                  Intent intentwillow = new Intent(LoginActivity.this, MainActivity.class);
//                  startActivity(intentwillow);
//                  finish();
//
//              }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("password", password);
                params.put("device_type", "android");
                params.put("device_token", "12345678");
                return params;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {

        //  super.onBackPressed();
        this.moveTaskToBack(true);

    }
}
