package com.example.administrator.vpindicator;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ScrollingActivity extends AppCompatActivity {

    private Button left;
    private Button right;
    ColorTextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_scrolling);
        view = (ColorTextView) findViewById(R.id.view);

        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setDirection(0);
                ObjectAnimator.ofFloat(view, "progress", 0, 1).setDuration(2000)
                        .start();
                view.setTranslationX(););
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setDirection(1);
                ObjectAnimator.ofFloat(view, "progress", 0, 1).setDuration(2000)
                        .start();
            }
        });
    }

}
