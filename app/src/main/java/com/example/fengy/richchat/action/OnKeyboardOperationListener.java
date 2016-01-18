/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
