package com.uottawa.keenan.cookhelper;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class AddRecipe extends AppCompatActivity {


    private ArrayList<String> steps = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
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
        TextView tv = new TextView(this);
        tv.setText(steps.size() + ". " + step_text.getText());
        tv.setTextSize(24);
        LinearLayout recipe_steps_layout = (LinearLayout) findViewById(R.id.recipe_steps_layout);
        recipe_steps_layout.addView(tv);
        step_text.setText(null);
    }


}
