# CookHelper

An Android application.

## Coding Standards

1. Any front end element's (Button, TextView, Spinner, etc) accompanying back end connector method (OnClick, etc) must be written a certain way. For buttons, for example, the `id` of a button should be `add_recipe_btn` and it's accompanying `OnClick` method should be the *SAME* thing without the `btn` part, underscores (_), and capatalize all word beginnings. In this case, it would be `OnAddRecipe`.

2. No broken shit in `master`. No exceptions. If your feature does not work fully (I.e. Crashes for an unknown reason), ask a team member for help. Partial feature are allowed as long as they don't produce any errors/crashes.

3. Think of this application as one on the playstore/appstore. Think of actions people would do in this app. Remember, no bugs allowed in master.

## Things To Do:
1. Restrict users from typing "|" verticle pole(s) and "`" backtick(s) into ANY field
2. Parse search query in "Find a Recipe" view, and display results in a listview
3. Above listview must be a custom XML like the one presented in the lab
