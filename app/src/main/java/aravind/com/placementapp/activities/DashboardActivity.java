package aravind.com.placementapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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
import aravind.com.placementapp.fragments.PlacementDashboardFragment;
import aravind.com.placementapp.fragments.admin.AddStudentFragment;
import aravind.com.placementapp.fragments.admin.AddTPOFragment;
import aravind.com.placementapp.fragments.admin.ViewStudentsFragment;
import aravind.com.placementapp.fragments.admin.ViewTPOFragment;
import aravind.com.placementapp.fragments.student.student.ViewPapersFragment;
import aravind.com.placementapp.fragments.tpo.tpo.AddCompanyFragment;
import aravind.com.placementapp.fragments.tpo.tpo.AddPapers;
import aravind.com.placementapp.fragments.tpo.tpo.SendNotificationsFragment;
import aravind.com.placementapp.fragments.tpo.tpo.ViewCompanyFragment;
import aravind.com.placementapp.helper.SharedPrefHelper;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private String userName;
    private TextView userNameView;
    private Toolbar toolbar;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);

            int userType = Integer.parseInt(SharedPrefHelper.getEntryFromSharedPrefs(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_USER_TYPE));
            setUserNameToHeader(navigationView);

            if (userType == Constants.UserTypes.USER_TYPE_ADMIN) {
                navigationView.getMenu().removeGroup(R.id.tpoGroup);
                navigationView.getMenu().removeGroup(R.id.studentGroup);
            }
            if (userType == Constants.UserTypes.USER_TYPE_TPO) {
                navigationView.getMenu().removeGroup(R.id.adminGroup);
                navigationView.getMenu().removeGroup(R.id.studentGroup);
            }
            if (userType == Constants.UserTypes.USER_TYPE_STUDENT) {
                navigationView.getMenu().removeGroup(R.id.adminGroup);
                navigationView.getMenu().removeGroup(R.id.tpoGroup);
            }
        }
    }

    private void setUserNameToHeader(NavigationView navigationView) {
        if (navigationView != null) {
            View navHeaderView = navigationView.getHeaderView(0);
            userNameView = (TextView) navHeaderView.findViewById(R.id.nav_header_titleId);
            if (userNameView != null) {
                userName = SharedPrefHelper.getEntryFromSharedPrefs(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_USER_NAME);
                userNameView.setText("Hi, " + userName);
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
        if (item.getItemId() == R.id.viewProfile) {
            Intent intent = new Intent(DashboardActivity.this, ViewProfileActivity.class);
            startActivity(intent);
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

        drawer = findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }

        switch (id) {
            case R.id.nav_placementdashboard:
            case R.id.admin_viewtpos:
            case R.id.admin_viewstudents:
            case R.id.admin_addtpo:
            case R.id.admin_addstudent:
            case R.id.tpo_addCompany:
            case R.id.tpo_viewCompany:
            case R.id.tpo_sendNotifications:
            case R.id.tpo_addPapers:
            case R.id.student_viewCompany:
            case R.id.student_viewPapers:
            case R.id.tpo_addStudents:
                displaySelectedFragment(id);
                break;
            case R.id.logout:
                logOutFromApp();
                break;
        }
        return false;
    }

    private void displaySelectedFragment(int itemId) {
        Fragment fragment = null;

        switch (itemId) {
            case R.id.nav_placementdashboard: {
                toolbar.setTitle("Placement Dashboard");
                fragment = new PlacementDashboardFragment();
                break;
            }

            case R.id.student_viewCompany: {
            }

            case R.id.tpo_viewCompany: {
                toolbar.setTitle("View Company");
                fragment = new ViewCompanyFragment();
                break;
            }

            case R.id.student_viewPapers: {
                toolbar.setTitle("View Papers");
                fragment = new ViewPapersFragment();
                break;
            }

            case R.id.admin_viewtpos: {
                toolbar.setTitle("View TPOs");
                fragment = new ViewTPOFragment();
                break;
            }

            case R.id.admin_viewstudents: {
                toolbar.setTitle("View Students");
                fragment = new ViewStudentsFragment();
                break;
            }

            case R.id.admin_addtpo: {
                toolbar.setTitle("Add TPO");
                fragment = new AddTPOFragment();
                break;
            }

            case R.id.admin_addstudent: {
                toolbar.setTitle("Add Student");
                fragment = new AddStudentFragment();
                break;
            }

            case R.id.tpo_addCompany: {
                toolbar.setTitle("Add Company");
                fragment = new AddCompanyFragment();
                break;
            }

            case R.id.tpo_sendNotifications: {
                toolbar.setTitle("Send Notifications");
                fragment = new SendNotificationsFragment();
                break;
            }

            case R.id.tpo_addPapers: {
                toolbar.setTitle("Add Previous Papers");
                fragment = new AddPapers();
                break;
            }

            case R.id.tpo_addStudents: {
                toolbar.setTitle("Add Selected Students");
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
