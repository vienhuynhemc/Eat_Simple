package com.vientamthuong.eatsimple.wishlist;

import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.vientamthuong.eatsimple.R;
import com.vientamthuong.eatsimple.SharedReferences.DataLocalManager;
import com.vientamthuong.eatsimple.beans.Cart;
import com.vientamthuong.eatsimple.beans.Product;
import com.vientamthuong.eatsimple.detail.Activity_detail;
import com.vientamthuong.eatsimple.loadData.VolleyPool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHoler> {

    Set<String> checkboxes = new HashSet<>();
    HashMap<String,Wishlist> chooseItem = new HashMap<>();
    LinearLayout hidenDialog;

    private Context context;
    private ArrayList<Wishlist> products;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private int pos;

    private WishlistDAO wishlistDAO = new WishlistDAO(context);


    public WishlistAdapter(Context context, ArrayList<Wishlist> products) {
        this.context = context;
        this.products = products;
    }

//    public ArrayList<Wishlist> getChooseItem() {
//        return chooseItem;
//    }
//
//    public void setChooseItem(ArrayList<Wishlist> chooseItem) {
//        this.chooseItem = chooseItem;
//    }


    public HashMap<String, Wishlist> getChooseItem() {
        return chooseItem;
    }

    public void setChooseItem(HashMap<String, Wishlist> chooseItem) {
        this.chooseItem = chooseItem;
    }

    public WishlistAdapter(Set<String> checkboxes) {
        this.checkboxes = checkboxes;
    }

    public Set<String> getCheckboxes() {
        return checkboxes;
    }
    public String print(ArrayList<Wishlist> chooseItem){
        String rs = "";
        for (int i = 0; i < chooseItem.size();i++){
           if(checkExistItem(chooseItem.get(i).getName(),rs)){
               if(i != chooseItem.size() - 1){
                   rs += chooseItem.get(i).getName()+", ";
               }
               else{
                   rs += chooseItem.get(i).getName()+"";
               }
           }
        }
        return rs;
    }
    public boolean checkExistItem(String s, String parent){
        String[] list = parent.split(", ");
        for (int i = 0; i <list.length ;i++){
            if (s.equals(list[i])){
                return false;
            }
        }
        return true;
    }

    public void setCheckboxes(Set<String> checkboxes) {
        this.checkboxes = checkboxes;
    }

    @NonNull
    @Override
    public WishlistViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_wishlist_item,parent,false);


        Animation animation = AnimationUtils.loadAnimation(context,R.anim.animation_item);
        view.startAnimation(animation);


        return new WishlistViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHoler holder, int position) {
        Wishlist w = products.get(position);
        pos = position;
        if(w == null){
            return;
        }
        viewBinderHelper.bind(holder.swipeRevealLayout,w.getName());
        holder.txtName.setText(w.getName());

        viewBinderHelper.bind(holder.swipeRevealLayout,w.getNameSize());
        holder.txtSize.setText(w.getNameSize());

        viewBinderHelper.bind(holder.swipeRevealLayout,String.valueOf(w.getPriceP()));
        holder.txtPrice.setText(w.getPriceP()+" VNĐ");

        viewBinderHelper.bind(holder.swipeRevealLayout,String.valueOf(w.getImg()));
        Glide.with(context).load(w.getImg()).into(holder.img);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get dish's information

                String url ="https://eat-simple-app.000webhostapp.com/dataTranmissionWishlist.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    TranmissionData dish = new TranmissionData();
                                    dish.setId(object.getString("ma_sp"));
                                    dish.setName(object.getString("ten_sp"));
                                    dish.setNumberRest(Integer.parseInt(object.getString("so_luong_con_lai")));
                                    dish.setNumberSaled(Integer.parseInt(object.getString("so_luong_ban_ra")));
                                    dish.setPrice(Integer.parseInt(object.getString("gia")));
                                    dish.setPriceSale(Integer.parseInt(object.getString("gia_km")));
                                    dish.setInformation(object.getString("thong_tin"));
                                    dish.setKcal(Integer.parseInt(object.getString("kcal")));
                                    dish.setTime(Integer.parseInt(object.getString("thoi_gian_nau")));
                                    dish.setUrl(object.getString("url"));


                                    holder.img.buildDrawingCache();
                                    Bitmap bitmap = holder.img.getDrawingCache();

                                    detail_product(dish,bitmap);



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error.toString());
                            }
                        }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params = new HashMap<>();
                        params.put("ma_sp",w.getId());
                        return params;
                    }
                };
                VolleyPool.getInstance(context).addRequest(stringRequest);
            }
        });



        holder.layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.remove(holder.getAdapterPosition());

                // xoa khoi ds wishlist
                deleleteWishlist1(w.getIdCustomer(),w.getId(),w.getSize());
                wishlistDAO.deleteWishlist(w.getIdCustomer(),w.getId(),w.getSize());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(DataLocalManager.getAccount().getId(),w.getId(),w.getSize());
            }
        });

        holder.cbAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.isCheck = isChecked;
            }
        });
        holder.cbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wishlist wishlist = products.get(position);
                Log.d("WWW",position+" pos");
                checkboxes.add(wishlist.getId()+"_"+wishlist.getSize());
                if(holder.isCheck) {
                    if (checkboxes.size() > 0) {
                        //  Toast.makeText(context, checkboxes.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    checkboxes.remove(wishlist.getId()+"_"+wishlist.getSize());
                    if (checkboxes.size() > 0) {
                        // Toast.makeText(context, checkboxes.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                for (Wishlist w : products){
                    for(String s : checkboxes){
                        if (s.equals(w.getId()+"_"+w.getSize())){
                            chooseItem.put(s,w);
                        }
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(products != null) {
            return products.size();
        }
        return 0;
    }

    public class WishlistViewHoler extends RecyclerView.ViewHolder{

        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout layoutDelete;
        private ImageView img;
        private TextView txtName, txtPrice,txtSize;
        private CardView btnAdd,btnDetail;
        private CheckBox cbAdd;

        private boolean isCheck = false;

        ShimmerFrameLayout shimmer;

        Handler handler = new Handler();


        public WishlistViewHoler(@NonNull View itemView) {
            super(itemView);

            swipeRevealLayout = itemView.findViewById(R.id.swipeLayout);
            layoutDelete = itemView.findViewById(R.id.swipDelete);
            btnAdd = itemView.findViewById(R.id.btnAddCart);
            cbAdd = itemView.findViewById(R.id.checkboxAdd);

            img = itemView.findViewById(R.id.imgP);
            txtName = itemView.findViewById(R.id.nameP);
            txtPrice = itemView.findViewById(R.id.priceP);
            txtSize = itemView.findViewById(R.id.activity_wishlist_size);
            btnDetail = itemView.findViewById(R.id.activity_wishlist_btnDetail);

//            shimmer = itemView.findViewById(R.id.shimmer);
//
//
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    shimmer.stopShimmer();
//                    shimmer.hideShimmer();
//                    shimmer.setVisibility(View.GONE);
//
//
//
//                    txtName.setVisibility(View.VISIBLE);
//                    txtPrice.setVisibility(View.VISIBLE);
//                    txtDes.setVisibility(View.VISIBLE);
//                    img.setVisibility(View.VISIBLE);
//                    cbAdd.setVisibility(View.VISIBLE);
//                    btnAdd.setVisibility(View.VISIBLE);
//
//                }
//            },5000);


        }
    }
    private void addCart(String idCustomer,String idDish,String idSize){
        String url = "https://eat-simple-app.000webhostapp.com/addCart.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("THEM_THANH_CONG")){
                            Toast.makeText(context, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Không thể thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA",error.toString());
                    }
                }){
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("ma_kh",idCustomer);
                params.put("ma_sp",idDish);
                params.put("ma_size",idSize);
                params.put("so_luong",1+"");
                return params;
            }
        };
        VolleyPool.getInstance(context).addRequest(stringRequest);
    }
    void detail_product(TranmissionData dish,Bitmap bitmap){

            Intent intent = new Intent(context, Activity_detail.class);

            Product product = new Product();
            product.setMa_sp(dish.getId());
            product.setTen_sp(dish.getName());
            product.setGia_km(dish.getPriceSale());
            product.setGia(dish.getPrice());
            product.setSo_luong_con_lai(dish.getNumberRest());
            product.setSo_luong_ban_ra(dish.getNumberSaled());

            product.setKcal(dish.getKcal());

            product.setThoi_gian_nau(dish.getTime());

            product.setThong_tin(dish.getInformation());

            product.setUrl(dish.getUrl());

            intent.putExtra("product",(Serializable) product );


            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            byte[] bytes = baos.toByteArray();

            intent.putExtra("bitmap",bytes);



            context.startActivity(intent);
    }
    public void deleleteWishlist1(String idCustomer,String idProduct,String idSize){
        String url = "http://eat-simple-app.000webhostapp.com/deleteWishlist.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")){
                            Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Đã xảy ra lỗi! Không thể xóa!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Loi xóa wishlist", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("ma_khach_hang", idCustomer);
                params.put("ma_san_pham", idProduct);
                params.put("ma_size", idSize);
                return params;
            }
        };
        VolleyPool.getInstance(context).addRequest(stringRequest);
    }



}
