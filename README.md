# Project 2 - Popular Movies Stage One

Android app to display a list of popular or highest-rated movies via [the MovieDB API](https://www.themoviedb.org/settings/api) and additional details of the movie. 

## User Stories
* [x] User is shown a grid view of movie posters upon launch.
* [x] User is allowed to change the sort order (most popular/highest-rated).
* [x] User can tap on a movie poster and see the details of the movie:
  * [x] Original Title
  * [x] Movie Poster
  * [x] Plot Synopsis
  * [x] User Rating
  * [x] Release Date

## Required Features
* [x] Build a UI layout for multiple Activities.
* [x] Launch these Activities via intent.
* [x] Fetch data from themovieDB API.

## Open-source libraries used
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Parceler](https://github.com/johncarl81/parceler) - Serialization library
- [Butterknife](http://jakewharton.github.io/butterknife/i) - Popular view injection library.

## Notes
An API key is required to fetch data from themovieDB API. The key is specified in the file `NetworkUtils.java`.
