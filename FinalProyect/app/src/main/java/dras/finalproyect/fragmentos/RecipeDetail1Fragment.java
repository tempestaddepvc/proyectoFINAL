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

    public RecipeDetail1Fragment() {
        // Required empty public constructor
    }


    public static RecipeDetail1Fragment newInstance(Recipe recipe) {
        RecipeDetail1Fragment fragment = new RecipeDetail1Fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(ARG_RECIPE);
        }
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
        TextView lblNombre = (TextView) getView().findViewById(R.id.lblNombre);
        TextView lblCreador = (TextView) getView().findViewById(R.id.lblCreador);
        TextView lblDificultad = (TextView) getView().findViewById(R.id.lblDificultad);
        TextView lblComensales = (TextView) getView().findViewById(R.id.lblComensales);
        TextView lblTiempo = (TextView) getView().findViewById(R.id.lblTiempo);
        TextView lblDescripcion = (TextView) getView().findViewById(R.id.lblDescripcion);
        ImageView imgFoto = (ImageView) getView().findViewById(R.id.imgFoto);

        lblNombre.setText(mRecipe.getName());
        lblCreador.setText(mRecipe.getCreator());
        lblTiempo.setText(mRecipe.getTime().toString()+" min");
        lblDificultad.setText(mRecipe.getDifficulty().toString());
        lblComensales.setText(mRecipe.getDiners().toString());
        lblDescripcion.setText(mRecipe.getDetails());

        if (mRecipe.getPicture() != null)
            Picasso.with(getContext()).load(mRecipe.getPicture()).into(imgFoto);
        else
            Picasso.with(getContext()).load(R.drawable.default_recipe).into(imgFoto);



        //Lista de ingredientes
        rvLista = (RecyclerView) getView().findViewById(R.id.rvLista);
        mAdapter = new RecipeIngredientsAdapter((ArrayList<Quantity>) mRecipe.getQuantities());
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, true);

        rvLista.setAdapter(mAdapter);
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());

    }
}

