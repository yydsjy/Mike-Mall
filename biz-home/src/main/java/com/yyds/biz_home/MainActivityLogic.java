package com.yyds.biz_home;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.mikeyyds.common.tab.MikeFragmentTabView;
import com.mikeyyds.common.tab.MikeTabViewAdapter;

import com.yyds.biz_home.category.CategoryFragment;
import com.yyds.biz_home.favorite.FavoriteFragment;

import com.yyds.biz_home.profile.ProfileFragment;
import com.yyds.biz_home.recommend.RecommendFragment;
import com.yyds.biz_home.home.HomePageFragment;
import com.mikeyyds.ui.tab.bottom.MikeTabBottomInfo;
import com.mikeyyds.ui.tab.bottom.MikeTabBottomLayout;
import com.mikeyyds.ui.tab.common.IMikeTabLayout;

import java.util.ArrayList;
import java.util.List;



public class MainActivityLogic {
    private MikeFragmentTabView mikeFragmentTabView;
    private ActivityProvider activityProvider;
    private MikeTabBottomLayout mikeTabBottomLayout;
    private List<MikeTabBottomInfo<?>> infoList;
    private final static String SAVED_CURRENT_ID = "SAVED_CURRENT_ID";
    private int currentItemIndex;


    public MainActivityLogic(ActivityProvider activityProvider, @Nullable Bundle savedInstanceState){
        this.activityProvider = activityProvider;
        if (savedInstanceState!=null){
            currentItemIndex = savedInstanceState.getInt(SAVED_CURRENT_ID);
        }
        initTabBottom();
    }



    private void initTabBottom() {
        mikeTabBottomLayout = activityProvider.findViewById(R.id.tab_bottom_layout);
        mikeTabBottomLayout.setTabAlpha(0.85f);
        infoList = new ArrayList<>();
        int defaultColor = activityProvider.getResources().getColor(R.color.tabBottomDefaultColor);
        int tintColor = activityProvider.getResources().getColor(R.color.tabBottomTintColor);
        MikeTabBottomInfo homeInfo = new MikeTabBottomInfo<Integer>(
                "Home",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_home),
                null,
                defaultColor,
                tintColor
        );
        homeInfo.fragment = HomePageFragment.class;

        MikeTabBottomInfo favoriteInfo = new MikeTabBottomInfo<Integer>(
                "Favorite",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_favorite_tab_bottom),
                null,
                defaultColor,
                tintColor
        );
        favoriteInfo.fragment = FavoriteFragment.class;

        MikeTabBottomInfo categoryInfo = new MikeTabBottomInfo<Integer>(
                "Category",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_category),
                null,
                defaultColor,
                tintColor
        );
        categoryInfo.fragment = CategoryFragment.class;

        MikeTabBottomInfo recommendInfo = new MikeTabBottomInfo<Integer>(
                "Recommend",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_recommend),
                null,
                defaultColor,
                tintColor
        );
        recommendInfo.fragment = RecommendFragment.class;

        MikeTabBottomInfo profileInfo = new MikeTabBottomInfo<Integer>(
                "Me",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_profile),
                null,
                defaultColor,
                tintColor
        );
        profileInfo.fragment = ProfileFragment.class;

        infoList.add(homeInfo);
        infoList.add(favoriteInfo);
        infoList.add(categoryInfo);
        infoList.add(recommendInfo);
        infoList.add(profileInfo);

        mikeTabBottomLayout.inflateInfo(infoList);

        initFragmentTabView();

        mikeTabBottomLayout.addTabSelectedChangeListener(new IMikeTabLayout.OnTabSelectedListener<MikeTabBottomInfo<?>>() {
            @Override
            public void onTabSelectedChange(int index, @Nullable MikeTabBottomInfo<?> prevInfo, @NonNull MikeTabBottomInfo<?> nextInfo) {
                mikeFragmentTabView.setCurrentItem(index);
                MainActivityLogic.this.currentItemIndex = index;
            }
        });

        mikeTabBottomLayout.defaultSelected(infoList.get(currentItemIndex));
    }

    private void initFragmentTabView() {
        MikeTabViewAdapter adapter = new MikeTabViewAdapter(activityProvider.getSupportFragmentManager(),infoList);
        mikeFragmentTabView = activityProvider.findViewById(R.id.fragment_tab_view);
        mikeFragmentTabView.setAdapter(adapter);
    }

    public interface ActivityProvider{
        <T extends View> T findViewById(@IdRes int id);
        Resources getResources();
        FragmentManager getSupportFragmentManager();
        String getString(@StringRes int resId);
    }

    public void onSaveInstanceState(@NonNull Bundle outState){
        outState.putInt(SAVED_CURRENT_ID,currentItemIndex);
    }
}
