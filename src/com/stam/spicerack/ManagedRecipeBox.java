package com.stam.spicerack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

/*
 * Singleton class which opens the main recipe repository. The main 
 */
public class ManagedRecipeBox extends RecipeBox {
	
	// Singleton pattern managedRecipeBox
	private static ManagedRecipeBox mMRB;
	
	// List of favorite recipes
	private static ArrayList<String> mFavorites;
	
	// Strings for handling preferences
	private static final String TAG = "ManagedRecipeBox";
	private static final String MRB_PREFS = "RecipeBoxSettings";
	private static final String RBS_FAV = "Favorites";
	
	private ManagedRecipeBox() {
		super();
		Log.v(TAG, "Initialize empty managed recipe box");
	}
	
	public static ManagedRecipeBox createBox(Context context){
		// createBox reads in a txt file from a pre-defined location and
		// creates a series of recipes from the text file. 
		
		// The format for the file will be:
		// TITLE || DESCRIPTION || CATEGORIES || INGREDIENTS || INSTRUCTIONS
		// CATEGORIES, INGREDIENTS, and INSTRUCTIONS will separate the
		// items with the " | "
		
		Log.d(TAG, "Begin creating recipe box");
		
		// Null check context
		if(mMRB != null) {
			Log.d(TAG, "ManagedRecipebox has already been created");
			return mMRB;
		}
		
		// Initialize the variables
		mMRB = new ManagedRecipeBox();
		mFavorites = new ArrayList<String>();
		
		//Open the file containing the recipes
	    final Resources resources = context.getResources();
	    InputStream inputStream = resources.openRawResource(R.raw.recipes);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
	    Log.v(TAG, "Buffered Reader Ready");
	    
	    // Variable to hold the lines as they are read
		String line;
	    try {
	    	//Read in one line from the recipe file 
			while ((line = reader.readLine()) != null) {
				Log.v(TAG, "Read line from buffer: " + line);
				
				//Split the based on the pipe delimiter "|"
				String[] strings = TextUtils.split(line, "\\|\\|");
				
				//Position zero will always be the Recipe Name
				Log.v(TAG, "Set recipe name: " + strings[0]);
				String recipeName = strings[0];
				
				//Position zero will always be the Recipe Name
				Log.v(TAG, "Set recipe description: " + strings[1]);
			    String recipeDescription = strings[1];
				
			    String splitter = "\\|";
			    
			    // The array lists for the recipe
			    ArrayList<String> recipeCategories = stringToArrayList(strings[2], splitter);
				ArrayList<String> recipeIngredients = stringToArrayList(strings[3], splitter);
			    ArrayList<String> recipeInstructions = stringToArrayList(strings[4], splitter);
				
				mMRB.add(new Recipe(recipeName, recipeDescription, recipeCategories, recipeIngredients, recipeInstructions));
				
				readFavoritesFromPreferences(context);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Log.v(TAG, "Recipe box complete");
	    return mMRB;
	}

	public ArrayList<String> favoriteList(){
		return mFavorites;
	}
	
	public RecipeBox favoriteBox() {
		// Create RecipeBox to hold recipes in favorites list
		RecipeBox mFavoriteRecipeBox = new RecipeBox(); 
		
		// Fetch the recipes and add the to the recipe box
		RecipeBox mTemp;
		for (String s : mFavorites) {
			mTemp = mMRB.search(s);
			if (mTemp.numRecipes() == 1) {
				mFavoriteRecipeBox.add(mTemp.getItemAtPosition(0));
			} else {
				Log.e(TAG, "Favorite search term returned more than one recipe");
			}
		}
		return mFavoriteRecipeBox;
	}
	
	public boolean isFavorite(String s) {
		return mFavorites.contains(s);
	}
	
	public void toggleFavorite(String s, Context c) {
		if(!mFavorites.remove(s)) {
			mFavorites.add(s);
			Log.v(TAG, s + " added to favorites");
			Log.v(TAG, mFavorites.size() + " recipes in favorites list");
		} else {
			Log.v(TAG, s + " removed from favorites");
			Log.v(TAG, mFavorites.size() + " recipes in favorites list");
		}
		writeFavoritesToPreferences(c);
	}
	
	/*
	 * Write the favorites to a persistant key-value pair in a preference file. The preference file
	 * is "RecipeBoxSettings" and is stored in MRB_PREFS. The Key is "Favorites". The Value is a 
	 * HashSet<String> of the favorites 
	 */
	private static void writeFavoritesToPreferences(Context c) {
		// Open preferences and the preferences editor
		SharedPreferences settings = c.getSharedPreferences(MRB_PREFS, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    
	    // Convert ArrayList to HashSet
	    HashSet<String> hs = new HashSet<String>();
	    hs.addAll(mFavorites);
	    
	    // Put the HashSet into the editor and apply the results;
	    editor.putStringSet(RBS_FAV, hs);
	    if(editor.commit()){
	    	Log.v(TAG, "Favorites updated in SharedPreferences");
	    }
	}
	
	private static void readFavoritesFromPreferences(Context c) {
		// Open preferences and the preferences editor
		SharedPreferences settings = c.getSharedPreferences(MRB_PREFS, 0);
	    
	    // Put the HashSet into the editor and apply the results;
		HashSet<String> hs = new HashSet<String>();
	    hs = (HashSet<String>) settings.getStringSet(RBS_FAV, new HashSet<String>());
	    if(hs != null) {
	    	mFavorites.addAll(hs);
	    	Log.v(TAG, "Read Favorites from SharedPreferences");
	    } else {
	    	Log.v(TAG, "No Favorites key in SharedPreferences");
	    }
	}
	
	
	// Private method to split the strings based on the splitter character and 
	// return an array of the sub strings. Used as part of the creation of the
	// Recipe Box
	private static ArrayList<String> stringToArrayList(String group, String splitter) {
		
		// Temp variables for splitting the string
		String[] splitString = TextUtils.split(group, splitter);
		ArrayList<String> al = new ArrayList<String>();
		
		// Takes each group in splitString and adds it to the ArrayList
		for(int i=0; i<splitString.length; i++){
			Log.v(TAG, "Read group " + splitString[i]);
			al.add(splitString[i]);
		}
		
		return al;
	}
	
	// End of class
}
