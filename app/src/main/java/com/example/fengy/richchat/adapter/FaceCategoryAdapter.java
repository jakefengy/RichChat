package com.example.fengy.richchat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.fengy.richchat.action.OnKeyboardOperationListener;
import com.example.fengy.richchat.fragment.ChatFunctionFragment;
import com.example.fengy.richchat.fragment.FaceFragment;
import com.example.fengy.richchat.fragment.Likefragment;


/**
 * 控件分类的viewpager适配器，还没加分类
 *
 * @author 小孩子xm
 */
public class FaceCategoryAdapter extends FragmentStatePagerAdapter {

    private final static int Page_Type_Count = 3;
    public final static int Page_Type_Face = 0;
    public final static int Page_Type_Like = 1;
    public final static int Page_Type_More = 2;

    private OnKeyboardOperationListener listener;

    public FaceCategoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return Page_Type_Count;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (position == Page_Type_Face) {
            f = new FaceFragment();
            ((FaceFragment) f).setOnKeyboardOperationListener(listener);

        } else if (position == Page_Type_Like) {
            f = new Likefragment();
        } else if (position == Page_Type_More) {
            f = new ChatFunctionFragment();
            ((ChatFunctionFragment) f).setOnKeyboardOperationListener(listener);
        }
        return f;
    }

    public void setOnKeyboardOperationListener(OnKeyboardOperationListener onKeyboardOperationListener) {
        this.listener = onKeyboardOperationListener;
    }

}