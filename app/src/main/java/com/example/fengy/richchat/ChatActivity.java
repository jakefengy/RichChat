package com.example.fengy.richchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.example.fengy.richchat.action.OnKeyboardOperationListener;
import com.example.fengy.richchat.adapter.ChatAdapter;
import com.example.fengy.richchat.bean.Face;
import com.example.fengy.richchat.widget.ChatKeyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天主界面
 */
public class ChatActivity extends AppCompatActivity {

    private ChatKeyboard box;
    private RecyclerView mRealListView;

    private ChatAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        box = (ChatKeyboard) findViewById(R.id.chat_msg_input_box);
        mRealListView = (RecyclerView) findViewById(R.id.chat_listview);

        initMessageInputToolBox();
        initListView();

    }

    private void initMessageInputToolBox() {
        box.setOnKeyboardOperationListener(new OnKeyboardOperationListener() {
            @Override
            public void send(String content) {
                adapter.addMsg("From Tom: " + content);

                adapter.addMsg("Reply Tom: I received " + content);

            }

            @Override
            public void selectedFace(Face content) {
                box.getEditTextBox().setText(box.getEditTextBox().getText() + content.getName());
            }

            @Override
            public void selectedBackSpace(Face back) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                box.getEditTextBox().onKeyDown(KeyEvent.KEYCODE_DEL, event);

            }

            @Override
            public void selectedFunction(int index) {
                switch (index) {
                    case 0:
                        Toast.makeText(ChatActivity.this, "跳转相册", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(ChatActivity.this, "跳转相机", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mRealListView.setOnTouchListener(getOnTouchListener());
    }

    private void initListView() {

        List<String> datas = new ArrayList<>();

        datas.add("From Tom: " + "都干啥呢");
        datas.add("From Tom: " + "[龇牙][龇牙][龇牙]");
        datas.add("From Tom: " + "[调皮]嗯[调皮]eee[调皮]");

        adapter = new ChatAdapter(this, datas);
        LinearLayoutManager lm = new LinearLayoutManager(ChatActivity.this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRealListView.setLayoutManager(lm);

        mRealListView.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && box.isShow()) {
            box.hideLayout();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //若软键盘或表情键盘弹起，点击上端空白处应该隐藏输入法键盘
    private View.OnTouchListener getOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                box.hideLayout();
                box.hideKeyboard(ChatActivity.this);
                return false;
            }
        };
    }
}
