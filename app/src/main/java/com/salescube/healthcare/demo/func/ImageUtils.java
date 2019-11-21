package com.salescube.healthcare.demo.func;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.salescube.healthcare.demo.app.App;

import java.io.ByteArrayOutputStream;

/**
 * Created by user on 28/10/2016.
 */

public class ImageUtils {

    public static String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = App.getContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path =  cursor.getString(idx);
            cursor.close();
            return  path;
        }
    }

    public static Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(App.getContext().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
