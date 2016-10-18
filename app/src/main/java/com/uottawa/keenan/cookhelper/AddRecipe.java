package com.uottawa.keenan.cookhelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;


public class AddRecipe extends AppCompatActivity {


    private ArrayList<RecipeStep> steps = new ArrayList<RecipeStep>();
    private ArrayList<String> category_entries = new ArrayList<String>();
    private ArrayList<String> type_entries = new ArrayList<String>();
    private ArrayList<String> ingredients = new ArrayList<String>();

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        setupCategorySpinner();
        setupTypeSpinner();
    }

    public void setupCategorySpinner() {
        Spinner category_spinner = (Spinner) findViewById(R.id.category_spinner);

        ArrayAdapter<String> dataAdapterCategory = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, category_entries);

        category_entries.add("Appetizer");
        category_entries.add("Main Meal");
        category_entries.add("Side Meal");
        category_entries.add("Dessert");
        category_entries.add("Non Alcoholic Drink");
        category_entries.add("Sauce");
        category_entries.add("Dressing");
        category_entries.add("Non Alcoholic Drink");
        category_entries.add("Alcoholic Drink");

        dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(dataAdapterCategory);


    }

    public void setupTypeSpinner() {
        Spinner type_spinner = (Spinner) findViewById(R.id.type_spinner);
        ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, type_entries);

        type_entries.add("Italian");
        type_entries.add("Greek");
        type_entries.add("Chinese");
        type_entries.add("Colombian");
        type_entries.add("Indian");
        type_entries.add("Korean");

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
        ingredients.add(ingredient_text.getText().toString());
        ingredient_text.setText(null);
    }


    private int getVisibleChildCount(LinearLayout layout) {
        int count = 0;
        for (int i = 0; i < layout.getChildCount(); i++) {
            if(layout.getChildAt(i).getVisibility() == View.VISIBLE) {
                count++;
            }
        }
        return count;
    }

    public void OnAddStep(View view) {
        final EditText step_text = (EditText) findViewById(R.id.enter_step_editText);
        LinearLayout recipe_steps_layout = (LinearLayout) findViewById(R.id.recipe_steps_layout);
        final LinearLayout recipe_step_btn_layout = (LinearLayout) findViewById(R.id.recipe_add_step_btn_layout);
        final LinearLayout recipe_save_step_btn_layout = (LinearLayout) findViewById(R.id.recipe_save_step_btn_layout);

        RecipeStep rs = new RecipeStep(step_text.getText().toString());
        steps.add(rs);
        final int index = steps.indexOf(rs) + 1;
        final TextView tv = new TextView(this);
        tv.setText(steps.size() + ". " + step_text.getText());
        tv.setTextSize(22);

        final EditText et = new EditText(this);
        et.setText(step_text.getText());
        recipe_steps_layout.addView(et);
        et.setVisibility(View.GONE);

        final Button save_recipe_step_btn = new Button(this);
        save_recipe_step_btn.setText("Save");
        recipe_save_step_btn_layout.addView(save_recipe_step_btn);
        save_recipe_step_btn.setVisibility(View.GONE);

        tv.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View v) {
                if (getVisibleChildCount(recipe_save_step_btn_layout) == 0)  {
                    tv.setVisibility(View.GONE);
                    et.setVisibility(View.VISIBLE);
                    et.setFocusableInTouchMode(true);
                    et.requestFocus();
                    et.setSelection(et.getText().length());

                    final Button add_recipe_step_btn = (Button) findViewById(R.id.add_recipe_step_btn);
                    add_recipe_step_btn.setVisibility(View.GONE);
                    save_recipe_step_btn.setVisibility(View.VISIBLE);

                    save_recipe_step_btn.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {


                            String new_text = et.getText().toString();
                            tv.setText(index + ". " +new_text);
                            RecipeStep new_rs = new RecipeStep(tv.getText().toString());
                            steps.set(index-1, new_rs);
                            save_recipe_step_btn.setVisibility(View.GONE);
                            add_recipe_step_btn.setVisibility(View.VISIBLE);
                            et.setVisibility(View.GONE);
                            tv.setVisibility(View.VISIBLE);
                            step_text.setFocusableInTouchMode(true);
                            step_text.requestFocus();


                        }
                    });
                }



            }
        });


        recipe_steps_layout.addView(tv);
        step_text.setText(null);
    }

}
