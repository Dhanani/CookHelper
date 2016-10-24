package com.uottawa.keenan.cookhelper;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AddRecipe extends AppCompatActivity {
    private ArrayList<RecipeStep> steps = new ArrayList<RecipeStep>();
    private ArrayList<String> category_entries = new ArrayList<String>();
    private ArrayList<String> type_entries = new ArrayList<String>();
    private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        setupCategorySpinner();
        setupTypeSpinner();
        setupIngredients();
    }

    public void setupIngredients() {
        Ingredient milk = new Ingredient("Milk");
        Ingredient butter = new Ingredient("Butter");
        Ingredient onion = new Ingredient("Onion");
        ingredients.add(milk);
        ingredients.add(butter);
        ingredients.add(onion);
        updateIngredients(milk);
        updateIngredients(butter);
        updateIngredients(onion);
    }

    public boolean isDuplicateIngredient(Ingredient new_ingredient) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.equals(new_ingredient)) {
                return true;
            }
        }
        return false;
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


        if (ingredient_text.getText().toString().trim().isEmpty()) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Name your Ingredient first!", duration);

            toast.setGravity(Gravity.TOP|Gravity.LEFT, 420, 300);
            toast.show();
        }  else {
            Ingredient new_ingredient = new Ingredient(ingredient_text.getText().toString());

            if (isDuplicateIngredient(new_ingredient)) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this, new_ingredient.getIngredient() + " is already available!", duration);

                toast.setGravity(Gravity.TOP|Gravity.LEFT, 420, 300);
                toast.show();
            } else {
                ingredients.add(new_ingredient);
                updateIngredients(new_ingredient);
                ingredient_text.setText(null);
            }
        }


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


    public void setOnClicks(final TextView tv, final EditText et, final RecipeStep current_step) {
        final LinearLayout recipe_save_step_btn_layout = (LinearLayout) findViewById(R.id.recipe_save_step_btn_layout);
        LinearLayout delete_step_btn_layout = (LinearLayout) findViewById(R.id.delete_step_btn_layout);

        final Button save_recipe_step_btn = new Button(this);
        save_recipe_step_btn.setText("Save");
        recipe_save_step_btn_layout.addView(save_recipe_step_btn);
        save_recipe_step_btn.setVisibility(View.GONE);

        final Button delete_step_btn = new Button(this);
        delete_step_btn.setText("Delete");
        delete_step_btn_layout.addView(delete_step_btn);
        delete_step_btn.setVisibility(View.GONE);

        tv.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View v) {

                final EditText step_text = (EditText) findViewById(R.id.enter_step_editText);
                final Button add_recipe_step_btn = (Button) findViewById(R.id.add_recipe_step_btn);

                if (getVisibleChildCount(recipe_save_step_btn_layout) == 0)  {
                    step_text.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                    et.setVisibility(View.VISIBLE);
                    et.setFocusableInTouchMode(true);
                    et.requestFocus();
                    et.setSelection(et.getText().length());
                    add_recipe_step_btn.setVisibility(View.GONE);
                    save_recipe_step_btn.setVisibility(View.VISIBLE);
                    delete_step_btn.setVisibility(View.VISIBLE);

                    delete_step_btn.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            steps.remove(current_step);
                            updateSteps();
                            delete_step_btn.setVisibility(View.GONE);
                            save_recipe_step_btn.setVisibility(View.GONE);
                            add_recipe_step_btn.setVisibility(View.VISIBLE);
                            step_text.setVisibility(View.VISIBLE);
                            step_text.setFocusableInTouchMode(true);
                            step_text.requestFocus();

                        }
                    });

                    save_recipe_step_btn.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            int index = steps.indexOf(current_step);
                            steps.set(index, new RecipeStep(et.getText().toString()));
                            delete_step_btn.setVisibility(View.GONE);
                            save_recipe_step_btn.setVisibility(View.GONE);
                            et.setVisibility(View.GONE);
                            tv.setVisibility(View.VISIBLE);
                            add_recipe_step_btn.setVisibility(View.VISIBLE);
                            step_text.setVisibility(View.VISIBLE);
                            step_text.setFocusableInTouchMode(true);
                            step_text.requestFocus();

                            updateSteps();
                        }
                    });
                }
            }
        });
    }

    public void updateSteps() {
        LinearLayout recipe_steps_layout = (LinearLayout) findViewById(R.id.recipe_steps_layout);
        recipe_steps_layout.removeAllViewsInLayout();
        recipe_steps_layout.requestLayout();

        for (int i = 0; i < steps.size(); i ++) {
            TextView tv = new TextView(this);
            tv.setText(i + 1 + ". " + steps.get(i).getStep());
            tv.setTextSize(22);
            recipe_steps_layout.addView(tv);
            EditText et = new EditText(this);
            et.setText(steps.get(i).getStep());
            recipe_steps_layout.addView(et);
            et.setVisibility(View.GONE);
            setOnClicks(tv, et, steps.get(i));
        }

    }

    public void on_add_current_recipe_btn(View view) {
//        code goes here
    }

    public void OnAddStep(View view) {
        EditText step_text = (EditText) findViewById(R.id.enter_step_editText);
        RecipeStep rs = new RecipeStep(step_text.getText().toString());
        steps.add(rs);
        updateSteps();
        step_text.setText(null);
        step_text.setVisibility(View.VISIBLE);
    }

    public void updateIngredients(Ingredient ingredient) {
        LinearLayout ingredients_layout = (LinearLayout) findViewById(R.id.ingredients_layout);
        CheckBox cb = new CheckBox(this);

        cb.setText(ingredient.getIngredient());
        cb.setTextColor(Color.BLACK);
        ingredients_layout.addView(cb);
    }

    public Ingredient findWithString(String s) {
        for (int i = 0; i < ingredients.size(); i ++ ) {
            if (ingredients.get(i).getIngredient().equals(s)) {
                return ingredients.get(i);
            }
        }
        return null;
    }

    public void OnEditCurrentRecipe(View view) {
        LinearLayout ingredient_elements_layout = (LinearLayout) findViewById(R.id.ingredient_elements_layout);
        Button add_ingredient_btn = (Button) findViewById(R.id.add_ingredient_btn);
        add_ingredient_btn.setVisibility(View.GONE);

        Button delete_ingredient_btn = new Button(this);
        delete_ingredient_btn.setText("Delete Ingredients");
        ingredient_elements_layout.addView(delete_ingredient_btn);
        delete_ingredient_btn.setVisibility(View.VISIBLE);

        delete_ingredient_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                LinearLayout ingredients_layout = (LinearLayout) findViewById(R.id.ingredients_layout);
                for (int i = 0; i < ingredients_layout.getChildCount(); i++) {
                    if(((CheckBox)ingredients_layout.getChildAt(i)).isChecked()) {
                        String checkbox_string = ((CheckBox)ingredients_layout.getChildAt(i)).getText().toString();
                        if (findWithString(checkbox_string) != null) {
                            ingredients.remove(findWithString(checkbox_string));
                            ((CheckBox)ingredients_layout.getChildAt(i)).setVisibility(View.GONE);
                        }
                    }
                }

            }
        });

    }

}

