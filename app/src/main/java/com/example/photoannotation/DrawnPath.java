package com.example.photoannotation;

import android.graphics.Path;

public class DrawnPath {

    public int color;
    public int strokeWidth;
    public Path path;

    public DrawnPath(int color, int strokeWidth, Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }


}
