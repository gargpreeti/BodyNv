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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoptal.bodynv.R;
import com.zoptal.bodynv.common.AlertDialogMsg;
import com.zoptal.bodynv.common.NetworkConnection;
import com.zoptal.bodynv.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zotal.102 on 19/04/17.
 */
public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_name,ed_pass,ed_email;
    private ImageView  img_disablecreditcard,img_disablepaypal;
    private Button btn_signup;
    private RelativeLayout img_bck;
    public static Boolean paypalselected=false;
    Boolean creditcrdselected=false;
    ProgressDialog loading;
    public static  String paymnttype;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    public static String username,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        initview();
    }

    public void initview(){

        ed_name=(EditText)findViewById(R.id.ed_name);
        ed_pass=(EditText)findViewById(R.id.ed_pass);
        ed_email=(EditText)findViewById(R.id.ed_email);

        img_bck=(RelativeLayout)findViewById(R.id.img_bck);

        img_disablecreditcard=(ImageView)findViewById(R.id.img_disablecreditcard);
        img_disablepaypal=(ImageView)findViewById(R.id.img_disablepaypal);
        btn_signup=(Button)findViewById(R.id.btn_signup);


        btn_signup.setOnClickListener(this);
        img_bck.setOnClickListener(this);
        img_disablecreditcard.setOnClickListener(this);
        img_disablepaypal.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_signup:


                if (NetworkConnection.isConnectedToInternet(SignupActivity.this)) {


                    registerUser();
                }
                else {
                    AlertDialogMsg alertmsg = new
                            AlertDialogMsg(SignupActivity.this, "Please Check your internet connection.");

                    alertmsg.dialog.show();
                    return;

                }


             break;

            case R.id.img_bck:


                Intent i = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.img_disablecreditcard:

                creditcrdselected=true;
                paypalselected=false;
                 img_disablecreditcard.setBackgroundResource(R.drawable.selectedimg);
                img_disablepaypal.setBackgroundResource(R.mipmap.disable);

                paymnttype="Creditcard";

                break;

            case R.id.img_disablepaypal:

                creditcrdselected=false;
                paypalselected=true;
                img_disablepaypal.setBackgroundResource(R.drawable.selectedimg);
                img_disablecreditcard.setBackgroundResource(R.mipmap.disable);

                paymnttype="Paypal";

                break;

        }



    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    // validating password with retype password
    private boolean isValidPassword(String password) {
        if (password != null && password.length() > 7) {
            return true;
        }
        return false;
    }


    private void registerUser() {

     username = ed_name.getText().toString().trim();
      email = ed_email.getText().toString().trim();
      password = ed_pass.getText().toString().trim();

        if (username.isEmpty()) {
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(SignupActivity.this, "Please enter your Username.");

            alertmsg.dialog.show();
            return;

        }
        if (email.isEmpty()) {
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(SignupActivity.this, "Please enter your Email.");

            alertmsg.dialog.show();
            return;
        }

        if (!isValidEmail(email)) {
            ed_email.setError("Invalid Email");
            return;
        }

        if (password.isEmpty()) {
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(SignupActivity.this, "Please enter your Password.");

            alertmsg.dialog.show();
            return;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(SignupActivity.this, "Password must contain minimum 8 characters.", Toast.LENGTH_SHORT).show();
            return;
        }


//        if (creditcrdselected == false && paypalselected == false) {
//
//            AlertDialogMsg alertmsg = new
//                    AlertDialogMsg(SignupActivity.this, "Please select payment option.");
//
//            alertmsg.dialog.show();
//            return;
//        }


        loading = new ProgressDialog(SignupActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.signup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose signup===",""+response);
//
                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String msg =main_obj.getString("message");

                            Toast.makeText(SignupActivity.this,msg,Toast.LENGTH_LONG).show();

                            JSONObject  obj=main_obj.getJSONObject("data");

                            String username =obj.getString("username");
                            String email =obj.getString("email");
                            String  access_token =obj.getString("access_token");
                            String membership =obj.getString("membership");

                            SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                            editor1.putString("username",username);
                            editor1.putString("email",email);
                            editor1.putString("access_token",access_token);
                            editor1.putString("membership",membership);
                            editor1.commit();


//                          Intent intentwillow= new Intent(SignupActivity.this, MembershipActivity.class);
//                          startActivity(intentwillow);

        Intent intent = new Intent(SignupActivity.this, BannerActivity.class);
        startActivity(intent);


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("email",email);
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

    //params.put("payment_type",paymnttype);

}
