package com.zoptal.bodynv.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.zoptal.bodynv.R;


public class TimerActivity1 extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_timer,tv,tv_msg,img_reset;
    private  ImageView img_home,img_timer,img_timer_pause,img_bck;
    private  ProgressDialog loading;
    private  RelativeLayout rel_timerbck;
    public     final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    private String access_tokn;
    private int orientation;
    Chronometer simpleChronometer;

    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;

    long timeSwapBuff = 0L;

    long updatedTime = 0L;
    long offset = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        initview();
    }

    public void initview(){

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");

        simpleChronometer = (Chronometer) findViewById(R.id.tv_timer);

        simpleChronometer.setText("00:00:00");

        tv_msg=(TextView)findViewById(R.id.tv_msg);
        tv=(TextView)findViewById(R.id.tv);
        img_home=(ImageView)findViewById(R.id.img_home);
        img_timer=(ImageView)findViewById(R.id.img_timer);
        img_reset=(TextView)findViewById(R.id.img_reset);
        img_bck=(ImageView)findViewById(R.id.img_bck);
        img_timer_pause=(ImageView)findViewById(R.id.img_timer_pause);
        rel_timerbck=(RelativeLayout)findViewById(R.id.rel_timerbck);


        YouTubeThumbnailView youTubeThumbnailView = (YouTubeThumbnailView)findViewById(R.id.youtube_thumbnail);

        final ImageView iv_yt_logo=(ImageView)findViewById(R.id.iv_yt_logo);


        tv.setVisibility(View.VISIBLE);
        //   tv_timer.setVisibility(View.GONE);
        img_home.setOnClickListener(this);
        img_timer.setOnClickListener(this);
        img_reset.setOnClickListener(this);
        img_timer_pause.setOnClickListener(this);
        img_bck.setOnClickListener(this);

        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

                iv_yt_logo.setVisibility(View.VISIBLE);


            }
        };


        youTubeThumbnailView.initialize("AIzaSyAxbFLuWoJQLEf1CoKz5Y_zr25shENIOrQ", new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(MainActivity.vidourl);

                //    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                        Log.e("in succ","succ");
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        Log.e("in err","err");
                    }
                });

            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("in fail","fail");
            }


        });

        iv_yt_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = YouTubeStandalonePlayer.createVideoIntent(TimerActivity1.this, "AIzaSyAxbFLuWoJQLEf1CoKz5Y_zr25shENIOrQ",MainActivity.vidourl,1, true,true);
                startActivity(intent);
            }
        });

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        orientation = display.getOrientation();
        Log.e("oriention====",""+orientation);

    }

    @Override //reconfigure display properties on screen rotation
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            Log.e("in else config==","prooo");
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // handle change here
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            onBackPressed();
            Log.e("in else config==","landscpr");


        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_home:

                Intent i = new Intent(TimerActivity1.this, MainActivity.class);
                startActivity(i);
                finish();

                break;

            case R.id.img_bck:

                Intent intnt = new Intent(TimerActivity1.this, MainActivity.class);
                startActivity(intnt);
                finish();

                break;
            case R.id.img_timer_pause:

                simpleChronometer.stop();
                offset = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
                img_timer.setVisibility(View.VISIBLE);
                img_timer_pause.setVisibility(View.GONE);

                break;

            case R.id.img_timer:
                img_reset.setVisibility(View.VISIBLE);
                img_bck.setVisibility(View.GONE);
                tv.setText("Congrats!");
                simpleChronometer.setText("00:00:00");
                rel_timerbck.setBackgroundResource(R.drawable.timerbck);

                simpleChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        // TODO Auto-generated method stub
                        simpleChronometer.setText("00:00:00");



                        long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                        int h   = (int)(time /3600000);
                        int m = (int)(time - h*3600000)/60000;
                        int s= (int)(time - h*3600000- m*60000)/1000 ;
                        String hh = h < 10 ? "0"+h: h+"";
                        String mm = m < 10 ? "0"+m: m+"";
                        String ss = s < 10 ? "0"+s: s+"";
                        chronometer.setText(hh+":"+mm+":"+ss);

                        String currentTime= simpleChronometer.getText().toString();
                        if(currentTime.equals("00:25:00")) //put time according to you
                        {
                            rel_timerbck.setBackgroundResource(R.drawable.pipr_bck);
                            tv.setVisibility(View.GONE);
                            tv_msg.setVisibility(View.VISIBLE);
                            simpleChronometer.stop();
                        }
                    }



                });

                simpleChronometer.setBase(SystemClock.elapsedRealtime() - offset);
                simpleChronometer.start();
                img_timer.setVisibility(View.GONE);
                img_timer_pause.setVisibility(View.VISIBLE);


//                rel_timerbck.setBackgroundResource(R.drawable.timerbck);
//                tv_timer.setVisibility(View.VISIBLE);
//                tv_msg.setVisibility(View.GONE);
//                tv.setVisibility(View.GONE);
//
//
//                startTime = SystemClock.uptimeMillis();
//                customHandler.postDelayed(updateTimerThread,0);






//                if (waitTimer != null) {
//                    waitTimer.cancel();
//                    waitTimer = null;
//                }
//                long milisecnds = 1500000;
//                // remainingtime=milisecnds;
//
//                waitTimer = new CountDownTimer(remainingtime, 1000) {
//
//                    public void onTick(long millisUntilFinished) {
//
//                        isRunning = true;
//                        remainingtime = millisUntilFinished;
//                        long secondsInMilli = 1000;
//                        long minutesInMilli = secondsInMilli * 60;
//                        long hoursInMilli = minutesInMilli * 60;
//
//                        long elapsedHours = millisUntilFinished / hoursInMilli;
//                        millisUntilFinished = millisUntilFinished % hoursInMilli;
//
//                        long elapsedMinutes = millisUntilFinished / minutesInMilli;
//                        millisUntilFinished = millisUntilFinished % minutesInMilli;
//
//                        long elapsedSeconds = millisUntilFinished / secondsInMilli;
//
//                        String yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
//                        tv_timer.setText(yy);
//
//                    }
//
//
//                    public void onFinish() {
//                        rel_timerbck.setBackgroundResource(R.drawable.backgroundgreen);
//                        isRunning = false;
//                        //After 60000 milliseconds (60 sec) finish current
//                        //if you would like to execute something when time finishes
//                        tv_msg.setVisibility(View.VISIBLE);
//                        tv_timer.setText("00:00:00");
//                    }
//                }.start();
//
//                if (isRunning == true) {
//                    Log.e("remaining time---", "" + remainingtime);
//                    waitTimer.cancel();
//
//                    img_timer_pause.setVisibility(View.VISIBLE);
//                    img_timer.setVisibility(View.GONE);
//                    isRunning = false;
//                }
//
//                break;
//
//
//            case R.id.img_timer_pause:
//
//                //  img_timer.setVisibility(View.GONE);
//                //  img_timer_pause.setVisibility(View.VISIBLE);
//
//                rel_timerbck.setBackgroundResource(R.drawable.timerbck);
//                tv_timer.setVisibility(View.VISIBLE);
//                tv_msg.setVisibility(View.GONE);
//                tv.setVisibility(View.GONE);
//
//
//                // remainingtime=milisecnds;
//
//
//                Log.e("remmmmmmmmmmmm", "" + remainingtime);
//                if (isRunning == false) {
//                    waitTimer = new CountDownTimer(remainingtime, 1000) {
//
//                        public void onTick(long millisUntilFinished) {
//
//                            isRunning = true;
//                            remainingtime = millisUntilFinished;
//                            long secondsInMilli = 1000;
//                            long minutesInMilli = secondsInMilli * 60;
//                            long hoursInMilli = minutesInMilli * 60;
//
//                            long elapsedHours = millisUntilFinished / hoursInMilli;
//                            millisUntilFinished = millisUntilFinished % hoursInMilli;
//
//                            long elapsedMinutes = millisUntilFinished / minutesInMilli;
//                            millisUntilFinished = millisUntilFinished % minutesInMilli;
//
//                            long elapsedSeconds = millisUntilFinished / secondsInMilli;
//
//                            String yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
//                            tv_timer.setText(yy);
//
//                        }
//
//                        public void onFinish() {
//                            rel_timerbck.setBackgroundResource(R.drawable.backgroundgreen);
//                            isRunning = false;
//                            //After 60000 milliseconds (60 sec) finish current
//                            //if you would like to execute something when time finishes
//                            tv_msg.setVisibility(View.VISIBLE);
//                            tv_timer.setText("00:00:00");
//                        }
//                    }.start();
//                }
//
//                if (isRunning == true) {
//
//                    Log.e("testingggg", "ok");
//                    waitTimer.cancel();
//                    isRunning = false;
//                    img_timer.setVisibility(View.VISIBLE);
//                    img_timer_pause.setVisibility(View.GONE);
//
//                }

                break;

            case R.id.img_reset:
                img_timer.setVisibility(View.VISIBLE);
                img_timer_pause.setVisibility(View.GONE);

                rel_timerbck.setBackgroundResource(R.drawable.timerbck);
                tv_msg.setVisibility(View.GONE);


                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                offset = 0;
                simpleChronometer.setText("00:00:00");
                simpleChronometer.stop();

//                if (waitTimer != null) {
//                    waitTimer.cancel();
//                   waitTimer = null;
//                }
//
//                // remainingtime=milisecnds;
//                 remainingtime=1500000;
//                  waitTimer = new CountDownTimer(remainingtime, 1000) {
//
//                    public void onTick(long millisUntilFinished) {
//
//                      isRunning =false;
//                        remainingtime = millisUntilFinished;
//                        long secondsInMilli = 1000;
//                        long minutesInMilli = secondsInMilli * 60;
//                        long hoursInMilli = minutesInMilli * 60;
//
//                        long elapsedHours = millisUntilFinished / hoursInMilli;
//                        millisUntilFinished = millisUntilFinished % hoursInMilli;
//
//                        long elapsedMinutes = millisUntilFinished / minutesInMilli;
//                        millisUntilFinished = millisUntilFinished % minutesInMilli;
//
//                        long elapsedSeconds = millisUntilFinished / secondsInMilli;
//
//                        String yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
//                        tv_timer.setText(yy);
//                        waitTimer.cancel();
//                    }
//
//
//                    public void onFinish() {
//                        rel_timerbck.setBackgroundResource(R.drawable.backgroundgreen);
//                        isRunning = false;
//                        //After 60000 milliseconds (60 sec) finish current
//                        //if you would like to execute something when time finishes
//                        tv_msg.setVisibility(View.VISIBLE);
//                        tv_timer.setText("00:00:00");
//                    }
//                }.start();


                break;
        }

    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);

            int mins = secs / 60;

            secs = secs % 60;

            int milliseconds = (int) (updatedTime % 1000);

            tv_timer.setText("" + mins + ":"

                    + String.format("%02d", secs) + ":"

                    + String.format("%03d", milliseconds));

            customHandler.postDelayed(this,1000);

        }

    };


}


