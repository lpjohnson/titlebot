# Titlebot Application for Chatmeter Interview
By Louis Johnson

This is my implementation of the Titlebot application challenge from https://github.com/chatmeter/titlebot. The backend is using Scala with the Play 2.8 framework with Slick powering access to a MySQL database.

The backend will accept API calls via two endpoints (POST `/add` and GET `/get-sites`) to both add a new site to the database as well as list the current sites in the store. `/add` takes in a JSON body of `{"url": "some-url"}`

The `scala-scraper` library is used to scrape a given URL for information about any favicons that may be present. Once found, it contructs a proper URL for use on the frotnend.

The frontend is powered by React (create-react-app) using the Material UI. Upon page load it will hit the `/get-sites` endpoint to populate a list of any existing sites (though there won't be any when you launch this), and the interface allows adding a new site which is persisted.

Everything is configured to run via Docker, so no need for setting up a local environment.

A couple of things to note as a part of doing this:
- There are only a couple basic Scala unit tests included (this was in the interest of time and being unfamiliar with mocking Slick)
- The process of finding favicons isn't perfect. Could easily be refined given time as it won't work with some sites. But it does for a large majority. If it can't find a favicon or the URL is bad for some reason, no favicon will show.
- The MySQL table is pre-seeded in docker-compose so there's no need to do so manually, but you can access the database if needed via the credentials found in `application.conf`

To run the application:

- Install Docker on your machine (https://www.docker.com/products/docker-desktop) (preferrably a Mac ;))
- Download this repo
- From terminal in the root directory, execute the command `make up` 
  - This will build the images and launch all the containers, so it'll take a couple minutes
  - You'll see the logs of the main application container to get an idea of when it's ready - once you see `(Server started, use Enter to stop and go back to the console...)`
- Once ready, open a browser and point it to `localhost:3000`
- Enter a URL as prompted
    - After a few seconds Scala will do it's first compile and the operation will complete (subsequent adds should happen instantly)
- Enjoy!
