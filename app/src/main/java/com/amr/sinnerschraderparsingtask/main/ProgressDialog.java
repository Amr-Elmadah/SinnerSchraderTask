package com.amr.sinnerschraderparsingtask.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.amr.sinnerschraderparsingtask.R;


public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        setContentView(R.layout.common_progress);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setCancelable(false);
    }
}
