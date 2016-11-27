package com.uottawa.keenan.cookhelper;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;

public class EditRecipe extends AppCompatActivity {
    public CreateDB tempDB;
    public CreateDB recipeDB;
    public CreateDB ingredientDB;
    public CreateDB categoryDB;
    public CreateDB typeDB;

    public String recipe_raw;
    public ArrayList<Ingredient> ingredients = new ArrayList<>();
    public ArrayList<RecipeStep> recipe_steps = new ArrayList<>();
    public RecipeCategory recipe_category;
    public RecipeType recipe_type;
    public String recipe_name;

    public ArrayList<Ingredient> all_ingredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        // Open Recipe DB and temp file containing recipe name to edit
        try {
            recipeDB = new CreateDB(getApplicationContext(), "RecipeDB.txt");
            tempDB = new CreateDB(getApplicationContext(), "tempDB.txt");
            ingredientDB = new CreateDB(getApplicationContext(), "IngredientsDB.txt");
            categoryDB = new CreateDB(getApplicationContext(), "CategoriesDB.txt");
            typeDB = new CreateDB(getApplicationContext(), "TypesDB.txt");
            System.out.println("recipe name:   "+tempDB.getAsArrayList().get(0));
            recipe_raw = recipeDB.getRecipeListingFromName(tempDB.getAsArrayList().get(0));
            System.out.println(recipe_raw);
            updateIngredients();
            updateSteps();
            updateRecipeName();
            updateCategoryAndType();
            updateIngredientsArrayList();
            getDatabaseItems();
            setupView();
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

    public void getDatabaseItems() {
        ArrayList<String> saved_ings = ingredientDB.getAsArrayList();
        for (String ing : saved_ings) {
            Ingredient past = new Ingredient(ing);
            try {
                updateIngredients(past);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateIngredients(Ingredient ingredient) throws IOException {
        LinearLayout ingredients_layout = (LinearLayout) findViewById(R.id.ingredients_layout);
        CheckBox cb = new CheckBox(this);
        cb.setText(ingredient.getIngredient());
        if (isMatch(ingredient)){
            cb.setChecked(true);
        }
        cb.setTextColor(Color.BLACK);
        ingredients_layout.addView(cb);
    }

    public boolean isMatch(Ingredient new_ingredient) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.equals(new_ingredient)) {
                return true;
            }
        }
        return false;
    }

    public void updateIngredientsArrayList(){
        all_ingredients.clear();
        ArrayList<String> dbSource = ingredientDB.getAsArrayList();
        for(int i=0; i<dbSource.size(); i++){
            // System.out.println(dbSource.get(i));
            all_ingredients.add(new Ingredient(dbSource.get(i)));
        }

    }
    public void setupView() {
        // Set Recipe Name TextBox
        EditText recipe_name_edittext = (EditText) findViewById(R.id.recipe_name_edittext);
        recipe_name_edittext.setText(recipe_name);

//        LinearLayout ingredients_layout = (LinearLayout) findViewById(R.id.ingredients_layout);
//        for (Ingredient i : ingredients) {
//            CheckBox cb = new CheckBox(this);
//            cb.setText(ingredient.getIngredient());
//            cb.setTextColor(Color.BLACK);
//            ingredients_layout.addView(cb);
//        }

    }

}
