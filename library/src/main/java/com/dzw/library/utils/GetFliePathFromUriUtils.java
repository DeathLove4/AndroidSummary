package com.dzw.library.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Death丶Love
 * @date 2022-01-11 3:18 PM
 * @description 根据文件 Uri 获取其真实路径的方法
 * 兼容至 Android 12.0，API 32
 */
public class GetFliePathFromUriUtils {

    /**
     * 文件缓存主机名称
     */
    private static final String FILE_PROVIDER_AUTHORITY = ".fileProvider";

    /**
     * 获取文件真实地址
     *
     * @param context
     * @param path
     * @return
     */
    public static String getFileAbsolutePath(Context context, @NonNull String path) {
        if (TextUtils.isEmpty(path)) {
            return path;
        }
        Uri imgUri = Uri.parse(path);
        return getFilePathByUri(context, imgUri);
    }

    /**
     * 根据 fileUrl 获取到文件真实 path
     *
     * @param context context
     * @param fileUrl 文件 uri
     * @return 该 uri 对应的真实文件路径
     */
    public static String getFilePathByUri(@NonNull Context context, @NonNull Uri fileUrl) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < Build.VERSION_CODES.KITKAT) {
            //SDK版本小于4.4
            return getFilePathByUriBelow19(context, fileUrl);
        } else {
            if (DocumentsContract.isDocumentUri(context, fileUrl)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    return getFilePathByUriAbove29(context, fileUrl);
                } else {
                    return getFilePathByUriBetween19To28(context, fileUrl, sdkVersion);
                }
            } else {
                return notDocumentUriPath(context, fileUrl);
            }
        }
    }

    /**
     * sdk 版本低于 19 时根据 fileUri 获取真实 path 方式
     * 此方法只适用于 sdk 版本低于 19
     *
     * @param context context
     * @param fileUri 文件 uri
     * @return 该 uri 对应的真实文件路径
     */
    public static String getFilePathByUriBelow19(@NonNull Context context, @NonNull Uri fileUri) {
        if (null == fileUri) {
            return null;
        }
        final String scheme = fileUri.getScheme();
        String data = null;
        if (scheme == null) {
            data = fileUri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = fileUri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            data = getDataColumn(context, fileUri, null, null);
        }
        return data;
    }

    /**
     * sdk 版本高于 19 并且小于 28 时根据 fileUri 获取真实 path 方式
     *
     * @param context context
     * @param fileUri 文件 uri
     * @return 该 uri 对应的真实文件路径
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getFilePathByUriBetween19To28(@NonNull Context context, @NonNull Uri
            fileUri, int sdkVersion) {
        if (DocumentsContract.isDocumentUri(context, fileUri)) {
            // LocalStorageProvider : 使用 fileProvider 转存的文件
            if (isLocalStorageDocument(fileUri)) {
                // The path is the id : 路径为 fileUri 的id
                return DocumentsContract.getDocumentId(fileUri);
            }
            //通过 fileUri 获取到的文件在系统内的 id
            String docId = DocumentsContract.getDocumentId(fileUri);
            // ExternalStorageProvider : 外部存储，external目录
            if (isExternalStorageDocument(fileUri)) {
                String[] split = docId.split(":");
                //文件类型，转换成小写模式
                String type = split[0].toLowerCase();
                switch (type) {
                    case "primary":
                        return FileUtil.getExternalStorageDirectory() + File.separator + split[1];
                    case "home":
                        return FileUtil.getExternalStorageDirectory() + File.separator + "documents" + File.separator + split[1];
                    default:
                        String sdcardPath = FileUtil.getExternalStorageDirectory().toString() + File.separator + File.separator + split[1];
                        return sdcardPath.startsWith("file://") ? sdcardPath.replace("file://", "") : sdcardPath;
                }
            }
            //DownloadsProvider : 通过下载器下载的文件
            else if (isDownloadsDocument(fileUri)) {
                if (!TextUtils.isEmpty(docId) && docId.startsWith("raw:")) {
                    return docId.substring(4);
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    String[] contentUriPrefixesToTry = new String[]{
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads",
                            "content://downloads/all_downloads"
                    };
                    for (String contentUriPrefix : contentUriPrefixesToTry) {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.parseLong(docId));
                        try {
                            String path = getDataColumn(context, contentUri, null, null);
                            if (!TextUtils.isEmpty(path)) {
                                return path;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    //testPath(uri)
                    return getDataColumn(context, fileUri, null, null);
                }
            }
            //MediaProvider : 媒体类型文件
            else if (isMediaDocument(fileUri)) {
                String[] split = docId.split(":");
                Uri contentUri = null;
                switch (split[0]) {
                    case "image":
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "download":
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                        }
                        break;
                    default:
                        contentUri = null;
                        break;
                }
                String myPaht = getDataColumn(context, contentUri, MediaStore.Images.Media._ID + "=?", new String[]{split[1]});
                return getDataColumn(context, contentUri, MediaStore.Images.Media._ID + "=?", new String[]{split[1]});
            }
            //GoogleDriveProvider
            else if (isGoogleDriveUri(fileUri)) {
                return getGoogleDriveFilePath(context, fileUri);
            }
        }
        return getDataColumn(context, fileUri, null, null);
    }


    /**
     * Android 10 以上适配
     * sdk 版本高于 29 时根据 fileUri 获取真实 path 方式
     *
     * @param context context
     * @param fileUri 文件 uri
     * @return 该 uri 对应的真实文件路径
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static String getFilePathByUriAbove29(@NonNull Context context, @NonNull Uri fileUri) {
        File file = null;
        //android10以上转换
        if (fileUri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(fileUri.getPath());
        } else if (fileUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(fileUri, null, null, null, null);
            if (cursor.moveToFirst()) {
                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                try {
                    InputStream is = contentResolver.openInputStream(fileUri);
//                    File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + displayName);
                    File cache = new File(context.getExternalCacheDir().getAbsolutePath(), displayName);
                    FileOutputStream fos = new FileOutputStream(cache);
                    FileUtils.copy(is, fos);
                    file = cache;
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 非 document 类型文件获取 path 地址
     *
     * @param context
     * @param fileUri
     * @return
     */
    private static String notDocumentUriPath(@NonNull Context context, @NonNull Uri fileUri) {
        if ("content".equalsIgnoreCase(fileUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(fileUri)) {
                return fileUri.getLastPathSegment();
            }
            // Google drive legacy provider
            else if (isGoogleDriveUri(fileUri)) {
                return getGoogleDriveFilePath(context, fileUri);
            }
            // Huawei
            else if (isHuaWeiUri(fileUri)) {
                String columnPath = getDataColumn(context, fileUri, null, null);
                String uriPath = TextUtils.isEmpty(columnPath) ? fileUri.toString() : columnPath;
                //content://com.huawei.hidisk.fileprovider/root/storage/emulated/0/Android/data/com.xxx.xxx/
                if (uriPath.startsWith("/root")) {
                    return uriPath.replace("/root", "");
                }
            }
            return getDataColumn(context, fileUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(fileUri.getScheme())) {
            return fileUri.getPath();
        } else {
            return getDataColumn(context, fileUri, null, null);
        }
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[]
            selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor = null;
            }
            if (path != null) {
                return path;
            }
            if (uri.getPath() != null) {
                return uri.getPath();
            }
        }
        return path;
    }

    /**
     * 获取 googleDrive 类型的文件路径
     *
     * @param context context
     * @param uri     uri
     * @return
     */
    private static String getGoogleDriveFilePath(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {


            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (!cursor.moveToFirst()) {
                return uri.toString();
            }
            String name = cursor.getString(nameIndex);
            File file = new File(context.getCacheDir(), name);
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                outputStream = new FileOutputStream(file);
                int read = 0;
                int maxBufferSize = 1 * 1024 * 1024;
                int bytesAvailable = inputStream != null ? inputStream.available() : 0;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffers = new byte[bufferSize];
                while ((read = inputStream.read(buffers)) != -1) {
                    outputStream.write(buffers, 0, read);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file.getPath();
        }
        return uri.toString();
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is local.
     */
    private static boolean isLocalStorageDocument(Uri uri) {
        return FILE_PROVIDER_AUTHORITY.equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Drive.
     */
    private static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage".equals(uri.getAuthority());
    }

    /**
     * content://com.huawei.hidisk.fileprovider/root/storage/emulated/0/Android/data/com.xxx.xxx/
     *
     * @param uri
     * @return
     */
    private static boolean isHuaWeiUri(Uri uri) {
        return "com.huawei.hidisk.fileprovider".equalsIgnoreCase(uri.getAuthority());
    }
}
