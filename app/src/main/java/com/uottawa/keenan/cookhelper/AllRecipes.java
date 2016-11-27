package com.uottawa.keenan.cookhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class AllRecipes extends AppCompatActivity {

    public CreateDB recipeDB;
    public CreateDB tempDB;
    public ArrayList<Recipe> all_recipes = new ArrayList<Recipe>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recipes);

        // Open Database for Recipes
        try {
            recipeDB = new CreateDB(getApplicationContext(), "RecipeDB.txt");
            // recipeDB.readContents();
        } catch (IOException e) {
            e.printStackTrace();
        }



        ListView listView = (ListView) findViewById(R.id.all_recipes_listView);
        updateRecipes();

        ArrayList<String> recipe_names = new ArrayList<String>();

        for (Recipe r : all_recipes) {
            recipe_names.add(r.getRecipeName());
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recipe_names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                // Create Database "temp". temp will only include one line, the recipe name.
                // i.e. the selected listview item string
                // It will be read by EditRecipe. EditRecipe will then locate recipe in database
                // and populate EditRecipe view
                try {
                    tempDB = new CreateDB(getApplicationContext(), "tempDB.txt");
                    tempDB.destroyDataBase();
                    tempDB = new CreateDB(getApplicationContext(), "tempDB.txt");
                    tempDB.addToDB(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), EditRecipe.class);
                startActivityForResult (intent,0);
            }
        });
    }


    public void updateRecipes() {
        ArrayList<String> databaseRecipes = recipeDB.getAsArrayList();

        // Recipe Name, Category, Type, Steps, Ingredients
        for (String rec : databaseRecipes) {
            String recipe_name = rec.split("\\|")[0];
            RecipeCategory recipe_category = new RecipeCategory(rec.split("\\|")[1]);
            RecipeType recipe_type = new RecipeType(rec.split("\\|")[2]);

            try {
                ArrayList<RecipeStep> recipe_steps = getStepsList(rec);
                ArrayList<Ingredient> recipe_ingredients = getIngredientsList(rec);
                all_recipes.add(new Recipe(recipe_ingredients, recipe_steps,recipe_category,recipe_type, recipe_name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the steps of a recipe
     * @param recipe
     * @return
     * @throws IOException
     */
    public ArrayList<RecipeStep> getStepsList(String recipe) throws IOException{

        String[] temp =  recipe.split("\\|")[3].split("`");
        ArrayList<RecipeStep> steps = new ArrayList<RecipeStep>();

        for (int i=0; i<temp.length; i++){
            steps.add(new RecipeStep(temp[i]));
        }
        return steps;
    }

    /**
     * Gets all the ingredients of a recipe
     * @param recipe
     * @return
     * @throws IOException
     */
    public ArrayList<Ingredient> getIngredientsList(String recipe) throws IOException{

        String[] temp =  recipe.split("\\|")[4].split("`");
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        for (int i=0; i<temp.length; i++){
            ingredients.add( new Ingredient(temp[i]));
        }

        return ingredients;
    }


}
