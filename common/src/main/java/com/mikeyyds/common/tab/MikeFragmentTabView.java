package com.mikeyyds.common.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MikeFragmentTabView extends FrameLayout {

    private MikeTabViewAdapter mAdapter;
    private int currentPosition;

    public MikeFragmentTabView(@NonNull Context context) {
        super(context);
    }

    public MikeFragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MikeFragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MikeTabViewAdapter getAdapter(){
        return mAdapter;
    }

    public void setAdapter(MikeTabViewAdapter adapter){
        if (mAdapter!=null||adapter==null) return;
        this.mAdapter = adapter;
        currentPosition = -1;
    }

    public void setCurrentItem(int position){
        if (position<0 || position>=mAdapter.getCount()) return;
        if (currentPosition != position) {
            currentPosition = position;
            mAdapter.instantiateItem(this,position);
        }
    }

    public int getCurrentPosition(){
        return currentPosition;
    }

    public Fragment getCurrentFragment(){
        if (this.mAdapter == null) {
            throw new IllegalArgumentException("Please call setAdapter first");
        }

        return mAdapter.getCurrentFragment();
    }


}
