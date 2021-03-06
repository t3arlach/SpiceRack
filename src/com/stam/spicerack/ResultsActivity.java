package com.stam.spicerack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ResultsActivity extends Activity{

	private static final String TAG = "ResultsActivity";
//	private RecipeBox searchResults;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String fTAG = "onCreate: ";
        
        Log.d(TAG, fTAG + "Set Content View");
        setContentView(R.layout.activity_results);
	
        Log.d(TAG, fTAG + "Unpack the Parcel from the intent");
	
        Intent i = getIntent();
//        searchResults = i.getParcelableExtra("selected_recipe");
//        
//        showResults(searchResults);
        
        Log.d(TAG, fTAG + "Unpack the Parcel from the intent");
        showResults((RecipeBox) i.getParcelableExtra("search_results"));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    
	    Log.d(TAG, "onCreateOptionsMenu");
	
		// Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.main, menu);
	    return true;
	}

	private void showResults(final RecipeBox results) {
    	Log.d(TAG, "Displays the results of the search");
    	
    	// Check if results exist
    	if (results == null){
    		// TODO Display no results text
    		Log.v(TAG, "null response");
    		
    	} else {
    		// Bind results to list view

    		
    		Log.d(TAG, "Set the adapter for recipe results");
    		Adapter adapter = new RecipeAdapter(this.getBaseContext(), 
    				R.layout.recipe_result, 
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
                    startActivity(wordIntent);
                }
            });
    	}	
    }

// End of Class
}