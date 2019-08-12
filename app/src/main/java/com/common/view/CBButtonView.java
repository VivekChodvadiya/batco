package com.common.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.softfinite.utils.Utils;


/**
 * @author VaViAn Labs.
 */
public class CBButtonView extends AppCompatButton {

    public CBButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CBButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CBButtonView(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            try {
                setTypeface(Utils.getBold(getContext()));
            } catch (Exception e) {
            }
        }
    }

};