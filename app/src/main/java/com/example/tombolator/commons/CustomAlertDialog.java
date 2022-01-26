package com.example.tombolator.commons;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.tombolator.R;

public class CustomAlertDialog extends Dialog {

    private String title;
    private String message;

    private View.OnClickListener acceptListener;
    private View.OnClickListener denyListener;

    public CustomAlertDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Window window = getWindow();

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER_HORIZONTAL);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.custom_alert_dialog);

        TextView title = findViewById(R.id.alert_title);
        TextView message = findViewById(R.id.alert_message);

        title.setText(this.title);
        message.setText(this.message);

        Button acceptButton = findViewById(R.id.alert_button_accept);
        Button denyButton = findViewById(R.id.alert_button_deny);

        acceptButton.setOnClickListener(acceptListener);
        denyButton.setOnClickListener(denyListener);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOnAccepted(View.OnClickListener listener) {
        acceptListener = listener;
    }

    public void setOnDeny(View.OnClickListener listener) {
        denyListener = listener;
    }
}