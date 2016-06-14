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
import java.util.ArrayList;

import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.adaptadores.RecipePasosAdapter;
import dras.finalproyect.pojos.Recipe;
import dras.finalproyect.pojos.Step;

public class RecipeDetail2Fragment extends Fragment {

    private static final String ARG_RECIPE = "recipe";
    private Recipe mRecipe;
    private RecyclerView rvLista;
    private RecipePasosAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public RecipeDetail2Fragment() {
        // Required empty public constructor
    }

    public static RecipeDetail2Fragment newInstance() {
        RecipeDetail2Fragment fragment = new RecipeDetail2Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            mRecipe = App.mRecipeAcutal;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_detail2, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initViews();
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews() {
        //Lista de ingredientes
        rvLista = (RecyclerView) getView().findViewById(R.id.rvLista);
        mAdapter = new RecipePasosAdapter((ArrayList<Step>) mRecipe.getSteps());
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        rvLista.setAdapter(mAdapter);
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());

    }

}
