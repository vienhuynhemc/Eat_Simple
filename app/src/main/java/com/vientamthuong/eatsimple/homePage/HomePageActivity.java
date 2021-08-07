//package com.vientamthuong.eatsimple.homePage;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.vientamthuong.eatsimple.R;
//import com.vientamthuong.eatsimple.beans.Product;
//import com.vientamthuong.eatsimple.connection.CheckConnection;
//import com.vientamthuong.eatsimple.diaLog.DiaLogLoader;
//import com.vientamthuong.eatsimple.diaLog.DiaLogLostConnection;
//import com.vientamthuong.eatsimple.footer.FooterPublicFragment;
//import com.vientamthuong.eatsimple.header.HeaderPublicFragment;
//import com.vientamthuong.eatsimple.loadData.LoadDataConfiguration;
//import com.vientamthuong.eatsimple.loadData.LoadImageForView;
//import com.vientamthuong.eatsimple.loadProductByID.GetListProduct;
//import com.vientamthuong.eatsimple.loadProductByID.LoadProductConfiguration;
//import com.vientamthuong.eatsimple.loadProductByID.LoadProductHandler;
//import com.vientamthuong.eatsimple.loadProductByID.LoadProductHelp;
//import com.vientamthuong.eatsimple.loadProductByID.LoadProductViewAdapter;
//import com.vientamthuong.eatsimple.protocol.ActivityProtocol;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class HomePageActivity extends AppCompatActivity implements ActivityProtocol {
//
//    // Layout để xử lý refresh
//    private FrameLayout swipeRefreshLayout;
//    // Thời gian thoát activity
//    private long lastTimePressBack;
//    // Dialog
//    private DiaLogLostConnection diaLogLostConnection;
//    private DiaLogLoader diaLogLoader;
//    // Header
//    private HeaderPublicFragment headerPublicFragment;
//    // Footer
//    private FooterPublicFragment footerPublicFragment;
//    // Fragment danh mục
//    private HomePageDanhMucFragment homePageDanhMucFragment;
//    // List image cần tải hình
//    private List<LoadImageForView> imagesNeedLoad;
//    // Biến boolean để kiểm tra luồng volley có đang chạy hay chưa
//    private boolean isRunningVolley;
//
//    // bên load sản phẩm
//    private RecyclerView recyclerView;
//    private LoadProductViewAdapter loadProductViewAdapter;
//    private ArrayList<Product> productList;
//
//    private ScrollView scrollView;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Xóa status bar
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_home_page_main);
//        // Ánh xạ view
//        getView();
//        // Thêm dữ liệu
//        init();
//        // Hành động
//       // action();
//        // sự kiện load sản phẩm
//        event();
//        // event scroll
//        eventScroll();
//    }
//    private void event(){
//
//        LoadProductHandler handler = LoadProductHandler.getLoadPoductHandler();
//        handler.setProductList(productList);
//        handler.setLoadProductViewAdapter(loadProductViewAdapter);
//        handler.getHandler();
//
//    }
//    private void eventScroll(){
//
//
//        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//               System.out.println("OK ff" + scrollView.getTop());
//               System.out.println("Cuoii " + scrollY);
//               System.out.println("Đầu " + oldScrollY);
//                System.out.println("Zô chỗ này" + LoadProductHelp.getLoadProductHelp().getYMIN());
//                if (scrollY == LoadProductHelp.getLoadProductHelp().getYMIN()){
//                    LoadProductHelp.getLoadProductHelp().setKiem_tra_danh_muc_moi(false);
//                    LoadProductHelp.getLoadProductHelp().setNum(LoadProductHelp.getLoadProductHelp().getNum()+1);
//                    GetListProduct.getData(HomePageActivity.this);
//                    LoadProductHelp.getLoadProductHelp().setYMIN(LoadProductHelp.getLoadProductHelp().getYMIN() + 262);
//                }
//            }
//        });
//
//
//    }
//
////    private void action(){
////        // refresh
////        swipeRefreshLayout.setOnRefreshListener(() -> {
////           recreate();
////           swipeRefreshLayout.setRefreshing(false);
////        });
////    }
//
//    private void getView() {
//        swipeRefreshLayout = findViewById(R.id.activity_home_page_layout);
//        recyclerView = findViewById(R.id.list_sp);
//        productList = new ArrayList<>();
//        scrollView = findViewById(R.id.scroll_sp);
//    }
//
//    private void init() {
//        // Tạo dialog
//        initDialog();
//        // Tạo header
//        initHeader();
//        // Tạo footer
//        initFooter();
//        // Tạo fragment danh mục
//        initFragmentDanhMuc();
//        // Check connection
//        if (!CheckConnection.getInstance().isConnected(HomePageActivity.this)) {
//            diaLogLostConnection.show();
//        } else {
//            getData();
//        }
//        initProducts();
//    }
//    private void initProducts(){
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
//        linearLayoutManager.setSmoothScrollbarEnabled(true);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setHasFixedSize(true);
//        loadProductViewAdapter = new LoadProductViewAdapter(productList);
//        recyclerView.setAdapter(loadProductViewAdapter);
//        loadProductViewAdapter.notifyDataSetChanged();
//    }
//
//    private void initFooter() {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        footerPublicFragment = new FooterPublicFragment();
//        // Cho nó biết đang ở trang chủ
//        Bundle bundle = new Bundle();
//        bundle.putInt("data", HomePageConfiguration.HOME);
//        footerPublicFragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.activity_home_page_footer, footerPublicFragment, "footer");
//        fragmentTransaction.commit();
//    }
//
//    private void initHeader() {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        headerPublicFragment = new HeaderPublicFragment();
//        fragmentTransaction.replace(R.id.activity_home_page_header, headerPublicFragment, "header");
//        fragmentTransaction.commit();
//    }
//
//    private void initFragmentDanhMuc() {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        homePageDanhMucFragment = new HomePageDanhMucFragment();
//        fragmentTransaction.replace(R.id.activity_home_page_list_danh_muc, homePageDanhMucFragment, "home-page-danh-muc-fragment");
//        fragmentTransaction.commit();
//    }
//
//    private void getData() {
//        // Không mất kết nối thì lấy dữ liêu  fire base về của activity này
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference root = firebaseDatabase.getReference();
//        imagesNeedLoad = new ArrayList<>();
//        // Load dữ liệu header
//        headerPublicFragment.getData(root, diaLogLoader, imagesNeedLoad, HomePageActivity.this);
//        // Load danh mục fragment
//        homePageDanhMucFragment.getData(root,diaLogLoader,imagesNeedLoad,HomePageActivity.this);
//    }
//
//    @Override
//    public boolean isRunningVolley() {
//        return isRunningVolley;
//    }
//
//    @Override
//    public void setRunningVolley(boolean isRunningVolley) {
//        this.isRunningVolley = isRunningVolley;
//    }
//
//    @Override
//    public void loadImageFromIntenet() {
//        // Tải hình về
//        if (imagesNeedLoad.size() > 0) {
//            Thread thread = new Thread(() -> {
//                boolean isError = false;
//                do {
//                    int count = 0;
//                    while (count < imagesNeedLoad.size()) {
//                        LoadImageForView loadImageForView = imagesNeedLoad.get(count);
//                        if (loadImageForView!= null &&!loadImageForView.isStart()) {
//                            loadImageForView.setStart(true);
//                            loadImageForView.run();
//                            count++;
//                        } else if(loadImageForView!= null)  {
//                            if (loadImageForView.isComplete()) {
//                                // Kiểm tra nếu thằng xong này type là danh mục thì thôgn báo cho adpater danh mục
//                                if (imagesNeedLoad.get(count).getType() == LoadDataConfiguration.IMAGE_DANH_MUC) {
//                                    runOnUiThread(() -> homePageDanhMucFragment.update());
//                                }
//                                imagesNeedLoad.remove(count);
//                            } else if (loadImageForView.isError()) {
//                                isError = true;
//                                break;
//                            } else {
//                                count++;
//                            }
//                        }
//                    }
//                } while (!isError && imagesNeedLoad.size() != 0);
//                // Cho biến là hết chạy volley
//                isRunningVolley = false;
//                // Lỗi mạng
//                if (isError) {
//                    runOnUiThread(() -> {
//                        diaLogLostConnection.show();
//                        diaLogLostConnection.getBtTry().setOnClickListener(v -> {
//                            if (CheckConnection.getInstance().isConnected(HomePageActivity.this)) {
//                                diaLogLostConnection.dismiss();
//                                // Cho các đối tượng hiện tại trong list về trạng thái ban đầu
//                                for (LoadImageForView loadImageForView : imagesNeedLoad) {
//                                    if (loadImageForView.isStart()) {
//                                        loadImageForView.setStart(false);
//                                    }
//                                    if (loadImageForView.isError()) {
//                                        loadImageForView.setError(false);
//                                    }
//                                }
//                                if (!isRunningVolley) {
//                                    isRunningVolley = true;
//                                    loadImageFromIntenet();
//                                }
//                            } else {
//                                Toast.makeText(HomePageActivity.this, "Không tìm thấy kết nối!", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    });
//                }
//            });
//            thread.start();
//        }
//    }
//
//    private void initDialog() {
//        // lost connection
//        diaLogLostConnection = new DiaLogLostConnection(HomePageActivity.this);
//        diaLogLostConnection.getBtIgnore().setOnClickListener(v -> {
//            finish();
//            android.os.Process.killProcess(android.os.Process.myPid());
//        });
//        diaLogLostConnection.getBtTry().setOnClickListener(v -> {
//            if (CheckConnection.getInstance().isConnected(HomePageActivity.this)) {
//                diaLogLostConnection.dismiss();
//                getData();
//            } else {
//                Toast.makeText(HomePageActivity.this, "Không tìm thấy kết nối!", Toast.LENGTH_SHORT).show();
//            }
//        });
//        // loader
//        diaLogLoader = new DiaLogLoader(HomePageActivity.this);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (lastTimePressBack == 0 || System.currentTimeMillis() - lastTimePressBack > 2000) {
//            lastTimePressBack = System.currentTimeMillis();
//            Toast.makeText(HomePageActivity.this, getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
//        } else {
//            finish();
//        }
//    }
//
//
//}


package com.vientamthuong.eatsimple.homePage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vientamthuong.eatsimple.R;
import com.vientamthuong.eatsimple.SharedReferences.DataLocalManager;
import com.vientamthuong.eatsimple.beans.Product;
import com.vientamthuong.eatsimple.connection.CheckConnection;
import com.vientamthuong.eatsimple.diaLog.DiaLogLoader;
import com.vientamthuong.eatsimple.diaLog.DiaLogLostConnection;
import com.vientamthuong.eatsimple.footer.FooterPublicFragment;
import com.vientamthuong.eatsimple.header.HeaderPublicFragment;
import com.vientamthuong.eatsimple.loadData.LoadDataConfiguration;
import com.vientamthuong.eatsimple.loadData.LoadImageForView;
import com.vientamthuong.eatsimple.loadProductByID.GetListProduct;
import com.vientamthuong.eatsimple.loadProductByID.LoadProductConfiguration;
import com.vientamthuong.eatsimple.loadProductByID.LoadProductHandler;
import com.vientamthuong.eatsimple.loadProductByID.LoadProductHelp;
import com.vientamthuong.eatsimple.loadProductByID.LoadProductViewAdapter;
import com.vientamthuong.eatsimple.protocol.ActivityProtocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomePageActivity extends Fragment  {

    // Layout để xử lý refresh
    private FrameLayout swipeRefreshLayout;

    // Dialog
    private DiaLogLostConnection diaLogLostConnection;
    private DiaLogLoader diaLogLoader;
    // Header
    private HeaderPublicFragment headerPublicFragment;
    // Footer
//    private FooterPublicFragment footerPublicFragment;
    // Fragment danh mục
    private HomePageDanhMucFragment homePageDanhMucFragment;
    // List image cần tải hình
    private List<LoadImageForView> imagesNeedLoad;

    // bên load sản phẩm
    private RecyclerView recyclerView;
    private LoadProductViewAdapter loadProductViewAdapter;
    private ArrayList<Product> productList;
    private HomeMeowBottom homeMeowBottom;

    private ScrollView scrollView;
    private static HomePageActivity homePageActivity;

    private AppCompatActivity appCompatActivity;
    private EditText editText;
//
//    private HomePageActivity(){
//
//    }
//    public static HomePageActivity getInstance(){
//
//        if (homePageActivity == null){
//            homePageActivity = new HomePageActivity();
//        }
//        return homePageActivity;
//
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_page_main, container, false);

        getView(view);

        // Xử lý giao diện

     //   getView();
        // Thêm dữ liệu
        init();
        // Hành động
        // action();
        // sự kiện load sản phẩm
        event();
        // event scroll
        eventScroll();


        return view;
    }
    private void event(){

        LoadProductHandler handler = LoadProductHandler.getLoadPoductHandler();
        handler.setProductList(productList);
        handler.setLoadProductViewAdapter(loadProductViewAdapter);
        handler.getHandler();


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (productList.size()!=0){
                    String edit = editText.getText().toString().trim();
                    System.out.println("on seatch " + edit);
                    loadProductViewAdapter.getFilter().filter(edit);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void eventScroll(){


        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {

            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                System.out.println("OK ff" + scrollView.getTop());
                System.out.println("Cuoii " + scrollY);
                System.out.println("Đầu " + oldScrollY);
                System.out.println("Zô chỗ này" + LoadProductHelp.getLoadProductHelp().getYMIN());
                if ((scrollY >= LoadProductHelp.getLoadProductHelp().getYMIN())){
                    LoadProductHelp.getLoadProductHelp().setKiem_tra_danh_muc_moi(false);
                    LoadProductHelp.getLoadProductHelp().setNum(LoadProductHelp.getLoadProductHelp().getNum()+1);
                    GetListProduct.getData(getContext());
                    LoadProductHelp.getLoadProductHelp().setYMIN(LoadProductHelp.getLoadProductHelp().getYMIN() + 262);
                }
            }
        });


    }

//    private void action(){
//        // refresh
//        swipeRefreshLayout.setOnRefreshListener(() -> {
//           recreate();
//           swipeRefreshLayout.setRefreshing(false);
//        });
//    }

     void getView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.activity_home_page_layout);
        recyclerView = view.findViewById(R.id.list_sp);
        productList = new ArrayList<>();
        scrollView = view.findViewById(R.id.scroll_sp);
        editText = view.findViewById(R.id.search);
    }

    public void init() {
        // Tạo dialog
        initDialog();
        // Tạo header
        initHeader();
        // Tạo footer

        // Tạo fragment danh mục
        initFragmentDanhMuc();
        // Check connection

        if (!CheckConnection.getInstance().isConnected(appCompatActivity)) {
            diaLogLostConnection.show();

        } else {
                getData();
        }

        initProducts();
    }
    public void initProducts(){
       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(appCompatActivity);
        GridLayoutManager layoutManager = new GridLayoutManager(appCompatActivity,2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        loadProductViewAdapter = new LoadProductViewAdapter(productList);
        recyclerView.setAdapter(loadProductViewAdapter);
        loadProductViewAdapter.notifyDataSetChanged();
    }

//    private void initFooter() {
//        FragmentTransaction fragmentTransaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
//        footerPublicFragment = new FooterPublicFragment();
//        // Cho nó biết đang ở trang chủ
//        Bundle bundle = new Bundle();
//        bundle.putInt("data", HomePageConfiguration.HOME);
//        footerPublicFragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.activity_home_page_footer, footerPublicFragment, "footer");
//        fragmentTransaction.commit();
//    }

    public void initDialog() {
        // lost connection
        diaLogLostConnection = new DiaLogLostConnection(appCompatActivity);
        diaLogLostConnection.getBtIgnore().setOnClickListener(v -> {
            appCompatActivity.finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        });
        diaLogLostConnection.getBtTry().setOnClickListener(v -> {
            if (CheckConnection.getInstance().isConnected(appCompatActivity)) {
                diaLogLostConnection.dismiss();
                getData();
            } else {
                Toast.makeText(getContext(), "Không tìm thấy kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
        // loader
        diaLogLoader = new DiaLogLoader(getContext());
    }

    private void initHeader() {
        FragmentTransaction fragmentTransaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
        headerPublicFragment = new HeaderPublicFragment();
        fragmentTransaction.replace(R.id.activity_home_page_header, headerPublicFragment, "header");
        fragmentTransaction.commit();
    }

    public void getData() {
        // Không mất kết nối thì lấy dữ liêu  fire base về của activity này
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();
        imagesNeedLoad = new ArrayList<>();
        homeMeowBottom.setImagesNeedLoad(imagesNeedLoad);
        // Load dữ liệu header
        headerPublicFragment.getData(root, diaLogLoader, imagesNeedLoad, appCompatActivity);
        // Load danh mục fragment
        homePageDanhMucFragment.getData(root,diaLogLoader,imagesNeedLoad,appCompatActivity);
    }
    public void initFragmentDanhMuc() {
        FragmentTransaction fragmentTransaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
        homePageDanhMucFragment = new HomePageDanhMucFragment();
        homeMeowBottom.setHomePageDanhMucFragment(homePageDanhMucFragment);
        fragmentTransaction.replace(R.id.activity_home_page_list_danh_muc, homePageDanhMucFragment, "home-page-danh-muc-fragment");
        fragmentTransaction.commit();
    }





    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public DiaLogLostConnection getDiaLogLostConnection() {
        return diaLogLostConnection;
    }

    public void setDiaLogLostConnection(DiaLogLostConnection diaLogLostConnection) {
        this.diaLogLostConnection = diaLogLostConnection;
    }

    public DiaLogLoader getDiaLogLoader() {
        return diaLogLoader;
    }

    public void setDiaLogLoader(DiaLogLoader diaLogLoader) {
        this.diaLogLoader = diaLogLoader;
    }




    public HomePageDanhMucFragment getHomePageDanhMucFragment() {
        return homePageDanhMucFragment;
    }

    public void setHomePageDanhMucFragment(HomePageDanhMucFragment homePageDanhMucFragment) {
        this.homePageDanhMucFragment = homePageDanhMucFragment;
    }

    public List<LoadImageForView> getImagesNeedLoad() {
        return imagesNeedLoad;
    }

    public void setImagesNeedLoad(List<LoadImageForView> imagesNeedLoad) {
        this.imagesNeedLoad = imagesNeedLoad;
    }

    public void setHomeMeowBottom(HomeMeowBottom homeMeowBottom) {
        this.homeMeowBottom = homeMeowBottom;
    }
}
