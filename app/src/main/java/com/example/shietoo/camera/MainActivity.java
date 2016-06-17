package com.example.shietoo.camera;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private ImageView mImg;
    private DisplayMetrics mPhone;
    private final static int CAMERA = 66;
    private final static int PHOTO = 99;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);

        mImg =(ImageView)findViewById(R.id.img);
        Button camera1 = (Button)findViewById(R.id.camera1);
        final Button openphoto1 = (Button)findViewById(R.id.openphoto1);

        camera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues cv = new ContentValues();
                cv.put(MediaStore.Audio.Media.MIME_TYPE,"image/jpeg");
                Uri uri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,cv);

                Intent intent =new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri.getPath());
                startActivityForResult(intent,CAMERA);


            }
        });
    openphoto1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,PHOTO);
        }
    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == CAMERA || requestCode == PHOTO) && data != null) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {

                BitmapFactory.Options moptions = new BitmapFactory.Options();
                moptions.inSampleSize=4;

                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri),null,moptions);
                if (bitmap.getWidth() > bitmap.getHeight()) {
                    ScalePic(bitmap,mPhone.heightPixels);
                } else {
                    ScalePic(bitmap,mPhone.widthPixels);
                }


            } catch (Exception e) {
            }


            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void ScalePic(Bitmap bitmap,int phone){
        float mScal = 1;
        if (bitmap.getWidth()>phone){
            mScal = (float)phone/bitmap.getWidth();
            Matrix matrix = new Matrix();
            matrix.setScale(mScal,mScal);
            Bitmap mScalBitmap = Bitmap.createBitmap(
                    bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
            mImg.setImageBitmap(mScalBitmap);
        }else{
            mImg.setImageBitmap(bitmap);
        }

    }


}
