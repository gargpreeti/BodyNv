package com.zoptal.bodynv.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.zoptal.bodynv.R;
import com.zoptal.bodynv.common.NetworkConnection;
import com.zoptal.bodynv.databinding.ItemViewBinding;
import com.zoptal.bodynv.lib.*;
import com.zoptal.bodynv.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by zotal.102 on 19/04/17.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  //  RelativeLayout mRlCenter;
    private ImageView img_user;
   // private ArrayList<Integer> images;
   // public static   GalleryAdapter mAdapter;
    private List<Home_List> homeList = new ArrayList<Home_List>();
    ProgressDialog loading;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn;
    public static String membership;
       private Button btn_upgrade;
    private ArrayList<Integer> mData = new ArrayList<Integer>(0);

    com.zoptal.bodynv.lib.CarouselLayoutManager mCaurausalManager;
    public static int lastpos = 0, pos = 0,textview1=0,textview2=0;
    public static String vidourl = "";
    RecyclerView mRecylerView;

    public static boolean isScroll=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");
        membership = sharedpreferences1.getString("membership", "");


        textview1=1;textview2=2;
        isScroll=false;
        if (NetworkConnection.isConnectedToInternet(MainActivity.this)) {

            gethomefeed();
        } else {
            Toast.makeText(MainActivity.this, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
            return;

        }

        initview();
    }

    public void initview(){

      btn_upgrade=(Button)findViewById(R.id.btn_upgrade);

        try {
            mRecylerView = (RecyclerView) findViewById(R.id.list_vertical);
            img_user=(ImageView)findViewById(R.id.img_user);
            img_user.setOnClickListener(this);
            btn_upgrade.setOnClickListener(this);

            if(membership.equals("false")){

                btn_upgrade.setVisibility(View.VISIBLE);

            }
            else{
                btn_upgrade.setVisibility(View.GONE);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.img_user:

                Intent i = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.btn_upgrade:

                Intent intent = new Intent(MainActivity.this,UpgradeActivity.class);
                startActivity(intent);

                break;
        }

    }

    private void gethomefeed(){

        loading = new ProgressDialog(MainActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, RegisterUrl.homefeeds,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        //  Toast.makeText(Business_ChangePwActivity.this,"User Registration Successfully",Toast.LENGTH_LONG).show();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONArray ary_store= main_obj.getJSONArray("listing_data");

                            for (int i = 0; i < ary_store.length(); i++) {

                                JSONObject obj = ary_store.getJSONObject(i);
                                Home_List storelistdata = new Home_List();

                                storelistdata.setImgae_url(obj.getString("imgae_url"));
                                storelistdata.setId(obj.getString("id"));
                                storelistdata.setVideo_url(obj.getString("video_url"));
                                storelistdata.setVideo_name(obj.getString("video_name"));
                                storelistdata.setVideo_time(obj.getString("video_time"));
                                storelistdata.setDay_number(obj.getString("day_number"));

                                homeList.add(storelistdata);
                            }

                        final TestAdapter adapter = new TestAdapter(homeList);

                        mCaurausalManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true);
                        // create layout manager with needed params: vertical, cycle
//                            initRecyclerView(binding.listHorizontal, new SliderLayoutManager(SliderLayoutManager.HORIZONTAL, false), adapter);
                        initRecyclerView(mRecylerView, mCaurausalManager, adapter);

                        mRecylerView.setOnScrollListener(new CenterScrollListener());
                    } catch (JSONException e) {
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

                params.put("access_token",access_tokn);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {

        //  super.onBackPressed();
        this.moveTaskToBack(true);

    }

    private void initRecyclerView(final RecyclerView recyclerView, final CarouselLayoutManager sliderLayoutManager, final TestAdapter adapter) {
        // enable zoom effect. this line can be customized
        sliderLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        sliderLayoutManager.setMaxVisibleItems(2);

        recyclerView.setLayoutManager(sliderLayoutManager);
        // we expect only fixed sized item for now
        recyclerView.setHasFixedSize(true);
        // sample adapter with random data
        recyclerView.setAdapter(adapter);
        // enable center post scrolling
        recyclerView.addOnScrollListener(new CenterScrollListener());
//        // enable center post touching on item and item click listener
//        com.zoptal.bodynv.lib.DefaultChildSelectionListener.initCenterItemListener(new com.zoptal.bodynv.lib.DefaultChildSelectionListener.OnCenterItemClickListener() {
//            @Override
//            public void onCenterItemClicked(@NonNull final RecyclerView recyclerView, @NonNull final CarouselLayoutManager sliderLayoutManager, @NonNull final View v) {
//                final int position = recyclerView.getChildLayoutPosition(v);
//                final String msg = String.format(Locale.US, "Item %1$d was clicked", position);
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//            }
//        }, recyclerView, sliderLayoutManager);
//
//        sliderLayoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {
//
//            @Override
//            public void onCenterItemChanged(final int adapterPosition) {
//                if (CarouselLayoutManager.INVALID_POSITION != adapterPosition) {
//
////                    final int value = adapter.mPosition[adapterPosition];
///*
//                    adapter.mPosition[adapterPosition] = (value % 10) + (value / 10 + 1) * 10;
//                    adapter.notifyItemChanged(adapterPosition);
//*/
//                }
//            }
//        });
    }
    class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {
        List<Home_List> mHomeList;

        public TestAdapter(List<Home_List> homeList) {
            this.mHomeList=homeList;
        }

        @Override
        public TestViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            return new TestViewHolder(ItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(final TestViewHolder holder, final int position) {
            Log.e("==position=", mCaurausalManager.getCenterItemPosition() + "=====" + lastpos+"===="+position);
            lastpos = pos;

            Glide.with(MainActivity.this).load(mHomeList.get(position).getImgae_url()).diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .bitmapTransform(new BlurTransformation(CarouselPreviewActivity.this))
                    .into(holder.mItemViewBinding.cItem1);

//            if (position == pos) {
//                holder.mItemViewBinding.cItem1.setAlpha(1f);
//            } else {
//                holder.mItemViewBinding.cItem1.setAlpha(0.3f);
//            }
//
//
//            if(position==MainActivity.textview1 || position==MainActivity.textview2){
//                holder.mItemViewBinding.textviewDay.setVisibility(View.GONE);
//            }else{
//                holder.mItemViewBinding.textviewDay.setVisibility(View.VISIBLE);
//            }
            if (position == pos) {
                holder.mItemViewBinding.cItem1.setAlpha(1f);
            } else {
                holder.mItemViewBinding.cItem1.setAlpha(0.3f);
            }
            if(isScroll){
                if (position == pos) {
                    holder.mItemViewBinding.textviewDay.setPadding(10,10,120,10);
                } else {
                    holder.mItemViewBinding.textviewDay.setPadding(10,10,10,10);
                }
            }else{
                holder.mItemViewBinding.textviewDay.setPadding(10,10,10,10);
            }
            if(position==MainActivity.textview1 || position==MainActivity.textview2){
                holder.mItemViewBinding.textviewDay.setVisibility(View.GONE);
            }else{
                holder.mItemViewBinding.textviewDay.setVisibility(View.VISIBLE);
            }

            holder.mItemViewBinding.textviewDay.setText("DAY" + " " +mHomeList.get(position).getDay_number());

            holder.mItemViewBinding.cItem1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (MainActivity.membership.equals("false")) {

                        Intent intent = new Intent(MainActivity.this,UpgradeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        if (mHomeList.get(position).getVideo_url().contains("https://www.youtube.com/embed/")) {
                            vidourl = mHomeList.get(position).getVideo_url().replace("https://www.youtube.com/embed/", "");
                        }
                        Intent i = new Intent(MainActivity.this, TimerActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mHomeList.size();
        }
    }

    private static class TestViewHolder extends RecyclerView.ViewHolder {

        private final ItemViewBinding mItemViewBinding;

        TestViewHolder(final ItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());

            mItemViewBinding = itemViewBinding;
        }
    }

}


