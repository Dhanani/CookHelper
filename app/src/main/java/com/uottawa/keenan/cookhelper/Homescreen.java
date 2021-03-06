package com.uottawa.keenan.cookhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

public class Homescreen extends AppCompatActivity {

    CreateDB recipiesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
    }

    public void OnAddRecipe(View view) {
        Intent intent = new Intent(getApplicationContext(), AddRecipe.class);
        startActivityForResult (intent,0);
    }

    public void OnHelp(View view){
        Intent intent = new Intent(getApplicationContext(), Help.class);
        startActivity(intent);
    }

    public void OnFindRecipe(View view) {
        Intent intent = new Intent(getApplicationContext(), FindRecipe.class);
        startActivityForResult (intent,0);
    }

    public void OnAllRecipes(View view) {
        Intent intent = new Intent(getApplicationContext(), AllRecipes.class);
        startActivityForResult (intent,0);
    }
}
