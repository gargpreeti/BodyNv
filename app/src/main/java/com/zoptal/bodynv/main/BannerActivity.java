package com.zoptal.bodynv.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zoptal.bodynv.R;

public class BannerActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_upgrade;
    private TextView tv_browsefree;

    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");

        initview();

    }
    public void initview(){

        btn_upgrade=(Button)findViewById(R.id.btn_upgrade);
        tv_browsefree=(TextView) findViewById(R.id.tv_browsefree);

        SpannableString terms = new SpannableString("or browse for free");
        terms.setSpan(new UnderlineSpan(), 0, terms.length(), 0);
        tv_browsefree.setText(terms);

        btn_upgrade.setOnClickListener(this);
        tv_browsefree.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btn_upgrade:

                Intent intent = new Intent(BannerActivity.this,UpgradeActivity.class);
                startActivity(intent);

                break;
            case R.id.tv_browsefree:

                Intent intentfree = new Intent(BannerActivity.this,MainActivity.class);
                startActivity(intentfree);

                break;
                }


    }


}
