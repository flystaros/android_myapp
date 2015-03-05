package com.flystar.timenotes;

        import android.app.Activity;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.ListView;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnText,btnImg,btnVideo;
    private ListView listview;
    private Intent intent;
    private SQLiteDatabase dbReader;
    private NotesDB notesDB;
    private NoteAdapter adapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       initView();

    }

    private void initView()
    {

        btnText = (Button) findViewById(R.id.btnText);
        btnImg = (Button) findViewById(R.id.btnImg);
        btnVideo = (Button) findViewById(R.id.btnVideo);

        listview = (ListView) findViewById(R.id.list);


        btnText.setOnClickListener(this);
        btnImg.setOnClickListener(this);
        btnVideo.setOnClickListener(this);


        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent detailIntent = new Intent(MainActivity.this,DetailActivity.class);
                detailIntent.putExtra(NotesDB.ID,cursor.getString(cursor.getColumnIndex(NotesDB.ID)));
                detailIntent.putExtra(NotesDB.CONTENT,cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
                detailIntent.putExtra(NotesDB.PATH,cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                detailIntent.putExtra(NotesDB.VIDEO,cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
                startActivity(detailIntent);
            }
        });





    }


    @Override
    public void onClick(View v)
    {
        intent = new Intent(this,AddNote.class);
        switch (v.getId())
        {
            case R.id.btnText:
                intent.putExtra("flag","1");
                startActivity(intent);
                break;
            case R.id.btnImg:
                intent.putExtra("flag","2");
                startActivity(intent);
                break;
            case R.id.btnVideo:
                intent.putExtra("flag","3");
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        selectedDB();
    }

    private void selectedDB() {
        cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, null);
        adapter = new NoteAdapter(this,cursor);
        listview.setAdapter(adapter);

    }
}
