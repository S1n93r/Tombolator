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
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.tombolator.R;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaUtil;

public class DrawDialogSunflower extends Dialog implements View.OnClickListener, DrawDialog {

    private View petalN;
    private View petalNNE;
    private View petalNE;
    private View petalENE;
    private View petalE;
    private View petalESE;
    private View petalSE;
    private View petalSSE;
    private View petalS;
    private View petalSSW;
    private View petalSW;
    private View petalWSW;
    private View petalW;
    private View petalWNW;
    private View petalNW;
    private View petalNNW;

    private ImageView mediaTypeIcon;
    private TextView contentText;

    public DrawDialogSunflower(@NonNull Context context) {
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
        setContentView(R.layout.draw_dialog_sunflower);

        petalN = findViewById(R.id.petal_north);
        petalNNE = findViewById(R.id.petal_nne);
        petalNE = findViewById(R.id.petal_north_east);
        petalENE = findViewById(R.id.petal_ene);
        petalE = findViewById(R.id.petal_east);
        petalESE = findViewById(R.id.petal_ese);
        petalSE = findViewById(R.id.petal_south_east);
        petalSSE = findViewById(R.id.petal_sse);
        petalS = findViewById(R.id.petal_south);
        petalSSW = findViewById(R.id.petal_ssw);
        petalSW = findViewById(R.id.petal_south_west);
        petalWSW = findViewById(R.id.petal_wsw);
        petalW = findViewById(R.id.petal_west);
        petalWNW = findViewById(R.id.petal_wnw);
        petalNW = findViewById(R.id.petal_north_west);
        petalNNW = findViewById(R.id.petal_nnw);

        petalN.setScaleY(0);
        petalNNE.setScaleY(0);
        petalNE.setScaleY(0);
        petalENE.setScaleY(0);
        petalE.setScaleY(0);
        petalESE.setScaleY(0);
        petalSE.setScaleY(0);
        petalSSE.setScaleY(0);
        petalS.setScaleY(0);
        petalSSW.setScaleY(0);
        petalSW.setScaleY(0);
        petalWSW.setScaleY(0);
        petalW.setScaleY(0);
        petalWNW.setScaleY(0);
        petalNW.setScaleY(0);
        petalNNW.setScaleY(0);

        mediaTypeIcon = findViewById(R.id.icon_media_type);
        contentText = findViewById(R.id.label_content);

        /* TODO: Start animation here. */
        float targetScale = 1.5F;
        long duration = 1000;

        petalN.animate().scaleY(targetScale).setDuration(duration)
                .setUpdateListener(new FlowerPetalAnimatorListener()).start();
        petalNNE.animate().scaleY(targetScale).setDuration(duration).start();
        petalNE.animate().scaleY(targetScale).setDuration(duration).start();
        petalENE.animate().scaleY(targetScale).setDuration(duration).start();
        petalE.animate().scaleY(targetScale).setDuration(duration).start();
        petalESE.animate().scaleY(targetScale).setDuration(duration).start();
        petalSE.animate().scaleY(targetScale).setDuration(duration).start();
        petalSSE.animate().scaleY(targetScale).setDuration(duration).start();
        petalS.animate().scaleY(targetScale).setDuration(duration).start();
        petalSSW.animate().scaleY(targetScale).setDuration(duration).start();
        petalSW.animate().scaleY(targetScale).setDuration(duration).start();
        petalWSW.animate().scaleY(targetScale).setDuration(duration).start();
        petalW.animate().scaleY(targetScale).setDuration(duration).start();
        petalWNW.animate().scaleY(targetScale).setDuration(duration).start();
        petalNW.animate().scaleY(targetScale).setDuration(duration).start();
        petalNNW.animate().scaleY(targetScale).setDuration(duration).start();

        contentText.setAlpha(0);

        registerOnClickListener();
    }

    private void registerOnClickListener() {

        contentText.setOnClickListener(view -> {
            contentText.animate().alpha(0).setDuration(2000).start();
        });
    }

    private class FlowerPetalAnimatorListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {

            if(petalN.getScaleY() == 1.5F)
                contentText.animate().alpha(1).setDuration(2000).start();
        }
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