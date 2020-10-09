package net.simplifiedlearning.retrofitexample.ui;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("repositories")
    Call<List<Movie>> getMovies();
}
