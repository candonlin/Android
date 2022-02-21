package com.denny.mqtt_sample.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.denny.mqtt_sample.R;
import com.denny.mqtt_sample.callback.IGetMessageCallBack;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTService extends Service {

    public static final String TAG = MQTTService.class.getSimpleName();

    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;

    private String host = "tcp://192.168.200.91:1883";
    private String userName = "tp00191";
    private String passWord = "12345678";
    private static String myTopic = "SYS/broker/messages";      //要訂閱的主題
//    private String clientId = "test_01";//客戶端標識
    private String clientId = MqttClient.generateClientId();
    private IGetMessageCallBack callBack;

    public MQTTService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(getClass().getName(), "onCreate");
        init();
    }

    public static void publish(String msg){
        String topic = myTopic;
        Integer qos = 0;
        Boolean retained = false;
        try {
            if (client != null){
                client.publish(topic, msg.getBytes(), qos.intValue(), retained.booleanValue());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // 服務器地址（協議+地址+端口號）
        String uri = host;
        client = new MqttAndroidClient(this, uri, clientId);
        // 設置MQTT監聽並且接受消息
        client.setCallback(mqttCallback);

        conOpt = new MqttConnectOptions();
        // 清除緩存
        conOpt.setCleanSession(true);
        // 設置超時時間，單位：秒
        conOpt.setConnectionTimeout(10);
        // 心跳包發送間隔，單位：秒
        conOpt.setKeepAliveInterval(20);
        // 用戶名
        conOpt.setUserName(userName);
        // 密碼
        conOpt.setPassword(passWord.toCharArray());     //將字符串轉換爲字符串數組

        // last will message
        boolean doConnect = true;
        String message = "{\"terminal_uid\":\"" + clientId + "\"}";
        Log.e(getClass().getName(), "message是:" + message);
        String topic = myTopic;
        Integer qos = 0;
        Boolean retained = false;
        if ((!message.equals("")) || (!topic.equals(""))) {
            // 最後的遺囑
            // MQTT本身就是爲信號不穩定的網絡設計的，所以難免一些客戶端會無故的和Broker斷開連接。
            //當客戶端連接到Broker時，可以指定LWT，Broker會定期檢測客戶端是否有異常。
            //當客戶端異常掉線時，Broker就往連接時指定的topic裏推送當時指定的LWT消息。

            try {
                conOpt.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
            } catch (Exception e) {
                Log.i(TAG, "Exception Occured", e);
                doConnect = false;
                iMqttActionListener.onFailure(null, e);
            }
        }

        if (doConnect) {
            doClientConnection();
        }

    }


    @Override
    public void onDestroy() {
        stopSelf();
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /** 連接MQTT服務器 */
    private void doClientConnection() {
        if (!client.isConnected() && isConnectIsNormal()) {
            Log.i(TAG, "doClientConnection ");
            try {
                client.connect(conOpt, null, iMqttActionListener);
//                IMqttToken token = client.connect(conOpt);
//
//                token.setActionCallback(new IMqttActionListener() {
//                    @Override
//                    public void onSuccess(IMqttToken asyncActionToken) {
//                        Log.d(TAG, "onSuccess");
//                    }
//                    @Override
//                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                        Log.d(TAG, "onFailure ");
//                        exception.printStackTrace();
//                    }
//                });

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    // MQTT是否連接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "連接成功 ");
            try {
                // 訂閱myTopic話題
                client.subscribe(myTopic,1);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            Log.i(TAG, "連接失敗 "+   arg1.getMessage());
            Log.i(TAG, "連接失敗 "+   arg1.toString());
            Log.i(TAG, "連接失敗 "+   arg0.getException());
            Log.i(TAG, "連接失敗 "+   arg0.toString());
            // 連接失敗，重連
        }
    };

    // MQTT監聽並且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            String str1 = new String(message.getPayload());
            if (callBack != null){
                callBack.setMessage(str1);
            }
            String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
            Log.i(TAG, "messageArrived:" + str1);
            Log.i(TAG, str2);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去連接，重連
        }
    };

    /** 判斷網絡是否連接 */
    private boolean isConnectIsNormal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT當前網絡名稱：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT 沒有可用網絡");
            return false;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.e(getClass().getName(), "onBind");
        return new CustomBinder();
    }

    public void setIGetMessageCallBack(IGetMessageCallBack callBack){
        this.callBack = callBack;
    }

    public class CustomBinder extends Binder {
        public MQTTService getService(){
            return MQTTService.this;
        }
    }

    public  void toCreateNotification(String message){
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this,MQTTService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);//3、創建一個通知，屬性太多，使用構造器模式

        Notification notification = builder
                .setTicker("測試標題")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("")
                .setContentText(message)
                .setContentInfo("")
                .setContentIntent(pendingIntent)//點擊後才觸發的意圖，“掛起的”意圖
                .setAutoCancel(true)        //設置點擊之後notification消失
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(0, notification);
        notificationManager.notify(0, notification);

    }
}
