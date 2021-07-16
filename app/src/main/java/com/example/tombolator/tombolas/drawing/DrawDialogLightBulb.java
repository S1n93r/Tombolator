package com.example.tombolator.tombolas.drawing;

import android.animation.ValueAnimator;
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

public class DrawDialogLightBulb extends Dialog implements View.OnClickListener, DrawDialog {

    private View switchCable;
    private View switchKnob;
    private View lightRadiant;
    private View lightBulbOff;

    private ImageView mediaTypeIcon;
    private TextView contentText;

    public DrawDialogLightBulb(@NonNull Context context) {
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
        setContentView(R.layout.draw_dialog_lightbulb);

        switchCable = findViewById(R.id.cable_switch);
        switchKnob = findViewById(R.id.cable_switch_knob);
        lightRadiant = findViewById(R.id.light_radiant);
        lightBulbOff = findViewById(R.id.light_bulb_off);

        mediaTypeIcon = findViewById(R.id.icon_media_type);
        contentText = findViewById(R.id.label_content);

        contentText.setAlpha(0);

        switchCable.setPivotY(0);
        switchCable.animate().scaleY(1.1F).setUpdateListener(new CableSwitchPulledAnimationListener())
                .setDuration(500).start();

        switchKnob.animate().translationYBy(50).setDuration(500).start();
    }

    private class CableSwitchPulledAnimationListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {

            if(switchCable.getScaleY() == 1.1F) {

                switchCable.animate().scaleY(1).setUpdateListener(new CableSwitchReleasedAnimationListener())
                        .setDuration(500).start();

                switchKnob.animate().translationYBy(-50).setDuration(500).start();
            }
        }
    }

    private class CableSwitchReleasedAnimationListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {

            if(switchCable.getScaleY() == 1F) {

                lightBulbOff.setVisibility(View.INVISIBLE);
                lightRadiant.setVisibility(View.VISIBLE);
                contentText.animate().alpha(1).setDuration(2000).start();
            }
        }
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