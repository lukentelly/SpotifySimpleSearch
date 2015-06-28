package com.example.aaronluke.spotifysimplesearch;

// various imported libraries
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.view.ViewGroup;
import android.content.res.Resources;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.RetrofitError;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    // list of Artists
    // TODO: create array list of artist, artist ID, artist image, etc
    private ArrayList<String> artistList;

    // Adapter to tie the data to the listview
    private ArrayAdapter<String> mArtistAdapter;

    public MainActivityFragment() {
    }

    // TODO: need an oncreate for non-visual elements- move out of onCreateView()

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // allocate array list
        artistList= new ArrayList();

        // perform artist search and add to artistStringList
        // TODO: pass in from text box instead of static
        performArtistSearch("Keith");

        // populatin
        // g adapter with current activity, layout ID, id of textview, and the string data
        mArtistAdapter =
                new ArrayAdapter<>(
                        getActivity(),
                        R.layout.artist_list,
                        R.id.artist_list_text_view,
                        artistList);

        // grabbing rootView to manipulate
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // retrieving a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.artist_list_view);
        listView.setAdapter(mArtistAdapter);

        // returning rootView that now has adapter in place
        return rootView;
    }

    // method to search for Artist
    private void performArtistSearch (String query) {

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();

        // getting pointer to resources- will pull strings, views from this
        // Resources res = getResources();

        spotify.searchArtists(query, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {

                // grab all artists that match
                List<Artist> listOfArtists = artistsPager.artists.items;

                // cycle through artist list names to add to artistStringList
                for(Artist element : listOfArtists){
                    artistList.add(element.name);
                    Log.d("Name", element.name);
                }

                // notify adapter that data changed via notifyDataSetchange

                Log.d("artist success", artistsPager.toString());

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("artist failure", error.toString());

            }
        });

    }

    // TODO: need to implement a call back for the text editor
    // onEditorActionListener(new TextView.OnEditorActionListener()


}
