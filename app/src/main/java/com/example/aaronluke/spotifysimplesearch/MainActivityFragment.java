package com.example.aaronluke.spotifysimplesearch;

// various imported libraries
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import java.io.IOException;
import android.widget.Toast;
import android.content.Context;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

// TODO: remove once doen: from old design- keeping around "just in case"

import android.content.res.Resources;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.RetrofitError;

// TODO: will need to make fragment for second screen and switch them

// Fragment for Main Screen
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

        // TODO: need to work on bundling state
        super.onCreate(savedInstanceState);

        // allocate array list
        artistList = new ArrayList();

        // grabbing rootView to manipulate
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // TODO: pass in from text box instead of static

        // get reference to artist input box
        final EditText inputArtistBox = (EditText) rootView.findViewById(R.id.artist_name_input);

        inputArtistBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // clearing artistList for repopulation
                artistList.clear();

                // perform artist search and add to artistStringList in background
                new populateArtistSearch().execute(inputArtistBox.getText().toString());

                // notify adapter that data set has changed and needs to grab this
                mArtistAdapter.notifyDataSetChanged();

                // was trying to hide keyboard
                // hideKeyboard();

            }
        });

        // populating adapter with current activity, layout ID, id of textview, and the string data
        mArtistAdapter =
                new ArrayAdapter<>(
                        getActivity(),
                        R.layout.artist_list,
                        R.id.artist_list_text_view,
                        artistList);

        // retrieving a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.artist_list_view);
        listView.setAdapter(mArtistAdapter);

        // finish();
        // startActivity(getIntent());

        // returning rootView that now has adapter in place
        return rootView;
    }

    private void hideKeyboard() {
        View view = this.getView();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // method to search for Artist
    public class populateArtistSearch extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            // TODO: implement try/catch
            //try {
            String query = params[0];
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            ArtistsPager artistsPager = spotify.searchArtists(query);

            // grab all artists that match
            List<Artist> listOfArtists = artistsPager.artists.items;

            // cycle through artist list names to add to artistStringList
            for (Artist element : listOfArtists) {

                // add name to artistList
                artistList.add(element.name);

                // logging for debug purposes
                Log.d("Name", element.name);

                // TODO: need to grab and display image
                // element.id;

                // logging for debug purposes
                Log.d("id", element.id);

            }

            // Log success
            Log.d("artist success", artistsPager.toString());

            // Todo: pop up toast to declare success
            // Toast.makeText(getApplicationContext(), "Artist(s) Found!", Toast.LENGTH_SHORT).show();

            return null;



            //} catch (IOException e) {

            //Log.d("artist failure",);
            // Todo: pop up toast to declare success
            //oast.makeText(getView().getContext(), "Artist Not Found", Toast.LENGTH_SHORT).show();
            //}

        }

    }
}
