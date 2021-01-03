package com.cookandroid.ccit_suda.UtilClass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class UtilClass {
    //사진올릴때 0 90 180 270 등의 각도를 0으로 맞춰주는 함수
    //this is the byte stream that I upload.
    public static byte[] getStreamByteFromImage(final File imageFile) {

        Bitmap photoBitmap = BitmapFactory.decodeFile(imageFile.getPath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        int imageRotation = getImageRotation(imageFile);

        if (imageRotation != 0)
            photoBitmap = getBitmapRotatedByDegree(photoBitmap, imageRotation);

        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        return stream.toByteArray();
    }



    private static int getImageRotation(final File imageFile) {

        ExifInterface exif = null;
        int exifRotation = 0;

        try {
            exif = new ExifInterface(imageFile.getPath());
            exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif == null)
            return 0;
        else
            return exifToDegrees(exifRotation);
    }

    private static int exifToDegrees(int rotation) {
        if (rotation == ExifInterface.ORIENTATION_ROTATE_90)
            return 90;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_180)
            return 180;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_270)
            return 270;

        return 0;
    }

    private static Bitmap getBitmapRotatedByDegree(Bitmap bitmap, int rotationDegree) {
        Matrix matrix = new Matrix();
        matrix.preRotate(rotationDegree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
