package souleima.qcm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class Starter extends AppCompatActivity {
    protected ProgressBar mProgressBar;
    protected  boolean mbActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        final Thread timerThread =new Thread(){
            @Override
            public void run(){
                mbActive=true;
                try{
                    int waited=0;
                    while(mbActive && (waited <4000)){
                        sleep(10);
                        if(mbActive){
                            waited +=10;
                            updateProgress(waited);
                        }
                    }
                }
                catch (InterruptedException e){}
                finally {
                    onContinue();
                }
            }
        };
        timerThread.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Starter.this, Qodorat.class);
                startActivity(i);
                finish();
            }
        }, 5000);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void updateProgress (final int timePassed){
        if(null != mProgressBar){
            final int progress=mProgressBar.getMax() * timePassed /4000 ;
            mProgressBar.setProgress(progress);
        }
    }
    public void onContinue(){
        Log.d("message","ok progress");
    }
}
