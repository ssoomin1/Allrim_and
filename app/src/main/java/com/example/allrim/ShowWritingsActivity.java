package com.example.allrim;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ShowWritingsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;

    private TextView tv_nickname; // 닉네임 text
    private ImageView iv_profile; // 이미지 뷰

    private ListView listview;
    private ListViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_writings);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);

        navigationView.getMenu().getItem(0).setChecked(true); // 페이지별로 바꾸기

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            if (!menuItem.isChecked()) {
                Intent intent;
                switch (id) {
                    case R.id.navigation_item_info:
                        intent = new Intent(this, MyPageActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_writing:
                        intent = new Intent(this, MyWritingActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_schedule:
                        intent = new Intent(this, ScheduleActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_meal:
                        intent = new Intent(this, MealActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_lost:
                        intent = new Intent(this, LostActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_set:
                        intent = new Intent(this, SettingActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                }
            }
            return false;
        });

        String nickName = mAuth.getCurrentUser().getDisplayName(); // MainActivity로 부터 닉네임 전달받음
        Uri photoUrl = mAuth.getCurrentUser().getPhotoUrl(); // MainActivity로 부터 프로필사진 Url 전달받음

        iv_profile = headerView.findViewById(R.id.img_userImage);
        Glide.with(this).load(photoUrl).into(iv_profile); // 프로필 url을 이미지 뷰에 세팅

        tv_nickname = (TextView) headerView.findViewById(R.id.tv_userName);
        tv_nickname.setText(nickName); // 닉네임 text를 텍스트 뷰에 세팅

        headerView.findViewById(R.id.bt_logout).setOnClickListener(onClickListener);

        // 리스트뷰 객체 생성 및 Adapter 설정
        listview = (ListView) findViewById(R.id.writing_listview);

        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트 뷰 아이템 추가.
        adapter.addItem("익깅", "배고프다", "진짜배고픔");
        adapter.addItem("익깅2", "정처기어카지", "개망한듯");
        adapter.addItem("익깅3", "집가고싶다", "오늘집감");
        // 리스트 뷰 아이템 추가.
        adapter.addItem("익깅", "배고프다", "진짜배고픔");
        adapter.addItem("익깅2", "정처기어카지", "개망한듯");
        adapter.addItem("익깅3", "집가고싶다", "오늘집감");
        // 리스트 뷰 아이템 추가.
        adapter.addItem("익깅", "배고프다", "진짜배고픔");
        adapter.addItem("익깅2", "정처기어카지", "개망한듯");
        adapter.addItem("익깅3", "집가고싶다", "오늘집감");
        // 리스트 뷰 아이템 추가.
        adapter.addItem("익깅", "배고프다", "진짜배고픔");
        adapter.addItem("익깅2", "정처기어카지", "개망한듯");
        adapter.addItem("익깅3", "집가고싶다", "오늘집감");

        // 리스트뷰의 높이를 계산에서 layout 크기를 설정
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++){
            View listItem = adapter.getView(i, null, listview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = totalHeight + (listview.getDividerHeight() * (adapter.getCount() - 1));
        listview.setLayoutParams(params);


        listview.setAdapter(adapter);

        String community = getIntent().getStringExtra("community");

        TextView commnunity_name = findViewById(R.id.community_name);
        commnunity_name.setText(community);
    }

    // 버튼 클릭 부분
    View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.bt_logout:
                signOut();
                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignInClient googleApiClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build());
        googleApiClient.signOut();
    }
}