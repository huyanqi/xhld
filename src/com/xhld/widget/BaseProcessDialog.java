package com.xhld.widget;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.xhld.R;

public class BaseProcessDialog extends ProgressDialog {

    
    public BaseProcessDialog(Context context){
	super(context,R.style.callDialogTheme) ;
    }
    
    public BaseProcessDialog(Context context, int theme) {
	super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressdialog);
    }
}