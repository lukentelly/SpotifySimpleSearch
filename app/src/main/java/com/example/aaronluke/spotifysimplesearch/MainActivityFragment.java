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

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Adapter to tie the data to the listview
        ArrayAdapter<String> mArtistAdapter;

        // getting pointer to resources- will pull strings, views from this
        Resources res = getResources();

        // creating string list based on temp_list_items out of strings.xml
        List<String> listOfNumbers = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.temp_list_items)));

        // TODO: will need to load this with artists
        performArtistSearch("Keith Green");

        // populating adapter with current activity, layout ID, id of textview, and the string data
        mArtistAdapter =
                new ArrayAdapter<>(
                        getActivity(),
                        R.layout.artist_list,
                        R.id.artist_list_text_view,
                        listOfNumbers);

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

        spotify.getArtist(query, new Callback<Artist>() {
            @Override
            public void success(Artist artist, Response response) {
                Log.d("artist success", artist.name);
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d("artist failure", error.toString());
            }
        });
    }


}
