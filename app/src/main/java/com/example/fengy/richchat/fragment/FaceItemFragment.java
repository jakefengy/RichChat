package com.example.fengy.richchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.fengy.richchat.R;
import com.example.fengy.richchat.action.OnKeyboardOperationListener;
import com.example.fengy.richchat.adapter.FaceItemAdapter;
import com.example.fengy.richchat.bean.Face;
import com.example.fengy.richchat.helper.FaceHelper;

import java.util.List;

/**
 * 显示自定义表情
 *
 * @author 小孩子xm
 */
public class FaceItemFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.face_item_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.face_contain);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    //Views
    private RecyclerView recyclerView;
    private FaceItemAdapter faceItemAdapter;
    private List<Face> faceList;

    private void initViews() {
        int index = getArguments().getInt("Index");
        faceList = FaceHelper.getFaceByIndex(index);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        recyclerView.setHasFixedSize(true);

        faceItemAdapter = new FaceItemAdapter(getActivity(), faceList);

        recyclerView.setAdapter(faceItemAdapter);

        faceItemAdapter.setOnItemClickListener(new FaceItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Face face) {

                if (listener == null) {
                    return;
                }
                if (FaceHelper.isDelete(face)) {
                    listener.selectedBackSpace(face);
                } else {
                    listener.selectedFace(face);
                }

            }
        });

    }

    private OnKeyboardOperationListener listener;

    public void setOnKeyboardOperationListener(OnKeyboardOperationListener onKeyboardOperationListener) {
        this.listener = onKeyboardOperationListener;
    }

}
