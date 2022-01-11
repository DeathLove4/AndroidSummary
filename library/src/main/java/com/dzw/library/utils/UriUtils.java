package com.dzw.library.utils;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * @author Death丶Love
 * @date 2021年04月20日09:38:16
 * @description 文件 Uri 以及 path 之间的相互转换
 */
public class UriUtils {
    /**
     * 解析选中通讯录目标后解析成 姓名 、号码
     * 使用方式
     * onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
     * resolvingContractFromUri(getContentResolver(), data.getData())
     *
     * @param resolver resolver
     * @param uri      uri
     */
    public static void resolvingContractFromUri(ContentResolver resolver, Uri uri) {
        String phoneNum = null;
        String contactName = null;
        // 创建内容解析者
        ContentResolver contentResolver = resolver;
        Cursor cursor = null;
        if (uri != null) {
            cursor = contentResolver.query(uri,
                    new String[]{"display_name", "data1"}, null, null, null);
        }
        while (cursor.moveToNext()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursor.close();
        //  把电话号码中的  -  符号 替换成空格
        if (phoneNum != null) {
            phoneNum = phoneNum.replaceAll("-", " ");
            // 空格去掉  为什么不直接-替换成"" 因为测试的时候发现还是会有空格 只能这么处理
            phoneNum = phoneNum.replaceAll(" ", "");
        }
    }
}
