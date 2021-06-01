package com.vientamthuong.eatsimple.admin.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.vientamthuong.eatsimple.R;

public class ThongBaoNoiFragment extends Fragment {

    private TextView thong_bao_noi_mui_ten;
    private CardView thong_bao_noi_card_view;
    private boolean isShow;

    public ThongBaoNoiFragment(TextView textView, CardView cardView) {
        this.thong_bao_noi_mui_ten = textView;
        this.thong_bao_noi_card_view = cardView;
        isShow = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_thong_bao_noi, container, false);
        return view;
    }

    public void handleShowHide() {
        if (isShow) {
            isShow = false;
            thong_bao_noi_card_view.setVisibility(View.GONE);
            thong_bao_noi_mui_ten.setVisibility(View.GONE);
        } else {
            isShow = true;
            thong_bao_noi_card_view.setVisibility(View.VISIBLE);
            thong_bao_noi_mui_ten.setVisibility(View.VISIBLE);
        }
    }
}
