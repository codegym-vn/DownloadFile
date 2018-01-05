package com.codegym.downloadfile;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadService extends IntentService {

    public static final int UPDATE_PROGRESS = 8344;
    public static final String URL = "url";
    public static final String FILENAME = "name";
    public DownloadService() {
        super("DownloadService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String urlToDownload = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        try {

                URL url = new URL(urlToDownload);
                URLConnection connection = url.openConnection();
                connection.connect();

                // Biến này để có thể hiển thị tiến trình download từ 0-100%
                int fileLength = connection.getContentLength();

                // Thực hiện download file và lưu vào mục Music của điện thoại
                File storagePath = new File(Environment.getExternalStorageDirectory() + "/Music");
                InputStream input = new BufferedInputStream(connection.getInputStream());
                OutputStream output = new FileOutputStream(new File(storagePath,fileName));
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Xử lí truyền dữ liệu sang Activity để set vào progressDialog
                    Bundle resultData = new Bundle();
                    resultData.putInt("progress", (int) (total * 100 / fileLength));
                    receiver.send(UPDATE_PROGRESS, resultData);
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
