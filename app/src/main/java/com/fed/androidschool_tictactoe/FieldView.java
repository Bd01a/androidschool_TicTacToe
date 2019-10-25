package com.fed.androidschool_tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class FieldView extends View {

    public static final int ZERO = 1;
    public static final int CROSS = 0;
    public static final int NULL = -1;
    public final static int NO_WINNER = 3;
    public final static int NO_END = 4;

    private static final int FIELD_COLOR = Color.BLACK;
    private  static final int ZERO_COLOR = Color.BLUE;
    private  static final int CROSS_COLOR = Color.RED;
    private  static final int FIELD_STROKE_WIDTH = 8;
    private  static final int SYMBOL_STROKE_WIDTH = 36;

    public boolean mIsGameOn = true;

    private Paint mFieldPaint;
    private Paint mCrossPaint;
    private Paint mZeroPaint;

    private RectF mField;
    private RectF mSymbol;

    private int[][] mPlaces;

    private int mNumberMove = 0;

    private int winner;

    private TicTacToeHelper mTicTacToeHelper;
    private FieldHolder mFieldHolder;

    public FieldView(Context context) {
        super(context);
        init();
    }

    public FieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initFieldPaint();
        mZeroPaint = initSymbolPaint(ZERO_COLOR);
        mCrossPaint = initSymbolPaint(CROSS_COLOR);

        mField = new RectF(0, 0, 0, 0);
        mSymbol = new RectF(0, 0, 0, 0);

        mPlaces = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mPlaces[i][j]=NULL;
            }
        }

        mTicTacToeHelper = new TicTacToeHelper(mPlaces);
    }
    public void setFieldHolder(FieldHolder fieldHolder){
        mFieldHolder = fieldHolder;
    }

    public void setNullsForPlaces() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mPlaces[i][j]=NULL;
            }
        }
        mNumberMove=0;
        invalidate();
    }

    public void setIsGameOn(boolean isGameOn){
        mIsGameOn = isGameOn;
    }

    private void initFieldPaint() {
        mFieldPaint = new Paint();
        mFieldPaint.setColor(FIELD_COLOR);
        mFieldPaint.setAntiAlias(true);
        mFieldPaint.setStyle(Paint.Style.STROKE);
        mFieldPaint.setStrokeWidth(FIELD_STROKE_WIDTH);
    }
    private Paint initSymbolPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(SYMBOL_STROKE_WIDTH);
        return paint;
    }

    public String getWinner() {
        switch (winner) {
            case CROSS:
                return getResources().getString(R.string.cross);
            case ZERO:
                return getResources().getString(R.string.zero);
            case NO_WINNER:
                return getResources().getString(R.string.no_winner);
            default:
                return null;
        }

    }

    public int getWinnerId(){
        return winner;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 1; i < 3; i++) {
            canvas.drawLine(mField.right/3f*i, mField.top, mField.right/3f*i, mField.bottom, mFieldPaint);
            canvas.drawLine(mField.right, mField.bottom/3f*i, mField.left, mField.bottom/3f*i, mFieldPaint);
        }
        canvas.drawRect(mField, mFieldPaint);

        for (int i = 0; i < mPlaces.length; i++) {
            for (int j = 0; j < mPlaces[0].length; j++) {
                int x=(int)((i+0.5)*mField.right/mPlaces.length - mSymbol.right/2);
                int y=(int)((j+0.5)*mField.bottom/mPlaces[0].length - mSymbol.bottom/2);
                switch (mPlaces[i][j]){
                    case ZERO:
                        drawZero(x,y,canvas);
                        break;
                    case CROSS:
                        drawCross(x,y,canvas);
                        break;
                    case NULL:
                        break;
                }
            }
        }
    }

    private void drawCross(int x, int y,Canvas canvas){
        canvas.drawLine(x+mSymbol.left, y+mSymbol.top, x+mSymbol.right,
                y+mSymbol.bottom, mCrossPaint);
        canvas.drawLine(x+mSymbol.right, y+mSymbol.top, x+mSymbol.left,
                y+mSymbol.bottom, mCrossPaint);
    }

    private void drawZero(int x, int y, Canvas canvas){
        canvas.drawOval(x+mSymbol.left, y+mSymbol.top,
                x+mSymbol.right, y+mSymbol.bottom, mZeroPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

       if(mIsGameOn) {
           touchEventProcessing(event);
       }

        return super.onTouchEvent(event);
    }

    private void touchEventProcessing(MotionEvent event) {
        PointF pointEvent = new PointF(event.getX(),event.getY());

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int n = (int)(pointEvent.x/(mField.right/3f));
                int m = (int)(pointEvent.y/(mField.bottom/3f));
                if((n<mPlaces.length && m<mPlaces[0].length) &&
                        (n>=0 && m>=0) && mPlaces[n][m]==NULL){
                    mPlaces[n][m]=mNumberMove%2;
                    mNumberMove++;
                    int result = mTicTacToeHelper.check();
                    if(result!=NO_END){
                        winner = result;
                        mIsGameOn=false;
                        mFieldHolder.winnerDetermined();

                    }
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mField.right = MeasureSpec.getSize(w);
        mField.bottom = MeasureSpec.getSize(h);
        mSymbol.right = mField.right/9f*2;
        mSymbol.bottom = mField.bottom/9f*2;
    }
        @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int specSize = Math.min(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));

        setMeasuredDimension(specSize, specSize);
    }

}
