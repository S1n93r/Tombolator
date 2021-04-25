package com.example.tombolator.tombolas;

import android.animation.Animator;
import android.animation.AnimatorInflater;
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

public class DrawnMediaDialog extends Dialog implements View.OnClickListener {

    private ImageView mediaTypeIcon;
    private TextView contentText;

    private final Animator fadeIn = AnimatorInflater.loadAnimator(getContext(), R.animator.fade_in);
    private final Animator fadeOut = AnimatorInflater.loadAnimator(getContext(), R.animator.fade_out);

    public DrawnMediaDialog(@NonNull Context context) {
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
        setContentView(R.layout.drawn_media_dialog);

        mediaTypeIcon = findViewById(R.id.icon_media_type);
        contentText = findViewById(R.id.label_content);

        fadeIn.setTarget(contentText);
        fadeIn.start();

        contentText.setOnClickListener(view -> {

            fadeOut.setTarget(contentText);
            fadeOut.start();
            fadeOut.addListener(new FadeOutEndListener(this));
        });
    }

    private static class FadeOutEndListener implements Animator.AnimatorListener {

        private final Dialog dialog;

        public FadeOutEndListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            dialog.cancel();
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }

    @Override
    public void onClick(View view) {

    }

    public void setIcon(Media media) {
        mediaTypeIcon.setImageResource(MediaUtil.getMediaIcon(media));
    }

    public TextView getContentText() {
        return contentText;
    }
}