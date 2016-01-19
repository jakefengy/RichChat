package com.example.fengy.richchat.action;


import com.example.fengy.richchat.bean.Face;

/**
 * 表情栏按钮监听
 *
 * @author 小孩子xm 2016年1月18日 18:40:09
 */
public interface OnKeyboardOperationListener {

    void send(String content);

    void selectedFace(Face content);

    void selectedBackSpace(Face back);

    void selectedFunction(int index);
}
