package com.zoptal.bodynv.lib;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zoptal.bodynv.main.MainActivity;


/**
 * Class for centering items after scroll event.<br />
 * This class will listen to current scroll state and if item is not centered after scroll it will automatically scroll it to center.
 */
public class CenterScrollListener extends RecyclerView.OnScrollListener {

    private boolean mAutoSet = true;
    boolean isScroll = true;
    int val = 0;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, final int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof CarouselLayoutManager)) {
            mAutoSet = true;
            return;
        }

        final CarouselLayoutManager lm = (CarouselLayoutManager) layoutManager;
        if (!mAutoSet) {
            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                final int scrollNeeded = lm.getOffsetCenterView();
                if (CarouselLayoutManager.HORIZONTAL == lm.getOrientation()) {
                    recyclerView.smoothScrollBy(scrollNeeded, 0);
                } else {
                    MainActivity.pos = lm.getCenterItemPosition();
                    Log.e("CenterScrollListener", lm.getCenterItemPosition() + "");

                    MainActivity.isScroll=true;
                    if (MainActivity.pos <= 2) {
                        MainActivity.textview1 = MainActivity.pos + 1;
                        MainActivity.textview2 = MainActivity.pos + 2;
                    } else if (MainActivity.pos == 3) {
                        MainActivity.textview1 = MainActivity.pos + 1;
                        MainActivity.textview2 = 0;
                    } else if (MainActivity.pos == 4) {
                        MainActivity.textview1 = 1;
                        MainActivity.textview2 = 0;
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
//                    recyclerView.getAdapter().notifyItemChanged(HomeActivity.pos);
//                    recyclerView.getAdapter().notifyItemChanged(HomeActivity.lastpos);
//                    recyclerView.getAdapter().notifyItemChanged(HomeActivity.textview1);
//                    recyclerView.getAdapter().notifyItemChanged(HomeActivity.textview2);
//                    if(HomeActivity.pos<=2) {
//                        recyclerView.getAdapter().notifyItemChanged(HomeActivity.pos+1);
//                        recyclerView.getAdapter().notifyItemChanged(HomeActivity.pos+2);
//                    }else if(HomeActivity.pos==3){
//                        recyclerView.getAdapter().notifyItemChanged(4);
//                        recyclerView.getAdapter().notifyItemChanged(0);
//                    }else if(HomeActivity.pos==4){
//                        recyclerView.getAdapter().notifyItemChanged(1);
//                        recyclerView.getAdapter().notifyItemChanged(0);
//                    }
                    recyclerView.smoothScrollBy(0, scrollNeeded);
                }
                mAutoSet = true;
            }
        }
        if (RecyclerView.SCROLL_STATE_DRAGGING == newState || RecyclerView.SCROLL_STATE_SETTLING == newState) {
            mAutoSet = false;
        }
    }
}