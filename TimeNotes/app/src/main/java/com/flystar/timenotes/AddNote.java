package com.flystar.timenotes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddNote extends Activity implements View.OnClickListener {
    private Button btnSave,btnCancle;
    private EditText etText;
    private NotesDB notesDB;
    private ImageView ivimg;
    private VideoView videoView;
    private File picFile;
    private File videoFile;

    private SQLiteDatabase databaseWriter;

    private String val;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancle = (Button)findViewById(R.id.btnCancle);

        etText = (EditText)findViewById(R.id.etText);
        ivimg = (ImageView) findViewById(R.id.iv_img);
        videoView = (VideoView) findViewById(R.id.v_video);


        btnCancle.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        notesDB = new NotesDB(this);
        databaseWriter = notesDB.getWritableDatabase();

        val = getIntent().getStringExtra("flag");

        Log.i("ffllyy*****","initView");


        initView();
    }

    private void initView()
    {
        switch ( getIntent().getStringExtra("flag"))
        {
            case "1":
                ivimg.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                break;
            case "2":
                ivimg.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
                Log.i("ffllyy*****","打开相机");
                Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                picFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +getTime()+".jpg"); //指定图片路径和名称
                picIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));   //添加额外的参数指定相机拍照后图片的名称路径
                startActivityForResult(picIntent, 1);

                break;
            case "3":
                ivimg.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);

                Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                videoFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getTime()+".mp4");
                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(videoFile));
                startActivityForResult(videoIntent,2);
                break;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSave:
                addDB();
                finish();
                break;
            case R.id.btnCancle:
                finish();
                break;
        }


    }

    private void addDB() {
        ContentValues values = new ContentValues();
        values.put(NotesDB.CONTENT,etText.getText().toString());
        values.put(NotesDB.TIME,getTime());
        values.put(NotesDB.PATH,picFile+"");
        values.put(NotesDB.VIDEO,videoFile+"");
        databaseWriter.insert(NotesDB.TABLE_NAME,null,values);
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return format.format(date);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            //更具一个文件的路径生成一个bitmap对象
            Bitmap bitmap = BitmapFactory.decodeFile(picFile+"");
            ivimg.setImageBitmap(getImageThumbnail(picFile+"",200,200));
        }

        if(requestCode == 2)
        {
            videoView.setVideoURI(Uri.fromFile(videoFile));
            videoView.start();

        }
    }



    public Bitmap getImageThumbnail(String uri,int width , int height)
    {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri,options);
        options.inJustDecodeBounds = false;

        int beWidth = options.outWidth/width;
        int beheight = options.outHeight/height;

        int be = 1;
        if(beWidth < beheight)
        {
            be = beWidth;
        }
        else
        {
            be = beheight;
        }
        if(be <=0)
        {
            be = 1;
        }

        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri,options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return  bitmap;
    }
}
