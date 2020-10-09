package net.simplifiedlearning.retrofitexample.ui;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    @SerializedName("name")
    private String title;

    @SerializedName("html_url")
    private String imageUrl;

    public class Datum {

        @SerializedName("login")
        public String id;
        @SerializedName("login")
        public String name;
        @SerializedName("login")
        public String year;
        @SerializedName("login")
        public String pantoneValue;

    }
    public Movie() {
    }
    public Movie(String title) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public static List<Movie> createMovies(int itemCount) {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Movie movie = new Movie("Movie " + (itemCount == 0 ?
                    (itemCount + 1 + i) : (itemCount + i)));
            movies.add(movie);
        }
        return movies;
    }

}
