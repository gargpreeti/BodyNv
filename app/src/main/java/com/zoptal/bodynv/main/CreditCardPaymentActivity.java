package com.zoptal.bodynv.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
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
import com.zoptal.bodynv.R;
import com.zoptal.bodynv.common.AlertDialogMsg;
import com.zoptal.bodynv.common.GooglePlacesAutocompleteAdapter;
import com.zoptal.bodynv.model.Card_type;
import com.zoptal.bodynv.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreditCardPaymentActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

   private ProgressDialog loading;
   private EditText ed_fname,ed_lastname,ed_country,ed_state,ed_phnno,ed_email,ed_confirmemail,ed_city,ed_zipcode,ed_accountnum,ed_cardtype,ed_cvvnumber,ed_mnth,ed_year;
    private Button btn_pay;
   private  AutoCompleteTextView ed_address1,ed_addresss2;

    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
   String access_tokn;
    private  ListView listview;
    private ArrayList<Card_type> arrayofcardtype = new ArrayList<Card_type>();
    private CustomListAdapterCardtype adapter;
    private String text;
    private ImageView img_bck;
    Spinner list_mnth;
    int year = Calendar.getInstance().get(Calendar.YEAR);
String selectedmnth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_payment);


        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");

        arrayofcardtype.add(new Card_type("Visa",false));
        arrayofcardtype.add(new Card_type("MasterCard",false));
        arrayofcardtype.add(new Card_type("American Express",false));
        arrayofcardtype.add(new Card_type("Discover",false));

        img_bck=(ImageView)findViewById(R.id.img_bck);
        ed_fname=(EditText)findViewById(R.id.ed_fname);
        ed_lastname=(EditText)findViewById(R.id.ed_lastname);
        ed_country=(EditText)findViewById(R.id.ed_country);
        ed_state=(EditText)findViewById(R.id.ed_state);
        ed_phnno=(EditText)findViewById(R.id.ed_phnno);
        ed_email=(EditText)findViewById(R.id.ed_email);
        ed_confirmemail=(EditText)findViewById(R.id.ed_confirmemail);
        ed_city=(EditText)findViewById(R.id.ed_city);
        ed_zipcode=(EditText)findViewById(R.id.ed_zipcode);
        ed_accountnum=(EditText)findViewById(R.id.ed_accountnum);
        ed_cardtype=(EditText)findViewById(R.id.ed_cardtype);
        ed_cvvnumber=(EditText)findViewById(R.id.ed_cvvnumber);
        ed_mnth=(EditText)findViewById(R.id.ed_mnth);
        ed_year=(EditText)findViewById(R.id.ed_year);
        list_mnth=(Spinner)findViewById(R.id.list_mnth);
        btn_pay=(Button)findViewById(R.id.btn_pay);

        ed_mnth.setInputType(InputType.TYPE_NULL);
        ed_mnth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_mnth.setVisibility(View.INVISIBLE);
                list_mnth.setVisibility(View.VISIBLE);
            }
        });

        String[] items_mnth = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(CreditCardPaymentActivity.this, android.R.layout.simple_spinner_dropdown_item, items_mnth);
        list_mnth.setAdapter(adapter1);
        //   list_mnth.setPrompt("Exp.[MM]");
        list_mnth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                // ed_exp.setText(list_mnth.getSelectedItem().toString().trim());
                ((TextView) list_mnth.getSelectedView()).setTextColor(getResources().getColor(R.color.colorwhite));
                selectedmnth=list_mnth.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        ed_year.setInputType(InputType.TYPE_NULL);
        ed_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showYearDialog();
            }
        });

        ed_address1=(AutoCompleteTextView)findViewById(R.id.ed_address1);
        ed_addresss2=(AutoCompleteTextView)findViewById(R.id.ed_addresss2);

        ed_address1.setAdapter(new GooglePlacesAutocompleteAdapter(CreditCardPaymentActivity.this, R.layout.list_item));
        ed_addresss2.setAdapter(new GooglePlacesAutocompleteAdapter(CreditCardPaymentActivity.this, R.layout.list_item));


        ed_cardtype.setInputType(InputType.TYPE_NULL);
        ed_cardtype.setFocusable(false);
        ed_cardtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_cardtype();
            }
        });
        btn_pay.setOnClickListener(this);

        img_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CreditCardPaymentActivity.this,MembershipActivity.class);
                startActivity(i);
                finish();
            }
        });



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_pay:

                getpayment();

                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    private void getpayment() {

        final String fname=ed_fname.getText().toString().trim();
        final String lstname=ed_lastname.getText().toString().trim();
        final String cntry=ed_country.getText().toString().trim();
        final String state=ed_state.getText().toString().trim();
        final String phnno=ed_phnno.getText().toString().trim();
        final String email=ed_email.getText().toString().trim();
        final String confrmemail=ed_confirmemail.getText().toString().trim();
        final String city=ed_city.getText().toString().trim();
        final String zpcode=ed_zipcode.getText().toString().trim();
        final String accountnum=ed_accountnum.getText().toString().trim();
        final String crdtype=ed_cardtype.getText().toString().trim();
        final String cvnum=ed_cvvnumber.getText().toString().trim();
     //   final String mnth=ed_mnth.getText().toString().trim();
        final String year_val=ed_year.getText().toString().trim();
        final String address1=ed_address1.getText().toString().trim();
        final String address2=ed_addresss2.getText().toString().trim();


        if(fname.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your First name.");

            alertmsg.dialog.show();
            return;

        }
        if(lstname.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your Last name.");

            alertmsg.dialog.show();
            return;

        }
        if(cntry.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your Country");

            alertmsg.dialog.show();
            return;

        }
        if(state.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your State");

            alertmsg.dialog.show();
            return;

        }
        if(phnno.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your Phone no.");

            alertmsg.dialog.show();
            return;
        }

        if(email.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your Email");

            alertmsg.dialog.show();
            return;
        }
        if (!isValidEmail(email)) {
            ed_email.setError("Invalid Email");
            return;
        }
        if(confrmemail.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your Confirm Email");

            alertmsg.dialog.show();
            return;
        }

        if (!isValidEmail(email)) {
            ed_confirmemail.setError("Invalid Email");
            return;
        }
        if(city.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your City");

            alertmsg.dialog.show();
            return;
        }

        if(zpcode.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your Zip code");

            alertmsg.dialog.show();
            return;
        }

        if(accountnum.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your Account num");

            alertmsg.dialog.show();
            return;
        }

        if(crdtype.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your Card");

            alertmsg.dialog.show();
            return;
        }

        if(cvnum.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter CVV ");

            alertmsg.dialog.show();
            return;
        }

        if(selectedmnth.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter Month ");

            alertmsg.dialog.show();
            return;
        }



        if(year_val.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter Year ");

            alertmsg.dialog.show();
            return;
        }
        if(address1.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your address ");

            alertmsg.dialog.show();
            return;
        }
        if(address2.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(CreditCardPaymentActivity.this, "Please enter your address ");

            alertmsg.dialog.show();
            return;
        }


        loading = new ProgressDialog(CreditCardPaymentActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.paypal_paymnt,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);


                        try {
                            JSONObject main_obj = new JSONObject(response);
                          String code=main_obj.getString("code");
                            String msg=main_obj.getString("message");

                            if(code.equals("200")){

                           Toast.makeText(CreditCardPaymentActivity.this, msg, Toast.LENGTH_LONG).show();

                           String membership=main_obj.getString("membership");

//                                JSONObject  obj=main_obj.getJSONObject("data");
//
//                            String username =obj.getString("username");
//                            String email =obj.getString("email");
//                            access_tokn =obj.getString("access_token");
//
                             SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                             editor1.putString("membership",membership);

                             editor1.commit();


                           Intent i = new Intent(CreditCardPaymentActivity.this,MainActivity.class);
                           startActivity(i);

                       }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreditCardPaymentActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("bfirstname",fname);
                map.put("blastname",lstname);
                map.put("CVV",cvnum);
                map.put("bcountry",cntry);
                map.put("baddress1",address1);
                map.put("baddress2",address2);
                map.put("bcity",city);
                map.put("bstate",state);
                map.put("bzipcode",zpcode);
                map.put("bphone",phnno);
                map.put("bemail",email);
                map.put("bconfirmemail",confrmemail);
                map.put("gateway","payflowpro");
                map.put("submit-checkout","1");
                map.put("level",UpgradeActivity.selectedmembrid);
                map.put("checkjavascript","1");
                map.put("javascriptok","1");
                map.put("ExpirationMonth",selectedmnth);
                map.put("ExpirationYear",year_val);
                map.put("CardType",crdtype);
                map.put("AccountNumber",accountnum);
                map.put("access_token",access_tokn);
                 map.put("payment_type","Creditcard");
                 map.put("device_type", "android");
                map.put("device_token", "12345678");


                Log.e("map","  "+map);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void dialog_cardtype() {

      final Dialog  dialog = new Dialog(CreditCardPaymentActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_cardtype);

        TextView tv_head=(TextView)dialog.findViewById(R.id.tv_head);
        tv_head.setText("Select");
        listview=(ListView)dialog.findViewById(R.id.listview);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Button btn_cncl = (Button) dialog.findViewById(R.id.btn_cncl);
        Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);

        adapter = new CustomListAdapterCardtype(CreditCardPaymentActivity.this,arrayofcardtype);
        listview.setAdapter(adapter);

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
//
//                if(listview.isItemChecked(position)) {
//
//              text = cardtypedata[position];
//
//                    //String str=listview.getItemAtPosition(arg2).toString();
//             Log.e("value---",""+text);
//                }
//            }
//        });

        btn_cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ed_cardtype.setText(text);
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public void showYearDialog()
    {

        final Dialog d = new Dialog(CreditCardPaymentActivity.this);
        d.setTitle("Year Picker");
        d.setContentView(R.layout.yeardialog);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year+50);
        nopicker.setMinValue(year-50);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //  b.setText(String.valueOf(nopicker.getValue()));
                ed_year.setText(String.valueOf(nopicker.getValue()));
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }
    public class CustomListAdapterCardtype extends BaseAdapter {

        LayoutInflater inflater;
        Context context;
        ArrayList<Card_type> arraylist;

        public CustomListAdapterCardtype(Context context, ArrayList<Card_type> arraylist) {

            this.context = context;
            this.arraylist=arraylist;
            inflater = LayoutInflater.from(context);

            // TODO Auto-generated constructor stub
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            return arraylist.size();

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            final Holdler holder = new Holdler();
            convertView = inflater.inflate(R.layout.customview_cardtype, null);
            holder.img_check = (ImageView) convertView.findViewById(R.id.img_check);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_crdtype);
            holder.tv.setText(arrayofcardtype.get(position).getCard_name());

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {


                    for(int i=0;i<arrayofcardtype.size();i++) {

                        if(i==position){

                            Log.e("in if====","ok");

                            arrayofcardtype.get(i).setStatus(true);
                            // holder.img_check.setVisibility(View.VISIBLE);

                        }
                        else {

                            Log.e("in else====","ok...");
                            arrayofcardtype.get(i).setStatus(false);
                        }
                        text = arrayofcardtype.get(position).getCard_name();
                        Log.e("value---",""+text);
                        notifyDataSetChanged();
                    }


//                    if(listview.isItemChecked(position)) {
//
//                        text = cardtypedata[position];
//
//                        //String str=listview.getItemAtPosition(arg2).toString();
//
//                    }
                }
            });
            notifyDataSetChanged();

            Log.e("arrayofcardtype.get(position).getStatus()"," "+arrayofcardtype.get(position).getStatus());

            if (arrayofcardtype.get(position).getStatus()==true) {

                Log.e("in if---","in if----");
                holder.img_check.setVisibility(View.VISIBLE);
            } else {

                Log.e("in else---","in else----");
                holder.img_check.setVisibility(View.GONE);
            }


            return convertView;


        }
        class Holdler
        {
            ImageView img_check;
            TextView tv;
        }
    }

}
