package com.flystar.timenotes;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;


public class DetailActivity extends Activity implements View.OnClickListener {
    private String id,content,imgPath,videoPath;
    private Intent intent;
    private ImageView imageView;
    private VideoView videoView;
    private TextView textView;
    private Button btnDelete;
    private Button btnBack;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = (ImageView) findViewById(R.id.iv_img);
        videoView = (VideoView) findViewById(R.id.vv_video);
        textView = (TextView) findViewById(R.id.tvText);
        btnDelete = (Button) findViewById(R.id.btDelete);
        btnBack = (Button) findViewById(R.id.btBack);


        intent = getIntent();
        id = intent.getStringExtra(NotesDB.ID);
        content = intent.getStringExtra(NotesDB.CONTENT);
        imgPath = intent.getStringExtra(NotesDB.PATH);
        videoPath = intent.getStringExtra(NotesDB.VIDEO);


        btnDelete.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        if(getIntent().getStringExtra(NotesDB.PATH).equals("null"))
        {
            Log.i("ffllyy","iv不可见");

            imageView.setVisibility(View.GONE);

        }
        else {
            Log.i("ffllyy","iv可见");
            imageView.setVisibility(View.VISIBLE);
        }

        if(getIntent().getStringExtra(NotesDB.VIDEO).equals("null"))
        {
            videoView.setVisibility(View.GONE);
        }
        else
        {
            videoView.setVisibility(View.VISIBLE);
        }

        textView.setText(getIntent().getStringExtra(NotesDB.CONTENT));


        if(getIntent().getStringExtra(NotesDB.PATH) != null) {
            Log.i("ffllyy******", "BitmapFactory=====before" + getIntent().getStringExtra(NotesDB.PATH));
            Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra(NotesDB.PATH));
            Log.i("ffllyy******", "BitmapFactory-----after" + getIntent().getStringExtra(NotesDB.PATH));
//            imageView.setImageBitmap(getImageThumbnail(getIntent().getStringExtra(NotesDB.PATH),200,200));
            imageView.setImageBitmap(getImageThumbnail(imgPath,270,480));
        }
        if(getIntent().getStringExtra(NotesDB.VIDEO) != null)
        {
            videoView.setVideoURI(Uri.parse(getIntent().getStringExtra(NotesDB.VIDEO)));
            videoView.start();


        }



        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btDelete:
                deleteData();
                finish();
                break;
            case R.id.btBack:
                finish();
                break;
        }

    }


    public void deleteData()
    {
        Log.i("ffllyy***","id=   "+getIntent().getIntExtra(NotesDB.ID,0));
        Log.i("ffllyy***","id   ----=   "+ id);
        dbWriter.delete(NotesDB.TABLE_NAME,"_id=" + id,null);
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
