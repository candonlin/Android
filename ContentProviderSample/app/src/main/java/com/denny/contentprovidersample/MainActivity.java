package com.denny.contentprovidersample;

import android.Manifest;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.denny.contentprovidersample.db.AppDataBase.DBNAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        findViewById(R.id.btn_export).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        checkPremission();
    }
    //region Android 6.0以上取得權限
    public boolean checkPremission() {
        //取得權限
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(MainActivity.this, perms)) {
            EasyPermissions.requestPermissions(MainActivity.this, "請按確定並允許儲存權限",
                    100, perms);
            return false;
        }
        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
        Toast.makeText(getApplicationContext(), "權限拒絕! 無法使用匯出功能", Toast.LENGTH_LONG).show();
    }
    //endregion
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_export:
                if (!checkPremission()) {
                    return;
                }

                File DbFile = getDatabasePath(DBNAME);
                //資料庫位置
                File exportDir = new File(Environment.getExternalStorageDirectory(), "DBExport"); //匯出位置
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }
                File backup = new File(exportDir, DbFile.getName());

                try {
                    backup.createNewFile();
                    fileCopy(DbFile, backup);
                    Toast.makeText(MainActivity.this,"資料庫匯出成功",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    //TODO:
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"匯出失敗，請聯絡系統管理員",Toast.LENGTH_SHORT).show();

                } finally {
                    reScanSdCard();
                }
                break;
            case R.id.btn_delete:
                try {

                    MyContentProvider.sdb.delete("MOVIE", null, null);
                    Toast.makeText(MainActivity.this,"資料庫刪除成功",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    //TODO:
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"刪除失敗，請聯絡系統管理員",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void fileCopy(File dbFile, File backup) throws IOException {
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
    //重新掃描儲存空間
    public void reScanSdCard() {
        try {
            String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
            MediaScannerConnection.scanFile(getApplicationContext(), paths, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
