package net.simplifiedlearning.retrofitexample;

import android.view.View;

import net.simplifiedlearning.retrofitexample.ui.Movie;

public interface OnItemClickListener {
 //   void onItemClick(Movie item);
  //  void onItemClick(View view, int position);
 void clickPosition(int position, int id);

}

