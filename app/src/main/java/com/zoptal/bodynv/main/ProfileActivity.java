package com.zoptal.bodynv.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.Transformation;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.zoptal.bodynv.R;
import com.zoptal.bodynv.common.AlertDialogMsg;
import com.zoptal.bodynv.common.NetworkConnection;
import com.zoptal.bodynv.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zotal.102 on 19/04/17.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout rel_member;
    private ImageView user_img;
    private TextView tv_logout,tv_cncl,tv_upgrade,tv_paid,tv_plan;
     private Button btn_chngepw,btn_save;
    private EditText ed_name,ed_email;
    private RelativeLayout img_bck;
    private Bitmap bitmap=null;
    String userimg;
    String access_tokn,membership;
    ProgressDialog loading;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");

        initview();
    }

    public void initview(){

        btn_chngepw=(Button)findViewById(R.id.btn_chngepw);
        btn_save=(Button)findViewById(R.id.btn_save);
        img_bck=(RelativeLayout)findViewById(R.id.img_bck);
        user_img=(ImageView) findViewById(R.id.user_img);
        ed_name=(EditText)findViewById(R.id.ed_name);
        ed_email=(EditText)findViewById(R.id.ed_email);
        tv_logout=(TextView)findViewById(R.id.tv_logout);
        rel_member=(LinearLayout)findViewById(R.id.rel_member);

        tv_paid=(TextView)findViewById(R.id.tv_paid);
        tv_plan=(TextView)findViewById(R.id.tv_plan);

        tv_cncl=(TextView)findViewById(R.id.tv_cncl);
        tv_upgrade=(TextView)findViewById(R.id.tv_upgrade);

        //  img_user.setOnClickListener(this);
          btn_chngepw.setOnClickListener(this);
          img_bck.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        user_img.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        tv_cncl.setOnClickListener(this);
        tv_upgrade.setOnClickListener(this);


        if (NetworkConnection.isConnectedToInternet(ProfileActivity.this)) {

            getprofile();

        }
        else {
            // Toast.makeText(LoginActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(ProfileActivity.this, "Please Check your internet connection.");

            alertmsg.dialog.show();
            return;

        }



    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.btn_chngepw:


                Intent i = new Intent(ProfileActivity.this,ChangepwActivity.class);
                startActivity(i);
                finish();

                break;

            case R.id.btn_save:


                if (NetworkConnection.isConnectedToInternet(ProfileActivity.this)) {

                    userprofileupdate();

                }
                else {
                    // Toast.makeText(LoginActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();

                    AlertDialogMsg alertmsg = new
                            AlertDialogMsg(ProfileActivity.this, "Please Check your internet connection.");

                    alertmsg.dialog.show();
                    return;

                }




                break;

            case R.id.img_bck:

                Log.e("clicked om img-==wew","ok");

                Intent intnt = new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intnt);
                finish();
                break;

            case R.id.user_img:
                Crop.pickImage(this);
                break;

            case R.id.tv_logout:

                dialoglogout();
                break;

            case R.id.tv_cncl:

              dialogconfirmcncl();
                break;


            case R.id.tv_upgrade:

                Intent intent = new Intent(ProfileActivity.this,UpgradeActivity.class);
                startActivity(intent);



                break;

        }



    }
    public void dialoglogout() {

   final  Dialog  dialog = new Dialog(ProfileActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_logout);

        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);

        TextView tv_yes=(TextView)dialog.findViewById(R.id.tv_yes);
        TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);



        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                userLogout();

            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    public void dialogconfirmcncl() {

        final  Dialog  dialog = new Dialog(ProfileActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_logout);

        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        TextView tv_msg=(TextView)dialog.findViewById(R.id.tv_msg);
        TextView tv_yes=(TextView)dialog.findViewById(R.id.tv_yes);
        TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);

          tv_msg.setText("Are you sure you want to cancel membership?");

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cnclmembership();
                dialog.dismiss();

            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);

    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            user_img.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
//    private void beginCrop(Uri source) {
//        Log.e("test crop===","test");
//        Uri destination = Uri.fromFile(new File(ProfileActivity.this.getCacheDir(), "cropped"));
//        Crop.of(source, destination).asSquare().start(ProfileActivity.this, 2);
//    }
//
//    private void handleCrop(int resultCode, Intent result) {
//
//        if (resultCode == RESULT_OK) {
//
//            user_img.setImageURI(null);
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            user_img.setImageURI(Crop.getOutput(result));
//
//
//        } else if (resultCode == Crop.RESULT_ERROR) {
//            Toast.makeText(ProfileActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
        // For API >= 23 we need to check specifically that we have permissions to read external storage,
        // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
//        boolean requirePermissions = false;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//              ) {
//
//            // request permissions and handle the result in onRequestPermissionsResult()
//            requirePermissions = true;
//
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
//        }
//
//        if (!requirePermissions) {
//
//        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void getprofile() {

        loading = new ProgressDialog(ProfileActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.myprofile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONObject  obj=main_obj.getJSONObject("data");

                             String   email  =obj.getString("email");
                            String name =obj.getString("name");
                            String img=obj.getString("profile_pic");
                             membership=obj.getString("membership");

                            ed_name.setText(name);
                            ed_email.setText(email);

                            membership_usr();
                            if(membership.equals("true")){
                                rel_member.setVisibility(View.VISIBLE);

                                tv_upgrade.setVisibility(View.GONE);
                                tv_cncl.setVisibility(View.VISIBLE);
                            }
                            else {
                                rel_member.setVisibility(View.GONE);

                                tv_upgrade.setVisibility(View.VISIBLE);
                                tv_cncl.setVisibility(View.GONE);
                            }

                            if(img.isEmpty()){

                            }
                          else{
                                Picasso.with(ProfileActivity.this).load(img).into(user_img);
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
                        Toast.makeText(ProfileActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_tokn);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }



    private void userprofileupdate(){

        final String username = ed_name.getText().toString().trim();
        final String email = ed_email.getText().toString().trim();



        if(username.isEmpty()){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(ProfileActivity.this, "Please enter the name first.");

            alertmsg.dialog.show();
            return;
        }



        if(email.isEmpty()){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(ProfileActivity.this, "Please enter email address.");

            alertmsg.dialog.show();
            return;
        }

        if(bitmap == null){
//            bitmap=((BitmapDrawable)user_img.getDrawable()).getBitmap();
//            userimg=getStringImage(bitmap);

            BitmapDrawable drawable = (BitmapDrawable) user_img.getDrawable();
            bitmap = drawable.getBitmap();
            userimg = getStringImage(bitmap);

        }
        try {
            if (bitmap != null) {
                userimg = getStringImage(bitmap);

            }
        }
        catch (NullPointerException e){

        }

        loading = new ProgressDialog(ProfileActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.updateprofile,
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
                                Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                String message =main_obj.getString("message");
                                Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_LONG).show();

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
                params.put("name",username);
                params.put("email",email);
                if(userimg==null){
                    userimg="";
                }

                params.put("profile_pic",userimg);
                params.put("access_token",access_tokn);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void userLogout() {

        loading = new ProgressDialog(ProfileActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.logout,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(ProfileActivity.this," Logout Successfully",Toast.LENGTH_SHORT).show();

                        Log.e("respose===",""+response);


                        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                        editor1.putString("id","");
                        editor1.putString("access_token","");
                        editor1.putString("username","");
                        editor1.putString("userpw","");
                        editor1.commit();

                        Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_tokn);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void cnclmembership() {

        loading = new ProgressDialog(ProfileActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.cncl_membership,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String code =main_obj.getString("code");
                            if(code.equals("201")){
                                String message =main_obj.getString("message");
                                Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {

                                String message =main_obj.getString("message");
                                String membership =main_obj.getString("membership");

                                SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                                editor1.putString("membership",membership);

                                editor1.commit();


                                Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_LONG).show();
                                getprofile();

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
                        Toast.makeText(ProfileActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_tokn);
                map.put("confirm","true");

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void membership_usr() {

        loading = new ProgressDialog(ProfileActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.membership_user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);


                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONArray ary_store= main_obj.getJSONArray("mylevels");

                            for (int i = 0; i < ary_store.length(); i++) {

                                JSONObject obj = ary_store.getJSONObject(i);

                                tv_plan.setText(obj.getString("name"+" - "));
                                tv_paid.setText(obj.getString("$  "+"initial_payment"));
                              //  Home_List storelistdata = new Home_List();

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
                        Toast.makeText(ProfileActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_tokn);

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
