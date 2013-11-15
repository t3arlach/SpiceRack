package com.stam.spicerack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;


public class RecipeDatabase {
	private static final String TAG = "DictionaryDatabase";

//    //The columns we'll include in the dictionary table
//    public static final String KEY_WORD = "aa";//SearchManager.SUGGEST_COLUMN_TEXT_1;
//    public static final String KEY_DEFINITION = "bb";//SearchManager.SUGGEST_COLUMN_TEXT_2;

    private static final String DATABASE_NAME = "recipe";
    private static final String FTS_VIRTUAL_TABLE = "FTSdictionary";
    private static final int DATABASE_VERSION = 0;

    @SuppressWarnings("unused")
	private final DatabaseHelper mDatabaseOpenHelper;
//    private static final HashMap<String,String> mColumnMap = buildColumnMap();

    public RecipeDatabase(Context context) {
        mDatabaseOpenHelper = new DatabaseHelper(context);
    }
    
    
 // TODO: This class should be private and static? Figure out what is wrong 
    class DatabaseHelper extends SQLiteOpenHelper {

    	private final Context mHelperContext;
        private SQLiteDatabase mDatabase;
        
        DatabaseHelper(Context context) {

            // calls the super constructor, requesting the default cursor factory.
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        /*
         *
         * Creates the underlying database with table name and column names taken from the
         * NotePad class.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
          db.execSQL("CREATE TABLE " +"Main" + " ("
        		  + "Id INTEGER PRIMARY KEY,"
        		  + "RecipeName VARCHAR,"
        		  + "IsFavorite INTEGER"
        		  + ");");
          db.execSQL("CREATE TABLE " +"Attributes" + " ("
        		  + "Id INTEGER FOREIGN KEY,"
        		  + "AttributeType VARCHAR,"
        		  + "AttributeValue VARCHAR"
        		  + ");");
          loadRecipes();
        }

        /**
         *
         * Demonstrates that the provider must consider what happens when the
         * underlying datastore is changed. In this sample, the database is upgraded the database
         * by destroying the existing data.
         * A real application should upgrade the database in place.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // Logs that the database is being upgraded
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            // Kills the table and existing data
            db.execSQL("DROP TABLE IF EXISTS notes");

            // Recreates the database with a new version
            onCreate(db);
        }
        
        /**
         * Starts a thread to load the database table with words
         */
        private void loadRecipes() {
            new Thread(new Runnable() {
                public void run() {
                	// TODO: Add back in exception try/catch brackets
                    createBox(mHelperContext);
                }
            }).start();
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
    				
    			    // Helper method for adding the recipe to the database
    				addRecipe(recipeName, recipeDescription, recipeCategories, recipeIngredients, recipeInstructions);
    				
    			}
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            Log.v(TAG, "Recipe box complete");
    	}
        
        /*
         * Adds information to the database
         */
        public void addRecipe(String name, String description, ArrayList<String> categories, 
				ArrayList<String> ingredients, ArrayList<String> instructions) {
        
        }
        
        /*
    	 * Private method to split the strings based on the splitter character and return an array 
    	 * of the sub strings. Used as part of the creation of the Recipe Box
    	 */
        private ArrayList<String> stringToArrayList(String group, String splitter) {
    		
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

    }
    
// End of Class
}

