package com.mikeyyds.common.tab;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mikeyyds.ui.tab.bottom.MikeTabBottomInfo;

import java.util.List;

public class MikeTabViewAdapter {

    private List<MikeTabBottomInfo<?>> mInfoList;
    private Fragment mCurrFragment;
    private FragmentManager mFragmentManager;

    public MikeTabViewAdapter(FragmentManager fragmentManager, List<MikeTabBottomInfo<?>> infoList) {
        this.mFragmentManager = fragmentManager;
        this.mInfoList = infoList;
    }

    public void instantiateItem(View container, int position) {
        FragmentTransaction mCurrTransaction = mFragmentManager.beginTransaction();
        if (mCurrFragment!=null){
            mCurrTransaction.hide(mCurrFragment);
        }
        String name = container.getId()+":"+position;
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment!=null){
            mCurrTransaction.show(fragment);
        } else {
            fragment = getItem(position);
            if (!fragment.isAdded()){
                mCurrTransaction.add(container.getId(),fragment,name);
            }
        }
        mCurrFragment = fragment;
        mCurrTransaction.commitAllowingStateLoss();

    }

    public Fragment getCurrentFragment() {
        return mCurrFragment;
    }

    public Fragment getItem(int position) {
        try {
            return mInfoList.get(position).fragment.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }

}
