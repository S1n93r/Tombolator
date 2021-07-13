package com.example.tombolator.tombolas;

import android.widget.TextView;
import com.example.tombolator.media.Media;

public interface DrawDialog {

    TextView getContentText();
    void setIcon(Media media);
    void show();

}