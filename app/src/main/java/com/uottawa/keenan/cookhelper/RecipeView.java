package com.uottawa.keenan.cookhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class RecipeView extends AppCompatActivity {

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
        setContentView(R.layout.activity_recipe_view);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        setup();

    }

    public void setup() {
        try {
            recipeDB = new CreateDB(getApplicationContext(), "RecipeDB.txt");
            tempDB = new CreateDB(getApplicationContext(), "tempDB.txt");
            recipe_raw = recipeDB.getRecipeListingFromName(tempDB.getAsArrayList().get(0));
            updateIngredients();
            updateSteps();
            updateRecipeName();
            updateCategoryAndType();
            setupView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
        Populates view with: Recipe Name, Recipe Steps, Ingredients, Category & Type
     */
    public void setupView() {
        TextView recipe_name_textview = (TextView) findViewById(R.id.recipe_name);
        recipe_name_textview.setText(recipe_name);

        TextView recipe_ing_textview = (TextView) findViewById(R.id.recipe_ingredients);
        recipe_ing_textview.setText(null);
        for (Ingredient i : ingredients) {
            recipe_ing_textview.setText( recipe_ing_textview.getText() + "\n" + "- " + i.getIngredient());
        }

        TextView recipe_steps_textview = (TextView) findViewById(R.id.recipe_steps);
        recipe_steps_textview.setText(null);
        for (int i = 0; i < recipe_steps.size(); i++) {
            int sum = i+1;
            recipe_steps_textview.setText(recipe_steps_textview.getText() + "\n" + Integer.toString(sum) +". "+ recipe_steps.get(i).getStep());
        }

        TextView recipe_type_textview = (TextView) findViewById(R.id.recipe_type);
        recipe_type_textview.setText(recipe_type.getRecipeType());

        TextView recipe_category_textview = (TextView) findViewById(R.id.recipe_category);
        recipe_category_textview.setText(recipe_category.getRecipeCategory());
    }

    public void OnEditRecipe(View view) {
        Intent intent = new Intent(getApplicationContext(), EditRecipe.class);
        startActivityForResult (intent,0);
    }
    public void updateIngredients() {
        ingredients.clear();
        String[] temp =  recipe_raw.split("\\|")[4].split("`");

        for (int i=0; i<temp.length; i++){
            ingredients.add( new Ingredient(temp[i]));
        }
    }

    public void updateSteps() {
        recipe_steps.clear();
        String[] temp =  recipe_raw.split("\\|")[3].split("`");

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

    // Override back button to destroy temp file
    @Override
    public void onBackPressed()
    {
        tempDB.destroyDataBase();
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

}
