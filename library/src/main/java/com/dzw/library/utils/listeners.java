package com.dzw.library.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @author Death丶Love
 * @date 2021-01-08 9:35 AM
 * @description 一些控件的监听事件的特殊处理
 */
public class listeners {
    /**
     * 当 recyclerview 在 swipeRefreshLayout 时，如果 recyclerview 中数据超过一屏，则滑到底部时再下拉
     * 有时候下拉事件会被 swipeRefreshLayout 捕获，导致 recyclerview 无法滑到顶部
     * 因此在 recyclerview 的滑动监听中判断当 recyclerview 不处于顶部时，swipeRefreshLayout 不获得焦点
     */
    public static void recyclerViewInSWip(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                refreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
    }
}
