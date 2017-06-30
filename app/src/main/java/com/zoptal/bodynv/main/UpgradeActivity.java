package com.zoptal.bodynv.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoptal.bodynv.R;
import com.zoptal.bodynv.common.AlertDialogMsg;
import com.zoptal.bodynv.model.Membership_List;
import com.zoptal.bodynv.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeActivity extends AppCompatActivity {

    ProgressDialog loading;
    public static Boolean paypalselected=false;
    Boolean creditcrdselected=false;
    String paymnttype;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn,selectedmembr;
    public static String selectedmembrid="null";
    CustomListAdapter adapter;
    boolean singleselect=true;
    private List<Membership_List> membrList = new ArrayList<Membership_List>();
    List  arrayList_storeitems;
    List  arrayList_storeitems_id;
    ListView listview;
    public  StringBuilder commaSepValueBuilder_id;
    public static String weburl,successurl,faliururl;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");

        final ImageView img_disablecreditcard=(ImageView)findViewById(R.id.img_disablecreditcard);
        final ImageView img_disablepaypal=(ImageView)findViewById(R.id.img_disablepaypal);

        Button btn_cncl = (Button)findViewById(R.id.btn_cncl);
        Button btn_confirm = (Button)findViewById(R.id.btn_confirm);


        img_disablecreditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                creditcrdselected=true;
                paypalselected=false;
                img_disablecreditcard.setBackgroundResource(R.drawable.selectedimg);
                img_disablepaypal.setBackgroundResource(R.mipmap.disable);

                paymnttype="Creditcard";
            }
        });

        img_disablepaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                creditcrdselected=false;
                paypalselected=true;
                img_disablepaypal.setBackgroundResource(R.drawable.selectedimg);
                img_disablecreditcard.setBackgroundResource(R.mipmap.disable);

                paymnttype="Paypal";
            }
        });
        btn_cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

//                Intent i = new Intent(UpgradeActivity.this,ProfileActivity.class);
//                startActivity(i);
//                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("creditcrdselected",""+creditcrdselected);
                Log.e("paypalselected",""+paypalselected);

                if(creditcrdselected==false && paypalselected==false)
                {

                    AlertDialogMsg alertmsg = new
                            AlertDialogMsg(UpgradeActivity.this, "Please select payment option.");

                    alertmsg.dialog.show();
                    return;
                }
                else{

                    getmembershiplist();
                    dialogmembership();
                }


            }
        });

      //  dialogpaymntmethod();

    }

//
//    public void dialogpaymntmethod() {
//
//        dialog  = new Dialog(UpgradeActivity.this, android.R.style.Theme_Translucent);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.dialog_choosepayment);
//
//        final ImageView img_disablecreditcard=(ImageView) dialog.findViewById(R.id.img_disablecreditcard);
//        final ImageView img_disablepaypal=(ImageView) dialog.findViewById(R.id.img_disablepaypal);
//
//        Button btn_cncl = (Button) dialog.findViewById(R.id.btn_cncl);
//        Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);
//
//
//        img_disablecreditcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                creditcrdselected=true;
//                paypalselected=false;
//                img_disablecreditcard.setBackgroundResource(R.drawable.selectedimg);
//                img_disablepaypal.setBackgroundResource(R.mipmap.disable);
//
//                paymnttype="Creditcard";
//            }
//        });
//
//        img_disablepaypal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                creditcrdselected=false;
//                paypalselected=true;
//                img_disablepaypal.setBackgroundResource(R.drawable.selectedimg);
//                img_disablecreditcard.setBackgroundResource(R.mipmap.disable);
//
//                paymnttype="Paypal";
//            }
//        });
//        btn_cncl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//
////                Intent i = new Intent(UpgradeActivity.this,ProfileActivity.class);
////                startActivity(i);
////                dialog.dismiss();
//            }
//        });
//
//        btn_confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("creditcrdselected",""+creditcrdselected);
//                Log.e("paypalselected",""+paypalselected);
//
//                if(creditcrdselected==false && paypalselected==false)
//                {
//
//                    AlertDialogMsg alertmsg = new
//                            AlertDialogMsg(UpgradeActivity.this, "Please select payment option.");
//
//                    alertmsg.dialog.show();
//                    return;
//                }
//                else{
//
//                    getmembershiplist();
//                    dialogmembership();
//                }
//
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//
//    }

    public void dialogmembership() {

      dialog = new Dialog(UpgradeActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_membershipplan);

        listview=(ListView)dialog.findViewById(R.id.listview);
        adapter = new CustomListAdapter(membrList);
        listview.setAdapter(adapter);
        arrayList_storeitems  = new ArrayList<String>();
        arrayList_storeitems_id  = new ArrayList<String>();
        Button btn_cncl = (Button) dialog.findViewById(R.id.btn_cncl);
        Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);

        btn_cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
//                Intent i = new Intent(UpgradeActivity.this,ProfileActivity.class);
//                startActivity(i);
//                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                for(int i=0;i<arrayList_storeitems.size();i++) {
//
//                    select_store.setText(arrayList_storeitems.get(i).toString());
//
//                }
//                commaSepValueBuilder_id = new StringBuilder();

                commaSepValueBuilder_id = new StringBuilder();
                for(int i=0;i<arrayList_storeitems_id.size();i++) {


                    commaSepValueBuilder_id.append(arrayList_storeitems_id.get(i));

                    if ( i != arrayList_storeitems_id.size()-1){
                        commaSepValueBuilder_id.append(", ");
                    }
                }
                Log.e("commaSepValueBuilder_id",""+commaSepValueBuilder_id.toString());

                if(paypalselected==true) {
                    if (selectedmembrid.equals("null")) {

                        AlertDialogMsg alertmsg = new
                                AlertDialogMsg(UpgradeActivity.this, "Please select membership plan.");

                        alertmsg.dialog.show();
                        return;

                    }
                    else {
                        getpayment();
                    }
                }
                else {

                    if (selectedmembrid.equals("null")) {

                        AlertDialogMsg alertmsg = new
                                AlertDialogMsg(UpgradeActivity.this, "Please select membership plan.");

                        alertmsg.dialog.show();
                        return;

                    }
                    else {

                        Intent i = new Intent(UpgradeActivity.this, CreditCardPaymentActivity.class);
                        startActivity(i);
                        finish();
                    }
                }


                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void getmembershiplist() {

        loading = new ProgressDialog(UpgradeActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.membership_level,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONArray ary_store= main_obj.getJSONArray("data");

                            for (int i = 0; i < ary_store.length(); i++) {

                                JSONObject obj = ary_store.getJSONObject(i);

                                Membership_List storelistdata = new Membership_List();

                                storelistdata.setName(obj.getString("name"));
                                storelistdata.setId(obj.getString("id"));
                                storelistdata.setStatus(false);


// adding storelistdata to storeList array
                                membrList.add(storelistdata);

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                        adapter.notifyDataSetChanged();
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpgradeActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
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
        requestQueue.add(stringRequest);
    }

    class CustomListAdapter extends BaseAdapter {

        LayoutInflater inflater;
        Context context;

        private List<Membership_List> memberItems;

        public CustomListAdapter(List<Membership_List> memberItems) {

            this.context = context;
            this.memberItems = memberItems;
            inflater = LayoutInflater.from(getApplicationContext());

            // TODO Auto-generated constructor stub
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            return memberItems.size();

        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            // TODO Auto-generated method stub

            Holdler holder = new Holdler();
            convertView = inflater.inflate(R.layout.customview_member, null);

            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.img_check= (ImageView) convertView.findViewById(R.id.img_check);
            holder.tv.setText(memberItems.get(position).getName());



            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    for(int i=0;i<memberItems.size();i++) {

                        if(i==position){
                            memberItems.get(i).setStatus(true);

                        }
                        else {

                            memberItems.get(i).setStatus(false);
                        }
                    }

                    selectedmembr=memberItems.get(position).getName();
                    arrayList_storeitems.add(selectedmembr);


                    selectedmembrid=memberItems.get(position).getId();
                    arrayList_storeitems_id.clear();
                    arrayList_storeitems_id.add(selectedmembrid);

                    Log.e("selectd--"," "+selectedmembr);
                    Log.e("selectd id--","  "+selectedmembrid);

                    notifyDataSetChanged();
                }
            });

            notifyDataSetChanged();


            if (memberItems.get(position).getStatus() == true) {

                holder.img_check.setVisibility(View.VISIBLE);
            } else {
                holder.img_check.setVisibility(View.GONE);
            }



            return convertView;

        }
        class Holdler
        {

            TextView tv;
            ImageView img_check;

        }

    }

    private void getpayment() {

              Log.e("member id-----", "" + selectedmembrid);

            loading = new ProgressDialog(UpgradeActivity.this, R.style.AppCompatAlertDialogStyle);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.paypal_paymnt,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();

                            Log.e("respose===", "" + response);

                            try {
                                JSONObject main_obj = new JSONObject(response);
                                weburl = main_obj.getString("url");
                                successurl = main_obj.getString("success_url");
                                faliururl = main_obj.getString("failure_url");

                                Intent i = new Intent(UpgradeActivity.this, PaymentActivity1.class);
                                startActivity(i);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UpgradeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();

                    map.put("access_token", access_tokn);
                    map.put("checkjavascript", "1");
                    map.put("gateway", "paypalexpress");
                    map.put("javascriptok", "1");
                    map.put("level", selectedmembrid);
                    map.put("submit-checkout", "1");

                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }
}
