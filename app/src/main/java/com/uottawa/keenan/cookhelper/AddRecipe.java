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
import android.view.ViewGroup;
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
    private ArrayList<RecipeStep> steps = new ArrayList<>();
    private ArrayList<String> category_entries = new ArrayList<>();
    private ArrayList<String> type_entries = new ArrayList<>();
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<RecipeCategory> recipe_categories = new ArrayList<>();
    private ArrayList<RecipeType> recipe_types = new ArrayList<>();

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        setupCategories();
        updateCategorySpinner();
        setupTypes();
        updateTypeSpinner();
        setupIngredients();
    }

    public void setupCategories() {
        recipe_categories.add(new RecipeCategory("Appetizer"));
        recipe_categories.add(new RecipeCategory("Main Meal"));
        recipe_categories.add(new RecipeCategory("Side Meal"));
        recipe_categories.add(new RecipeCategory("Non Alcoholic Drink"));
        recipe_categories.add(new RecipeCategory("Dessert"));
        recipe_categories.add(new RecipeCategory("Sauce"));
        recipe_categories.add(new RecipeCategory("Dressing"));
        recipe_categories.add(new RecipeCategory("Alcoholic Drink"));
    }

    public void setupTypes() {
        recipe_types.add(new RecipeType("Italian"));
        recipe_types.add(new RecipeType("Greek"));
        recipe_types.add(new RecipeType("Chinese"));
        recipe_types.add(new RecipeType("Colombian"));
        recipe_types.add(new RecipeType("Indian"));
        recipe_types.add(new RecipeType("Korean"));
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

    public void updateCategorySpinner() {
        Spinner category_spinner = (Spinner) findViewById(R.id.category_spinner);

        category_entries.clear();
        category_spinner.setAdapter(null);

        ArrayAdapter<String> dataAdapterCategory = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, category_entries);


        for (RecipeCategory rc : recipe_categories) {
            category_entries.add(rc.getRecipeCategory());
        }

        dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(dataAdapterCategory);


        int spinnerPosition = dataAdapterCategory.getPosition(category_entries.get(category_entries.size()-1));
        category_spinner.setSelection(spinnerPosition);
    }

    public void updateTypeSpinner() {
        Spinner type_spinner = (Spinner) findViewById(R.id.type_spinner);

        type_entries.clear();
        type_spinner.setAdapter(null);

        ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, type_entries);

        for (RecipeType rt : recipe_types) {
            type_entries.add(rt.getRecipeType());
        }

        dataAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(dataAdapterType);

        int spinnerPosition = dataAdapterType.getPosition(type_entries.get(type_entries.size()-1));
        type_spinner.setSelection(spinnerPosition);
    }


    public void OnAddIngredient(View view) {
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

    public boolean isDuplicateCategory(RecipeCategory other) {
        for (RecipeCategory rc : recipe_categories) {
            if (rc.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDuplicateType(RecipeType other) {
        for (RecipeType rt : recipe_types) {
            if (rt.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public void OnAddCategory(View view) {
        EditText category_or_type_editText = (EditText) findViewById(R.id.category_or_type_editText);
        String category_name = category_or_type_editText.getText().toString().trim().toLowerCase();
        RecipeCategory new_category = new RecipeCategory(category_name);

        if (category_name.isEmpty()) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Name your Category first!", duration);

            toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
            toast.show();
        } else if (isDuplicateCategory(new_category)) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, new_category.getRecipeCategory() + " already exists!", duration);

            toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
            toast.show();
        } else {
            recipe_categories.add(new_category);
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
        } else if (isDuplicateType(new_type)) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, new_type.getRecipeType() + " already exists!", duration);

            toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
            toast.show();
        } else {
            recipe_types.add(new_type);
            updateTypeSpinner();
            category_or_type_editText.setText(null);
        }

    }



    public void OnEditCurrentRecipe(View view) {
        LinearLayout ingredient_elements_layout = (LinearLayout) findViewById(R.id.ingredient_elements_layout);
        LinearLayout add_edit_recipe_layout = (LinearLayout) findViewById(R.id.add_edit_recipe_layout);
        LinearLayout add_type_category_layout = (LinearLayout) findViewById(R.id.add_type_category_layout);

        final EditText category_or_type_editText = (EditText) findViewById(R.id.category_or_type_editText);
        category_or_type_editText.setVisibility(View.GONE);

        final Button add_ingredient_btn = (Button) findViewById(R.id.add_ingredient_btn);
        final Button edit_current_recipe_button = (Button) findViewById(R.id.edit_current_recipe_button);
        final Button add_current_recipe_btn = (Button) findViewById(R.id.add_current_recipe_btn);
        final Button add_category_btn = (Button) findViewById(R.id.add_category_btn);
        final Button add_type_btn = (Button) findViewById(R.id.add_type_btn);

        add_category_btn.setVisibility(View.GONE);
        add_type_btn.setVisibility(View.GONE);

        add_ingredient_btn.setVisibility(View.GONE);
        add_current_recipe_btn.setVisibility(View.GONE);
        edit_current_recipe_button.setVisibility(View.GONE);

        final Button delete_category_btn = new Button(this);
        delete_category_btn.setText("Delete Category");
        add_type_category_layout.addView(delete_category_btn);
        delete_category_btn.setVisibility(View.VISIBLE);

        int duration = Toast.LENGTH_SHORT;
        final Toast delete_category_toast = Toast.makeText(this, "There's nothing to delete!", duration);

        delete_category_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Spinner category_spinner = (Spinner) findViewById(R.id.category_spinner);

                if (category_spinner.getSelectedItem() == null) {
                    delete_category_toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
                    delete_category_toast.show();
                } else {
                    RecipeCategory selected_item = new RecipeCategory(category_spinner.getSelectedItem().toString());

                    ((ArrayAdapter<String>) category_spinner.getAdapter()).remove((String)category_spinner.getSelectedItem());
                    ((ArrayAdapter<String>) category_spinner.getAdapter()).notifyDataSetChanged();
                    recipe_categories.clear();

                    for (int i = 0; i < ((ArrayAdapter<String>) category_spinner.getAdapter()).getCount(); i++) {
                        recipe_categories.add(new RecipeCategory(((ArrayAdapter<String>) category_spinner.getAdapter()).getItem(i).toString()));
                    }
                }
            }
        });


        final Button delete_type_btn = new Button(this);
        delete_type_btn.setText("Delete Type");
        add_type_category_layout.addView(delete_type_btn);
        delete_type_btn.setVisibility(View.VISIBLE);

        delete_type_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Spinner type_spinner = (Spinner) findViewById(R.id.type_spinner);

                if (type_spinner.getSelectedItem() == null) {
                    delete_category_toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
                    delete_category_toast.show();
                } else {
                    RecipeType selected_item = new RecipeType(type_spinner.getSelectedItem().toString());

                    ((ArrayAdapter<String>) type_spinner.getAdapter()).remove((String)type_spinner.getSelectedItem());
                    ((ArrayAdapter<String>) type_spinner.getAdapter()).notifyDataSetChanged();

                    for (int i = 0; i < ((ArrayAdapter<String>) type_spinner.getAdapter()).getCount(); i++) {
                        recipe_types.add(new RecipeType(((ArrayAdapter<String>) type_spinner.getAdapter()).getItem(i).toString()));
                    }

                }
            }
        });

        final Button delete_ingredient_btn = new Button(this);
        delete_ingredient_btn.setText("Delete Ingredients");
        ingredient_elements_layout.addView(delete_ingredient_btn);
        delete_ingredient_btn.setVisibility(View.VISIBLE);

        final Button done_btn = new Button(this);
        done_btn.setText("Done");
        done_btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        add_edit_recipe_layout.addView(done_btn);
        done_btn.setVisibility(View.VISIBLE);

        done_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                add_ingredient_btn.setVisibility(View.VISIBLE);
                add_current_recipe_btn.setVisibility(View.VISIBLE);
                edit_current_recipe_button.setVisibility(View.VISIBLE);
                category_or_type_editText.setVisibility(View.VISIBLE);
                delete_type_btn.setVisibility(View.GONE);
                delete_category_btn.setVisibility(View.GONE);
                add_category_btn.setVisibility(View.VISIBLE);
                add_type_btn.setVisibility(View.VISIBLE);

                delete_ingredient_btn.setVisibility(View.GONE);
                done_btn.setVisibility(View.GONE);

            }
        });

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

    public void OnAddRecipe(View view) {
        EditText recipe_name_edittext = (EditText) findViewById(R.id.recipe_name_edittext);
        String recipe_name = recipe_name_edittext.getText().toString().trim();
        if (recipe_name.isEmpty()) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Name your Recipe!", duration);

            toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
            toast.show();
        } else {
            // continue
        }
    }

}

