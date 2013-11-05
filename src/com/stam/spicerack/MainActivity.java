package com.stam.spicerack;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;
import com.stam.spicerack.RecipeBox;

// TODO AutoCompleteTextView textView;

public class MainActivity extends Activity{

	private RecipeBox mRecipes = new RecipeBox();
	private final String TAG = "MainActivity";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "Set Content View");
        setContentView(R.layout.activity_main);

        //Create the storage container for the recipes
        mRecipes.createBox(getApplicationContext());
        
	    // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) findViewById(R.id.SearchBar1);//.getRootView();
	    
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

	    Intent intent = getIntent();

	    handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        Log.d(TAG, "onCreateOptionsMenu");

    	// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    protected void onNewIntent(Intent intent) {
    	Log.d(TAG, "onNewIntent");
    	handleIntent(intent);
    }
    
    // Function handles all intents created by the main activity 
    private void handleIntent(Intent intent) {

    	if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	        Log.v(TAG, "intent = ACTION_SEARCH");

	    	// Retrieve the query strong from the intent
	        String query = intent.getStringExtra(SearchManager.QUERY);
	    	
	    	// Display the results of function doMySearch
	    	showResults(doMySearch(query));
	    }
    }
    
    /* This function calls the RecipeBox search function and converts the results into a new
     * RecipeBox.
     */
    public RecipeBox doMySearch(String query) {
		String fTAG = "-doMySearch";
    	
		Log.d(TAG+fTAG, "start doMySearch");
		
		// Create temporary variables for lists of matches in search 
		ArrayList<Recipe> searchResults = new ArrayList<Recipe>();
		
		// Check to see if the search matches any of recipes
		searchResults.addAll(mRecipes.search(query));
		
		if(searchResults.isEmpty()) {
			
			// If the search did not return any results, return null
			return null;
		} else {
			
			// Return the results
			return new RecipeBox(searchResults);
       }
    }

 // Create the intent and start the activity for displaying results
    private void showResults(final RecipeBox results) {
    	Log.d(TAG, "Start function showResults");
    	
    	// Create the intent for showing the results
		Intent resultsIntent = new Intent(getApplicationContext(), ResultsActivity.class);
		
		// Place the results RecipeBox into the intent
		resultsIntent.putExtra("search_results", results);
		
		// Start the new activity
		startActivity(resultsIntent);
    }

 // End of Class
}
