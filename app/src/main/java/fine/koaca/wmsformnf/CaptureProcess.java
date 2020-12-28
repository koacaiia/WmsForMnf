package fine.koaca.wmsformnf;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class CaptureProcess implements SurfaceHolder.Callback{
    Camera camera;
    CameraCapture mainActivity;
    WindowDegree windowDegree;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference recvRef;
    ContentResolver contentResolver;
    ContentValues contentValues;


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
//                bitmap=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.ARGB_8888);
                contentResolver=mainActivity.getContentResolver();
                contentValues=new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,System.currentTimeMillis()+".jpg");
                contentValues.put(MediaStore.Images.Media.MIME_TYPE,"image/*");
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+"/Fine/2");
                Uri imageUri=contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

                String imageFilePath;

                imageUri= Uri.parse(String.valueOf(imageUri));
                Cursor cursor = mainActivity.getContentResolver().query(imageUri, null, null, null, null );
                assert cursor != null;
                cursor.moveToNext();
                imageFilePath = cursor.getString( cursor.getColumnIndex( "_data" ) );
                cursor.close();
                mainActivity.sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));

                try {
                    fos=contentResolver.openOutputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);

                camera.startPreview();
                firebaseCameraUpLoad(imageUri);
                Log.i("uri1",String.valueOf(imageUri));


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

    public void firebaseCameraUpLoad(Uri imageUri){

        storage=FirebaseStorage.getInstance("gs://wmsformnf.appspot.com");
        storageReference=storage.getReference();
        recvRef=storageReference.child("images/"+System.currentTimeMillis()+".jpg");
        Log.i("uri2",String.valueOf(imageUri));
        recvRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mainActivity, "failure Server Uploading", Toast.LENGTH_SHORT).show();
                    }
                });

        }

    public void getImageDownLoad()  {

        Uri imageUri=uriReturnMethod();
        storage=FirebaseStorage.getInstance("gs://wmsformnf.appspot.com");
        storageReference=storage.getReference();
        recvRef=storageReference.child("images/1609072391497.jpg");
        Log.i("storageImageDownLoad",String.valueOf(imageUri));

        recvRef.getFile(imageUri).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(mainActivity, "Successed DownLoading Local Device", Toast.LENGTH_SHORT).show();

            }        }).
                addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mainActivity, "Failure DownLoading Local Device", Toast.LENGTH_SHORT).show();
               Log.i("storagefilter","success");
            }
        });
    }



    public void getImageDownLoads(String itemName){
        Uri imageUri=uriReturnMethod();
        storage=FirebaseStorage.getInstance("gs://wmsformnf.appspot.com");
        storageReference=storage.getReference();
        recvRef=storageReference.child("/images/"+itemName);
        Log.i("storage", String.valueOf(recvRef));
        recvRef.getFile(imageUri).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(mainActivity, "Successed DownLoading Local Device", Toast.LENGTH_SHORT).show();

            }        }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mainActivity, "Failure DownLoading Local Device", Toast.LENGTH_SHORT).show();

                    }
                });
    }



    public Uri uriReturnMethod(){
        contentResolver=mainActivity.getContentResolver();
        contentValues=new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,System.currentTimeMillis()+".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"image/*");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,Environment.DIRECTORY_PICTURES+"/FINE/DownLoad");
        Uri imageUri=contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        return imageUri;
    }
    public void listAllFiles() {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://wmsformnf.appspot.com");
        // [START storage_list_all]
        StorageReference listRef = storage.getReference().child("/images");

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }

                        for (StorageReference item : listResult.getItems()) {
                            String itemName=item.getName();

                            Log.i("StorageRef",itemName);
                            Log.i("storageTotal", String.valueOf(item));
                            getImageDownLoads(itemName);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
        // [END storage_list_all]
    }

    public void getImageDown_temp(){
        storage=FirebaseStorage.getInstance("gs://wmsformnf.appspot.com");
        storageReference=storage.getReference();
        recvRef=storageReference.child("images/1609072391497.jpg");

        File tempFile=null;
        try {
            tempFile=File.createTempFile("koaca",".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File finalTempFile = tempFile;
        recvRef.getFile(tempFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(mainActivity, "success !씨발 이게 최선이냐?", Toast.LENGTH_SHORT).show();
                bitmapReturn(finalTempFile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        if(tempFile.exists()){
            tempFile.delete();
        }

    }
    public void bitmapReturn(File tempFile){
        OutputStream fos=null;
        Bitmap bitmap=BitmapFactory.decodeFile(String.valueOf(tempFile));
        contentResolver=mainActivity.getContentResolver();
        contentValues=new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,System.currentTimeMillis()+".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"image/*");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,Environment.DIRECTORY_PICTURES+"/Fine/DownLoad");
        Uri imageUri=contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        try {
            fos=contentResolver.openOutputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);

    }



}





