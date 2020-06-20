package com.example.retailsupporter.Model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.retailsupporter.R;

public class CalanderTextView extends AppCompatTextView {
    private Paint paint;
    public boolean isSelectday = false;

    public CalanderTextView(Context context) {
        super(context);
        initSelect();
    }

    public CalanderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSelect();
    }

    public CalanderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSelect();
    }

    //set up the work day has a red mark
    private void initSelect() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#FF9797"));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAlpha(100);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSelectday) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth()/2, paint);
        }
    }
}
