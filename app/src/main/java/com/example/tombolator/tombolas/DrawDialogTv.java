package com.example.tombolator.tombolas;

import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.tombolator.R;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaUtil;

public class DrawDialogTv extends Dialog implements View.OnClickListener {

    private View tvSwitchOne;

    private ImageView mediaTypeIcon;
    private TextView contentText;

    public DrawDialogTv(@NonNull Context context) {
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
        setContentView(R.layout.draw_dialog_tv);

        tvSwitchOne = findViewById(R.id.tv_button_one_switch);

        mediaTypeIcon = findViewById(R.id.icon_media_type);
        contentText = findViewById(R.id.label_content);

        /* TODO: Start animation here. */
        ViewPropertyAnimator viewPropertyAnimator = tvSwitchOne.animate();
        viewPropertyAnimator.setUpdateListener(new TvSwitchAnimatorListener());
        viewPropertyAnimator.setDuration(500);
        viewPropertyAnimator.rotation(180);
        viewPropertyAnimator.start();

        contentText.setAlpha(0);

        registerOnClickListener();
    }

    private void registerOnClickListener() {

        contentText.setOnClickListener(view -> {
            contentText.animate().alpha(0).setDuration(2000).start();
        });
    }

    private class TvSwitchAnimatorListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {

            if(tvSwitchOne.getRotation() == 180)
                contentText.animate().alpha(1).setDuration(2000).start();
        }
    }

    @Override
    public void onClick(View view) {

    }

    public void setIcon(Media media) {
        mediaTypeIcon.setImageResource(MediaUtil.getMediaTypeIcon(media));
    }

    public TextView getContentText() {
        return contentText;
    }
}