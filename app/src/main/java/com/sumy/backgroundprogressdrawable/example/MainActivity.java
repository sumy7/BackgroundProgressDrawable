package com.sumy.backgroundprogressdrawable.example;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sumy.backgroundprogressdrawable.BackgroundProgressDrawable;
import com.sumy.backgroundprogressdrawable.R;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BackgroundProgressDrawable.Builder progressDrawable = new BackgroundProgressDrawable.Builder().setMax(1000).setAlpha(150);
        final BackgroundProgressDrawable drawable = new BackgroundProgressDrawable.Builder().create();
        drawable.setMax(100);

        TextView text = (TextView) findViewById(R.id.text);
        text.setBackgroundDrawable(drawable);

        drawable.setProgress(20);

        Button increase = (Button) findViewById(R.id.increaseten);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawable.setProgress(drawable.getProgress() + 10);
            }
        });
        Button decrease = (Button) findViewById(R.id.decreaseten);
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawable.setProgress(drawable.getProgress() - 10);
            }
        });

        Button random = (Button) findViewById(R.id.random);
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawable.setProgress((int) (Math.random() * drawable.getMax()));
            }
        });

        final Button download = (Button) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundProgressDrawable drawable1 = progressDrawable.create();
                download.setBackgroundDrawable(drawable1);
                drawable1.setProgress(drawable1.getMax());
            }
        });

        final BackgroundProgressDrawable layoutprogress = new BackgroundProgressDrawable.Builder().setMode(BackgroundProgressDrawable.MODE_FULL).setMax(1000).setDuration(5000).create();
        layoutprogress.setProgressNative(0);
        findViewById(R.id.layout).setBackgroundDrawable(layoutprogress);
        Button scan = (Button) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutprogress.setProgressNative(1000);
                layoutprogress.setProgress(0);
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_listview, null);
                BackgroundProgressDrawable drawable = progressDrawable.create();
                view.setBackgroundDrawable(drawable);
                int progress = (int) (Math.random() * 1000);
                ((TextView) view.findViewById(R.id.text_progress)).setText("" + progress);
                drawable.setProgress(progress);
                return view;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
