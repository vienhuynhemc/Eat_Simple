package com.vientamthuong.eatsimple.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.vientamthuong.eatsimple.R;
import com.vientamthuong.eatsimple.admin.HomePageActivity;
import com.vientamthuong.eatsimple.admin.session.DataSession;
import com.vientamthuong.eatsimple.jbCrypt.BCrypt;
import com.vientamthuong.eatsimple.loadData.VolleyPool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginTabFragment extends Fragment {

    EditText username;
    View forgotPass;
    Button login;
    EditText pass;
    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_login_tab_fragment, container, false);

        username = root.findViewById(R.id.username_login);
        pass = root.findViewById(R.id.password_login);
        forgotPass = root.findViewById(R.id.forgetPassword_login);
        login = root.findViewById(R.id.btn_login);

        // Test để làm admin
        login.setOnClickListener(v1 -> {
            String tai_khoan = username.getText().toString().trim();
            String mat_khau = pass.getText().toString().trim();
            Map<String, String> result = new LinkedHashMap<>();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    "http://eat-simple-app.000webhostapp.com/lay_tai_khoan.php",
                    response -> {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                result.put("mat_khau", jsonObject.getString("mat_khau"));
                                result.put("ma_tai_khoan", jsonObject.getString("ma_tai_khoan"));
                                result.put("cap_do", jsonObject.getString("cap_do"));
                            }
                            if (result.size() == 0) {
                                System.out.println("Sai tai khoan");
                            } else {
                                if (BCrypt.checkpw(mat_khau, result.get("mat_khau"))) {
                                    System.out.println("dang nhap thanh cong");
                                    System.out.println(result);
                                    DataSession.getInstance().setMaTaiKhoan(result.get("ma_tai_khoan"));
                                    DataSession.getInstance().setCap_do(Integer.parseInt(result.get("cap_do")));
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), HomePageActivity.class);
                                    getActivity().finish();
                                    startActivity(intent);
                                } else {
                                    System.out.println("Sai mat khau");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("tai_khoan", tai_khoan);
                    return params;
                }
            };
            VolleyPool.getInstance(getActivity()).addRequest(stringRequest);
        });

        // hiển thị username khi đăng ký thành công!
        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("username_signup") != null){
            username.setText(intent.getStringExtra("username_signup"));
        }



        /////////////////////

//        email.setTranslationX(0);
//        pass.setTranslationX(0);
//        forgotPass.setTranslationX(0);
//        login.setTranslationX(0);
//
//        email.setAlpha(v);
//        pass.setAlpha(v);
//        forgotPass.setAlpha(v);
//        login.setAlpha(v);
//
//        email.animate().translationY(100).alpha(1).setDuration(800).setStartDelay(300).start();
//        pass.animate().translationY(100).alpha(1).setDuration(800).setStartDelay(500).start();
//        forgotPass.animate().translationY(100).alpha(1).setDuration(800).setStartDelay(500).start();
//        login.animate().translationY(100).alpha(1).setDuration(800).setStartDelay(700).start();


        return root;
    }
}
