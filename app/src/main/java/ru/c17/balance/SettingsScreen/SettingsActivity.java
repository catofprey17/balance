package ru.c17.balance.SettingsScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import ru.c17.balance.R;

public class SettingsActivity extends AppCompatActivity{

    public static final String EXTRA_PREF = "extra-setting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTheme(R.style.SettingsTheme);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = new Bundle();

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_PREF)) {
            bundle.putInt(EXTRA_PREF, intent.getIntExtra(EXTRA_PREF, -1));
        }



        MainPreferenceFragment fragment = new MainPreferenceFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_preferences, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
