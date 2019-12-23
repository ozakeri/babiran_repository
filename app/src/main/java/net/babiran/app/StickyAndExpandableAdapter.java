package net.babiran.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.captain_miao.recyclerviewutils.common.ClickableViewHolder;
import com.github.captain_miao.recyclerviewutils.listener.OnRecyclerItemClickListener;
import com.github.captain_miao.recyclerviewutils.stickyandexpandable.StickyAndExpandableRecyclerHeadersAdapter;

import Models.Image;


/**
 * @author YanLu
 * @since 16/3/28
 */
public class StickyAndExpandableAdapter
        extends StickyAndExpandableRecyclerHeadersAdapter<CommonQuestion, StickyAndExpandableAdapter.ItemViewHolder, StickyAndExpandableAdapter.HeaderItemViewHolder> {
    private static final String TAG = "VehicleDetectionAdapter";

    public StickyAndExpandableAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public int getContentViewType(int dataListIndex) {
        if (dataListIndex >= 0 && mItemList.size() > 0 && dataListIndex < mItemList.size()) {
            CommonQuestion model = mItemList.get(dataListIndex);
            return model.isParentItem() ? 1 : 0;
        } else {
            return 0;
        }
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view_sticky_and_header, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_sticky_header_hide, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindItemViewHolder(final ItemViewHolder holder, int position) {
        CommonQuestion vo = getItem(position);
        holder.mTvTitle.setText(vo.question);
        holder.mTvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mTvValue.getVisibility() == View.VISIBLE) {
                    holder.mTvValue.setVisibility(View.GONE);
                    holder.imageView.setImageResource(R.drawable.ic_add_black_24dp);
                } else {
                    holder.mTvValue.setVisibility(View.VISIBLE);
                    holder.imageView.setImageResource(R.drawable.ic_remove);
                }
            }
        });
        holder.mTvValue.setText("پاسخ:" + "\n" + vo.answer);
//        if (vo.isQualified) {
//            holder.mTvValue.setTextColor(Color.parseColor("#999999"));
//        } else {
//            holder.mTvValue.setTextColor(Color.parseColor("#FF0000"));
//        }
    }

    @Override
    public long getHeaderId(int position) {
        CommonQuestion vo = getItem(position);
        return vo != null ? vo.getHeaderId() : NO_ID;
    }

    @Override
    public HeaderItemViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_view_sticky_header, parent, false);
        return new HeaderItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderItemViewHolder holder, int position) {
        CommonQuestion vo = getItem(position);
        if (vo != null) {
            holder.mTvTitle.setText(vo.subject);
        } else {
            holder.mTvTitle.setText("موضوع نامشخص");
        }
    }

    public class ItemViewHolder extends ClickableViewHolder implements OnRecyclerItemClickListener {
        public TextView mTvTitle;
        public TextView mTvValue;
        public ImageView imageView;

        public ItemViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            mTvTitle = (TextView) view.findViewById(R.id.detail_title);
            mTvValue = (TextView) view.findViewById(R.id.detail_value);
            addOnItemViewClickListener();
            setOnRecyclerItemClickListener(this);
        }

        @Override
        public void onClick(View v, int position) {

        }
    }

    public class HeaderItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvTitle;

        public HeaderItemViewHolder(View view) {
            super(view);
            mTvTitle = (TextView) view.findViewById(R.id.detail_title);
        }
    }
}