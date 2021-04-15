# NASA Asteroid App

## Purpose 
This app uses the NASA API to be able to fetch information about object that are near earth on a given day
as well as download the image of the day as well. This was the project required to pass the 
Udacity Developing Android Apps with Kotlin PT2 as part of the Android Developer Nanodegree

## What this app demonstrates
- RecyclerView
- Requesting data from an API via Retrofit
- Downloading images using Picasso
- Formatting JSON using Moshi
- Using Room to create a DB as a offline cache
- Have app work when there is no network connection
- WorkManager to process tasks in the background

## Local Use
You will need to get your own NASA API key at api.nasa.gov. Once you do, all you need to do
is slap that key into `myApiNasa` in `gradle.properties`