package com.stam.spicerack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.stam.spicerack.Recipe;


public class RecipeBox implements Parcelable {
	
	//Public Variables
	

	//Private Variables
	private static final String TAG = "RecipeBox";
	private ArrayList<Recipe> RECIPES = new ArrayList<Recipe>();
	
	RecipeBox() {
		final String fTAG = "Constructor: ";
		Log.d(TAG, fTAG + "Initialize empty recipe box");
	}
	
	RecipeBox(ArrayList<Recipe> recipeList) {
		final String fTAG = "Constructor: ";
		Log.d(TAG, fTAG + "Initialize recipe box from ArrayList<Recipe>");
		RECIPES = recipeList;
		
		//Remove duplicates
		// TODO implement duplicate removal
	}
	
	public RecipeBox(Parcel in) {
		final String fTAG = "Constructor: ";
		
		Log.d(TAG, fTAG + "Initialize recipe box from Parcel");
		
		Log.v(TAG, fTAG + "Read in the number of recipes to be put into "
				+ "the RecipeBox");
		int arraySize = in.readInt();
		
		Log.v(TAG, fTAG + "Read in " + arraySize + " recipes into the ArrayList");
		for (int i = 0; i < arraySize; i++) {

			Log.v(TAG, fTAG + "Read in the " + i + "th recipe");
			Recipe r = Recipe.CREATOR.createFromParcel(in);

			Log.v(TAG, fTAG + "Successfully read in recipe " + r.getName());
			RECIPES.add(r); 
					
		}
		Log.v(TAG, fTAG + "RecipeBox initialized");
		
	}

	public void createBox(Context context){
		// createBox reads in a txt file from a pre-defined location and
		// creates a series of recipes from the text file. 
		
		// The format for the file will be:
		// TITLE || DESCRIPTION || CATEGORIES || INGREDIENTS || INSTRUCTIONS
		// CATEGORIES, INGREDIENTS, and INSTRUCTIONS will separate the
		// items with the " | "
		
		Log.d(TAG, "Begin creating recipe box");
		
		//Open the file containing the recipes
        final Resources resources = context.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.recipes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
        Log.v(TAG, "Buffered Reader Ready");
        
        // VAriable to hold the lines as they are read
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
				
				RECIPES.add(new Recipe(recipeName, recipeDescription, recipeCategories, recipeIngredients, recipeInstructions));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Log.d(TAG, "Recipe box complete");
	}
	
	// Returns and array list of recipes that contain the specified term any field
	// Results are prioritized based on where the search term was. The order of 
	// importance is: NAME, CATEGORY, INGREDIENT, DESCRIPTION, INSTRUCTION. 
	// 
			public ArrayList<Recipe> search(String searchTerm){
				
				// The final container to be returned
				ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();
				
				// Temporary containers to hold the search results until they can 
				// be combined in mRecipes 
				ArrayList<Recipe> mRecipesNAME = new ArrayList<Recipe>();
				ArrayList<Recipe> mRecipesCATEGORY = new ArrayList<Recipe>();
				ArrayList<Recipe> mRecipesINGREDIENT = new ArrayList<Recipe>();
				ArrayList<Recipe> mRecipesDESCRIPTION = new ArrayList<Recipe>();
				ArrayList<Recipe> mRecipesINSTRUCTION = new ArrayList<Recipe>();
				Iterator<Recipe> iterator = RECIPES.iterator(); 
				
				// Search all recipes in Recipe Box for name matches
				while (iterator.hasNext()){

					// Load the next recipe
					Recipe recipe = iterator.next();
					
					// Check for recipe NAME match
					if (recipe.nameContains(searchTerm)){
						
						// Positive search result, add to list
						mRecipesNAME.add(recipe);
					}
					// Check for recipe CATEGORY match
					else if (recipe.categoriesContain(searchTerm)) {
					
						// Positive search result, add to list
						mRecipesCATEGORY.add(recipe);
						
					}
					// Check for recipe INGREDIENT match
					else if (recipe.ingredientsContain(searchTerm)) {
						
						// Positive search result, add to list
						mRecipesINGREDIENT.add(recipe);
						
					}
					// Check for recipe DESCRIPTION match
					else if (recipe.descriptionContains(searchTerm)) {
						
						// Positive search result, add to list
						mRecipesDESCRIPTION.add(recipe);
						
					}
					// Check for recipe INSTRUCTION match
					else if (recipe.instructionsContain(searchTerm)) {
						
						// Positive search result, add to list
						mRecipesINSTRUCTION.add(recipe);
						
					}
					
				}
				
				// Create an ordered list of recipes from most important to least important
				mRecipes.addAll(mRecipesNAME);
				mRecipes.addAll(mRecipesCATEGORY);
				mRecipes.addAll(mRecipesINGREDIENT);
				mRecipes.addAll(mRecipesDESCRIPTION);
				mRecipes.addAll(mRecipesINSTRUCTION);
				
				// Return ordered list of recipes
				Log.d(TAG, "Finish function searchNames");
				return mRecipes;
				
			}

	// Returns an ArrayList of Strings of every recipe name in the Recipe Box
	public ArrayList<String> recipeNameList(){
		
		Log.d(TAG, "Create list of recipe names");
		
		//Temp variable to hold the recipe names
		ArrayList<String> recipeNames = new ArrayList<String>();
		
		//Iterator for moving through the recipe list
		Iterator<Recipe> iterator = RECIPES.iterator();
		
		// Gather the names of all of the recipes
		while(iterator.hasNext()){
			recipeNames.add(((Recipe) iterator.next()).getName());
		}
		
		// If the recipe box is empty return null
		if (recipeNames.size() == 0)
		{
			return null;
		}
		
		// Return the ArrayList of recipe names
		return recipeNames;
	}
	
	public int numRecipes(){
		Log.v(TAG, "return length of RECIPES array");
		return RECIPES.size();
	}
	
	public Recipe getItemAtPosition(int position) {
		
		return RECIPES.get(position);
	}
	
	public Recipe[] toArray() {
		
		return RECIPES.toArray(new Recipe[RECIPES.size()]);
		
	}
	
	// Private method to split the strings based on the splitter character and 
	// return an array of the sub strings. Used as part of the creation of the
	// Recipe Box
	private ArrayList<String> stringToArrayList(String group, String splitter) {
		
		// Temp variables for splitting the string
		String[] splitString = TextUtils.split(group, splitter);
		ArrayList<String> al = new ArrayList<String>();
		
		
		for(int i=0; i<splitString.length; i++){
			Log.v(TAG, "Read group " + splitString[i]);
			al.add(splitString[i]);
		}
		
		return al;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(RECIPES.size());
		
		Iterator<Recipe> iterator = RECIPES.iterator(); 
		
		// Search all recipes in Recipe Box for name matches
		while (iterator.hasNext()){

			// Load the next recipe
			Recipe recipe = iterator.next();
			recipe.writeToParcel(arg0, 0);
		}
		
	}
	
	public static final Parcelable.Creator<RecipeBox> CREATOR = 
			new Parcelable.Creator<RecipeBox>() {
        
		public RecipeBox createFromParcel(Parcel in) {

			return new RecipeBox(in);
        }
		
		public RecipeBox[] newArray(int size) {
            return new RecipeBox[size];
        }
	};
}