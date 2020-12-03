package fine.koaca.wmsformnf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;

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
        Camera.PictureCallback callback=new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                OutputStream fos=null;
                Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                windowDegree=new WindowDegree(mainActivity);
                int degree=windowDegree.getDegree();
                bitmap=rotate(bitmap,degree);
            }
        };

        camera.takePicture(null,null,callback);

    }

    private Bitmap rotate(Bitmap bitmap, int degree) {
        Matrix matrix=new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
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
