package com.uottawa.keenan.cookhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;

public class EditRecipe extends AppCompatActivity {
    public CreateDB tempDB;
    public CreateDB recipeDB;
    public String recipe_raw;
    public ArrayList<Ingredient> ingredients = new ArrayList<>();
    public ArrayList<RecipeStep> recipe_steps = new ArrayList<>();
    public RecipeCategory recipe_category;
    public RecipeType recipe_type;
    public String recipe_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        // Open Recipe DB and temp file containing recipe name to edit
        try {
            recipeDB = new CreateDB(getApplicationContext(), "RecipeDB.txt");
            tempDB = new CreateDB(getApplicationContext(), "tempDB.txt");
            System.out.println("recipe name:   "+tempDB.getAsArrayList().get(0));
            recipe_raw = recipeDB.getRecipeListingFromName(tempDB.getAsArrayList().get(0));
            System.out.println(recipe_raw);
            updateIngredients();
            updateSteps();
            updateRecipeName();
            updateCategoryAndType();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // Override back button to destroy temp file
    @Override
    public void onBackPressed()
    {
        tempDB.destroyDataBase();
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }


    public void updateIngredients() {
        String[] temp =  recipe_raw.split("\\|")[4].split("`");

        for (int i=0; i<temp.length; i++){
            ingredients.add( new Ingredient(temp[i]));
        }
    }

    public void updateSteps() {
        String[] temp =  recipe_raw.split("\\|")[5].split("`");

        for (int i=0; i<temp.length; i++){
            recipe_steps.add( new RecipeStep(temp[i]));
        }
    }

    public void updateRecipeName() {
        recipe_name =  recipe_raw.split("\\|")[0];
    }

    public void updateCategoryAndType() {
        recipe_category =  new RecipeCategory( recipe_raw.split("\\|")[1]);
        recipe_type = new RecipeType( recipe_raw.split("\\|")[2]);
    }

    public void setupView() {
        
    }

}
