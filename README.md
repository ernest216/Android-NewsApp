# MyNewsApp 

## Introduction
The MyNewsApp is a mobile application developed for Android devices, designed to provide
users with current and historic news articles published by English worldwide sources. The
app enables users to browse the latest news, explore articles across different categories
such as Business, Entertainment, Health, Science, Sports, and Technology, and search for
news articles using keywords. This could help Android users to read all kinds of news in
one app with ease.

To use the app, simply install it on your Android device and open it. The app's main
screen displays the latest news. You can navigate through the app using the bottom
navigation bar to access different sections. Clicking on an article will take you to a
detailed view where you can read the full article. The search functionality allows you to
find specific articles related to the user’s searching keyword, by clicking the search logo
on the search bar.

## Design Rationale
Fragments are implemented to provide a modular and responsive user interface. The use of
Fragments, in conjunction with the Jetpack Navigation Component, enables efficient
management of the app’s screens and facilitates smooth navigation between different
sections of the app, allowing users to perform search, read news by their interest in
different areas and read latest news separately. For other Activities, NewsRecyclerAdapter
activity are implemented to display RecyclerView news grids, while the loading page is
supported by the LoadActivity. Most importantly, the display of news content is running in
the NewsActivity by using explicit intent to open the webView for the users to read the
whole article in different Fragments.

The app utilizes ConstraintLayout for its layouts to ensure a flexible and adaptable UI
that accommodates different screen sizes and orientations. This choice supports a robust
and responsive design, enhancing the user experience across a wide range of devices.

Data storage for user preferences and search history is implemented using SharedPreferences.
This lightweight solution suits the app’s requirement for storing small amounts of data,
such as the users' search queries, providing a quick and efficient way to enhance the user
experience by recalling previous searches.

RecyclerView was employed extensively across the app to display lists of news articles. The
use of RecyclerView aligns with the core requirement, and creates an intuitive interface
that can handle dynamic data efficiently, matching MyNewsApp’s need of displaying news data.

## Novel Features
As mentioned above, one of the novel features of the MyNewsApp is its search history
function. This feature allows users to view and select from their past search queries,
facilitating quick access to previously searched topics. This enhances the user experience
by simplifying the search process and personalizing the app's functionality to the user's
needs.

Another novel feature is the implementation of a context menu that appears when a user
performs a long press on a news article. This menu offers the option to open the original
news source in a web browser, allowing users to read further details, explore additional
content from the author, or check the publishing source directly.

## Challenges and Future Improvements
A notable issue encountered was ensuring that all search queries, not just the most recent
one, were retained and displayed upon subsequent app launches when implementing
SharedPreferences. After testing and trying to find the problem, it was realized that
there was a possibility that the previous data set is being overwritten with just the new
query. The logic was then changed to retrieve the existing set first when a new query is
being saved, then update it with the new query, and save it back at last. This ensures
that it is always adding to the existing data rather than overwriting it. I also ensure
when updating the shared preferences with the new search set, `apply()` or `commit()` is
called correctly. The `apply()` method commits the changes asynchronously and doesn't
block the main thread, whereas `commit()` is synchronous and returns a boolean indicating
the success of the operation.

Another issue is that the photos fetched from the news site are not always loaded. Picasso
is properly implemented and I am calling .into(ImageView) with the correct ImageView
reference. Placeholders or error images in Picasso (or any similar library) with
.placeholder() or .error() are set to represent the photo instead. Implementing a
different API will lead to a large scale of change in the project so I decided not to, as
it was in the late implementation stage, but I believe the problem is the API news source,
as most of the new sites have more than one photo and the API would not be able to choose
and fetch one within the Java implementation.

For future improvements, the app could benefit from the following enhancements:
Implementing a caching mechanism to store articles offline, allowing users to access news
without an internet connection, introducing a user authentication system to enable
personalized news feeds based on users' interests and reading habits and expanding the app's
social features, such as sharing articles on social media or commenting on articles within
the app, to enhance user engagement. These proposed improvements aim to enhance the
functionality, performance, and user experience of the MyNewsApp in future versions.
