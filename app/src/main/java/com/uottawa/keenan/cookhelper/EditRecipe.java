package com.uottawa.keenan.cookhelper;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

    private ArrayList<String> category_entries = new ArrayList<>();
    private ArrayList<String> type_entries = new ArrayList<>();

    boolean oldIngredientsLoaded = false;
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
            updateCategoryEntries();
            updateCategorySpinner();
            updateTypeEntries();
            updateTypeSpinner();
            setupView();
            oldIngredientsLoaded = true;

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
        if (isMatch(ingredient) && !oldIngredientsLoaded){
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

    public boolean isDuplicateIngredient(Ingredient new_ingredient) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.equals(new_ingredient)) {
                return true;
            }
        }
        return false;
    }

    public void OnAddIngredient(View view) throws IOException {
        EditText ingredient_text = (EditText) findViewById(R.id.add_ingredient_editText);


        if (ingredient_text.getText().toString().trim().isEmpty()) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Name your Ingredient first!", duration);

            toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
            toast.show();
        }  else {
            Ingredient new_ingredient = new Ingredient(ingredient_text.getText().toString());

            if (isDuplicateIngredient(new_ingredient)) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this, new_ingredient.getIngredient() + " already exists!", duration);

                toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
                toast.show();
            } else {
                ingredients.add(new_ingredient);
                updateIngredients(new_ingredient);
                ingredientDB.addToDB(new_ingredient.getIngredient());
                ingredient_text.setText(null);
            }
        }


    }
    public void setupView() {
        // Set Recipe Name TextBox
        EditText recipe_name_edittext = (EditText) findViewById(R.id.recipe_name_edittext);
        recipe_name_edittext.setText(recipe_name);

        // Load steps (to do)


        // Load spinners to appropriate category and type
        Spinner category_spinner = (Spinner) findViewById(R.id.category_spinner);
        Spinner type_spinner = (Spinner) findViewById(R.id.type_spinner);
        category_spinner.setSelection(category_entries.indexOf(recipe_category.getRecipeCategory()));
        type_spinner.setSelection(type_entries.indexOf(recipe_type.getRecipeType()));

    }

    public void updateCategoryEntries(){
        category_entries.clear();
        ArrayList<String> dbSource = categoryDB.getAsArrayList();
        for(int i=0; i<dbSource.size(); i++){
            category_entries.add(i,dbSource.get(i));
        }
    }

    public void updateTypeEntries(){
        type_entries.clear();
        ArrayList<String> dbSource = typeDB.getAsArrayList();
        for(int i=0; i<dbSource.size(); i++){
            type_entries.add(i,dbSource.get(i));
        }
    }

    public void updateCategorySpinner() {
        if (category_entries.size()>0){
            Spinner category_spinner = (Spinner) findViewById(R.id.category_spinner);
            ArrayAdapter<String> dataAdapterCategory = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, category_entries);
            dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category_spinner.setAdapter(dataAdapterCategory);
            int spinnerPosition = dataAdapterCategory.getPosition(category_entries.get(category_entries.size()-1));
            category_spinner.setSelection(spinnerPosition);
        }
    }

    public void updateTypeSpinner() {
        if (type_entries.size()>0){
            Spinner type_spinner = (Spinner) findViewById(R.id.type_spinner);
            ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, type_entries);
            dataAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type_spinner.setAdapter(dataAdapterType);
            int spinnerPosition = dataAdapterType.getPosition(type_entries.get(type_entries.size()-1));
            type_spinner.setSelection(spinnerPosition);
        }

    }

    public boolean isDuplicateCategory(String other) {
        for (String s : category_entries) {
            if (s.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDuplicateType(String other) {
        for (String s : type_entries) {
            if (s.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public void OnAddCategory(View view) throws IOException {
        EditText category_or_type_editText = (EditText) findViewById(R.id.category_or_type_editText);
        String category_name = category_or_type_editText.getText().toString().trim().toLowerCase();
        RecipeCategory new_category = new RecipeCategory(category_name);

        if (category_name.isEmpty()) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Name your Category first!", duration);

            toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
            toast.show();
        } else if (isDuplicateCategory(new_category.toString())) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, new_category.getRecipeCategory() + " already exists!", duration);

            toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
            toast.show();
        } else {
            category_entries.add(new_category.getRecipeCategory());
            categoryDB.addToDB(new_category.getRecipeCategory());
            updateCategorySpinner();
            category_or_type_editText.setText(null);
        }

    }

    public void OnAddType(View view) {
        EditText category_or_type_editText = (EditText) findViewById(R.id.category_or_type_editText);
        String type_name = category_or_type_editText.getText().toString().trim().toLowerCase();
        RecipeType new_type = new RecipeType(type_name);

        if (type_name.isEmpty()) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Name your Type first!", duration);

            toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
            toast.show();
        } else if (isDuplicateType(new_type.toString())) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, new_type.getRecipeType() + " already exists!", duration);

            toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
            toast.show();
        } else {
            type_entries.add(new_type.getRecipeType());
            try {
                typeDB.addToDB(new_type.getRecipeType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateTypeSpinner();
            category_or_type_editText.setText(null);
        }

    }

}
