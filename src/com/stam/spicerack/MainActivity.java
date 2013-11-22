package com.stam.spicerack;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.stam.spicerack.RecipeBox;

// TODO AutoCompleteTextView textView;

public class MainActivity extends Activity{

	private ManagedRecipeBox mRecipes;
	private final String TAG = "MainActivity";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "Set Content View");
        setContentView(R.layout.activity_main);

        // Create the storage container for the recipes
        mRecipes = ManagedRecipeBox.createBox(getApplicationContext());
        
        // TODO: only show either favorites or recently viewed results
        populateMainActivityList(mRecipes.search("Featured"));
        
        
        Log.d(TAG, "Number of favorite recipes: " + mRecipes.favoriteList().size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        Log.d(TAG, "onCreateOptionsMenu");
        
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        
        /*
         * Setup the searchable configuration paramters to manage the search function.
         */
        Log.d(TAG, "Setup searchview");
        
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        
        return super.onCreateOptionsMenu(menu);
    }
    
    protected void onResume () {
    	super.onResume();
    	
    	// Make sure ic_star doesn't have any color filters active
	    Drawable drawable = getResources().getDrawable(R.drawable.ic_action_star);
    	drawable.setColorFilter(null);
    }
    
    protected void onNewIntent(Intent intent) {
    	Log.d(TAG, "onNewIntent");
    	handleIntent(intent);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
//            case R.id.action_search:
//               // openSearch();
//                return true;
        case R.id.action_browse_favorites:
            browseFavorites();
            return true;    
        case R.id.action_browse_all:
        	browseAll();
            return true;
        case R.id.action_spices:
            // TODO: Implement real click handling
        	actionBrowseToast(getResources().getString(R.string.action_spices));
            return true;
        case R.id.action_settings:
        	//// TODO: Implement real click handling
        	actionBrowseToast(getResources().getString(R.string.action_settings));
            return true;
        case R.id.action_help:
            //// TODO: Implement real click handling
        	actionBrowseToast(getResources().getString(R.string.action_help));
            return true;
        case R.id.action_about:
            //// TODO: Implement real click handling
        	actionBrowseToast(getResources().getString(R.string.action_about));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
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
    public void browseFavorites() {
        // Do something in response to button click
    	showResults(mRecipes.favoriteBox(), getString(R.string.browse_favs_title));
    }
    
    /*
     * This function listens for a click on the browseAll button. When it receives the click it 
     * passes the full RecipeBox to the display results function.
     */
    public void browseAll() {
        // Pass the full recipe box and the correct text to the show results function.4ewq
    	showResults(mRecipes, getString(R.string.browse_all_title));
    }
    
    // Stand in action for un-implemented action bar 
    public void actionBrowseToast(String s) {
        // Do something in response to button click
    	Toast toast = Toast.makeText(this, "User selected " + s, Toast.LENGTH_SHORT);
    	toast.show();
    }
    
    private void populateMainActivityList(final RecipeBox results) {
    	Log.d(TAG, "Displays the results of the search");
    	
    	// Check if results exist
    	if (results == null){
    		// TODO Display no results text
    		Log.d(TAG, "Trying to display null search results");
    		
    	} else {
    		// Bind results to list view

    		Log.v(TAG, "Set the adapter for recipe results");
    		Adapter adapter = new RecipeAdapter(this.getBaseContext(),R.layout.recipe_result, 
    				results.toArray());
    		
    		// Set the adapter for the list view
    		((ListView) findViewById(R.id.list)).setAdapter((ListAdapter) adapter);
    		
    		// Set the interface for click listener
    		((ListView) findViewById(R.id.list)).setOnItemClickListener(new OnItemClickListener() {

    			@Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Build the Intent used to open WordActivity with a specific word Uri
    				Intent wordIntent = new Intent(getApplicationContext(), 
    						RecipeActivity.class);
    				wordIntent.putExtra("selected_recipe", results.getItemAtPosition(position));
    				Log.v(TAG, "Start RecipeActivity");
                    startActivity(wordIntent);
                }
            });
    	}	
    }

 // End of Class
}
