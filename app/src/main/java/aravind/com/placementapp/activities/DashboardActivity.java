package aravind.com.placementapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import aravind.com.placementapp.R;
import aravind.com.placementapp.constants.Constants;
import aravind.com.placementapp.fragments.admin.AddStudentFragment;
import aravind.com.placementapp.fragments.admin.AddTPOFragment;
import aravind.com.placementapp.helper.SharedPrefHelper;
import aravind.com.placementapp.fragments.admin.ViewTPOFragment;
import aravind.com.placementapp.fragments.PlacementDashboardFragment;
import aravind.com.placementapp.fragments.admin.ViewStudentsFragment;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);

            int userType = Integer.parseInt(SharedPrefHelper.getEntryFromSharedPrefs(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_USER_TYPE));
            if (userType == Constants.UserTypes.USER_TYPE_ADMIN) {
                navigationView.getMenu().removeGroup(R.id.tpoGroup);
            }
            if (userType == Constants.UserTypes.USER_TYPE_TPO) {
                navigationView.getMenu().removeGroup(R.id.adminGroup);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOutFromApp() {
        if (SharedPrefHelper.clearEntriesInSharedPrefs(this.getApplicationContext())) {
            Toast.makeText(this, Constants.LOGGED_OUT, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_placementdashboard:
            case R.id.admin_viewtpos:
            case R.id.admin_viewstudents:
            case R.id.admin_addtpo:
            case R.id.admin_addstudent:
                displaySelectedFragment(id);
                break;
            case R.id.logout:
                logOutFromApp();
                break;
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return false;
    }

    private void displaySelectedFragment(int itemId) {
        Fragment fragment = null;

        switch (itemId) {
            case R.id.nav_placementdashboard: {
                fragment = new PlacementDashboardFragment();
                break;
            }

            case R.id.admin_viewtpos: {
                fragment = new ViewTPOFragment();
                break;
            }

            case R.id.admin_viewstudents: {
                fragment = new ViewStudentsFragment();
                break;
            }

            case R.id.admin_addtpo: {
                fragment = new AddTPOFragment();
                break;
            }

            case R.id.admin_addstudent: {
                fragment = new AddStudentFragment();
                break;
            }
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
            fragmentTransaction.commit();
        }
    }
}
