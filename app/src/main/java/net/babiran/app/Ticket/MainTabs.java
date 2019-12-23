package net.babiran.app.Ticket;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.babiran.app.R;
import net.babiran.app.Ticket.Page.Ghater;
import net.babiran.app.Ticket.Page.Otobos;
import net.babiran.app.Ticket.Page.ParvazDakhli;
import net.babiran.app.Ticket.Page.ParvazKhareji;

public class MainTabs extends AppCompatActivity {



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    SetMyFragment(new Otobos());
                    return true;
                case R.id.navigation_dashboard:
                    SetMyFragment(new Ghater());
                    return true;
                case R.id.navigation_notifications:
                    SetMyFragment(new ParvazDakhli());
                    return true;
                case R.id.navigation_notificationsd:
                    SetMyFragment(new ParvazKhareji());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        SetMyFragment(new Otobos());
    }


    private void SetMyFragment(Fragment f)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framf,f);
        fragmentTransaction.commit();

    }

}
