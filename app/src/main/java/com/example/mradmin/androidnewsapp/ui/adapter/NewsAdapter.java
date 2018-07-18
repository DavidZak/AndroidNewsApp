package com.example.mradmin.androidnewsapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mradmin.androidnewsapp.R;
import com.example.mradmin.androidnewsapp.entity.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mrAdmin on 25.08.2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.Holder> {

    private static final String TAG = "AdapterNews";

    private List<NewsItem> data;

    private ListenerAdapter listener;

    public NewsAdapter(ListenerAdapter listener){
        this.data = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(at(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public NewsItem at(int position){
        return data.get(position);
    }

    public void setItems(@NonNull List<NewsItem> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public void addItem(NewsItem item){
        this.data.add(item);
        notifyItemInserted(data.size());
    }

    public void clearItems(){
        this.data.clear();
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewNewsImage)
        ImageView imageView;
        @BindView(R.id.textViewNewsTitle)
        TextView textViewTitle;
        @BindView(R.id.textViewNewsDate)
        TextView textViewDate;
        @BindView(R.id.textViewNewsDescription)
        TextView textViewDescription;

        Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(final NewsItem curNewsItem){
            if (curNewsItem.getImage() != null && !curNewsItem.getImage().isEmpty()){
                Picasso.with(itemView.getContext()).load(curNewsItem.getImage()).placeholder(R.drawable.noimage).into(imageView);
            }

            textViewTitle.setText(curNewsItem.getTitle());
            textViewDate.setText(curNewsItem.getDate());
            textViewDescription.setText(curNewsItem.getDescription());

            itemView.setOnClickListener(v -> {
                listener.clickItem(curNewsItem);
            });
        }
    }

    public interface ListenerAdapter {
        void clickItem(NewsItem newsItem);
    }
}
