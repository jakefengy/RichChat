package com.example.fengy.richchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.fengy.richchat.R;
import com.example.fengy.richchat.action.OnKeyboardOperationListener;


/**
 * 聊天键盘功能界面
 *
 * @author 小孩子xm
 */
public class ChatFunctionFragment extends Fragment implements View.OnClickListener {

    private LinearLayout layout1;
    private LinearLayout layout2;

    private OnKeyboardOperationListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.chat_item_menu, null);
        layout1 = (LinearLayout) view.findViewById(R.id.chat_menu_images);
        layout2 = (LinearLayout) view.findViewById(R.id.chat_menu_photo);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setOnKeyboardOperationListener(OnKeyboardOperationListener onKeyboardOperationListener) {
        this.listener = onKeyboardOperationListener;
    }

    @Override
    public void onClick(View v) {
        if (v == layout1) {
            clickMenu(0);
        } else if (v == layout2) {
            clickMenu(1);
        }
    }

    private void clickMenu(int i) {
        if (listener != null) {
            listener.selectedFunction(i);
        }
    }
}
