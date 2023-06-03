package com.mananews.apandts.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {

    private static String getFileExtensionFromUri(ContentResolver contentResolver, Uri fileUri) {
        String extension = null;
        if ("content".equals(fileUri.getScheme())) {
            extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(fileUri));
        } else if ("file".equals(fileUri.getScheme())) {
            String path = fileUri.getPath();
            int lastDotIndex = path.lastIndexOf(".");
            if (lastDotIndex != -1 && lastDotIndex < path.length() - 1) {
                extension = path.substring(lastDotIndex + 1);
            }
        }
        return extension;
    }

    public static String getMimeTypeFromFileUri(Context context, Uri fileUri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        String extension = getFileExtensionFromUri(contentResolver, fileUri);
        if (extension != null) {
            return mimeTypeMap.getMimeTypeFromExtension(extension);
        }
        return null;
    }

    public static File getFile(Context context, Uri uri) {
        File destinationFilename = new File(
                context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try {
            InputStream ins = context.getContentResolver().openInputStream(uri);
            if (ins != null) {
                createFileFromStream(ins, destinationFilename);
            }
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e("Save File", ex.getMessage());
            }
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try {
            FileOutputStream os = new FileOutputStream(destination);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e("Save File", ex.getMessage());
            }
            ex.printStackTrace();
        }
    }

    public static String queryName(Context context, Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    result = cursor.getString(nameIndex);
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
}

