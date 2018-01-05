package com.codegym.downloadfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnItemClickListener {

    private ProgressDialog mProgressDialog;
    private RecyclerView rvListSong;
    private List<Song> songList;
    SongAdapter songAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvListSong = findViewById(R.id.rvListSong);
        rvListSong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //Tạo dữ liệu cố định gồm 3 bài hát, mỗi bài hát gồm tên và link tải.
        songList = new ArrayList<>();
        songList.add(new Song("Buồn của anh", "https://zmp3-mp3-s1-te-vnno-pt-1.zadn.vn/ffe961cab58e5cd0059f/68695268730484457?authen=exp=1515162680~acl=/ffe961cab58e5cd0059f/*~hmac=dc9042ff48cf414dab2f36dcbafef2ca&filename=Buon-Cua-Anh-K-ICM-Dat-G-Masew.mp3"));
        songList.add(new Song("Kém duyên", "https://zmp3-mp3-s1-te-vnno-vn-5.zadn.vn/ff1578e2aca645f81cb7/285902104879125743?authen=exp=1515170505~acl=/ff1578e2aca645f81cb7/*~hmac=6f6d16acd2dc0a44cca236155e4847db&filename=Kem-Duyen-Rum-NIT-Masew.mp3"));
        songList.add(new Song("Đã lỡ yêu em nhiều", "https://zmp3-mp3-s1-te-vnno-vn-6.zadn.vn/7147afa27be692b8cbf7/3858972813232405650?authen=exp=1515170516~acl=/7147afa27be692b8cbf7/*~hmac=e47a30b8221744fe5d656df1b3b9c25a&filename=Da-Lo-Yeu-Em-Nhieu-JustaTee.mp3"));
        songAdapter = new SongAdapter(MainActivity.this, this);
        songAdapter.setItems(songList);
        rvListSong.setAdapter(songAdapter);
        // Tạo progressDialog để hiển thị tiến trình tải
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Downloading....");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);


    }

    @Override
    public void onItemClick(Song item) {
        //Kiểm tra kết nối internet, nếu có thì startService để tiến hành download, nếu không thì hiển thị thông báo
        if (isNetworkConnected()) {
            mProgressDialog.show();
            Intent intent = new Intent(MainActivity.this, DownloadService.class);
            intent.putExtra(DownloadService.FILENAME, item.getName() + ".mp3");
            intent.putExtra(DownloadService.URL, item.getUrl());
            intent.putExtra("receiver", new DownloadReceiver(new Handler()));
            startService(intent);
        } else
            Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();

    }

        //Class này để giao tiếp giữa Service và Activity
    private class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        //Nhận dữ liệu từ service truyền sang để set vào progressDialog
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    Toast.makeText(getApplicationContext(), "Downloaded.", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            }
        }
    }
        // Phương thức kiểm tra kết nối mạng
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
