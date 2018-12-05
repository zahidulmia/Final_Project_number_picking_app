package com.theace.natok2017;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistListResponse;

import java.util.ArrayList;
import java.util.List;

public class PlayList_Selection extends AppCompatActivity {





    private AdView mAdView;

    private YouTube mYoutubeDataApi;

    public  static  int selected_pos;

    private String[] mPlaylistIds= {


           /* "PLKjns90ns08ANwC_5zJFvR2zrIijn1yyE",
            "PLXE2BchMCYLpg-syONl1S-aYrlHczbzAO",
            "PL2477CD2C0FB68F02",
            "PLIBcEGRmBj5AfwGpOlbY8pdMGxWX5G_Hj",
            "PLIBcEGRmBj5CvMRSDMqW6NPvBJDGEleyy"*/

            "PLm8d4p5E2sUm5urMuqe4LXeHntHA5W2KE",

            "PLm8d4p5E2sUnCVeSQ2Rs627LeLUTU7wzr",
            "PLsTbM4b-45gJ3fdVQL7txEQZZv0oNFfGO",
            "PLsTbM4b-45gLJJs1hgwXJutksSL9OBRGl",
            "PLm8d4p5E2sUm5urMuqe4LXeHntHA5W2KE"
    };




    private final GsonFactory mJsonFactory = new GsonFactory();
    private final HttpTransport mTransport = AndroidHttp.newCompatibleTransport();

    ArrayList<String> mPlaylistTitles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list__selection);



        if (haveNetworkConnection()) {
            if (savedInstanceState == null) {
                mYoutubeDataApi = new YouTube.Builder(mTransport, mJsonFactory, null)
                        .setApplicationName("slime videos")
                        .build();

            }


            final ListView playlist_view = (ListView) findViewById(R.id.listview_tareq); //here I am calling the listview

            final Context m_Context = this;

            if (mPlaylistTitles == null || mPlaylistTitles.isEmpty()) {
                // initialize the spinner with the playlist ID's so that there's something in the UI until the GetPlaylistTitlesAsyncTask finishes
              /*


              ArrayAdapter<List<String>> adapter = new ArrayAdapter(m_Context,
                        R.layout.listview_card,R.id.title_card, mPlaylistIds);
*/



                List<String> playLIST = new ArrayList<String>();
                for (String obj : mPlaylistIds) {
                    playLIST.add(obj);

                }

                CustomAdapter adapter=new CustomAdapter(PlayList_Selection.this,playLIST);
                playlist_view.setAdapter(adapter);
            }

            ///add
           // MobileAds.initialize(getApplicationContext(), "ca-app-pub-9804066021969935/1415226405");

            mAdView = (AdView) findViewById(R.id.adView_selection);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);



// start fetching the playlist titles
            new GetPlaylistTitlesAsyncTask(mYoutubeDataApi) {
                @Override
                protected void onPostExecute(PlaylistListResponse playlistListResponse) {
                    super.onPostExecute(playlistListResponse);
                    mPlaylistTitles = new ArrayList();
                    for (Playlist playlist : playlistListResponse.getItems()) {
                        mPlaylistTitles.add(playlist.getSnippet().getTitle());
                    }
                    // update the spinner adapter with the titles of the playlists
                    //       ArrayAdapter<List<String>> spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, mPlaylistTitles);

                   /* ArrayAdapter<List<String>> adapter = new ArrayAdapter(m_Context,
                            R.layout.listview_card, R.id.title_card, mPlaylistTitles);
*/
                    List<String> playLIST = new ArrayList<String>(); //this is how we will create an empty List or arraylist<String>
                    for (String obj : mPlaylistTitles) {
                        playLIST.add(obj);

                    }
                    CustomAdapter adapter=new CustomAdapter(PlayList_Selection.this,playLIST);
                    playlist_view.setAdapter(adapter);
                }
            }.execute(mPlaylistIds);

            //playlist_view.setAdapter(YouTubeRecyclerViewFragment.spinnerAdapter);


            playlist_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                   // Toast.makeText(m_Context, "Hello", Toast.LENGTH_SHORT).show();
                    // selected item
                 //   String product = ((TextView) view).getText().toString();

                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(m_Context, YouTubeActivity.class);
                    // sending data to new activity
                 //   i.putExtra("product", product);
                    selected_pos = position;
                    YouTubeActivity.YOUTUBE_PLAYLISTS[0] = mPlaylistIds[position];
                     //Toast.makeText(m_Context, "m_Playlist"+mPlaylistIds[position] + "**TAREQ**"+YouTubeActivity.YOUTUBE_PLAYLISTS[0], Toast.LENGTH_LONG).show();

                    startActivity(i);

                }
            });
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


