package com.common.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.softfinite.utils.Utils;


/**
 * 
 * @author VaViAn Labs.
 * 
 */
public class CBEditTextView extends AppCompatEditText {

	public CBEditTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CBEditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CBEditTextView(Context context) {
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