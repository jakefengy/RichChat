package com.example.fengy.richchat.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


import com.example.fengy.richchat.R;
import com.example.fengy.richchat.bean.Face;
import com.example.fengy.richchat.helper.FaceHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义TextView，显示表情
 *
 * @author 小孩子xm
 */
public class RichTextView extends TextView {

    public RichTextView(Context context) {
        this(context, null);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        Face_Tag_Pattern = Pattern.compile("\\[[a-zA-Z0-9\\/\\u4e00-\\u9fa5]+\\]");
        faceHashMap = new HashMap<>();
    }

    private static Pattern Face_Tag_Pattern;

    private HashMap<Integer, Face> faceHashMap;
    private OnFaceClickListener onFaceClickListener;

    public void setRichText(String input) {

        Spanned spanned = Html.fromHtml(matchFace(input), getImageGetterInstance(), null);
        SpannableStringBuilder spannableStringBuilder;
        if (spanned instanceof SpannableStringBuilder) {
            spannableStringBuilder = (SpannableStringBuilder) spanned;
        } else {
            spannableStringBuilder = new SpannableStringBuilder(spanned);
        }


        // 处理图片得点击事件
        ImageSpan[] imageSpans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
        final List<String> imageUrls = new ArrayList<>();

        for (int i = 0, size = imageSpans.length; i < size; i++) {
            ImageSpan imageSpan = imageSpans[i];
            String imageUrl = imageSpan.getSource();
            int start = spannableStringBuilder.getSpanStart(imageSpan);
            int end = spannableStringBuilder.getSpanEnd(imageSpan);
            imageUrls.add(imageUrl);

            final int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (onFaceClickListener != null) {
                        onFaceClickListener.faceClicked(faceHashMap.get(finalI));
                    }
                }
            };
            ClickableSpan[] clickableSpans = spannableStringBuilder.getSpans(start, end, ClickableSpan.class);
            if (clickableSpans != null && clickableSpans.length != 0) {
                for (ClickableSpan cs : clickableSpans) {
                    spannableStringBuilder.removeSpan(cs);
                }
            }
            spannableStringBuilder.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        super.setText(spanned);
    }

    private String matchFace(String input) {
        final String temp = input;
        Matcher matcher = Face_Tag_Pattern.matcher(temp);
        int position = 0;
        while (matcher.find()) {
            String key = matcher.group();
            faceHashMap.put(position, FaceHelper.getFaceByName(key));
            String spanImg = createImageSpan(key);
            input = input.replace(key, spanImg);
            position++;
        }

        return input;
    }

    private String createImageSpan(String key) {
        return "<img src='" + FaceHelper.getFaceIdByName(key) + "'/>";
    }

    private Html.ImageGetter getImageGetterInstance() {
        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int fontH = (int) (getResources().getDimension(R.dimen.text_font) * 1.5);
                Drawable d = file2Drawable(source);
                int height = fontH;
                int width = (int) ((float) d.getIntrinsicWidth() / (float) d.getIntrinsicHeight()) * fontH;
                if (width == 0) {
                    width = d.getIntrinsicWidth();
                }
                d.setBounds(0, 0, width, height);
                return d;
            }
        };
        return imgGetter;
    }

    private Drawable file2Drawable(String fileName) {

        Drawable d = null;
        try {
            InputStream is = getContext().getAssets().open("face/" + fileName + ".png");
            return new BitmapDrawable(getContext().getResources(), BitmapFactory.decodeStream(is));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

    //
    public void setOnFaceClickListener(OnFaceClickListener onFaceClickListener) {
        this.onFaceClickListener = onFaceClickListener;
    }

    //
    public interface OnFaceClickListener {

        void faceClicked(Face face);
    }

}
