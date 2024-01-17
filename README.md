# android-test-assignment
Template project for Shackle Android interview assignment

## Instructions

Just run the project as would any other Android project, on any device or emulator.
You may need to change the api key for your own, in `Service.kt`.


## Architecture

- MVVM architecture, with a repository layer to abstract the two data sources we're using.
- Data source 1 is the remote API, which is accessed via Ktor. This is used for fetching results and details with the given parameters.
- Data source 2 is the local database, which is accessed via Room. This is used for locally caching previous searches.
- The views follow the unidirectional data flow pattern, where the view model is the single source of for the search parameters and the user edits them in the grid.
This is done using liveData and the coroutines. Events from the compose views are passed up and the view model updates the liveData, which is then observed by the views.
- When the user hits search the navigation is quite simple. We just start a new activity with the search parameters as extras, and pop in on the nav stack.
- We SearchActivity has it's own ViewModel which fetches the results from the repository and observes them. When the results are received they are passed to the view.
We also save the search to the local database in this ViewModel
- The models aren't exactly decoupled from each layer of the architecture (The entitiy is used in the database and the view model), but I traded off speed of delivery for perfect architecture here.

## More comments
- The API is quite slow so I added a loading spinner. With more time I could've explored making it faster by use more parrallel requests when fetching the details.
- The API sometimes times out that's why I added the error message. Do re-run the search if you get an error.
- Please see fig_1.0.png for architecture diagram and dependencies.

## 3rd party libraries used
- Ktor for networking
- Room for database
- Coil for image loading
- Hilt for dependency injection
- Serialization for json parsing
- Mockito for testing
