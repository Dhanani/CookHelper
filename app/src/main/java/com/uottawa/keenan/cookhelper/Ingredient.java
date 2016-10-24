package com.uottawa.keenan.cookhelper;

/**
 * Created by Keenan on 2016-10-24.
 */

public class Ingredient {
    String ingredient;

    public Ingredient(String ingredient) {
        this.ingredient = ingredient.trim().toLowerCase();

    }

    public String getIngredient() {
        return this.ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public boolean equals(Ingredient other) {
        return (this.ingredient.equals(other.ingredient) );
    }
}
