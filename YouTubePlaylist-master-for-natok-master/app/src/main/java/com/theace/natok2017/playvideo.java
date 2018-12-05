package com.theace.natok2017;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Timer;



public class playvideo extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{


    private String TAG = playvideo.class.getSimpleName();
    InterstitialAd mInterstitialAd;

    Timer autoUpdate;

    final long delayMills=60000; //180 sec or 3 min
   public static Runnable refresh;
   // Handler m_handler=null;
 public static    Handler m_hanlder=null;
    public  static  String videoID;
    public  static  final  int RECOVERY_RECQUEST=1;
    private YouTubePlayerView youTubePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_play);

       ///*******************for first time showing Tutorials******************

      /*  Boolean isFirstRun=getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("Isfirstrun2",true);
        if(isFirstRun){
            Intent intent =new Intent(this,full_screen.class);
            startActivity(intent);
            Toast.makeText(this, "First Time2", Toast.LENGTH_SHORT).show();

            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("Isfirstrun2",false).commit();
        }*/

        //**************************END**************************



        youTubePlayerView=(YouTubePlayerView) findViewById(R.id.youtube_view_t);
        youTubePlayerView.initialize(ApiKey.API_KEY,this);

  /*      {
            mInterstitialAd = new InterstitialAd(this);

            // set the ad unit ID
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            // Load ads into Interstitial Ads
            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
        }*/

//handler
        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId("ca-app-pub-4967145486237755/9695191386");

     final   AdRequest adRequest = new AdRequest.Builder()
                .build();


      /*  // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);


        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

*/


///if(YouTubeActivity.firstTime) {

        //   firstTime=false;
    //Toast.makeText(this, "firstTime", Toast.LENGTH_SHORT).show();
    m_hanlder = new Handler(Looper.getMainLooper());
    refresh = new Runnable() {
        @Override
        public void run() {

         //   Toast.makeText(playvideo.this, "Calling", Toast.LENGTH_SHORT).show();
            {

                // Load ads into Interstitial Ads
                mInterstitialAd.loadAd(adRequest);


                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });

                m_hanlder.postDelayed(this, delayMills);
            }

        }
    };
    m_hanlder.post(refresh);
//}

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

public void showCustomAlert(){
    Context context = getApplicationContext();
    // Create layout inflator object to inflate toast.xml file
    LayoutInflater inflater = getLayoutInflater();

/**
 * Created by Tareq on 12/8/2016.
 */
    // Call toast.xml file for toast layout
    View toastRoot = inflater.inflate(R.layout.video_play, null);

    Toast toast = new Toast(context);

    // Set layout to toast
    toast.setView(toastRoot);
    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
            0, 0);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.show();
}


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b){
            youTubePlayer.cueVideo(videoID); //the url
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this,RECOVERY_RECQUEST).show();
        }else{
            String error=String.format("Error Initialize Youtube Player",youTubeInitializationResult.toString());
            Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RECOVERY_RECQUEST){
            getYouTubePlayerProvider().initialize(ApiKey.API_KEY,this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider(){
        return  youTubePlayerView;
    }


    @Override
    public void onDestroy() {

        m_hanlder.removeCallbacks(refresh);
        m_hanlder.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        m_hanlder.removeCallbacks(refresh);
        m_hanlder.removeCallbacksAndMessages(null);

        finish();
        super.onBackPressed();
    }

}
