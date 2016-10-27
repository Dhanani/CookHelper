package com.uottawa.keenan.cookhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FindRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_recipe);
    }

    private void add_boolean_operator(String operator){
        EditText ingredients_editText = (EditText) findViewById(R.id.ingredients_editText);
        String current_text = ingredients_editText.getText().toString().trim().toLowerCase();
        ingredients_editText.setText(current_text + " " + operator);
        ingredients_editText.setSelection(ingredients_editText.getText().length());
    }

    public void OnAnd(View view) {
        add_boolean_operator("and");
    }

    public void OnOr(View view) {
        add_boolean_operator("or");
    }

    public void OnNot(View view) {
        add_boolean_operator("not");
    }

}
