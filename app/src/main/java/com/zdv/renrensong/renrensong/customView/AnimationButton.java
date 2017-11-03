package com.zdv.renrensong.renrensong.customView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
public class AnimationButton extends ImageView{


		private Bitmap animBitmap = null;
		private Bitmap buttonBitmap = null;
		private int moveX = 0;
		private boolean isPress = false;
		private int timeID = 0;
		private Integer myButtonID[] = new Integer[]{
				
		};
		public AnimationButton(Context context) {
			this(context, null);
		}
		public AnimationButton(Context context, AttributeSet attrs) {
			super(context,attrs);
		//	animBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.myutil);
			buttonBitmap = BitmapFactory.decodeResource(getResources(),myButtonID[0]);
			new Thread(new MyThread()).start();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			drawImage(canvas,0,0,animBitmap,moveX,0,200,100);
			canvas.drawBitmap(buttonBitmap,20,15, null);
		}
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			setMeasuredDimension(131, 53);
		}

		private void drawImage(Canvas canvas , int x, int y,Bitmap oldBitmap , int sx,int sy,int width ,int height){
			Rect rect_x = new Rect();
			rect_x.left = sx;
			rect_x.right = sx + width;
			rect_x.top = sy;
			rect_x.bottom = sy + height;
			Rect rect_y = new Rect();
			rect_y.left = x;
			rect_y.right = x + width;
			rect_y.top = y;
			rect_y.bottom = y + height;
			//按指定的裁剪矩形来绘制图片
			//分别是屏幕上的矩形和图片上要绘制的矩形裁剪
			Bitmap output = Bitmap.createBitmap(oldBitmap.getWidth(),
					oldBitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas2 = new Canvas(output);
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			//canvas.drawARGB(0, 0, 0, 0);
			//paint.setColor(0xff424242);
			//paint.setAlpha(140);
			//canvas2.drawRoundRect(new RectF(2,4,130,50), 10.0f, 10.0f, paint);
			canvas2.drawRoundRect(new RectF(1,0,130,50), 15.0f, 15.0f, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas2.drawBitmap(oldBitmap, rect_x, rect_y, paint);
			canvas.drawBitmap(output, 0	, 0, null);
			rect_x = null;
			rect_y = null;
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent event) {
			/*	if(isReleased){
				touchPoint = new Point();
				touchPoint.x = (int)event.getX();
				touchPoint.y = (int)event.getY();	
			}*/
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:  
				isPress = true;
				break;
			case MotionEvent.ACTION_UP:
				isPress = false;
				break;
			default:
				break;
			}
			return true;
		}
		private class MyThread implements Runnable{
			@Override
			public void run() {
				while(!Thread.currentThread().isInterrupted()){
					try {
						moveX++;
						if(moveX > 100){
							moveX = 0;
						}
						if(isPress){
							timeID ++ ;
							if(timeID <20){
								buttonBitmap = BitmapFactory.decodeResource(getResources(), myButtonID[timeID]);
							}else{
								timeID =0 ;
							}
						}
						Thread.sleep(50);
					} catch (Exception e) {
						Thread.currentThread().interrupt();
					}
					postInvalidate();
				}
			}
		
	}

}
