package com.sirui.inquiry.hospital.ui.module.loadmore;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sirui.basiclib.utils.MyLog;
import com.sirui.inquiry.hospital.ui.module.loadmore.interfaces.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiepc on  2017-08-13 16:45
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TimeControler.OnCallBackListener {

    public static final int TYPE_COMMON_VIEW = 100001;//普通类型 Item
    public static final int TYPE_FOOTER_VIEW = 100002;//footer类型 Item

    protected Context mContext;
    protected List<T> mDatas;
    private boolean isAutoLoadMore = true; //是否自动加载，当滑动到底部时会自动加载

    private OnLoadMoreListener mLoadMoreListener;
    private View mLoadingView; //分页加载中view
    private View mLoadMoreView; //点击加载更多的view
    private View mLoadFailedView; //分页加载失败view
    private View mLoadEndView; //分页加载结束view
    private RelativeLayout mFooterLayout;//footer view
    private boolean isReset;//开始重新加载数据

    private TimeControler timeControler;

    private static final int TIME_DELAY_TYPE = 10;

    private static final int TIME_DELAY_MILLIS = 1500;

    protected abstract int getViewType(int position, T data);


    public BaseAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas == null ? new ArrayList<T>() : datas;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_FOOTER_VIEW:
                if (mFooterLayout == null) {
                    mFooterLayout = new RelativeLayout(mContext);
                }
                viewHolder = ViewHolder.create(mFooterLayout);
                break;
        }
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return mDatas.size() + getFooterViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterView(position)) {
            return TYPE_FOOTER_VIEW;
        }
        return getViewType(position, mDatas.get(position));
    }

    /**
     * 是否是FooterView
     *
     * @param position
     * @return
     */
    private boolean isFooterView(int position) {
        return position >= getItemCount() - 1;
    }

    /**
     * 返回 footer view数量
     *
     * @return
     */
    public int getFooterViewCount() {
        return !mDatas.isEmpty() ? 1 : 0;
    }

    protected boolean isCommonItemView(int viewType) {
        return viewType != TYPE_FOOTER_VIEW;
    }

    /**
     * 根据positiond得到data
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        if (mDatas.isEmpty()) {
            return null;
        }
        return mDatas.get(position);
    }


    /**
     * StaggeredGridLayoutManager模式时，FooterView可占据一行
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooterView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    /**
     * GridLayoutManager模式时， FooterView可占据一行，判断RecyclerView是否到达底部
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
        if (isAutoLoadMore) {
            startLoadMore(recyclerView, layoutManager);
        }
    }


    /**
     * 判断列表是否滑动到底部
     *
     * @param recyclerView
     * @param layoutManager
     */
    private void startLoadMore(RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager) {
        MyLog.i("------开启自动加载更多startLoadMore---------");
        if (mLoadMoreListener == null) {
            return;
        }
        if (timeControler == null) {
            timeControler = new TimeControler();
            timeControler.setOnListener(this);
        } else {
            timeControler.removeDelayTime();
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                MyLog.i("------onScrollStateChanged---------");
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                        scrollDelayLoadMore();
                    } else {
                        if (timeControler.isDelaying()) {
                            timeControler.removeDelayTime();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//
//                if (isAutoLoadMore && findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
//                    MyLog.i("------onScrolled ---isAutoLoadMore = true---------");
//                    scrollDelayLoadMore();
//                } else if (isAutoLoadMore) {
//                    MyLog.i("------onScrolled ---isAutoLoadMore = false---------");
//                    isAutoLoadMore = false;
//                }
            }
        });
    }

    /**
     * 到达底部开始刷新
     */
    private void scrollDelayLoadMore() {
        MyLog.i("--------延时加载更多scrollDelayLoadMore----------");
        if (isReset) {
            return;
        }
        if (isAutoLoadMore
                && mFooterLayout != null
                && mFooterLayout.getChildAt(0) != null
                && mFooterLayout.getChildAt(0) == mLoadMoreView) {
            timeControler.startDelayTime(TIME_DELAY_MILLIS, TIME_DELAY_TYPE);
        }
    }

//    private void cancelLoadMore(){
//         timeControler.removeDelayTime();
//    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            return Util.findMax(lastVisibleItemPositions);
        }
        return -1;
    }


    /**
     * 添加新的footer view
     *
     * @param footerView
     */
    private void addFooterView(View footerView) {
        if (footerView == null) {
            return;
        }

        if (mFooterLayout == null) {
            mFooterLayout = new RelativeLayout(mContext);
        }
        removeFooterView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mFooterLayout.addView(footerView, params);
    }

    /**
     * 清空footer view
     */
    public void removeFooterView() {
        if (mFooterLayout != null) {
            mFooterLayout.removeAllViews();
        }
    }

    /**
     * 刷新加载更多的数据
     *
     * @param datas
     */
    public void setLoadMoreData(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            int size = mDatas.size();
            mDatas.addAll(datas);
            notifyItemInserted(size);
//         notifyDataSetChanged();
        }
    }

    /**
     * 下拉刷新，得到的新数据查到原数据起始
     *
     * @param datas
     */
    public void setData(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(0, datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 初次加载、或下拉刷新要替换全部旧数据时刷新数据
     *
     * @param datas
     */
    public void setNewData(List<T> datas) {
        if (isReset) {
            isReset = false;
        }
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void postShowLoadMoreView() {
        if (mFooterLayout != null) {
            mFooterLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showLoadMore();
                }
            }, 1000);
        } else {
            showLoadMore();
        }
    }

    public void remove(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 初始化加载中布局
     *
     * @param loadingView
     */
    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
//        addFooterView(mLoadingView);
    }

    public void setLoadingView(int loadingId) {
        setLoadingView(Util.inflate(mContext, loadingId));
    }

    /**
     * 初始加载失败布局
     *
     * @param loadFailedView
     */
    public void setLoadFailedView(View loadFailedView) {
        mLoadFailedView = loadFailedView;
    }

    public void setLoadFailedView(int loadFailedId) {
        setLoadFailedView(Util.inflate(mContext, loadFailedId));
    }

    /**
     * 点击加载更多布局
     *
     * @param loadMoreView
     */
    public void setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
    }

    public void setLoadMoreView(int loadMoreId) {
        setLoadMoreView(Util.inflate(mContext, loadMoreId));
    }

    /**
     * 初始化全部加载完成布局
     *
     * @param loadEndView
     */
    public void setLoadEndView(View loadEndView) {
        mLoadEndView = loadEndView;
    }

    public void setLoadEndView(int loadEndId) {
        setLoadEndView(Util.inflate(mContext, loadEndId));
    }


    /**
     * 重置adapter，恢复到初始状态
     */
    public void reset() {
        if (mLoadingView != null) {
            addFooterView(mLoadingView);
        }
        isReset = true;
        isAutoLoadMore = true;
        mDatas.clear();
    }

    public void showLoading() {
        if (mLoadingView != null) {
            addFooterView(mLoadingView);
        }
    }

    /**
     * 数据加载完成
     */
    public void showLoadEnd() {
        if (mLoadEndView != null) {
            addFooterView(mLoadEndView);
        }
    }

    /**
     * 数据加载失败
     */
    public void showLoadFailed() {
        addFooterView(mLoadFailedView);
        mLoadFailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addFooterView(mLoadingView);
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore(true);
                }
            }
        });
    }

    /**
     * 数据加载失败
     */
    public void showLoadMore() {
        addFooterView(mLoadMoreView);
        if (mLoadMoreView != null) {
            mLoadMoreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
    //                addFooterView(mLoadingView);
                    if (mLoadMoreListener != null) {
                        timeControler.removeDelayTime();
                        mLoadMoreListener.onLoadMore(false);
                    }
                }
            });
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    @Override
    public void onTimeFinish(int operation) {
        if (operation == TIME_DELAY_TYPE && mFooterLayout.getChildAt(0) == mLoadMoreView) {
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore(false);
            }
        }
    }
}
