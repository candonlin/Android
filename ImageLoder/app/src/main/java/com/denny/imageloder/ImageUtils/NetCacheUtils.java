package com.denny.imageloder.ImageUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: Denny
 * created on: 2022/2/16 下午 03:43
 */
public class NetCacheUtils {
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;
    private ExecutorService service;
    private Handler mainHandler;
    private OkHttpClient client = new OkHttpClient();

    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils, Handler mainHandler) {
        mLocalCacheUtils = localCacheUtils;
        mMemoryCacheUtils = memoryCacheUtils;
        service = Executors.newFixedThreadPool(5);
        this.mainHandler = mainHandler;
    }

    public class MyThread1 extends Thread {
        private ImageView ivPic;
        private String url;

        public MyThread1(ImageView ivPic, String url) {
            this.ivPic = ivPic;
            this.url = url;
        }

        public void run() {
            Bitmap b = downLoadBitmap(url);
            if (b != null) {
                if (ivPic.getTag().equals(url)) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ivPic.setImageBitmap(b);

                        }
                    });
                }

                mLocalCacheUtils.setBitmapToLocal(url, b);
                //保存至内存中
                mMemoryCacheUtils.setBitmapToMemory(url, b);
            }
        }

    }


    public void run(ImageView ivPic, String url) {
        Bitmap b = downLoadBitmap(url);
        if (b != null) {
            if (ivPic.getTag().equals(url)) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ivPic.setImageBitmap(b);

                    }
                });
            }

            mLocalCacheUtils.setBitmapToLocal(url, b);
            //保存至内存中
            mMemoryCacheUtils.setBitmapToMemory(url, b);
        }
    }

    /**
     * 从网络下载图片
     *
     * @param ivPic 显示图片的imageview
     * @param url   下载图片的网络地址
     */
    public void getBitmapFromNet(ImageView ivPic, String url) {
//        new BitmapTask().execute(ivPic, url);//启动AsyncTask
        if (!ivPic.getTag().toString().equals(url)) {
            Log.e("DownLoadTask", "TAG 不對 3 ");
            return;
        }
//        service.execute(new MyThread1(ivPic, url));
        run(ivPic, url);
    }

    /**
     * AsyncTask就是对handler和线程池的封装
     * 第一个泛型:参数类型
     * 第二个泛型:更新进度的泛型
     * 第三个泛型:onPostExecute的返回结果
     */
    class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView ivPic;
        private String url;

        /**
         * 后台耗时操作,存在于子线程中
         *
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object[] params) {
            ivPic = (ImageView) params[0];
            url = (String) params[1];
            return downLoadBitmap(url);
        }

        /**
         * 更新进度,在主线程中
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void[] values) {
            super.onProgressUpdate(values);
        }

        /**
         * 耗时方法结束后执行该方法,主线程中
         *
         * @param result
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null && ivPic.getTag().equals(url)) {
                if (ivPic.getTag().equals(url)) {
                    ivPic.setImageBitmap(result);

                }

                System.out.println("从网络缓存图片啦.....");

                //从网络获取图片后,保存至本地缓存
                mLocalCacheUtils.setBitmapToLocal(url, result);
                //保存至内存中
                mMemoryCacheUtils.setBitmapToMemory(url, result);

            }
        }
    }

    /**
     * 网络下载图片
     *
     * @param url
     * @return
     */
    private Bitmap downLoadBitmap(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
//            bitmap = BitmapFactory.decodeStream(response.body().byteStream());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;//宽高压缩为原来的1/2
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;
            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream(), null, options);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.e("DownLoadTask", System.currentTimeMillis() + " finally");

        }

//        HttpURLConnection conn = null;
//        try {
//            conn = (HttpURLConnection) new URL(url).openConnection();
//            conn.setConnectTimeout(5000);
//            conn.setReadTimeout(5000);
//            conn.setRequestMethod("GET");
//
//            int responseCode = conn.getResponseCode();
//            if (responseCode == 200) {
//                //图片压缩
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize=2;//宽高压缩为原来的1/2
//                options.inPreferredConfig=Bitmap.Config.ARGB_4444;
//                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(),null,options);
//                return bitmap;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            conn.disconnect();
//        }

        return null;
    }
}
