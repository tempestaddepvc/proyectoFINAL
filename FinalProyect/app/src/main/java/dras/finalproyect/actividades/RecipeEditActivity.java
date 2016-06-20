package dras.finalproyect.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.design.widget.FloatingActionButton;
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
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.dialogos.DialogoFiltroName;
import dras.finalproyect.dialogos.DialogoIngredientes;
import dras.finalproyect.dialogos.DialogoPasos;
import dras.finalproyect.dialogos.FotoDialogFragment;
import dras.finalproyect.fragmentos.RecipeEdit1Fragment;
import dras.finalproyect.fragmentos.RecipeEdit2Fragment;
import dras.finalproyect.imgur.ImgurUploader;
import dras.finalproyect.pojos.Quantity;
import dras.finalproyect.pojos.Recipe;
import dras.finalproyect.pojos.Respuesta;
import dras.finalproyect.pojos.Step;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeEditActivity extends AppCompatActivity implements DialogoPasos.DialogPasosListener, DialogoIngredientes.DialogIngredientListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;


    public static void startForResult(Activity a, int requestCode) {
        Intent intent = new Intent(a, RecipeEditActivity.class);
        a.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        configFab();
    }

    private void configFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() == 0) {
                    new DialogoIngredientes().show(getSupportFragmentManager(), "Add Ingredient");
                } else {
                    new DialogoPasos().show(getSupportFragmentManager(), "Add Step");
                }
            }
        });
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
        switch (id) {
            case R.id.save:
                if (App.mRecipeActual.getIdrecipe() == 0)
                    crearRecipe();
                else
                    guardarRecipe();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void guardarRecipe() {
        mSectionsPagerAdapter.frgDetails.save();

    }


    private void crearRecipe() {
        if(mSectionsPagerAdapter.frgDetails.tieneDatos()){
            mSectionsPagerAdapter.frgDetails.save();
            new ImgurUploader(App.mRecipeActual).upload();
            Intent resultado = new Intent();
            setResult(RESULT_OK, resultado);
            finish();
        }else {
            Toast.makeText(this,"No se puede Guardar una receta sin insertar Datos",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAddIngredient(String nombre, int cant, String medida) {
        ((RecipeEdit1Fragment) mSectionsPagerAdapter.getItem(0)).addIngredient(new Quantity(0, nombre, cant, medida));
    }

    @Override
    public void onAddPaso(String detalles, String foto) {
        ((RecipeEdit2Fragment) mSectionsPagerAdapter.getItem(1)).addStep(new Step(0, detalles, foto));
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
                    currentFrag.onActivityResult(requestCode, resultCode, data);

            }
        }
    }
}
