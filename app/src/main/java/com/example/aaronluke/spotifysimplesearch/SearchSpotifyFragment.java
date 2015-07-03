package com.example.aaronluke.spotifysimplesearch;

// various imported libraries
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.content.Context;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import android.content.Intent;
import android.widget.AdapterView;


// TODO: will need to make fragment for second screen and switch them

// Fragment for Main Screen
public class SearchSpotifyFragment extends Fragment {

    // TODO: refactor array list to include artist, artist ID, artist image, etc
    private ArrayList<String> artistList;
    private ListView listView;
    private View rootView;
    private ViewGroup myContainer;
    private LayoutInflater myInflater;
    private EditText inputArtistBox;
    private ArrayAdapter<String> mArtistAdapter;
    private Boolean mSearchSuccess;

    public SearchSpotifyFragment() {
    }

    // TODO: need to move non-visual elements- move out of onCreateView()
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO: need to work on bundling state
        super.onCreate(savedInstanceState);

        // allocate array list
        artistList = new ArrayList();

        // saving off container for us in other methods
        myContainer= container;

        myInflater= inflater;

        mSearchSuccess= Boolean.TRUE;

        // grabbing rootView to manipulate
        rootView = myInflater.inflate(R.layout.search_spotify_fragment_main, myContainer, false);

        // TODO: pass in from text box instead of static

        // get reference to artist input box
        inputArtistBox = (EditText) rootView.findViewById(R.id.artist_name_input);

        // IN INPUT BOX  identify when the user his hit enter or clicked on box
        inputArtistBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // perform artist search and add to artistStringList in background
                new populateArtistSearch().execute(inputArtistBox.getText().toString());

                String popUp;

                if (mSearchSuccess) {
                    popUp= inputArtistBox.getText().toString();
                }
                else {
                    popUp= "No Artist by that Name";
                }

                Toast toast = Toast.makeText(rootView.getContext(), popUp , Toast.LENGTH_SHORT);
                toast.show();
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
        listView = (ListView) rootView.findViewById(R.id.artist_list_view);
        listView.setAdapter(mArtistAdapter);


        // IN LISTVIEW monitor clicks to identify which item was selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // pop up what the artist was that was selected
                Toast toast= Toast.makeText(rootView.getContext(), mArtistAdapter.getItem(position), Toast.LENGTH_SHORT);
                toast.show();

                // TODO: Create new Fragment and populate with top 10
                Intent intent = new Intent(getActivity(), TopTenActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, mArtistAdapter.getItem(position));
                startActivity(intent);

            }
        });

        // returning rootView that now has adapter in place
        return rootView;
    }

    // when menu item selected, do this
    private void onOptionsItemSelected(){
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


            // check to see if there is anything
            if (params.length == 0) {

                return null;
            }

            String query = params[0];
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            ArtistsPager artistsPager = spotify.searchArtists(query);

            // grab all artists that match
            List<Artist> listOfArtists = artistsPager.artists.items;

            // clearing artistList for repopulation
            artistList.clear();

            if (listOfArtists.isEmpty()) {

                // set to show we were unsuccessful
                mSearchSuccess= Boolean.FALSE;

                // logging for debug purposes
                Log.v("list_false", "list of Artists returned false");

                return null;

                }

                // cycle through artist list names to add to artistStringList
                for (Artist element : listOfArtists) {

                    // add name to artistList
                    artistList.add(element.name);

                    // logging for debug purposes
                    Log.v("Name", element.name);

                    // TODO: need to grab and add image
                    // element.id;

                    // logging for debug purposes
                    Log.v("id", element.id);

                }

                // Log success
                Log.v("artist success", artistsPager.toString());

            mSearchSuccess= Boolean.TRUE;

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // mToast.show();

        }
    }
}
