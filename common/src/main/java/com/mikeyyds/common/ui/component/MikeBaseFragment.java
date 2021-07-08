package com.mikeyyds.common.ui.component;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class MikeBaseFragment extends Fragment {
    protected View layoutView;

    @LayoutRes
    public abstract int getLayouId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        layoutView = inflater.inflate(getLayouId(),container,false);
        return layoutView;
    }

    public boolean isAlive(){
        if (isRemoving() || isDetached() || getActivity() == null) {
            return false;
        }
        return true;
    }

    public void showToast(String message){
        if (!TextUtils.isEmpty(message)){
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        }
    }

}
