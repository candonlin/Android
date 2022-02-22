package com.denny.imageloder.ImageUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * author: Denny
 * created on: 2022/2/16 下午 03:43
 */
public class LocalCacheUtils {
    private DiskLruCache mDiskLruCache;

    static String md5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    private static final int DISKMAXSIZE = 10 * 1024 * 1024;
    private String CACHE_PATH;

    public LocalCacheUtils(String path) {
        this.CACHE_PATH = path;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            mDiskLruCache = DiskLruCache.open(file, 1, 1, DISKMAXSIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地读取图片
     *
     * @param url
     */
    public Bitmap getBitmapFromLocal(String url) {
        String fileName = null;//把图片的url当做文件名,并进行MD5加密
        try {
            fileName = md5(url);
            if (mDiskLruCache.get(fileName) != null) {
                DiskLruCache.Snapshot s = mDiskLruCache.get(fileName);
                Bitmap bitmap = null;
                if (s != null) {
                    bitmap = BitmapFactory.decodeStream(s.getInputStream(0));
                    return bitmap;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从网络获取图片后,保存至本地缓存
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            String fileName = md5(url);//把图片的url当做文件名,并进行MD5加密
//            File file = new File(CACHE_PATH, fileName);

            if (mDiskLruCache.get(url) == null) {
                DiskLruCache.Editor e = mDiskLruCache.edit(fileName);
                if (e != null) {
                    OutputStream outputStream = e.newOutputStream(0);
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                        e.commit();
                    } else {
                        e.abort();
                    }
                }
                mDiskLruCache.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    /**
//     * 从本地读取图片
//     *
//     * @param url
//     */
//    public Bitmap getBitmapFromLocal(String url) {
//        String fileName = null;//把图片的url当做文件名,并进行MD5加密
//        try {
//            fileName = md5(url);
//            File file = new File(CACHE_PATH, fileName);
//
//            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
//
//            return bitmap;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    /**
//     * 从网络获取图片后,保存至本地缓存
//     *
//     * @param url
//     * @param bitmap
//     */
//    public void setBitmapToLocal(String url, Bitmap bitmap) {
//        try {
//            String fileName = md5(url);//把图片的url当做文件名,并进行MD5加密
//            File file = new File(CACHE_PATH, fileName);
//
//            //通过得到文件的父文件,判断父文件是否存在
//            File parentFile = file.getParentFile();
//            if (!parentFile.exists()) {
//                parentFile.mkdirs();
//            }
//
//            //把图片保存至本地
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
