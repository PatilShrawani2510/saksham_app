// DrawingView.java
package com.example.saksham;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom View class that acts as a drawing canvas.
 * It handles user touch input to draw paths and provides a clear functionality.
 */
public class DrawingView extends View {

    // Member variables for drawing
    private Path drawPath;
    private Paint drawPaint;
    private final List<Path> paths = new ArrayList<>();
    private final List<DroppedEmoji> droppedEmojis = new ArrayList<>();

    // A class to hold the emoji and its position
    private static class DroppedEmoji {
        int resourceId;
        float x;
        float y;

        public DroppedEmoji(int resourceId, float x, float y) {
            this.resourceId = resourceId;
            this.x = x;
            this.y = y;
        }
    }

    // Constructors for the DrawingView
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    /**
     * Initializes the drawing tools.
     */
    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(Color.BLACK);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(8f);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw all saved paths
        for (Path p : paths) {
            canvas.drawPath(p, drawPaint);
        }

        // Draw all dropped emojis
        for (DroppedEmoji emoji : droppedEmojis) {
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), emoji.resourceId);

            // Convert fixed size from dp to pixels (e.g., 80dp)
            float density = getResources().getDisplayMetrics().density;
            int fixedSizePx = (int) (80 * density + 0.5f); // You can change 80 to another size if needed

            // Resize bitmap
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, fixedSizePx, fixedSizePx, true);

            // Draw centered
            canvas.drawBitmap(scaledBitmap, emoji.x - fixedSizePx / 2f, emoji.y - fixedSizePx / 2f, null);
        }


        // Draw the current path being created by the user
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float touchX = e.getX();
        float touchY = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                paths.add(new Path(drawPath));
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    /**
     * Adds an emoji to the canvas at the specified coordinates.
     * @param emojiResourceId The resource ID of the emoji drawable.
     * @param x The x-coordinate to draw the emoji.
     * @param y The y-coordinate to draw the emoji.
     */
    public void addEmoji(int emojiResourceId, float x, float y) {
        droppedEmojis.add(new DroppedEmoji(emojiResourceId, x, y));
        invalidate(); // Redraw the canvas to show the new emoji
    }

    /**
     * Clears the drawing canvas and all saved paths and emojis.
     */
    public void clear() {
        paths.clear();
        droppedEmojis.clear();
        drawPath.reset();
        invalidate();
    }
}
