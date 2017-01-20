package app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import dev.hugo.friendlyfood.R;
import dev.hugo.friendlyfood.UI.login.LoginActivity;

import static dev.hugo.friendlyfood.R.id.toolbar;

public class MainActivity extends AppCompatActivity {


    private final static String TAG = "MAINACTIVITY";
    private final static String PREF_TAG = "FF";

    private String user_id, user_name, user_email, user_password;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TabLayout tl;
    private Toolbar toolbar;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        initDrawer();


        //setUpViewPager();

    }

    private void saveUser(){
        SharedPreferences.Editor editor = getSharedPreferences(PREF_TAG, MODE_PRIVATE).edit();
        editor.putString("user_name", user_name);
        editor.putString("user_id", user_id);
        editor.putString("user_email", user_email);
        //editor.putString("user_password", user_password);
        editor.commit();
    }


    private void initDrawer(){
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.messenger_button_blue_bg_round)
                .addProfiles(
                        new ProfileDrawerItem().withName(mAuth.getCurrentUser().getDisplayName()).withEmail(mAuth.getCurrentUser().getEmail()).withIcon(mAuth.getCurrentUser().getPhotoUrl())
                )
                .withDividerBelowHeader(true)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        showLogoutDialog();
                        return false;
                    }
                })
                .build();

        final Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Ranking Principal").withIcon(R.drawable.success_circle),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("About").withIcon(R.drawable.red_button_background),
                        new SecondaryDrawerItem().withName("Logout").withIcon(R.drawable.warning_circle)
                ).build();

        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch (position){
                    case 1:
                        break;
                    case 3: showAboutDialog();
                        break;
                    case 4: showLogoutDialog();
                        break;
                }

                drawer.closeDrawer();
                return true;
            }
        });
    }

    private void showAboutDialog(){
        Toast.makeText(this.getApplicationContext(), "show about dialog", Toast.LENGTH_LONG).show();
    }


    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Do you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                logout();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logout(){
        mAuth.signOut();
        LoginManager.getInstance().logOut();

        user_name = null;
        user_email = null;
        user_id = null;
        user_password = null;
        saveUser();
    }

    private boolean getUser(){
        SharedPreferences prefs = getSharedPreferences(PREF_TAG, MODE_PRIVATE);
        user_name = prefs.getString("user_name", null);
        user_id = prefs.getString("user_id", null);
        user_email = prefs.getString("user_email", null);
        user_password = prefs.getString("user_password", null);

        if(user_name!=null && user_id!=null && user_email!=null && user_password!=null)
            return true;

        return false;
    }

}
