package com.example.admin.clicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    long ideas = 0;
    long time;
    SharedPreferences sp;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button click = findViewById(R.id.btn);
        txt = findViewById(R.id.txt);


        sp = getSharedPreferences("hasVisited",
                Context.MODE_PRIVATE);
        boolean hasVisited = sp.getBoolean("hasVisited", false);


        if (!hasVisited) {



            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.putLong("ideas",0);
            e.commit();
            txt.setText("На счету 0 оригинальных идей");


        }
        else{
            try {
                ideas = sp.getLong("ideas", 0);
                time = sp.getLong("time", System.currentTimeMillis());

                ideas += (System.currentTimeMillis() - time) / (1000 );
                txt.setText("На счету " + ideas + " чего-то");
            } catch (Exception e){
                Log.e("ALARM",e.getMessage());
                ideas = 0;
            }
        }




        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    txt.setText("На счету " + ++ideas + " чего-то");
                }catch (Exception e){
                    Log.e("ALARM",e.getMessage());
                    ideas = 0;
                }
            }
        });


        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    ideas++;
                    Message stringMessage = Message.obtain(myTextHandler);
                    stringMessage.obj = "На счету " + ideas + " чего-то";
                    stringMessage.sendToTarget();
                    Log.e("BEZ_PANIKI", String.valueOf(ideas));
                }catch (Exception e){
                    Log.e("ALARM",e.getMessage());
                    ideas = 0;
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor e = sp.edit();
        e.putLong("time", System.currentTimeMillis());
        e.putLong("ideas",ideas);
        e.commit();
    }

    private final Handler myTextHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message stringMessage) {

            txt.setText((String) stringMessage.obj);
            return true;
        }
    });
    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            ideas = sp.getLong("ideas", 0);
            time = sp.getLong("time", System.currentTimeMillis());
            ideas += (System.currentTimeMillis() - time) / (1000);
            txt.setText("На счету " + ideas + " чего-то");
        } catch (Exception e){
            Log.e("ALARM",e.getMessage());
            ideas = 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            ideas = sp.getLong("ideas", 0);
            time = sp.getLong("time", System.currentTimeMillis());
            ideas += (System.currentTimeMillis() - time) / (1000 );
            txt.setText("На счету " + ideas + " чего-то");
        } catch (Exception e){
            Log.e("ALARM",e.getMessage());
            ideas = 0;
        }

    }


}



