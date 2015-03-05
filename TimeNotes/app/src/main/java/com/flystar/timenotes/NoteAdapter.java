package com.flystar.timenotes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by flystar on 2015/3/2.
 */
public class NoteAdapter extends BaseAdapter
{
    private static Context context;
    private Cursor cursor;

    public static Context getContext() {
        return context;
    }

    public NoteAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_note_cell,null);
            convertView.setTag(R.id.tag_first,convertView.findViewById(R.id.tv_cell_content));
            convertView.setTag(R.id.tag_second,convertView.findViewById(R.id.tv_cell_time));
            convertView.setTag(R.id.tag_img,convertView.findViewById(R.id.cell_img));
            convertView.setTag(R.id.tag_video,convertView.findViewById(R.id.cell_video));

        }
        CellView.note_content = (TextView) convertView.getTag(R.id.tag_first);
        CellView.note_time = (TextView) convertView.getTag(R.id.tag_second);
        CellView.note_img = (ImageView) convertView.getTag(R.id.tag_img);
        CellView.note_video = (ImageView) convertView.getTag(R.id.tag_video);


        cursor.moveToPosition(position);

        String content = cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT));
        String time = cursor.getString(cursor.getColumnIndex(NotesDB.TIME));
        String urlImg = cursor.getString(cursor.getColumnIndex(NotesDB.PATH));
        String urlVideo = cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO));


        CellView.note_content.setText(content);
        CellView.note_time.setText(time);
        CellView.note_img.setImageBitmap(getImageThumbnail(urlImg,200,200));
        CellView.note_video.setImageBitmap(getVideoThumnail(urlVideo,200,200, MediaStore.Images.Thumbnails.MICRO_KIND));




        return convertView;
    }

    private Bitmap getVideoThumnail(String uri,int width,int height,int kind)
    {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri,kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,width,height);
        return bitmap;
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
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return  bitmap;
    }

    private static class CellView
    {
        static TextView note_content;
        static TextView note_time;
        static ImageView note_img;
        static ImageView note_video;
    }
}
