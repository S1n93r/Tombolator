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

public class DrawDialogBook extends Dialog implements View.OnClickListener, DrawDialog {

    private static final long ANIMATION_DURATION = 2000;

    private TextView pageOne;
    private TextView pageTwo;

    private View pageOneA;
    private View pageTwoA;
    private View pageOneB;
    private View pageTwoB;
    private View pageOneC;
    private View pageTwoC;
    private View pageOneD;
    private View pageTwoD;
    private View coverOne;
    private View coverTwo;

    private ImageView mediaTypeIcon;
    private TextView contentText;

    public DrawDialogBook(@NonNull Context context) {
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
        setContentView(R.layout.draw_dialog_book);

        pageOne = findViewById(R.id.page_one);
        pageTwo = findViewById(R.id.page_two);

        pageOneA = findViewById(R.id.page_one_a);
        pageTwoA = findViewById(R.id.page_two_a);
        pageOneB = findViewById(R.id.page_one_b);
        pageTwoB = findViewById(R.id.page_two_b);
        pageOneC = findViewById(R.id.page_one_c);
        pageTwoC = findViewById(R.id.page_two_c);
        pageOneD = findViewById(R.id.page_one_d);
        pageTwoD = findViewById(R.id.page_two_d);
        coverOne = findViewById(R.id.cover_one);
        coverTwo = findViewById(R.id.cover_two);

        mediaTypeIcon = findViewById(R.id.icon_media_type);
        contentText = findViewById(R.id.label_content);

        /* TODO: Pretty hacky. */
        mediaTypeIcon.animate().alpha(0).setDuration(1).start();
        contentText.setAlpha(0);

        float xRotation = 22.5F;

        pageOne.setRotationX(xRotation);
        pageTwo.setRotationX(xRotation);

        pageOneA.setRotationX(xRotation);
        pageTwoA.setRotationX(xRotation);
        pageOneB.setRotationX(xRotation);
        pageTwoB.setRotationX(xRotation);
        pageOneC.setRotationX(xRotation);
        pageTwoC.setRotationX(xRotation);
        pageOneD.setRotationX(xRotation);
        pageTwoD.setRotationX(xRotation);
        coverOne.setRotationX(xRotation);
        coverTwo.setRotationX(xRotation);

        startAnimation();
    }

    private void startAnimation() {

        float startRotationY = 90;
        float endRotationY = 10;

        pageOne.setRotationY(startRotationY);
        pageTwo.setRotationY(-startRotationY);

        pageOneA.setRotationY(startRotationY);
        pageTwoA.setRotationY(-startRotationY);
        pageOneB.setRotationY(startRotationY);
        pageTwoB.setRotationY(-startRotationY);
        pageOneC.setRotationY(startRotationY);
        pageTwoC.setRotationY(-startRotationY);
        pageOneD.setRotationY(startRotationY);
        pageTwoD.setRotationY(-startRotationY);
        coverOne.setRotationY(startRotationY);
        coverTwo.setRotationY(-startRotationY);

        coverOne.animate().rotationY(endRotationY).setDuration(ANIMATION_DURATION).start();
        coverTwo.animate().rotationY(-endRotationY).setDuration(ANIMATION_DURATION).start();
        pageOneD.animate().rotationY(endRotationY).setDuration(ANIMATION_DURATION).setStartDelay(250).start();
        pageTwoD.animate().rotationY(-endRotationY).setDuration(ANIMATION_DURATION).setStartDelay(250).start();
        pageOneC.animate().rotationY(endRotationY).setDuration(ANIMATION_DURATION).setStartDelay(500).start();
        pageTwoC.animate().rotationY(-endRotationY).setDuration(ANIMATION_DURATION).setStartDelay(500).start();
        pageOneB.animate().rotationY(endRotationY).setDuration(ANIMATION_DURATION).setStartDelay(750).start();
        pageTwoB.animate().rotationY(-endRotationY).setDuration(ANIMATION_DURATION).setStartDelay(750).start();
        pageOneA.animate().rotationY(endRotationY).setDuration(ANIMATION_DURATION).setStartDelay(1000).start();
        pageTwoA.animate().rotationY(-endRotationY).setDuration(ANIMATION_DURATION).setStartDelay(1000).start();

        pageOne.animate().rotationY(endRotationY).setDuration(ANIMATION_DURATION).setStartDelay(1250).setUpdateListener(
                new ContentFadeInAnimatorUpdateListener()).start();
        pageTwo.animate().rotationY(-endRotationY).setDuration(ANIMATION_DURATION).setStartDelay(1250).start();
    }

    private class ContentFadeInAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {

            if(pageOne.getRotationY() == 10) {

                mediaTypeIcon.animate().alpha(1).setDuration(ANIMATION_DURATION).start();
                contentText.animate().alpha(1).setDuration(ANIMATION_DURATION).start();
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