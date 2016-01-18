package com.example.fengy.richchat.helper;

import android.text.TextUtils;
import android.util.Xml;
import android.widget.EditText;


import com.example.fengy.richchat.MyApplication;
import com.example.fengy.richchat.bean.Face;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 表情管理
 *
 * @author 小孩子xm
 */
public class FaceHelper {

    // Data
    private final static int Face_Page_Size = 20;
    private static HashMap<Integer, List<Face>> facePagerMap;
    private static HashMap<String, String> faceNameIdMap;

    public static void initFaceData() {
        facePagerMap = new HashMap<>();
        faceNameIdMap = new HashMap<>();

        Face deleteFace = new Face();
        deleteFace.setId("delete");
        deleteFace.setName("[删除]");

        List<Face> faceList = parserXMLPULL(MyApplication.getInstance().getClass().getClassLoader().getResourceAsStream("assets/face/normal_face.xml"));

        if (faceList != null && faceList.size() > 0) {
            for (Face item : faceList) {
                faceNameIdMap.put(item.getName(), item.getId());
            }

            int pageCount = faceList.size() / Face_Page_Size + (faceList.size() % Face_Page_Size == 0 ? 0 : 1);
            for (int i = 0; i < pageCount; i++) {
                List<Face> newFace = new ArrayList<>();
                int start = i * Face_Page_Size;
                int end = (i + 1) * Face_Page_Size > faceList.size() ? faceList.size() : (i + 1) * Face_Page_Size;
                final List<Face> subList = faceList.subList(start, end);
                newFace.addAll(subList);
                newFace.add(deleteFace);
                facePagerMap.put(i, newFace);
            }

        }


    }

    private static List<Face> parserXMLPULL(InputStream inputStream) {

        List<Face> faceList = null;
        Face face = null;
        boolean isFaceName = true;

        try {
            //在android下使用xmlpullparser进行解析
            XmlPullParser parser = Xml.newPullParser();
            //设置xmlpullparser的一些参数
            parser.setInput(inputStream, "utf-8");

            //获取pull解析器对应事件类型
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        faceList = new ArrayList<>();
                        break;

                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("dict")) {
                            face = new Face();
                        } else if (parser.getName().equals("key")) {
                            String name = parser.nextText();
                            if (name.equals("face_name")) {
                                isFaceName = true;
                            } else if (name.equals("face_id")) {
                                isFaceName = false;
                            }
                        } else if (parser.getName().equals("string")) {
                            String value = parser.nextText();
                            if (isFaceName) {
                                face.setName(value);
                            } else {
                                face.setId(String.valueOf(Integer.parseInt(value)));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("dict")) {
                            faceList.add(face);
                            isFaceName = true;
                            face = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return faceList;
    }

    // External interface
    public static List<Face> getFaceByIndex(int index) {
        return facePagerMap.get(index);
    }

    public static String getFaceIdByName(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        } else {
            return faceNameIdMap.get(name);
        }
    }

    public static Face getFaceByName(String name) {
        Face face = new Face();
        face.setName(name);
        face.setId(getFaceIdByName(name));
        return face;
    }

    public static int getPageCount() {
        return facePagerMap.size();
    }

    public static boolean isDelete(Face face) {
        if (face == null) {
            return false;
        }

        if (face.getId().equals("delete")) {
            return true;
        }

        return false;
    }

    public static void backspace(EditText editText) {
        String content = editText.getText().toString();

    }

}
