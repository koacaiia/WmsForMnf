package fine.koaca.wmsformnf;

import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class WindowDegree {
    CameraCapture mainActivity;
    int degree=0;

    public WindowDegree(CameraCapture mainActivity) {
        this.mainActivity = mainActivity;
    }

    public int getDegree(){
        WindowManager windowManager=mainActivity.getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        int rotation=display.getRotation();
        int degree=0;
        switch(rotation){
            case Surface
                    .ROTATION_0:
                degree=90;
                break;
            case Surface.ROTATION_90:
                degree=0;
                break;
            case Surface.ROTATION_180:
                degree=270;
                break;
            case Surface.ROTATION_270:
                degree=180;
                break;

        }
        return degree;
    }
}
