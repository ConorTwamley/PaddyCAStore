//package com.conor.paddycastore.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.conor.paddycastore.R;
//import com.conor.paddycastore.RecyclerViewClickListener;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
//
//    Context context;
//    private static RecyclerViewClickListener itemListener;
//    ArrayList<String> idList;
//    ArrayList<String> titleList;
//    ArrayList<String> categoryList;
//    ArrayList<String> manufacturerList;
//    ArrayList<String> priceList;
//    ArrayList<String> quantityList;
//    ArrayList<String> productImageList;
//
//    public SearchAdapter(Context context, ArrayList<String> list, ArrayList<String> titleList,
//                         ArrayList<String> categoryList, ArrayList<String> priceList, ArrayList<String> quantityList,
//                         ArrayList<String> productImageList, RecyclerViewClickListener recyclerViewClickListener) {
//        this.context = context;
////        this.idList = idList;
//        this.titleList = titleList;
//        this.categoryList = categoryList;
//        this.priceList = priceList;
//        this.quantityList = quantityList;
//        this.productImageList = productImageList;
//        this.itemListener = recyclerViewClickListener;
//    }
//
//    public static class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//        ImageView productImage;
//        TextView mTitle, mManufacturer, mCategory, mPrice;
//
//
//        public SearchViewHolder(View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(this);
//            productImage = (ImageView) itemView.findViewById(R.id.productImage);
//            mTitle = (TextView) itemView.findViewById(R.id.titleSearchStock);
//            mManufacturer = (TextView) itemView.findViewById(R.id.manufacturerSearchItem);
//            mPrice = (TextView) itemView.findViewById(R.id.priceSearchItem);
//            mCategory = (TextView) itemView.findViewById(R.id.categorySearchStock);
//
//        }
//
//        @Override
//        public void onClick(View v){
//            itemListener.recyclerViewLisClicked(v, this.getLayoutPosition());
//        }
//    }
//
//    @Override
//    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.search_list_stock, parent, false);
//        return new SearchAdapter.SearchViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(SearchViewHolder holder, int position) {
//        holder.mTitle.setText(titleList.get(position));
//        holder.mManufacturer.setText(manufacturerList.get(position));
//        holder.mPrice.setText("€ " + priceList.get(position));
//        holder.mCategory.setText(categoryList.get(position));
//
//        Picasso.with(context).load(productImageList.get(position)).fit().placeholder(R.mipmap.ic_launcher_round).into(holder.productImage);
//    }
//
//    @Override
//    public int getItemCount() {
//        return titleList.size();
//    }
//}
