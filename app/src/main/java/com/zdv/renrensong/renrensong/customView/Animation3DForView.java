/** 
 * @Filename Animation3DForView.java 
 * @Description TODO 
 * @Version 1.0
 * @Author xiaoyl
 * @Creation 2013-8-10 上午11:08:45   
 * @Copyright Copyright © 2009 - 2013 Victor.All Rights Reserved.
 **/
package com.zdv.renrensong.renrensong.customView;
import android.graphics.Camera;

import android.graphics.Matrix;

import android.graphics.Paint;

import android.view.animation.Animation;

import android.view.animation.Transformation;
/** 
 * @ClassName Animation3DForView 
 * @Description TODO 3D旋转效果类
 * @Version 1.0
 * @Creation 2013-8-10 上午11:08:45 
 * @Mender xiaoyl
 * @Modification 2013-8-10 上午11:08:45 
 **/
public class Animation3DForView extends Animation {
	private final float mFromDegrees;
	private final float mToDegrees;
	private final float mCenterX;
	private final float mCenterY;
	private final float mDepthZ;
	private final boolean mReverse;
	private Camera mCamera;

	public Animation3DForView(float fromDegrees, float toDegrees,
			float centerX,
			float centerY, float depthZ, boolean reverse) {
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
		mCenterX = centerX;
		mCenterY = centerY;
		mDepthZ = depthZ;
		mReverse = reverse;
		Paint paint = new Paint();
		paint.setAntiAlias(true);

	}

	@Override
	public void initialize(int width, int height, int parentWidth,
	int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final float fromDegrees = mFromDegrees;
		float degrees = fromDegrees
		+ ((mToDegrees - fromDegrees) * interpolatedTime);
		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Camera camera = mCamera;
		final Matrix matrix = t.getMatrix();
		camera.save();
		if (mReverse) {
			camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
		} else {
			camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
		}
		camera.rotateY(degrees);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);

	}
}
