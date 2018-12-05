package com.theace.natok2017;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;

import java.util.Timer;


public class YouTubeActivity extends AppCompatActivity {
    public static  String[] YOUTUBE_PLAYLISTS = {

            "PLIBcEGRmBj5CvMRSDMqW6NPvBJDGEleyy"
    };

    public static boolean firstTime=true; //for stopping thread
    private Timer autoUpdate;

    private YouTube mYoutubeDataApi;
    private final GsonFactory mJsonFactory = new GsonFactory();
    private final HttpTransport mTransport = AndroidHttp.newCompatibleTransport();

//tareq
    private AdView mAdView;











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);





if(haveNetworkConnection()){


    /// ***************For showing tutorials on first time use**************
   /* Boolean isFirstRun=getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("Isfirstrun",true);
    if(isFirstRun){
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "First Time", Toast.LENGTH_SHORT).show();

        getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("Isfirstrun",false).commit();
    }*/
//*****************************End*****************************
    if (ApiKey.API_KEY.startsWith("YOUR_API_KEY")) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Edit ApiKey.java and replace \"YOUR_API_KEY\" with your Applications Browser API Key")
                .setTitle("Missing API Key")
                .setNeutralButton("Ok, I got it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    } else if (savedInstanceState == null) {
        mYoutubeDataApi = new YouTube.Builder(mTransport, mJsonFactory, null)
                .setApplicationName(getResources().getString(R.string.app_name))
                .build();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, YouTubeRecyclerViewFragment.newInstance(mYoutubeDataApi, YOUTUBE_PLAYLISTS))
                .commit();

    }


    /////*********************Comment section for Add**********************


 //MobileAds.initialize(getApplicationContext(), "ca-app-pub-9804066021969935/1415226405");

    mAdView = (AdView) findViewById(R.id.adView_youtube);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);
////**********************END***********************************

  //  Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
}else{
    Toast.makeText(getApplicationContext(), "Trying to make Connection..", Toast.LENGTH_LONG).show();
    Button retry=new Button(getApplicationContext());
    retry.setText("Retry To Connect");
    FrameLayout ll = (FrameLayout) findViewById(R.id.container);
    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    ll.addView(retry, lp);
    retry.setOnClickListener(new Button.OnClickListener() {
        @Override
         public void onClick(View v) {
            finish();
            startActivity(getIntent());
        }
    });


}


    }

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.you_tube, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_recyclerview) {

            Toast.makeText(this,"id:"+id, Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, YouTubeRecyclerViewFragment.newInstance(mYoutubeDataApi, YOUTUBE_PLAYLISTS))
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }


    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }

        super.onDestroy();
    }
}
