package net.simplifiedlearning.retrofitexample.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.simplifiedlearning.retrofitexample.Hero;
import net.simplifiedlearning.retrofitexample.OnItemClickListener;
import net.simplifiedlearning.retrofitexample.R;
import net.simplifiedlearning.retrofitexample.RetrofitClient;
import net.simplifiedlearning.retrofitexample.ui.ApiClient;
import net.simplifiedlearning.retrofitexample.ui.ApiInterface;
import net.simplifiedlearning.retrofitexample.ui.Movie;
import net.simplifiedlearning.retrofitexample.ui.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnItemClickListener {

    private HomeViewModel homeViewModel;
    List<Movie> movieList;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ListView listView;
    private OnItemClickListener listItemClickListener;

    boolean isLoading = false;

    ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

       listView = root.findViewById(R.id.listViewHeroes);
        //populateData();
      //  initAdapter();



        movieList = new ArrayList<>();
        recyclerView = (RecyclerView)root.findViewById(R.id.recycleViewContainer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(getContext(),movieList,listItemClickListener);
        recyclerView.setAdapter(recyclerAdapter);
        progressBar = root.findViewById(R.id.progressBar);

      //  initScrollListener();

       // recyclerAdapter.setOnItemClickListener(onItemClickListener);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == movieList.size() - 1) {
                        //bottom of list!
                        loadMore();

                        isLoading = true;
                    }
                }

            }

            private void loadMore() {
                movieList.add(null);
                recyclerAdapter.notifyItemInserted(movieList.size() - 1);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        movieList.remove(movieList.size() - 1);
                        int scrollPosition = movieList.size();
                        recyclerAdapter.notifyItemRemoved(scrollPosition);
                        int currentSize = scrollPosition;
                        int nextLimit = currentSize + 10;
                        // progressBar.setVisibility(View.GONE);

                        while (currentSize - 1 < nextLimit) {
                            //  rowsArrayList.add("Item " + currentSize);
                            recyclerAdapter.setMovieList(movieList);
                         //   progressBar.setVisibility(View.GONE);


                            currentSize++;
                        }

                        recyclerAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }

                },2000);

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                progressBar.setVisibility(View.VISIBLE);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == movieList.size() - 1) {
                        //bottom of list!
                        loadMore();

                        isLoading = true;
                    }
                }
            }

            private void loadMore() {
                movieList.add(null);
                recyclerAdapter.notifyItemInserted(movieList.size() - 1);

                Handler handler = new Handler();
 handler.postDelayed(new Runnable() {
     @Override
     public void run() {

         movieList.remove(movieList.size() - 1);
         int scrollPosition = movieList.size();
         recyclerAdapter.notifyItemRemoved(scrollPosition);
         int currentSize = scrollPosition;
         int nextLimit = currentSize + 10;
        // progressBar.setVisibility(View.GONE);

         while (currentSize - 1 < nextLimit) {
             //  rowsArrayList.add("Item " + currentSize);
             recyclerAdapter.setMovieList(movieList);
         //    progressBar.setVisibility(View.GONE);


             currentSize++;
         }

         recyclerAdapter.notifyDataSetChanged();
         isLoading = false;
     }

 },2000);
            }
        });

/*
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Log.e("pos",""+String.valueOf(position)+view.toString());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }

                    @Override
                    public void onItemClick(Movie movie) {
                        Log.e("",""+movie.getTitle());
                        movie.getTitle();
                    }
                })
        );
*/


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Movie>> call = apiService.getMovies();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                movieList = response.body();
                Log.e("TAG","Response = "+movieList);
                populateData();
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.d("TAG","Response = "+t.toString());
            }
        });
        //calling the method to display the heroes
      //  getHeroes();

        //   fetchdetails();

        return root;
    }


    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == movieList.size() - 1) {
                        //bottom of list!
                        loadMore();

                        isLoading = true;
                    }
                }
            }

            private void loadMore() {
                movieList.add(null);
                recyclerAdapter.notifyItemInserted(movieList.size() - 1);


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        movieList.remove(movieList.size() - 1);
                        int scrollPosition = movieList.size();
                        recyclerAdapter.notifyItemRemoved(scrollPosition);
                        int currentSize = scrollPosition;
                        int nextLimit = currentSize + 10;
                      //  progressBar.setVisibility(View.GONE);

                        while (currentSize - 1 < nextLimit) {
                          //  rowsArrayList.add("Item " + currentSize);
                            recyclerAdapter.setMovieList(movieList);

                            currentSize++;
                        }

                        recyclerAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                }, 2000);


            }

        });
    }


    private void populateData() {
        int i = 0;
        while (i < 10) {
           // movieList.add();
            recyclerAdapter.setMovieList(movieList);

            i++;
        }
    }


    private void getHeroes() {
        Call<List<Hero>> call = RetrofitClient.getInstance().getMyApi().getHeroes();
        call.enqueue(new Callback<List<Hero>>() {
            @Override
            public void onResponse(Call<List<Hero>> call, Response<List<Hero>> response) {
                List<Hero> heroList = response.body();

                //Creating an String array for the ListView
                String[] heroes = new String[heroList.size()];

                //looping through all the heroes and inserting the names inside the string array
                for (int i = 0; i < heroList.size(); i++) {
                    heroes[i] = heroList.get(i).getName();
                }

                //displaying the string array into listview
                listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, heroes));
            }

            @Override
            public void onFailure(Call<List<Hero>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void clickPosition(int position, int id) {
        switch (id) {
            case R.id.title:
                Toast.makeText(getContext(), position + " Full View ID" + id, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}