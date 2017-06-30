package com.zoptal.bodynv.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.zoptal.bodynv.R;

public class PaymentActivity1 extends AppCompatActivity {

    private WebView webView;
    String successUrl;
    String failururl;
    boolean loadingFinished = true;
    boolean redirect = false;
    private ImageView img_bck;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        img_bck=(ImageView)findViewById(R.id.img_bck);
        //webview open
        webView = (WebView)findViewById(R.id.webView1);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(UpgradeActivity.weburl);


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                webView.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!redirect) {
                    loadingFinished = true;

                    //HIDE LOADING IT HAS FINISHED
               Log.e("if url----",""+url);

                    successUrl ="http://zop.bodynv.tv/ios_app/paypal_success.php";
                    failururl="http://zop.bodynv.tv//ios_app//paypal_failure.php";




                    if (url.equals(successUrl.trim())) {


                        Toast.makeText(PaymentActivity1.this, "Payment successfull!", Toast.LENGTH_LONG).show();
                        String membership="true";

                        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                        editor1.putString("membership",membership);

                        editor1.commit();

                        Intent i = new Intent(PaymentActivity1.this,MainActivity.class);
                        startActivity(i);

                    }
                    else if(url.equals(failururl)) {

                        Toast.makeText(PaymentActivity1.this, "Payment failed!", Toast.LENGTH_LONG).show();
//                        Fragment_Signup fragment_detail=new Fragment_Signup();
//                        FragmentManager fragmentManager3 =getFragmentManager();
//                        fragmentManager3.beginTransaction().replace(R.id.activity_main_content_fragment, fragment_detail).commit();

                    }
                    redirect = false;
                }


            }
        });

            img_bck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(PaymentActivity1.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });

    }

    @Override
    public void onBackPressed() {

        //  super.onBackPressed();
        this.moveTaskToBack(true);

    }
}
