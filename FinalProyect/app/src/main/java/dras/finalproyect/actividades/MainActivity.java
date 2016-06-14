package dras.finalproyect.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.adaptadores.RecipesAdapter;
import dras.finalproyect.dialogos.DialogoRegistro;
import dras.finalproyect.pojos.Recipe;
import dras.finalproyect.pojos.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecipesAdapter.OnItemClickListener {

    private Toolbar toolbar;
    private RecipesAdapter mAdaptador;
    private RecyclerView lstRecipes;
    private TextView mEmptyView;
    private LinearLayoutManager mLayoutManager;
    private Callback<Respuesta> callback;
    private App.APIinterface servicio;

    public static void start(Activity a) {
        Intent intent = new Intent(a, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        a.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configFab();
        configDrawer();
        configReciclerView();
        configRespuestas();
        getLista();
    }

    private void configRespuestas() {
        callback = new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                if (!respuesta.getError()) {

                    Gson gson = new Gson();
                    String a = gson.toJson(respuesta.getMessage());
                    ArrayList<Recipe> recipes = gson.fromJson(a, new TypeToken<ArrayList<Recipe>>() {
                    }.getType());

                    mAdaptador.cambiarDatos(recipes);
                    checkAdapterIsEmpty();
                }
                //Se ha producido un error
                else {
                    Log.e("FAIL1 - Main:", respuesta.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Log.e("FAIL2 - Main:", t.toString());
            }
        };
    }

    private void getLista() {
        App.getServicio().obtenerRecetas().enqueue(callback);
    }
    private void getFavs() {
        App.getServicio().obtenerFavs(App.user_id).enqueue(callback);
    }
    private void getCreated() {
        App.getServicio().obtenerCreados(App.user_id).enqueue(callback);
    }
    private void getCreatedAndFavs() {
        App.getServicio().obtenerFavsAndCreados(App.user_id).enqueue(callback);
    }

    private void configReciclerView() {
        mEmptyView = (TextView) findViewById(R.id.lblNoHayRecetas);
        mAdaptador = new RecipesAdapter(new ArrayList<Recipe>());
        mAdaptador.setOnItemClickListener(this);
        mAdaptador.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkAdapterIsEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkAdapterIsEmpty();
            }
        });

        lstRecipes = (RecyclerView) findViewById(R.id.list_recipes);
        if (lstRecipes != null) {
            lstRecipes.setHasFixedSize(true);
            lstRecipes.setAdapter(mAdaptador);
            checkAdapterIsEmpty();
            mLayoutManager = new GridLayoutManager(this, 2,
                    GridLayoutManager.VERTICAL, false);
            lstRecipes.setLayoutManager(mLayoutManager);
        }
    }

    private void configDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void configFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    // Muestra u oculta la empty view dependiendo de si el adaptador está vacío.
    private void checkAdapterIsEmpty() {
        mEmptyView.setVisibility(mAdaptador.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    // Cuando se hace click sobre un elemento de la lista.
    @Override
    public void onItemClick(View view, Recipe recipe, int position) {
        obtenerDatos(recipe.getIdrecipe());
    }

    private void obtenerDatos(int idRecipe) {
        servicio = App.getServicio();
        servicio.obtenerDatosReceta(idRecipe).enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                if (!respuesta.getError()) {
                    Gson gson = new Gson();
                    String a = gson.toJson(respuesta.getMessage());
                    ArrayList<Recipe> recipes = gson.fromJson(a, new TypeToken<ArrayList<Recipe>>() {
                    }.getType());

                    App.mRecipeAcutal=recipes.get(0);
                    actividadDetalles();
                }
                //Se ha producido un error
                else {
                    Log.e("FAIL1 - Main:", respuesta.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Log.e("FAIL - Details", t.toString());
            }
        });
    }

    private void actividadDetalles() {
        RecipeDetailsActivity.start(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
getLista();            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
getCreated();
        } else if (id == R.id.nav_slideshow) {
getFavs();
        } else if (id == R.id.nav_manage) {
            getCreatedAndFavs();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
