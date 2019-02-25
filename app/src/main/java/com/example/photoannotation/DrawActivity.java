package com.example.photoannotation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.ToggleButton;

public class DrawActivity extends AppCompatActivity {


    private canvasView canvas_view;
    private static SeekBar sizebar;
    private static TextView sizebar_text;
    private static ToggleButton eraser_toggle;
    private static Switch eraser_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_activity);

        Intent startIntent = getIntent();
        String currPhotoPath = startIntent.getStringExtra("CAPTURED_PHOTO");
        setPic(currPhotoPath);

        canvas_view = (canvasView) findViewById(R.id.canvas_view);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        canvas_view.init(metrics);
        size_seekbar();
        switch_button();

    }

    public void anothaOne(View view){
        MainActivity anotha = new MainActivity();
        anotha.takePhotoIntent(view);
    }

    public void clearPaths(View view) {
        canvas_view.clear();

    }

    public void switch_button() {
        eraser_switch = (Switch) findViewById(R.id.switch1);

        eraser_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {

                    canvas_view.setEraser(true);

                } else {
                    canvas_view.setEraser(false);
                }

//                if (isChecked == true) {
//
//                    canvas_view.mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//                } else {
//                    canvas_view.currentColor = Color.RED;
//                }
            }
        });
    }



    public void size_seekbar() {
        sizebar = (SeekBar) findViewById(R.id.sizebar);
        sizebar_text = (TextView) findViewById(R.id.sizebar_text);
        sizebar_text.setText("Brush Size = " + sizebar.getProgress() + " / " + sizebar.getMax());
        sizebar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        sizebar_text.setText("Brush Size = " + progress + " / " + sizebar.getMax());
                        canvas_view.strokeWidth = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        sizebar_text.setText("Brush Size = " + progress_value + " / " + sizebar.getMax());
                    }
                }
        );
    }





    private void setPic(String currPhotoPath) {
        ImageView imageView = findViewById(R.id.my_photo);

        // Get the dimensions of the View (manual b/c of divide by zero error)
        int targetW = 371;
        int targetH = 423;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }






}

