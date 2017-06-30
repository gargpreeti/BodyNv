package com.zoptal.bodynv.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoptal.bodynv.R;
import com.zoptal.bodynv.common.AlertDialogMsg;
import com.zoptal.bodynv.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zotal.102 on 19/04/17.
 */
public class ForgotPwActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_email;
    private RelativeLayout img_bck;
    private Button btn_continue,btn_cncl;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpw);

        initview();
    }

    public void initview(){


        ed_email=(EditText)findViewById(R.id.ed_email);
        img_bck=(RelativeLayout) findViewById(R.id.img_bck);

        btn_continue=(Button)findViewById(R.id.btn_continue);
        btn_cncl=(Button)findViewById(R.id.btn_cncl);


        btn_cncl.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        img_bck.setOnClickListener(this);
//        tv_forgotpw.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_continue:

                forgotpw();

             break;

            case R.id.btn_cncl:


                Intent i = new Intent(ForgotPwActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.img_bck:


                Intent intent = new Intent(ForgotPwActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }


    private void forgotpw() {

        final String email=ed_email.getText().toString().trim();

        if(email.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(ForgotPwActivity.this, "Please enter your email.");

            alertmsg.dialog.show();
            return;
        }

        loading = new ProgressDialog(ForgotPwActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.forgotpw,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String msg = main_obj.getString("message");


                            AlertDialogMsg alertmsg = new
                                    AlertDialogMsg(ForgotPwActivity.this,msg);

                            alertmsg.dialog.show();

                           // Toast.makeText(ForgotPwActivity.this,msg,Toast.LENGTH_SHORT).show();

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Toast.makeText(Business_ForgotActivity.this," Logout Successfully",Toast.LENGTH_SHORT).show();

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotPwActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("email",email);

                return map;
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
