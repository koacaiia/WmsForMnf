package fine.koaca.wmsformnf;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import java.io.IOException;

public class CaptureProcess implements SurfaceHolder.Callback{
    Camera camera;
    CameraCapture mainActivity;
    WindowDegree windowDegree;

    public CaptureProcess(CameraCapture mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void preViewProcess(){
        mainActivity.surfaceHolder.addCallback(this);
        mainActivity.surfaceHolder.setType(mainActivity.surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        camera=Camera.open();
        windowDegree=new WindowDegree(mainActivity);
        int degree=windowDegree.getDegree();
        camera.setDisplayOrientation(degree);
        try {
            camera.setPreviewDisplay(mainActivity.surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    public void captureProcess(){

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        preViewProcess();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
