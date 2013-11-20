package com.stam.spicerack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Recipe implements Parcelable{
	
	//Private Variables
	private String mName;
	private String mDescription;
	private ArrayList<String> mCategories;
	private ArrayList<String> mIngredients;
	private ArrayList<String> mInstructions;
	
	private static final String TAG = "Recipe";
	
	// Public Constructors
	
	public Recipe(String name, ArrayList<String> ingredients,
			ArrayList<String> instructions) {
		super();
		Log.d(TAG, "Create recipe " + name);
		this.mName = name;
		this.mIngredients = ingredients;
		this.mInstructions = instructions;
	}
	
	public Recipe(String name, String description, ArrayList<String> categories, 
					ArrayList<String> ingredients, ArrayList<String> instructions) {
		super();
		Log.d(TAG, "Create recipe " + name);
		this.mName = name;
		this.mDescription = description;
		this.mCategories = categories;
		this.mIngredients = ingredients;
		this.mInstructions = instructions;
	}
	
	// Private Constructors
	
	private Recipe(Parcel in) {
		Log.v(TAG, "Read name");
		this.mName = in.readString();
		Log.v(TAG, "Read description");
		this.mDescription = in.readString();
		Log.v(TAG, "Read categories");
		this.mCategories = arrayToAL(in.createStringArray());
		Log.v(TAG, "Read ingredients");
		this.mIngredients = arrayToAL(in.createStringArray());
		Log.v(TAG, "Read instructions");
		this.mInstructions = arrayToAL(in.createStringArray());

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
		return mName;
	}
	
public String getDescription(){
		
		Log.v(TAG, "getDescription");
		return mDescription;
	}
	
	public ArrayList<String> getCategories(){
	
	Log.v(TAG, "return Ingredients");
	return mCategories;
}

	public ArrayList<String> getIngredients(){
		
		Log.v(TAG, "return Ingredients");
		return mIngredients;
	}
	
	public ArrayList<String> getInstructions(){
		
		Log.v(TAG, "return Instructions");
		return mInstructions;
	}
	
	public boolean nameContains(String string){
		
		Log.v(TAG, "Search" + mName + " name for " + string);
		return containsStringLC(string, mName);
	}
	
	public boolean descriptionContains(String string){
		
		Log.v(TAG, "Search" + mName + " description for " + string);
		return containsStringLC(string, mDescription);
	}
	
	public boolean categoriesContain(String string){
		
		Log.v(TAG, "Search" + mName + " categories for " + string);
		// Check each line of the categories for the search term "string"
		return iterateALforQuery(string, mCategories);
	}
	
	public boolean ingredientsContain(String string){
	
		Log.v(TAG, "Search" + mName + " ingredients for " + string);
		// Check each line of the ingredients for the search term "string"
		return iterateALforQuery(string, mIngredients);
	}

	public boolean instructionsContain(String string){
		
		Log.v(TAG, "Search" + mName + " instructions for " + string);
		
		// Check each line of the instructions for the search term "string"
		return iterateALforQuery(string, mInstructions);
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
		Log.v(TAG, "Write " + mName + " to Parcel");
		arg0.writeString(mName);
		Log.v(TAG, "Write " + mDescription + " to Parcel");
		arg0.writeString(mDescription);
		Log.v(TAG, "Write categories to Parcel");
		arg0.writeStringArray(mCategories.toArray(new String[mCategories.size()]));
		Log.v(TAG, "Write ingredients to Parcel");
		arg0.writeStringArray(mIngredients.toArray(new String[mCategories.size()]));
		Log.v(TAG, "Write instructions to Parcel");
		arg0.writeStringArray(mInstructions.toArray(new String[mCategories.size()]));
	
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
