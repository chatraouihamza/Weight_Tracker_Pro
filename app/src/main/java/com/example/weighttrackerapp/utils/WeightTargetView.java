package com.example.weighttrackerapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.weighttrackerapp.R;

public class WeightTargetView extends View {

    private Paint linePaint;
    private Paint progressPaint;
    private Paint healthyZonePaint;
    private Paint textPaint;

    private double startWeight = 0;
    private double currentWeight = 0;
    private double goalWeight = 0;
    private double heightCm = 0;

    public WeightTargetView(Context context) {
        super(context);
        init();
    }

    public WeightTargetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Background Line (Grey)
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.parseColor("#E0E0E0"));
        linePaint.setStrokeWidth(12f);
        linePaint.setStrokeCap(Paint.Cap.ROUND);

        // Progress Indicator (Primary Color)
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(getResources().getColor(R.color.primary_green, null));
        progressPaint.setStyle(Paint.Style.FILL);

        // Healthy Zone (Light Green Transparent)
        healthyZonePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        healthyZonePaint.setColor(Color.parseColor("#80C8E6C9")); // 50% opacity
        healthyZonePaint.setStyle(Paint.Style.FILL);

        // Text
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(36f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(double start, double current, double goal, double height) {
        this.startWeight = start;
        this.currentWeight = current;
        this.goalWeight = goal;
        this.heightCm = height;
        invalidate(); // Redraw view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (startWeight == 0 || goalWeight == 0) return;

        float w = getWidth();
        float h = getHeight();
        float padding = 60f;
        float lineY = h / 2;
        float usableWidth = w - (padding * 2);

        // 1. Draw Background Line (Start to Goal)
        canvas.drawLine(padding, lineY, w - padding, lineY, linePaint);

        // 2. Draw Healthy Zone (BMI 18.5 - 24.9)
        if (heightCm > 0) {
            double heightM = heightCm / 100.0;
            double minHealthy = 18.5 * heightM * heightM;
            double maxHealthy = 24.9 * heightM * heightM;

            float x1 = getXPosition(minHealthy, padding, usableWidth);
            float x2 = getXPosition(maxHealthy, padding, usableWidth);

            // Clamp values to stay within the bar
            x1 = Math.max(padding, Math.min(w - padding, x1));
            x2 = Math.max(padding, Math.min(w - padding, x2));

            // Draw Rect
            if (x2 > x1) { // Only draw if valid range
                RectF zone = new RectF(x1, lineY - 20, x2, lineY + 20);
                canvas.drawRoundRect(zone, 10, 10, healthyZonePaint);

                // Label
                textPaint.setTextSize(30f);
                textPaint.setColor(Color.GRAY);
                canvas.drawText("Healthy Zone", (x1 + x2) / 2, lineY + 50, textPaint);
            }
        }

        // 3. Draw Start & Goal Labels
        textPaint.setTextSize(36f);
        textPaint.setColor(Color.BLACK);

        // Start Label
        canvas.drawCircle(padding, lineY, 12f, progressPaint); // Small dot
        canvas.drawText("Start", padding, lineY - 40, textPaint);
        canvas.drawText(String.format("%.1f", startWeight), padding, lineY + 80, textPaint);

        // Goal Label
        canvas.drawCircle(w - padding, lineY, 12f, progressPaint); // Small dot
        canvas.drawText("Goal", w - padding, lineY - 40, textPaint);
        canvas.drawText(String.format("%.1f", goalWeight), w - padding, lineY + 80, textPaint);

        // 4. Draw Current Position (Big Circle)
        float currentX = getXPosition(currentWeight, padding, usableWidth);

        // Clamp currentX so it doesn't go off screen
        currentX = Math.max(padding, Math.min(w - padding, currentX));

        progressPaint.setColor(getResources().getColor(R.color.primary_green, null));
        canvas.drawCircle(currentX, lineY, 25f, progressPaint);

        // Current Label (Above dot)
        textPaint.setColor(getResources().getColor(R.color.primary_dark, null));
        textPaint.setFakeBoldText(true);
        canvas.drawText("You", currentX, lineY - 40, textPaint);
        canvas.drawText(String.format("%.1f", currentWeight), currentX, lineY + 80, textPaint);
        textPaint.setFakeBoldText(false);
    }

    private float getXPosition(double weight, float padding, float usableWidth) {
        // Logic: Calculate ratio of progress between Start and Goal
        // Case A: Losing Weight (Start > Goal)
        // Case B: Gaining Weight (Start < Goal)

        double totalRange = Math.abs(startWeight - goalWeight);
        double progress;

        if (startWeight > goalWeight) {
            // Losing weight: 100 -> 80. Current 90. Diff is 10.
            progress = (startWeight - weight);
        } else {
            // Gaining weight: 60 -> 80. Current 70. Diff is 10.
            progress = (weight - startWeight);
        }

        double ratio = progress / totalRange;
        return padding + (float) (ratio * usableWidth);
    }
}