package com.common.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.softfinite.utils.Utils;


/**
 * @author VaViAn Labs.
 */
public class CButtonViewM extends AppCompatButton {

    public CButtonViewM(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CButtonViewM(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CButtonViewM(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            try {
//                if (!Locale.getDefault().toString().startsWith("en"))
                setTypeface(Utils.getNormal(getContext()));
            } catch (Exception e) {
            }
        }
    }

};