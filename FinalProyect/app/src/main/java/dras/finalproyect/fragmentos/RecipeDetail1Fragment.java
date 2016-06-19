package dras.finalproyect.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.adaptadores.RecipeIngredientsAdapter;
import dras.finalproyect.pojos.Quantity;
import dras.finalproyect.pojos.Recipe;


public class RecipeDetail1Fragment extends Fragment {

    private static final String ARG_RECIPE = "recipe";
    private Recipe mRecipe;
    private RecyclerView rvLista;
    private RecipeIngredientsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView lblNombre ;
    private TextView lblCreador ;
    private TextView lblDificultad;
    private TextView lblComensales ;
    private TextView lblTiempo;
    private TextView lblDescripcion ;
    private ImageView imgFoto ;


    public RecipeDetail1Fragment() {
        // Required empty public constructor
    }


    public static RecipeDetail1Fragment newInstance() {
        RecipeDetail1Fragment fragment = new RecipeDetail1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            mRecipe = App.mRecipeActual;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_detail1, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initViews();
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews() {
        lblNombre = (TextView) getView().findViewById(R.id.lblNombre);
        lblCreador = (TextView) getView().findViewById(R.id.lblCreador);
        lblDificultad = (TextView) getView().findViewById(R.id.lblDificultad);
        lblComensales = (TextView) getView().findViewById(R.id.lblComensales);
        lblTiempo = (TextView) getView().findViewById(R.id.lblTiempo);
        lblDescripcion = (TextView) getView().findViewById(R.id.lblDescripcion);
        imgFoto = (ImageView) getView().findViewById(R.id.imgFoto);

        cargarDatos();

        //Lista de ingredientes
        rvLista = (RecyclerView) getView().findViewById(R.id.rvLista);
        mAdapter = new RecipeIngredientsAdapter((ArrayList<Quantity>) mRecipe.getQuantities());
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        rvLista.setAdapter(mAdapter);
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());

    }

    public void cargarDatos() {
        lblNombre.setText(mRecipe.getName());
        lblCreador.setText(mRecipe.getCreator());
        lblTiempo.setText(mRecipe.getTime().toString());
        lblDificultad.setText(mRecipe.getDifficulty().toString());
        lblComensales.setText(mRecipe.getDiners().toString());
        lblDescripcion.setText(mRecipe.getDetails());

        if (mRecipe.getPicture() != null)
            if (!mRecipe.getPicture().isEmpty())
            Picasso.with(getContext()).load(mRecipe.getPicture()).into(imgFoto);
        else
            Picasso.with(getContext()).load(R.drawable.default_recipe).into(imgFoto);
    }
}

