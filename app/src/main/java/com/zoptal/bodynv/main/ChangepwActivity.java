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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoptal.bodynv.R;
import com.zoptal.bodynv.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zotal.102 on 19/04/17.
 */
public class ChangepwActivity extends AppCompatActivity implements View.OnClickListener {

     private ImageView img_bck;
    private EditText ed_oldpw,ed_newpass,ed_confirmpass;
     private Button btn_save;

    String access_tokn;
    ProgressDialog loading;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepw);


        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");
        initview();
    }

    public void initview(){

         img_bck=(ImageView)findViewById(R.id.img_bck);
        ed_oldpw=(EditText)findViewById(R.id.ed_oldpw);
        ed_newpass=(EditText)findViewById(R.id.ed_newpass);
        ed_confirmpass=(EditText)findViewById(R.id.ed_confirmpass);
        btn_save=(Button)findViewById(R.id.btn_save);

        img_bck.setOnClickListener(this);
        btn_save.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.img_bck:

                Log.e("clicked om img-==wew","ok");

             Intent i = new Intent(ChangepwActivity.this,ProfileActivity.class);
            startActivity(i);
                finish();
             break;
//
            case R.id.btn_save:

                Log.e("clicked om img-==","ok");
              changepw();
                break;

        }



    }





    private void changepw(){

        final String oldpw = ed_oldpw.getText().toString().trim();
        final String newpass = ed_newpass.getText().toString().trim();
        final String confirmpw=ed_confirmpass.getText().toString().trim();



        if (!isValidPassword(newpass)) {
            Toast.makeText(ChangepwActivity.this,"Password must contain minimum 8 characters.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!confirmpw.equals(newpass)){
            ed_confirmpass.setError("Password and confirm password should be same");
            return;
        }

        loading = new ProgressDialog(ChangepwActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.changepw,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        //  Toast.makeText(Business_ChangePwActivity.this,"User Registration Successfully",Toast.LENGTH_LONG).show();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String code =main_obj.getString("code");
                            if(code.equals("201")){
                                String message =main_obj.getString("message");
                                Toast.makeText(ChangepwActivity.this,message,Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                String message =main_obj.getString("message");
                                Toast.makeText(ChangepwActivity.this,message,Toast.LENGTH_LONG).show();

                                ed_oldpw.setText("");
                                ed_newpass.setText("");
                                ed_confirmpass.setText("");
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(Business_AccountActivity.this, error.toString(), Toast.LENGTH_LONG).show();


                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("old password",oldpw);
                params.put("new password",newpass);
                params.put("access_token",access_tokn);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean isValidPassword(String password) {
        if (password != null && password.length() > 7) {
            return true;
        }
        return false;
    }

}
