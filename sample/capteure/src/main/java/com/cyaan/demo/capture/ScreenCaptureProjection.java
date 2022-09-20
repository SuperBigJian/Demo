package com.cyaan.demo.capture;

import android.app.Activity;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceView;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class ScreenCaptureProjection {
    private static volatile ScreenCaptureProjection mInstance;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private WeakReference<Activity> mActivity;

    public void startScreenCapture(MediaProjection mediaProjection) {
        mMediaProjection = mediaProjection;
        createVirtualDisplay();
    }

    public void initContext(Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    public void initDisplay(Surface surface) {
        mVirtualDisplay.setSurface(surface);
    }

    private void createVirtualDisplay() {
        Activity activity = getActivity();
        if (activity == null) return;
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int density = metrics.densityDpi;
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getRealSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        mVirtualDisplay = mMediaProjection.createVirtualDisplay(ScreenCaptureProjection.class.getSimpleName(), screenWidth, screenHeight, density, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, null, null, null);
        mMediaProjection.registerCallback(mMediaProjectionCallback, null);
    }

    public void stopScreenCapture() {
        mMediaProjection.stop();
    }

    private ScreenCaptureProjection() {

    }

    public static ScreenCaptureProjection getInstance() {
        if (mInstance == null) {
            synchronized (ScreenCaptureProjection.class) {
                if (mInstance == null) {
                    mInstance = new ScreenCaptureProjection();
                }
            }
        }
        return mInstance;
    }

    private MediaProjection.Callback mMediaProjectionCallback = new MediaProjection.Callback() {
        @Override
        public void onStop() {
            super.onStop();
            if (mVirtualDisplay != null) {
                mVirtualDisplay.release();
            }
            if (mMediaProjection != null) {
                mMediaProjection.unregisterCallback(mMediaProjectionCallback);
            }
            Timber.d("MediaProjection onStop");
        }
    };

    @Nullable
    private Activity getActivity() {
        if (mActivity != null) {
            return mActivity.get();
        }
        return null;
    }
}
