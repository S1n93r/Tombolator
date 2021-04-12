package com.example.tombolator.tombolas;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.example.tombolator.R;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaUtil;

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
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.drawn_media_dialog);

        content = findViewById(R.id.label_content);
    }

    @Override
    public void onClick(View view) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setIcon(Media media) {
        content.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, MediaUtil.getMediaIcon(media), 0, 0);
    }

    public TextView getContent() {
        return content;
    }
}