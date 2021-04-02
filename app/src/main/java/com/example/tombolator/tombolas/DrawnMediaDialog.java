package com.example.tombolator.tombolas;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.tombolator.R;

public class DrawnMediaDialog extends Dialog implements View.OnClickListener {

    private TextView content;

    public DrawnMediaDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.drawn_media_dialog);

        content = findViewById(R.id.label_content);
    }

    @Override
    public void onClick(View view) {

    }

    public TextView getContent() {
        return content;
    }
}