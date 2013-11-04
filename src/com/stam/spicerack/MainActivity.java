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
	
	// Static variable showing the latest search results
	// TODO Replace with proper implementation
	public static RecipeBox searchResults;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "Set Content View");
        setContentView(R.layout.activity_main);

        //Create the storage container for the recipes
        Log.v(TAG, "Create RecipeBox");
        mRecipes.createBox(getApplicationContext());
        
	    // Get the SearchView and set the searchable configuration
	    Log.v(TAG, "Begin search manager configuration");
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
    
    private void handleIntent(Intent intent) {

    	if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	        Log.d(TAG, "intent = ACTION_SEARCH");

	    	String query = intent.getStringExtra(SearchManager.QUERY);
	    	
	    	// Display results
	    	Log.d(TAG, "execute doMySearch");
	    	showResults(doMySearch(query));
	    }
    }
    
    public RecipeBox doMySearch(String query) {
		String fTAG = "-doMySearch";
    	
		Log.d(TAG+fTAG, "start doMySearch");
		
		// Create temp variables for lists of matches in search 
		ArrayList<Recipe> searchResults = new ArrayList<Recipe>();
		
		// Check to see if the search matches any of recipes
		Log.v(TAG+fTAG, "Search names");
		searchResults.addAll(mRecipes.search(query));

		
		// Check to see if the search matches any categories of recipes
			// Categories are not implemented at this time
		
		// Create a recipe box out of the results
		Log.v(TAG+fTAG, "Creating recipe box of results");
		RecipeBox recipeResults = new RecipeBox(searchResults);
		
		// Return search results
		int numResults = recipeResults.numRecipes();
		if(numResults == 0){
			// If the search did not return any results, return null
			Log.d(TAG+fTAG, "no results, returning null");
			return null;
		} else {
			// return the results
			// Long term this should return the whole recipe box and the 
			// searchable activity will map the array
			
			// Old code which returns a string array
			Log.d(TAG+fTAG, "return recipe name list with " + numResults + " results");
			return recipeResults;
       }
    }

 // Create the intent and start the activity for displaying results
    private void showResults(final RecipeBox results) {
    	Log.d(TAG, "showResults");
    	
    	Log.v(TAG, "Create the intent for showing the results");
		Intent resultsIntent = new Intent(getApplicationContext(), 
				ResultsActivity.class);
		
		Log.v(TAG, "Place the results RecipeBox into the intent");
		resultsIntent.putExtra("search_results", results);
		
		Log.v(TAG, "Start the new Activity");
		startActivity(resultsIntent);
    
    }
    
}
