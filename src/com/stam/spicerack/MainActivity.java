package com.stam.spicerack;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

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
        
        setupSearchableConfig();
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
	    	showResults(doMySearch(query), getString(R.string.search_results_title));
	    }
    }
    
    /*
     * Setup the searchable configuration paramters to manage the search function.
     */
    private void setupSearchableConfig() {
    	
    	// Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) findViewById(R.id.SearchBar1);//.getRootView();
	    
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
    }
    
    /* This function calls the RecipeBox search function and converts the results into a new
     * RecipeBox.
     */
    public RecipeBox doMySearch(String query) {
   
    	return mRecipes.search(query);
    }

 // Create the intent and start the activity for displaying results
    private void showResults(final RecipeBox results, String displayName) {
    	Log.d(TAG, "Start function showResults");
    	
    	// Create the intent for showing the results
		Intent resultsIntent = new Intent(getApplicationContext(), ResultsActivity.class);
		
		// Place the results RecipeBox into the intent
		resultsIntent.putExtra("search_results", results);
		
		// Pass the name to be displayed at the top of the results page
		resultsIntent.putExtra("display_name", displayName);
		
		// Start the new activity
		startActivity(resultsIntent);
    }
    
    /*
     * This function will ultimately display a search results of the users favorite recipes.
     */
    		
    public void browseFavorites(View view) {
        // Do something in response to button click
    	Toast toast = Toast.makeText(this, "Browse Favorites", Toast.LENGTH_SHORT);
    	toast.show();
    	
    }
    
    /*
     * This function listens for a click on the browseAll button. When it receives the click it 
     * passes the full RecipeBox to the display results function.
     */
    public void browseAll(View view) {
        // Pass the full recipe box and the correct text to the show results function.4ewq
    	showResults(mRecipes, getString(R.string.browse_all_title));
    }
    
    public void browseCategory(View view) {
        // Do something in response to button click
    	Toast toast = Toast.makeText(this, "Browse Category", Toast.LENGTH_SHORT);
    	toast.show();
    }
    
    public void browseIngredient(View view) {
        // Do something in response to button click
    	Toast toast = Toast.makeText(this, "Browse Ingredient", Toast.LENGTH_SHORT);
    	toast.show();
    }

 // End of Class
}
