package dras.finalproyect.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.dialogos.FotoDialogFragment;
import dras.finalproyect.fragmentos.RecipeEdit1Fragment;
import dras.finalproyect.fragmentos.RecipeEdit2Fragment;
import dras.finalproyect.pojos.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeEditActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private App.APIinterface servicio;
    private Callback<Respuesta> callback;


    public static void startForResult(Activity a, int requestCode) {
        Intent intent = new Intent(a, RecipeEditActivity.class);
        a.startActivityForResult(intent, requestCode);
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

        servicio = App.getServicio();
        configRespuestas();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.save) {
            if (App.mRecipeActual.getIdrecipe() == 0)
                crearRecipe();
            else
                guardarRecipe();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void guardarRecipe() {
        mSectionsPagerAdapter.frgDetails.save();
//Falta el update en la base de datos
    }




    private void crearRecipe() {
        mSectionsPagerAdapter.frgDetails.save();
        //mSectionsPagerAdapter.frgMaking.save();
        Gson gson = new Gson();
        String a = gson.toJson(App.mRecipeActual);
        servicio.registrarReceta(App.api_key, a).enqueue(callback);
    }

    private void configRespuestas() {
        callback = new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                if (!respuesta.getError()) {
                    App.mRecipeActual.setIdrecipe(((Double)respuesta.getMessage()).intValue());
                    Intent resultado = new Intent();
                    setResult(RESULT_OK, resultado);
                    finish();
                }
                //Se ha producido un error
                else {
                    Log.e("FAIL1 - Edit:", respuesta.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Log.e("FAIL2 - Edit:", t.toString());
            }
        };
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        RecipeEdit1Fragment frgDetails;
        RecipeEdit2Fragment frgMaking;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (frgDetails == null)
                        frgDetails = RecipeEdit1Fragment.newInstance();
                    return frgDetails;
                case 1:
                    if (frgMaking == null)
                        frgMaking = RecipeEdit2Fragment.newInstance();
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
            Fragment currentFrag = mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());

            switch (requestCode) {
                case FotoDialogFragment.RC_SELECCIONAR_FOTO:
                case FotoDialogFragment.RC_CAPTURAR_FOTO:
                    currentFrag.onActivityResult(requestCode,resultCode,data);

            }
        }
    }
}
