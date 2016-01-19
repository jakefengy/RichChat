package com.example.fengy.richchat.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.fengy.richchat.R;
import com.example.fengy.richchat.action.OnKeyboardOperationListener;
import com.example.fengy.richchat.fragment.ChatFaceContainerFragment;
import com.example.fengy.richchat.fragment.ChatFunctionFragment;
import com.example.fengy.richchat.helper.SoftKeyboardStateHelper;


/**
 * 键盘、表情等控制
 *
 * @author 小孩子xm
 */
public class ChatKeyboard extends RelativeLayout implements SoftKeyboardStateHelper.SoftKeyboardStateListener {

    public static final int LAYOUT_TYPE_HIDE = 0;
    public static final int LAYOUT_TYPE_FACE = 1;
    public static final int LAYOUT_TYPE_MORE = 2;

    // 最上层输入框
    private EditText mEtMsg;
    private CheckBox mBtnFace;
    private CheckBox mBtnMore;
    private Button mBtnSend;

    /**
     * 表情区域
     */
    private RelativeLayout mRlFace;

    // 自定义表情页面初始化

    private int layoutType = LAYOUT_TYPE_HIDE;
    private ChatFaceContainerFragment chatFaceFragment;
    private ChatFunctionFragment chatFuncFragment;
    private Fragment currentFragment;

    private Context context;
    private OnKeyboardOperationListener listener;
    private OnResizeListener resizeListener;
    private SoftKeyboardStateHelper mKeyboardHelper;

    // 记录高度
    private int keyboardHeightInPx = 0;
    private int layoutHeightInPx = 320;
    private int resizeHeightInPx = 0;
    private int lastResizeHeight = resizeHeightInPx;

    public ChatKeyboard(Context context) {
        super(context);
        init(context);
    }

    public ChatKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatKeyboard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View root = View.inflate(context, R.layout.chat_tool_box, null);
        this.addView(root);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
        this.initWidget();
    }

    private void initData() {
        mKeyboardHelper = new SoftKeyboardStateHelper(((Activity) getContext()).getWindow().getDecorView());
        mKeyboardHelper.addSoftKeyboardStateListener(this);
    }

    private void initWidget() {
        initFragment();
        initInputButton();

    }

    private void initFragment() {
        chatFaceFragment = new ChatFaceContainerFragment();
        chatFuncFragment = new ChatFunctionFragment();

        ((FragmentActivity) getContext()).getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.toolbox_frame_function, chatFaceFragment)
                .add(R.id.toolbox_frame_function, chatFuncFragment)
                .commit();

        ((FragmentActivity) getContext()).getSupportFragmentManager()
                .beginTransaction()
                .hide(chatFaceFragment)
                .hide(chatFuncFragment)
                .commit();
    }

    private void initInputButton() {
        // 按钮
        mEtMsg = (EditText) findViewById(R.id.toolbox_et_message);
        mBtnSend = (Button) findViewById(R.id.toolbox_btn_send);
        mBtnFace = (CheckBox) findViewById(R.id.toolbox_btn_face);
        mBtnMore = (CheckBox) findViewById(R.id.toolbox_btn_more);
        mRlFace = (RelativeLayout) findViewById(R.id.toolbox_layout_face);

        // 点击表情按钮
        mBtnFace.setOnClickListener(getFunctionBtnListener(LAYOUT_TYPE_FACE));

        // 点击表情按钮旁边的加号
        mBtnMore.setOnClickListener(getFunctionBtnListener(LAYOUT_TYPE_MORE));

        // 点击消息输入框
        mEtMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLayout();
            }
        });

        mBtnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String content = mEtMsg.getText().toString();
                    listener.send(content);
                    mEtMsg.setText("");
                }
            }
        });
    }

    private OnClickListener getFunctionBtnListener(final int which) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow() && which == layoutType) {
                    hideLayout();
                    showKeyboard(context);
                } else {
                    changeLayout(which);
                    showLayout();
                    mBtnFace.setChecked(layoutType == LAYOUT_TYPE_FACE);
                    mBtnMore.setChecked(layoutType == LAYOUT_TYPE_MORE);
                }
            }
        };
    }

    private void changeLayout(int mode) {

        switch (mode) {
            case LAYOUT_TYPE_FACE:
                switchFragment(chatFaceFragment);
                break;
            case LAYOUT_TYPE_MORE:
                switchFragment(chatFuncFragment);
                break;
        }

        layoutType = mode;
    }

    private void switchFragment(Fragment fragment) {

        if (fragment instanceof ChatFaceContainerFragment) {
            ((ChatFaceContainerFragment) fragment).setOnKeyboardOperationListener(listener);
        } else if (fragment instanceof ChatFunctionFragment) {
            ((ChatFunctionFragment) fragment).setOnKeyboardOperationListener(listener);
        }

        FragmentManager fm = ((FragmentActivity) getContext()).getSupportFragmentManager();

        if (currentFragment == null) {
            fm.beginTransaction().show(fragment).commit();
        } else {
            if (fragment != currentFragment) {
                fm.beginTransaction().hide(currentFragment).show(fragment).commit();
            }
        }

        currentFragment = fragment;
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {
        this.keyboardHeightInPx = keyboardHeightInPx;
        hideLayout();
    }

    @Override
    public void onSoftKeyboardClosed() {
        this.keyboardHeightInPx = 0;
        check();
    }

    public EditText getEditTextBox() {
        return mEtMsg;
    }

    public void showLayout() {
        hideKeyboard(this.context);
        // 延迟一会，让键盘先隐藏再显示表情键盘，否则会有一瞬间表情键盘和软键盘同时显示
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mRlFace.setVisibility(View.VISIBLE);

                ViewTreeObserver vto = mRlFace.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mRlFace.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        layoutHeightInPx = mRlFace.getHeight();
                        check();
                    }
                });
            }
        }, 50);
    }

    public boolean isShow() {
        return mRlFace.getVisibility() == VISIBLE;
    }

    public void hideLayout() {
        layoutHeightInPx = 0;
        mRlFace.setVisibility(View.GONE);
        if (mBtnFace.isChecked()) {
            mBtnFace.setChecked(false);
        }
        if (mBtnMore.isChecked()) {
            mBtnMore.setChecked(false);
        }

        check();
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
    }

    /**
     * 关闭软键盘、关闭表情区域
     *
     * @param context
     */
    public void hideAllKeyboard(Context context) {
        hideLayout();
        hideKeyboard(context);
        this.keyboardHeightInPx = 0;
        this.layoutHeightInPx = 0;
        check();
    }

    /**
     * 显示软键盘
     */
    public static void showKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus()
                    .getWindowToken(), 0);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void setOnKeyboardOperationListener(OnKeyboardOperationListener onKeyboardOperationListener) {
        this.listener = onKeyboardOperationListener;
    }

    // Interface
    private void check() {
        resizeHeightInPx = Math.max(layoutHeightInPx, keyboardHeightInPx);
        if (resizeListener != null && lastResizeHeight != resizeHeightInPx) {
            resizeListener.onResize(resizeHeightInPx);
            lastResizeHeight = resizeHeightInPx;
        }
    }

    public void setResizeListener(OnResizeListener resizeListener) {
        this.resizeListener = resizeListener;
    }

    public interface OnResizeListener {
        void onResize(int heightInPx);
    }

}
