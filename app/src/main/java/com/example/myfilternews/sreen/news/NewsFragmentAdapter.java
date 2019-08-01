package com.example.myfilternews.sreen.news;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfilternews.model.News;
import com.example.myfilternews.dbhelper.NewsImplement;
import com.example.myfilternews.R;
import com.example.myfilternews.dbhelper.DBHelper;

import java.util.List;

public class NewsFragmentAdapter extends RecyclerView.Adapter<NewsFragmentAdapter.ViewHolder> {
    private List<News> mListNews;
    private Context mContext;
    private NewsImplement mDao;

    public NewsFragmentAdapter(List<News> listNews, Context context) {
        mListNews = listNews;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news,
            viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.bindData(mListNews.get(i));
        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position) {
            }

            @Override
            public void onLongClickItem(int position) {
                mDao = new NewsImplement(DBHelper.getInstance(mContext));
                saveNews(position);
                News news = mListNews.get(position);
                mDao.addNews(news);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListNews != null ? mListNews.size() : 0;
    }

    public News saveNews(int position) {
        News news = mListNews.get(position);
        String url = news.getUrl();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(news.getTitle());
        request.setDescription(news.getDescription());
        request.setDestinationUri(Uri.parse(news.getUrl()));
        request
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, "news" +
            ".ext");
        DownloadManager manager =
            (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        Toast.makeText(mContext, "Download successfully", Toast.LENGTH_SHORT).show();
        return mListNews.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener {
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewDescription;
        ItemClickListener listener;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image_view);
            textViewTitle = view.findViewById(R.id.text_view_title);
            textViewDescription = view.findViewById(R.id.text_view_description);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.listener = itemClickListener;
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

    public interface ItemClickListener {
        void onClickItem(int position);
        void onLongClickItem(int position);
    }
}
