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
import com.example.fengy.richchat.R;
import com.example.fengy.richchat.action.OnKeyboardOperationListener;


/**
 * 聊天表情容器
 *
 * @author 小孩子xm 2016年1月18日 18:40:09
 */
public class ChatFaceContainerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.face_container_fragment, container, false);
        faceContainerPager = (ViewPager) view.findViewById(R.id.container);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    // Views
    private ViewPager faceContainerPager;
    private ContainerAdapter adapter;
    private final static int Face_Type_Count = 1;

    private void initViews() {
        adapter = new ContainerAdapter(getChildFragmentManager(), Face_Type_Count);
        faceContainerPager.setAdapter(adapter);
    }

    private class ContainerAdapter extends FragmentStatePagerAdapter {

        private int size;

        public ContainerAdapter(FragmentManager fm, int size) {
            super(fm);
            this.size = size;
        }

        @Override
        public Fragment getItem(int position) {

            FaceFragment f = new FaceFragment();
            f.setOnKeyboardOperationListener(listener);
            return f;

        }

        @Override
        public int getCount() {
            return size;
        }
    }

    private OnKeyboardOperationListener listener;

    public void setOnKeyboardOperationListener(OnKeyboardOperationListener onKeyboardOperationListener) {
        this.listener = onKeyboardOperationListener;
    }

}
