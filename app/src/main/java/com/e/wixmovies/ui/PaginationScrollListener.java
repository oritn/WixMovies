package com.e.wixmovies.ui;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private int visibleThreshold = 5;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private boolean disableScrolling = false;

    final RecyclerView.LayoutManager mLayoutManager;

    public PaginationScrollListener(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        if(disableScrolling) return;
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();
        lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            onLoadMore(totalItemCount, view);
            loading = true;
        }
    }

    public void resetState() {
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    /**
     * notify that the listener should load more data
     * @param totalItemsCount
     * @param view
     */
    public abstract void onLoadMore(int totalItemsCount, RecyclerView view);

    /**
     * disable loading more data
     * @param disableScrolling
     */
    public void setDisableScrolling(boolean disableScrolling) {
        this.disableScrolling = disableScrolling;
    }
}
