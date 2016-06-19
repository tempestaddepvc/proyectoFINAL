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
import android.widget.Toast;

import java.util.ArrayList;

import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.adaptadores.EditPasosAdapter;
import dras.finalproyect.pojos.Recipe;
import dras.finalproyect.pojos.Step;

public class RecipeEdit2Fragment extends Fragment implements EditPasosAdapter.OnItemClickListener{

    private Recipe mRecipe;
    private RecyclerView rvLista;
    private EditPasosAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public RecipeEdit2Fragment() {
        // Required empty public constructor
    }

    public static RecipeEdit2Fragment newInstance() {
        RecipeEdit2Fragment fragment = new RecipeEdit2Fragment();
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
        mAdapter = new EditPasosAdapter((ArrayList<Step>) mRecipe.getSteps());
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
    public void onStepClick(View view, Step step, int position) {
        Toast.makeText(getContext(),"Al hacer click Step",Toast.LENGTH_SHORT).show();
    }

    public void save(){

    }
}
