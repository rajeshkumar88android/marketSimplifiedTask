package net.simplifiedlearning.retrofitexample.ui;

import android.content.Context;
//import android.graphics.Movie;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.simplifiedlearning.retrofitexample.OnItemClickListener;
import net.simplifiedlearning.retrofitexample.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyviewHolder> {

    Context context;
    List<Movie> movieList;
    public RecyclerItemClickListener.OnItemClickListener listener;
    public RecyclerAdapter recycleadpater;
    private View.OnClickListener mOnItemClickListener;

    private OnItemClickListener listItemClickListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    

    public RecyclerAdapter(Context context, OnItemClickListener listItemClickListener) {
        this.context = context;
        this.listItemClickListener = listItemClickListener;

    }

    public RecyclerAdapter(Context context, List<Movie> movieList,OnItemClickListener listItemClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.listItemClickListener = listItemClickListener;
        //mItemList = movieList;

    }


    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_adapter,parent,false);
      //  view.setOnClickListener((View.OnClickListener) context);
      //  mTextView = (TextView) view;

        if (viewType == VIEW_TYPE_ITEM) {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter, parent, false);
            return new MyviewHolder(view);
        } else {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new MyviewHolder(view);
        }


       // return new MyviewHolder(view);
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.title);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        Movie item = movieList.get(position);
        viewHolder.tvItem.setText(item.getTitle());

    }


    @Override
    public void onBindViewHolder(RecyclerAdapter.MyviewHolder holder, int position) {
        holder.tvMovieName.setText(movieList.get(position).getTitle());

        holder.tvMovieName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("",""+movieList.get(position).getTitle());
//                listener.onItemClick(movieList.get(position));

            }
        });

        //Glide.with(context).load(movieList.get(position).getImageUrl()).apply(RequestOptions.centerCropTransform()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if(movieList != null){
            return movieList.size();
        }
        return 0;

    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;

    }

    public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMovieName;
        ImageView image;
        ProgressBar progressBar;


        public MyviewHolder(View itemView) {
            super(itemView);
            tvMovieName = (TextView)itemView.findViewById(R.id.title);
            image = (ImageView)itemView.findViewById(R.id.image);
           // itemView.setTag(this);
            //itemView.setOnClickListener(mOnItemClickListener);
            progressBar = itemView.findViewById(R.id.progressBar);

            tvMovieName.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listItemClickListener.clickPosition(getAdapterPosition(), v.getId());
         //   listItemClickListener.clickPosition();
            int position = getLayoutPosition(); // gets item position
            Movie user = movieList.get(position);
            // We can access the data within the views
            Toast.makeText(context,tvMovieName.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
