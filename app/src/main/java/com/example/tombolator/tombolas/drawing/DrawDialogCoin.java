package com.example.tombolator.tombolas.drawing;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.tombolator.R;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaUtil;

public class DrawDialogCoin extends Dialog implements View.OnClickListener, DrawDialog {

    private View coin;

    private ImageView mediaTypeIcon;
    private TextView contentText;

    private boolean fadeInFired = false;

    public DrawDialogCoin(@NonNull Context context) {
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
        setContentView(R.layout.draw_dialog_coin);

        coin = findViewById(R.id.coin);

        mediaTypeIcon = findViewById(R.id.icon_media_type);
        contentText = findViewById(R.id.label_content);

        coin.setRotationY(180);
        mediaTypeIcon.setRotationY(180);
        contentText.setRotationY(180);

        mediaTypeIcon.setVisibility(View.INVISIBLE);
        contentText.setVisibility(View.INVISIBLE);

        startAnimation();
    }

    private void startAnimation() {

        mediaTypeIcon.animate().rotationY(0).setDuration(2000).start();
        contentText.animate().rotationY(0).setDuration(2000).start();

        coin.animate().rotationY(0).setDuration(2000).setUpdateListener(valueAnimator -> {

            if(valueAnimator.getCurrentPlayTime() >= 1000 && !fadeInFired) {

                fadeInFired = true;

                mediaTypeIcon.setVisibility(View.VISIBLE);
                contentText.setVisibility(View.VISIBLE);
            }
        }).start();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void setIcon(Media media) {
        mediaTypeIcon.setImageResource(MediaUtil.getMediaTypeIcon(media));
    }

    @Override
    public TextView getContentText() {
        return contentText;
    }
}