package com.stam.spicerack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

// Activity for displaying the selected recipe
public class RecipeActivity extends Activity {
	
	// Recipe container for the selected recipe
    private Recipe mRecipe;
	private static final String TAG = "RecipeActivity";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String fTAG = "onCreate: ";
        
        Log.d(TAG, fTAG + "Display the selected recipe");
        setContentView(R.layout.activity_recipe);
        
        Log.v(TAG, fTAG + "Get the intent for the activity");
        Intent i = getIntent();
        
        Log.v(TAG, fTAG + "Unpack the recipe stored in the Intent");
        mRecipe = i.getParcelableExtra("selected_recipe");
        
        setupName();
        
        setupDescription();
        
        setupIngredients();
        
        setupInstructions();

	}
	


	private void setupName(){
		// Set the recipe name from the selected recipe to the 
        Log.v(TAG, "Set Recipe Name");
        ((TextView) findViewById(R.id.recipe_name)).setText(mRecipe.getName());
	}
	
	private void setupDescription() {
		// Set the recipe description from the selected recipe to the 
        Log.v(TAG, "Set Recipe Description");
        ((TextView) findViewById(R.id.description_text)).setText(mRecipe.getDescription());
		
	}
	
	private void setupIngredients(){
		// Write the ingredients to the ingredients field
		Log.v(TAG, "Set Recipe Ingredients");
		
		addTextView((LinearLayout) findViewById(R.id.ingredients_list), 
				mRecipe.getIngredients());
	}
	
	private void setupInstructions(){
		// Write the instructions to the instructions field
		Log.v(TAG, "Set Recipe Instructions");
		addTextView((LinearLayout) findViewById(R.id.instructions_list), 
				mRecipe.getInstructions());
	}

	private void addTextView(LinearLayout ll, ArrayList<String> strings) {

		for (int i = 0; i < strings.size(); i++){
			TextView tv = new TextView(this);
			tv.setText(strings.get(i));
			ll.addView(tv);
		}	
	}
	
}
