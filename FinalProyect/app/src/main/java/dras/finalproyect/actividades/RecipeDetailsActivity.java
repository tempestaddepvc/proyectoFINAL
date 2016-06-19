package dras.finalproyect.actividades;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.fragmentos.RecipeDetail1Fragment;
import dras.finalproyect.fragmentos.RecipeDetail2Fragment;
import dras.finalproyect.fragmentos.RecipeEdit1Fragment;
import dras.finalproyect.pojos.Recipe;
import dras.finalproyect.pojos.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailsActivity extends AppCompatActivity {


    private static final int EDITAR_RECIPE = 3;
    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;


    public static void start(Activity a) {
        Intent intent = new Intent(a, RecipeDetailsActivity.class);
        a.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case  R.id.Editar:
                RecipeEditActivity.startForResult(this, EDITAR_RECIPE);
                return true;
            case  R.id.Favoritos:

                return true;
            case  R.id.UnFavoritos:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        RecipeDetail1Fragment frgDetails;
        RecipeDetail2Fragment frgMaking;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (frgDetails == null)
                        frgDetails = RecipeDetail1Fragment.newInstance();
                    return frgDetails;
                case 1:
                    if (frgMaking == null)
                        frgMaking = RecipeDetail2Fragment.newInstance();
                    return frgMaking;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Detalles";
                case 1:
                    return "Preparacion";
            }
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ((RecipeDetail1Fragment) mSectionsPagerAdapter.getItem(0)).cargarDatos();
        }
    }
}
