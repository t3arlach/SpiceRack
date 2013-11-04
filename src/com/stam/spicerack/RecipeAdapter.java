package com.stam.spicerack;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

// Class which packages a recipe for display in a list of 
// search results
public class RecipeAdapter extends ArrayAdapter<Recipe> {

	private Context context;
	private int resource;
	private Recipe[] items;
	
	private final static String TAG = "RecipeAdapter";
	
	
	public RecipeAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);

	    this.context = context;
	    this.resource = textViewResourceId;
	}

	
	public RecipeAdapter(Context context, int resource, Recipe[] items) {

	    super(context, resource, items);

	    this.context = context;
	    this.resource = resource;
	    this.items = items;

	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    Log.d(TAG, "getView - Start Function");
		View v = convertView;

	    if (v == null) {

		    Log.v(TAG, "getView - null view");
//	    	LayoutInflater vi = ((Activity) context).getLayoutInflater();
	        LayoutInflater vi = LayoutInflater.from(context);

	        v = vi.inflate(R.layout.recipe_result, null);

	    }

//	    Recipe p = MainActivity.searchResults.getItemAtPosition(position);
	    Log.v(TAG, "getView - get recipe at postion" + position);
	    Recipe p = items[position];

	    if (p != null) {
		    Log.v(TAG, "getView - Map recipe information to fields");

	        TextView recipeTitle = (TextView) v.findViewById(R.id.recipe_title);
	        TextView recipeDescription = (TextView) v.findViewById(R.id.recipe_description);

	        recipeTitle.setText(p.getName());
	        recipeDescription.setText(p.getDescription());

	    }

	    Log.v(TAG, "getView - Return view");
	    return v;

	}
	
}
