package com.stam.spicerack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Recipe implements Parcelable{
	
	//Private Variables
	private String name;
	private String description;
	private ArrayList<String> categories;
	private ArrayList<String> ingredients;
	private ArrayList<String> instructions;
	
	private static final String TAG = "Recipe";
	
	// Public Constructors
	
	public Recipe(String name, ArrayList<String> ingredients,
			ArrayList<String> instructions) {
		super();
		Log.d(TAG, "Create recipe " + name);
		this.name = name;
		this.ingredients = ingredients;
		this.instructions = instructions;
	}
	
	public Recipe(String name, String description, ArrayList<String> categories, 
					ArrayList<String> ingredients, ArrayList<String> instructions) {
		super();
		Log.d(TAG, "Create recipe " + name);
		this.name = name;
		this.description = description;
		this.categories = categories;
		this.ingredients = ingredients;
		this.instructions = instructions;
	}
	
	// Private Constructors
	
	private Recipe(Parcel in) {
		Log.v(TAG, "Read name");
		this.name = in.readString();
		Log.v(TAG, "Read description");
		this.description = in.readString();
		Log.v(TAG, "Read categories");
		this.categories = arrayToAL(in.createStringArray());
		Log.v(TAG, "Read ingredients");
		this.ingredients = arrayToAL(in.createStringArray());
		Log.v(TAG, "Read instructions");
		this.instructions = arrayToAL(in.createStringArray());

	}
	
	// Helper class for constructor Recipe (Parcel)
	private ArrayList<String> arrayToAL(String[] stringArray) {
		ArrayList<String> al = new ArrayList<String>();
		for(int i=0; i<stringArray.length; i++){
			Log.v(TAG, "Adding string to array: " + stringArray[i]);
			al.add(stringArray[i]);
		}
		return al;
	}
	
	// Public Methods
	
	public String getName(){
		
		Log.v(TAG, "getName");
		return name;
	}
	
public String getDescription(){
		
		Log.v(TAG, "getDescription");
		return description;
	}
	
	public ArrayList<String> getCategories(){
	
	Log.v(TAG, "return Ingredients");
	return categories;
}

	public ArrayList<String> getIngredients(){
		
		Log.v(TAG, "return Ingredients");
		return ingredients;
	}
	
	public ArrayList<String> getInstructions(){
		
		Log.v(TAG, "return Instructions");
		return instructions;
	}
	
	public boolean nameContains(String string){
		
		Log.v(TAG, "Search" + name + " name for " + string);
		return containsStringLC(string, name);
	}
	
	public boolean descriptionContains(String string){
		
		Log.v(TAG, "Search" + name + " description for " + string);
		return containsStringLC(string, description);
	}
	
	public boolean categoriesContain(String string){
		
		Log.v(TAG, "Search" + name + " categories for " + string);
		// Check each line of the categories for the search term "string"
		return iterateALforQuery(string, categories);
	}
	
	public boolean ingredientsContain(String string){
	
		Log.v(TAG, "Search" + name + " ingredients for " + string);
		// Check each line of the ingredients for the search term "string"
		return iterateALforQuery(string, ingredients);
	}

	public boolean instructionsContain(String string){
		
		Log.v(TAG, "Search" + name + " instructions for " + string);
		
		// Check each line of the instructions for the search term "string"
		return iterateALforQuery(string, instructions);
	}
	
	// Helper function to iterate through an ArrayList looking for a specific search term
		private boolean iterateALforQuery(String queryString, ArrayList<String> al) {
			
			// Create string iterator 
			Iterator<String> iterator = al.iterator();
			
			// Create temp string to store the current line
			String line;
			
			// Check each line of the instructions for the search term "string"
			while (iterator.hasNext()) {
				
				// Get the next line
				line = iterator.next();
				
				// Check the line against that search term
				Log.v(TAG, "Checking \"" + queryString + " \" against " + line);
				if(containsStringLC (queryString, line) ) {
					Log.v(TAG, "returning true");
					return true;
				}
				
			}
			
			Log.v(TAG, "returning false");
			return false;
		}
	
	// Helper function to compare to strings in the specific manner this search requires
	private boolean containsStringLC(String queryString, String dataString) {
		
		// All text is converted to lower case to increase matching
		if(dataString.toLowerCase(new Locale("en", "US")).contains(queryString.toLowerCase(new Locale("en", "US") ) ) ) {
			return true;
		}
		
		return false;
	}

	// Parcel implementation
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// Writes each variable to the parcel
		Log.v(TAG, "Write " + name + " to Parcel");
		arg0.writeString(name);
		Log.v(TAG, "Write " + description + " to Parcel");
		arg0.writeString(description);
		Log.v(TAG, "Write categories to Parcel");
		arg0.writeStringArray(categories.toArray(new String[categories.size()]));
		Log.v(TAG, "Write ingredients to Parcel");
		arg0.writeStringArray(ingredients.toArray(new String[categories.size()]));
		Log.v(TAG, "Write instructions to Parcel");
		arg0.writeStringArray(instructions.toArray(new String[categories.size()]));
	
	}
	
	public static final Parcelable.Creator<Recipe> CREATOR = 
			new Parcelable.Creator<Recipe>() {
        
		public Recipe createFromParcel(Parcel in) {
			Log.d(TAG, "Parcelable.Creator - createFromParcel");
			return new Recipe(in);
        }
		
		public Recipe[] newArray(int size) {
			Log.d(TAG, "Parcelable.Creator - newArray");
            return new Recipe[size];
        }
	};
	// End of Class
}
