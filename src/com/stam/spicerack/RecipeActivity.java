package com.stam.spicerack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

// Activity for displaying the selected recipe
public class RecipeActivity extends Activity {
	
	// Recipe container for the selected recipe
    private Recipe mRecipe;
	private static final String TAG = "RecipeActivity";
	private ManagedRecipeBox mRecipes;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String fTAG = "onCreate: ";
        
        mRecipes = ManagedRecipeBox.createBox(getApplicationContext());
        
        Log.d(TAG, fTAG + "Display the selected recipe");
        setContentView(R.layout.test_layout);
        
        Log.v(TAG, fTAG + "Get the intent for the activity");
        Intent i = getIntent();
        
        Log.v(TAG, fTAG + "Unpack the recipe stored in the Intent");
        mRecipe = i.getParcelableExtra("selected_recipe");
        
        setupName();
        
        setupDescription();
        
        setupIngredients();
        
        setupInstructions();
        
        // Enable up navigation in the action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        final TextView tv = (TextView) findViewById(R.id.recipe_name);
        
        findViewById(R.id.recipe_name).setOnTouchListener(new RightDrawableOnTouchListener(tv) {
        	/*
        	 * The user touched the star icon in the recipe name bar. This event toggles the 
        	 * favorite status of the recipe and redraws the action_star icon with the correct 
        	 * color filter
        	 */
        	@Override
            public boolean onDrawableTouch(final MotionEvent event) {
            	// Toggle the favorite status
            	Log.v(TAG, "Toggle favorite status");
                mRecipes.toggleFavorite((String)((TextView)findViewById(R.id.recipe_name)).getText(), getApplicationContext());
                
                // Determine the correct color for the star and apply the filter. The view will need to be invalidated
                if(mRecipes.isFavorite(mRecipe.getName())) {  
                	Log.v(TAG, "Recipe toggled to favorite, set action_star to yellow");
			        drawable.setColorFilter(0xffffd700, PorterDuff.Mode.MULTIPLY);
			        tv.invalidateDrawable(drawable);
		        } else {
		        	Log.v(TAG, "Recipe toggled to not favorite, set action_star to white");
		        	drawable.setColorFilter(null);
		        	tv.invalidateDrawable(drawable);
		        }
                return true;
            }
        });
	}
	
	/*
	 * OnTouchListener for detecting if the right drawable has been clicked.
	 */
	public abstract class RightDrawableOnTouchListener implements OnTouchListener {
	    Drawable drawable;
	    private int fuzz = 10;

	    /**
	     * @param keyword
	     */
	    public RightDrawableOnTouchListener(TextView view) {
	        super();
	        
	        /*
	         * getCompoundDrawables returns an array with drawable items for each side in the 
	         * following order:
	         * [0] = drawableLeft
	         * [1] = drawableTop
	         * [2] = drawableRight
	         * [3] = drawableBottom
	         */
	        final Drawable[] drawables = view.getCompoundDrawables();
	        if (drawables != null && drawables.length == 4)
	            this.drawable = drawables[2];
	    }

	    /*
	     * (non-Javadoc)
	     * 
	     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	     */
	    @Override
	    public boolean onTouch(final View v, final MotionEvent event) {
	    	Log.v(TAG, "touch registered");
	        if (event.getAction() == MotionEvent.ACTION_DOWN && drawable != null) {
	            final int x = (int) event.getX();
	            final int y = (int) event.getY();
	            final Rect bounds = drawable.getBounds();
	            if (x >= (v.getRight() - bounds.width() - fuzz) && x <= (v.getRight() - v.getPaddingRight() + fuzz)
	                    && y >= (v.getPaddingTop() - fuzz) && y <= (v.getHeight() - v.getPaddingBottom()) + fuzz) {
	            	Log.v(TAG, "touch on CompoundDrawable. Call onDrawableTouch");
	            	return onDrawableTouch(event);
	            }
	        }
	        return false;
	    }

	    public abstract boolean onDrawableTouch(final MotionEvent event);
	}

	private void setupName(){
		// Set the recipe name from the selected recipe to the 
        Log.v(TAG, "Set Recipe Name");
        TextView tv = (TextView) findViewById(R.id.recipe_name);
        tv.setText(mRecipe.getName());
       
        // correctly draw the star in the name bar
        final Drawable[] drawables = tv.getCompoundDrawables();
        Drawable drawable;
        if (drawables != null && drawables.length == 4) {
            drawable = drawables[2];
            if(mRecipes.isFavorite(mRecipe.getName())) {  
		        drawable.setColorFilter(0xffffd700, PorterDuff.Mode.MULTIPLY);
//			        tv.invalidateDrawable(drawable);
	        } else {
	        	drawable.setColorFilter(null);
//		        	tv.invalidateDrawable(drawable);
	        }
        } 
	}
	
	private void setupDescription() {
		// Set the recipe description from the selected recipe to the 
        Log.v(TAG, "Set Recipe Description");
        ((TextView) findViewById(R.id.description_text)).setText(mRecipe.getDescription());
		
	}
	
	private void setupIngredients(){
		// Write the ingredients to the ingredients field
		Log.v(TAG, "Set Recipe Ingredients");
		findViewById(R.id.ingredients_placeholder).setVisibility(View.GONE);
		addTextView((LinearLayout) findViewById(R.id.ingredients_list), mRecipe.getIngredients());
	}
	
	private void setupInstructions(){
		// Write the instructions to the instructions field
		Log.v(TAG, "Set Recipe Instructions");
		findViewById(R.id.instructions_placeholder).setVisibility(View.GONE);
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
	// End of Class
}
