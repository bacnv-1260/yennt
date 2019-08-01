package com.example.myfilternews.favorite;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myfilternews.R;
import com.example.myfilternews.dbhelper.DBHelper;
import com.example.myfilternews.dbhelper.NewsImplement;
import com.example.myfilternews.model.News;

import java.util.List;

public class FavoriteFragmentAdapter extends RecyclerView.Adapter<FavoriteFragmentAdapter.ViewHolder> {
    private List<News> mListNews;
    private Context mContext;
    private NewsImplement mDao;
    private News news;

    public FavoriteFragmentAdapter(List<News> listNews, Context context) {
        mListNews = listNews;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_save,
            viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.bindData(mListNews.get(i));
        viewHolder.setClickListener(viewHolder);
    }

    @Override
    public int getItemCount() {
        return mListNews != null ? mListNews.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener {
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewDescription;
        ItemSaveClickListener listener;
        private News news;
        private List<News> listNews;

        public ViewHolder(View view) {
            super(view);
            listNews = mListNews;
            imageView = view.findViewById(R.id.image_view);
            textViewTitle = view.findViewById(R.id.text_view_title);
            textViewDescription = view.findViewById(R.id.text_view_description);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setClickListener(final ViewHolder viewHolder) {
            listener = new ItemSaveClickListener() {
                @Override
                public void onClickItem(int position) {
                    News news = mListNews.get(getAdapterPosition());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
                    mContext.startActivity(browserIntent);
                }

                @Override
                public void onLongClickItem(int position) {
                    PopupMenu popupMenu = new PopupMenu(mContext, viewHolder.itemView);
                    popupMenu.inflate(R.menu.menu_popup_favorite);
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.item_delete:
                                    mDao = new NewsImplement(DBHelper.getInstance(mContext));
                                    News news2 = mListNews.get(getAdapterPosition());
                                    mDao.deleteNewsFavorite(news2.getTitle());
                                    mListNews.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    return true;
                            }
                            return false;
                        }
                    });
                }
            };
        }

        public void bindData(News news) {
            if (news.getUrlToImage() == null) {
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }
            Glide.with(mContext).load(news.getUrlToImage())
                .into(imageView);
            textViewTitle.setText(news.getTitle());
            textViewDescription.setText(news.getDescription());
        }

        @Override
        public void onClick(View v) {
            listener.onClickItem(getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onLongClickItem(getLayoutPosition());
            return true;
        }

    }
    public interface ItemSaveClickListener {
        void onClickItem(int position);
        void onLongClickItem(int position);
    }
}
