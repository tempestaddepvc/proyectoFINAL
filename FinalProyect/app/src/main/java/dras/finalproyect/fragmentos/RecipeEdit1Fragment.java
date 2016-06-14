package dras.finalproyect.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.adaptadores.EditIngredientsAdapter;
import dras.finalproyect.adaptadores.RecipeIngredientsAdapter;
import dras.finalproyect.pojos.Quantity;
import dras.finalproyect.pojos.Recipe;


public class RecipeEdit1Fragment extends Fragment implements EditIngredientsAdapter.OnItemClickListener {

    private Recipe mRecipe;
    private RecyclerView rvLista;
    private EditIngredientsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public RecipeEdit1Fragment() {
        // Required empty public constructor
    }


    public static RecipeEdit1Fragment newInstance() {
        RecipeEdit1Fragment fragment = new RecipeEdit1Fragment();
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
        mAdapter = new EditIngredientsAdapter((ArrayList<Quantity>) mRecipe.getQuantities());
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mAdapter.setOnItemClickListener(this);

        rvLista.setAdapter(mAdapter);
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
                        ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        mAdapter.swapItems(fromPos, toPos);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        // Se elimina el elemento.
                        mAdapter.removeItem(viewHolder.getAdapterPosition());
                    }
                });
        itemTouchHelper.attachToRecyclerView(rvLista);
    }

    @Override
    public void onIngredientClick(View view, Quantity ingredient, int position) {
        Toast.makeText(getContext(),"Al hacer click Ingredient",Toast.LENGTH_SHORT).show();
    }
}

