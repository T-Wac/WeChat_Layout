package com.twac.wechat6_0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ChangeColorIconWithText extends View {
	private int mColor = 0xFF45c01a;
	private Bitmap mIconBitmap;
	private String mText = "΢��";
	private Canvas mCanvas;
	private Bitmap mbitmap;
	private Paint mPaint;
	private float mAlpha;
	private Rect mIconRect;
	private Rect mTextBound;
	private Paint mTextPaint;

	private static final String INSTANCE_STATUS = "instance_status";
	private static final String STATUS_ALPHA = "status_alpha";

	private int mTextSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

	public ChangeColorIconWithText(Context context) {
		this(context, null);

	}

	public ChangeColorIconWithText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public ChangeColorIconWithText(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ChangeColorIconWithText);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.ChangeColorIconWithText_icon:
				BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
				mIconBitmap = drawable.getBitmap();
				break;
			case R.styleable.ChangeColorIconWithText_color:
				mColor = a.getColor(attr, 0xFF45c01a);
				break;
			case R.styleable.ChangeColorIconWithText_text:
				mText = a.getString(attr);
				break;
			case R.styleable.ChangeColorIconWithText_text_size:
				mTextSize = a.getDimensionPixelOffset(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
								getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}
		a.recycle();
		mTextBound = new Rect();
		mTextPaint = new Paint();
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(0xff555555);
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
				- getPaddingRight(), getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom() - mTextBound.height());

		int left = getMeasuredWidth() / 2 - iconWidth / 2;
		int top = (getMeasuredHeight() - mTextBound.height()) / 2 - iconWidth
				/ 2;

		mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
		int alpha = (int) Math.ceil(255 * mAlpha);
		setupTargetBitmap(alpha);

		drawSourceText(canvas, alpha);
		drawTargetText(canvas, alpha);
		canvas.drawBitmap(mbitmap, 0, 0, null);
	}

	private void drawTargetText(Canvas canvas, int alpha) {
		mTextPaint.setColor(mColor);
		mTextPaint.setAlpha(alpha);
		int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
		int y = mIconRect.bottom + mTextBound.height();
		canvas.drawText(mText, x, y, mTextPaint);

	}

	private void drawSourceText(Canvas canvas, int alpha) {
		mTextPaint.setColor(0xff333333);
		mTextPaint.setAlpha(255 - alpha);

		int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
		int y = mIconRect.bottom + mTextBound.height();
		canvas.drawText(mText, x, y, mTextPaint);

	}

	private void setupTargetBitmap(int alpha) {
		mbitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Config.ARGB_8888);
		mCanvas = new Canvas(mbitmap);
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);

		mCanvas.drawRect(mIconRect, mPaint);

		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
		bundle.putFloat(STATUS_ALPHA, mAlpha);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {

		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			mAlpha = bundle.getFloat(STATUS_ALPHA);
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));

		}
		super.onRestoreInstanceState(state);
	}

	public void setIconAlpha(float alpha) {
		this.mAlpha = alpha;
		invalidateView();
	}

	// �ػ�
	private void invalidateView() {
		if (Looper.getMainLooper() == Looper.myLooper()) {
			invalidate();
		} else {
			postInvalidate();
		}

	}
}
