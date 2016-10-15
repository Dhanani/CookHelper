package com.uottawa.keenan.cookhelper;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddRecipe extends AppCompatActivity {

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


}
