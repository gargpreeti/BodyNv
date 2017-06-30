package com.zoptal.bodynv.main;

/**
 * Created by zotal.102 on 24/04/17.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zoptal.bodynv.R;

import java.util.List;

//public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
//
//    public static List<Home_List> images;
//    private Context mContext;
//    public  static String vidourl;
//    private int selectedItem = -1;
//    public static TextView textview_day;
//    public static ImageView thumbnail;
//  //  CarouselLayoutManager layoutManager;
//public  static int pos;
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//      // public  TextView textview_day;
//
//        public MyViewHolder(View view) {
//            super(view);
//
//            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
//            textview_day = (TextView) view.findViewById(R.id.textview_day);
//        }
//    }
////    public void setLayoutManager(CarouselLayoutManager layoutManager) {
////        this.layoutManager = layoutManager;
////    }
//
////    private GalleryAdapter() {
////        for (int i = 0; 10 > i; ++i) {
////            //noinspection MagicNumber
////            mColors[i] = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
////            mPosition[i] = i;
////        }
////    }
//    public GalleryAdapter(Context context, List<Home_List> images) {
//        mContext = context;
//        this.images = images;
//
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.gallery_thumbnail, parent, false);
//
//        return new MyViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, final int position) {
////        Integer image = images.get(position);
//
//
//       // Log.e("mposs---",""+ CarouselZoomPostLayoutListener.mPosition);
//        Glide.with(mContext).load(images.get(position).getImgae_url()).thumbnail(0.5f)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(thumbnail);
//       // pos=position;
//
//
//        textview_day.setText("DAY"  + " "+ images.get(position).getDay_number());
//
//
////
////            Log.e("in contiiii","tester");
////         //   Log.e(" if adapter thumbnail---",""+GalleryAdapter.thumbnail);
////
////           thumbnail.setAlpha(50);
////        }
////        else{
////           // Log.e("else  adapter thumbnail---",""+GalleryAdapter.thumbnail);
////          thumbnail.setAlpha(100);
////        }
//
//
////        if(CarouselLayoutManager.ofvalue==128){
////
////            Log.e("in galll","tst");
////            GalleryAdapter.textview_day.setText("DAY");
////        }
////        else{
////            Log.e("in galll","tstfdg");
////            GalleryAdapter.textview_day.setText("testing");
////
////        }
//
//
////        Log.e("day num---",""+images.get(position).getDay_number());
//
//
//       thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (MainActivity.membership.equals("false")) {
//
//                    Intent intent = new Intent(mContext,UpgradeActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(intent);
//                }
//                else {
//                    if (images.get(position).getVideo_url().contains("https://www.youtube.com/embed/")) {
//                        vidourl = images.get(position).getVideo_url().replace("https://www.youtube.com/embed/", "");
//                    }
//                    //  vidourl=images.get(position).getVideo_url();
//                    Intent i = new Intent(mContext, TimerActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(i);
//
//                    Log.e("videourl===", "" + vidourl);
//                    Log.e("day num---", "" + position);
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return images.size();
//    }
//
//    public interface ClickListener {
//        void onClick(View view, int position);
//
//        void onLongClick(View view, int position);
//    }
//
//
//
//
////    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
////
////        private GestureDetector gestureDetector;
////        private GalleryAdapter.ClickListener clickListener;
////
////        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
////            this.clickListener = clickListener;
////            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
////                @Override
////                public boolean onSingleTapUp(MotionEvent e) {
////                    return true;
////                }
////
////                @Override
////                public void onLongPress(MotionEvent e) {
////                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
////                    if (child != null && clickListener != null) {
////                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
////                    }
////                }
////            });
////        }
////
////        @Override
////        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
////
////            View child = rv.findChildViewUnder(e.getX(), e.getY());
////            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
////                clickListener.onClick(child, rv.getChildPosition(child));
////            }
////            return false;
////        }
////
////        @Override
////        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
////        }
////
////        @Override
////        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
////
////        }
////    }
//}
//
