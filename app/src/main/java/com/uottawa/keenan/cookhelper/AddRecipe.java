package com.uottawa.keenan.cookhelper;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class AddRecipe extends AppCompatActivity {


    private ArrayList<String> steps = new ArrayList<String>();
    private ArrayList<String> category_entries = new ArrayList<String>();
    private ArrayList<String> type_entries = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Load from database (txt file)
        // Select Category Spinner
        Spinner category_spinner = (Spinner) findViewById(R.id.category_spinner);
        Spinner type_spinner = (Spinner) findViewById(R.id.type_spinner);

        ArrayAdapter<String> dataAdapterCategory = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, category_entries);
        ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, type_entries);


        AssetManager am = this.getAssets();
        System.out.println("getting assets");

        try {
            InputStream isd = am.open("database/CookingCategories.txt");
            System.out.println(isd.read());
        } catch (IOException e) {
            e.printStackTrace();
        }



        category_entries.add("Appetizer");
        category_entries.add("Main Meal");
        category_entries.add("Side Meal");
        category_entries.add("Dessert");
        category_entries.add("Non Alcoholic Drink");
        category_entries.add("Alcoholic");
        category_entries.add("Dressing");
        category_entries.add("Sauce");


        // Add entries to type spinner
        type_entries.add("Italian");
        type_entries.add("Greek");
        type_entries.add("Chinese");
        type_entries.add("Colombian");
        type_entries.add("Indian");
        type_entries.add("Korean");


        dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(dataAdapterCategory);

        dataAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(dataAdapterType);

    }

    public void OnAddIngredient(View view) {
        EditText ingredient_text = (EditText) findViewById(R.id.add_ingredient_editText);
        CheckBox cb = new CheckBox(this);
        cb.setText(ingredient_text.getText());
        cb.setTextColor(Color.BLACK);
        LinearLayout ingredients_layout = (LinearLayout) findViewById(R.id.ingredients_layout);
        ingredients_layout.addView(cb);
        ingredient_text.setText(null);
        // Add ingredient to database

    }

    public void OnAddStep(View view) {
        EditText step_text = (EditText) findViewById(R.id.enter_step_editText);
        steps.add(step_text.getText().toString());
        EditText et = new EditText(this);
        et.setText(steps.size() + ". " + step_text.getText());
        et.setTextSize(22);
        LinearLayout recipe_steps_layout = (LinearLayout) findViewById(R.id.recipe_steps_layout);
        recipe_steps_layout.addView(et);
        step_text.setText(null);
    }


}
