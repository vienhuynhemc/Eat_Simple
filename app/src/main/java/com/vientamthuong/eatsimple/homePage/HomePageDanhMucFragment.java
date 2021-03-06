package com.vientamthuong.eatsimple.homePage;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.vientamthuong.eatsimple.R;
import com.vientamthuong.eatsimple.model.DanhMuc;
import com.vientamthuong.eatsimple.diaLog.DiaLogLoader;
import com.vientamthuong.eatsimple.loadData.LoadDataConfiguration;
import com.vientamthuong.eatsimple.loadData.LoadImageForView;
import com.vientamthuong.eatsimple.protocol.ActivityProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomePageDanhMucFragment extends Fragment {

    // List danh muc
    private List<DanhMuc> danhMucs;
    private RecyclerView recyclerViewDanhMuc;
    private CustomDanhMucAdapter customDanhMucAdapter;
//    private static HomePageDanhMucFragment homePageDanhMucFragment;
//
//    private HomePageDanhMucFragment(){}
//    public static HomePageDanhMucFragment getInstance(){
//        if (homePageDanhMucFragment == null){
//            homePageDanhMucFragment = new HomePageDanhMucFragment();
//        }
//        return homePageDanhMucFragment;
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homme_page_danh_muc, container, false);
        getView(view);
        init();
        return view;
    }

    public void update() {
        customDanhMucAdapter.notifyDataSetChanged();
    }

    public void getData(DatabaseReference root, DiaLogLoader diaLogLoader, List<LoadImageForView> imagesNeedLoad, AppCompatActivity appCompatActivity) {
        // Lay activity protocol
        ActivityProtocol activityProtocol = (ActivityProtocol) appCompatActivity;
        // load
        DatabaseReference databaseReferenceDanhMuc = root.child("danh_muc");
        databaseReferenceDanhMuc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Hi???n m??n h??nh ch???
                diaLogLoader.show();
                // L??m cho 4 th???ng ?????u ti???n full null
                danhMucs.clear();
                for (int i = 0; i < 4; i++) {
                    danhMucs.add(new DanhMuc(null, null, null));
                }
                customDanhMucAdapter.notifyDataSetChanged();
                // Bi???n count ????? bi???t c?? bao nhi??u th???ng
                int countDanhMuc = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int ton_tai = Integer.parseInt(dataSnapshot.child("ton_tai").getValue().toString());
                    if (ton_tai == 0) {
                        countDanhMuc++;
                        DanhMuc danhMuc = new DanhMuc(dataSnapshot.getKey());
                        danhMuc.setHinh(Objects.requireNonNull(dataSnapshot.child("hinh").getValue()).toString());
                        danhMuc.setTen_danh_muc(Objects.requireNonNull(dataSnapshot.child("ten").getValue()).toString());
                        // N???u nh?? ch??a l???n h??n th?? thay v?? add v??o ta thay ?????i thu???c t??nh c???a n??
                        if (countDanhMuc < 5) {
                            danhMucs.get(countDanhMuc - 1).setTen_danh_muc(danhMuc.getTen_danh_muc());
                            danhMucs.get(countDanhMuc - 1).setHinh(danhMuc.getHinh());
                            danhMucs.get(countDanhMuc - 1).setMa_danh_muc(danhMuc.getMa_danh_muc());
                        } else {
                            danhMucs.add(danhMuc);
                        }
                        customDanhMucAdapter.notifyDataSetChanged();
                    }
                }
                // N???u nh?? s??? danh m???c < 4 (m???c ?????nh )th?? x??a b???t
                if (countDanhMuc < 4) {
                    int count = 0;
                    while (count < danhMucs.size()) {
                        if (!danhMucs.get(count).isLoaded()) {
                            danhMucs.remove(count);
                        } else {
                            count++;
                        }
                    }
                }
                // T???i d??? li???u t??? firebase v??? th??nh c??ng
                // ????a v?? imageNeedLoad
                System.out.println("DANH MUC: " + danhMucs.size() + activityProtocol.toString());
                for (DanhMuc danhMuc : danhMucs) {
                    System.out.println("DANH MUC: " + 100);
                    imagesNeedLoad.add(new LoadImageForView(appCompatActivity, danhMuc, LoadDataConfiguration.IMAGE_DANH_MUC));
                }
                // V?? gi??? t???i h??nh t??? c??c link h??nh
                if (!activityProtocol.isRunningVolley()) {
                    System.out.println("ZO LOAD HINH");
                    activityProtocol.setRunningVolley(true);
                    activityProtocol.loadImageFromIntenet();
                }
                // T???t m??n h??nh ch???
                diaLogLoader.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(appCompatActivity, "L???i t???i d??? li???u t??? firebase !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        danhMucs = new ArrayList<>();
        // Layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerViewDanhMuc.setLayoutManager(linearLayoutManager);
        recyclerViewDanhMuc.setHasFixedSize(true);
        // adapter
        // Resource
        int[] resources = {R.layout.activity_home_page_custom_danh_muc_first,
                R.layout.activity_home_page_custom_danh_muc,
                R.layout.activity_home_page_custom_danh_muc_last};
        // T???o 4 object loader ban ?????u
        // Cho full tam s??? l?? null
        // Khi n???p d??? li???u t??? fire base th?? tr???i qua c??c b?????c
        // 1. Thay v?? clear ta x??a h???t ????? l???i 4 th???ng ?????u xong cho full thu???c t??nh n?? l?? null
        for (int i = 0; i < 4; i++) {
            danhMucs.add(new DanhMuc(null, null, null));
        }
        // 2. Sau ???? c??? c?? d??? li???u th?? l???n l?????t thay th??? 4 ??ng n??y , n???u nh?? c?? ??t h??n 4 th?? ta x??a
        // Ng?????c l???i nhi???u h??n 4 th?? th??m v??o
        // Nh??ng ??? tr?????ng h???p thi???u khi x??a ??i th?? l??c n???p v??o ta v???n ph???i l??m sao ????? c?? ???????c 4 th???ng
        customDanhMucAdapter = new CustomDanhMucAdapter(resources, danhMucs);
        recyclerViewDanhMuc.setAdapter(customDanhMucAdapter);
        customDanhMucAdapter.notifyDataSetChanged();
    }

    private void getView(View view) {
        // recyclerview danh m???c
        recyclerViewDanhMuc = view.findViewById(R.id.activity_home_page_list_danh_muc);
    }
    public void setHander(Handler hander){
        customDanhMucAdapter.setHandler(hander);
    }

}
