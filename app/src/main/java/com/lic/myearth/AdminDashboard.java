package com.lic.myearth;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AdminDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView imageView;
    ListView listView;
    GridLayout mainGrid;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    private View navHeader;
    NavigationView navigationView;
    String urladdress="http://192.168.137.138/earthrecycler/menu.php";
    String deleteUrl = "http://192.168.137.138/earthrecycler/delete.php";
    String[] username;
    String[] uid;
    String[] designation;
    String[] image_path;
    String[] email;
    BufferedInputStream is;
    String line=null;
    String result=null;
    RadioGroup radioGroup;
    RadioButton selection;
    TextView userid, userdesignation, textViewId, textViewUsername, textViewEmail, textViewType, textViewDesignation, textViewGender, textViewImagepath;
    CustomListView1 customListView;
    public static ArrayList<CustomListView> models = new ArrayList<>();
    ImageView myOfferImageView, myOfferImageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        myOfferImageView1 = (ImageView) navHeader.findViewById(R.id.myOfferImage1);
        userid = (TextView) navHeader.findViewById(R.id.textViewEmail);
        userdesignation = (TextView) navHeader.findViewById(R.id.textViewDesignation);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        addMenuItemInNavMenuDrawer();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginPage.class));
        }

        textViewId = (TextView) findViewById(R.id.textViewId);
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewGender = (TextView) findViewById(R.id.textViewGender);
        textViewDesignation = (TextView) findViewById(R.id.textViewDesignation);
        textViewType = (TextView) findViewById(R.id.textViewType);
        textViewImagepath = (TextView) findViewById(R.id.textViewImagepath);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewId.setText(String.valueOf(user.getId()));
        textViewUsername.setText(user.getUsername());
        textViewEmail.setText(user.getEmail());
        textViewGender.setText(user.getGender());
        textViewDesignation.setText(user.getDesignation());
        textViewImagepath.setText(user.getImage_path());
        userid.setText(user.getEmail());
        userdesignation.setText(user.getDesignation());

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);






        myOfferImageView = findViewById(R.id.myOfferImage);

        Glide.with(this).load(user.getImage_path())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .circleCrop()
                .into(myOfferImageView);

        Glide.with(this).load(user.getImage_path())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .circleCrop()
                .into(myOfferImageView1);

    }



    private void addMenuItemInNavMenuDrawer() {
        try{

            URL url=new URL(urladdress);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            is=new BufferedInputStream(con.getInputStream());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        //content
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();
            while ((line=br.readLine())!=null){
                sb.append(line+"\n");
            }
            is.close();
            result=sb.toString();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }

//JSON
        try{
            JSONArray ja=new JSONArray(result);
            JSONObject jo=null;
            uid=new String[ja.length()];
            image_path=new String[ja.length()];

            username=new String[ja.length()];



            for(int i=0;i<=ja.length();i++){
                jo=ja.getJSONObject(i);
                uid[i]=jo.getString("id");
                image_path[i]=jo.getString("items");
                username[i]=jo.getString("designation");





            }
        }
        catch (Exception ex)
        {

            ex.printStackTrace();
        }



    }

    public void openActivity3(View view) {
        Intent intent = new Intent(this, EmployeeRegister.class);
        startActivity(intent);
    }

    public void openActivity5(View view) {
        Intent intent = new Intent(this, DriversDetails.class);
        startActivity(intent);
    }
    public void openActivity6(View view) {
        Intent intent = new Intent(this, EmployeeRegister.class);
        startActivity(intent);
    }
    public void openActivity4(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));


                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                    }
                }
            });
        }
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                }
            });
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    public void btn_add_activity(View view) {
        startActivity(new Intent(getApplicationContext(),EmployeeRegister.class));
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        int id=item.getItemId();

        if (id==R.id.nav_Home) {
//            fragmentManager=getSupportFragmentManager();
//            fragmentTransaction=fragmentManager.beginTransaction();fragmentTransaction.replace(R.id.content_fragment,new AdminFragment());
//            fragmentTransaction.commit();
            startActivity(new Intent(AdminDashboard.this,HomeAdmin.class));
            finish();
        }
        if (id==R.id.nav_add_client) {
//            fragmentManager=getSupportFragmentManager();
//            fragmentTransaction=fragmentManager.beginTransaction();fragmentTransaction.replace(R.id.content_fragment,new AddClientFragment());
//            fragmentTransaction.commit();
            startActivity(new Intent(AdminDashboard.this,Clients.class));
            finish();
        }
        if (id == R.id.nav_attendance) {
            startActivity(new Intent(AdminDashboard.this,Attendance.class));
            finish();
        }

        if (id == R.id.nav_logout) {


            SharedPrefManager.getInstance(getApplicationContext()).logout();
            finish();
        }
        if (id == R.id.nav_truck_schedule) {
            startActivity(new Intent(AdminDashboard.this,TruckScheduling.class));
            finish();
        }
        if (id == R.id.nav_expense_update) {
            startActivity(new Intent(AdminDashboard.this,ExpenseUpdateDetail.class));
            finish();
        }
        return true;
    }

}