package com.mzeat.ui.widget;


import com.mzeat.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.numchange);

	}

}
