package com.example.fengy.richchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.fengy.richchat.R;
import com.example.fengy.richchat.action.OnKeyboardOperationListener;
import com.example.fengy.richchat.helper.FaceHelper;


/**
 * 显示自定义表情
 *
 * @author 小孩子xm
 */
public class FaceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_face, container, false);
        facePager = (ViewPager) view.findViewById(R.id.face_pager);
        pagePointLayout = (LinearLayout) view.findViewById(R.id.face_point);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    // Views
    private int pageSize;
    private ViewPager facePager;
    private FaceAdapter faceAdapter;

    private LinearLayout pagePointLayout;
    private RadioButton[] pointViews;

    private void initViews() {
        pageSize = FaceHelper.getPageCount();
        pointViews = new RadioButton[pageSize];

        for (int i = 0; i < pageSize; i++) {

            RadioButton tip = new RadioButton(getActivity());
            tip.setBackgroundResource(R.drawable.selector_bg_tip);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(8, 8);
            layoutParams.leftMargin = 10;
            pagePointLayout.addView(tip, layoutParams);
            if (i == 0) {
                tip.setChecked(true);
            }
            pointViews[i] = tip;
        }

        faceAdapter = new FaceAdapter(getChildFragmentManager(), pageSize);
        facePager.setAdapter(faceAdapter);
        facePager.setOffscreenPageLimit(pageSize - 1);

        facePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pointViews[position].setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private class FaceAdapter extends FragmentStatePagerAdapter {

        private int size;

        public FaceAdapter(FragmentManager fm, int size) {
            super(fm);
            this.size = size;
        }

        @Override
        public Fragment getItem(int position) {

            FaceItemFragment f = new FaceItemFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("Index", position);
            f.setArguments(bundle);
            f.setOnKeyboardOperationListener(listener);

            return f;

        }

        @Override
        public int getCount() {
            return size;
        }
    }

    // operation
    private OnKeyboardOperationListener listener;

    public void setOnKeyboardOperationListener(OnKeyboardOperationListener onKeyboardOperationListener) {
        this.listener = onKeyboardOperationListener;
    }
}
