package dras.finalproyect.adaptadores;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dras.finalproyect.R;
import dras.finalproyect.pojos.Recipe;

/**
 * Created by Dras on 01/06/2016.
 */
// Adaptador para la lista de recetas.
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private final ArrayList<Recipe> mDatos;
    private OnItemClickListener onItemClickListener;

    public void add(Recipe recipe) {
        mDatos.add(recipe);
        notifyItemInserted(mDatos.size()-1);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Recipe recipe, int position);
    }
    // Constructor.
    public RecipesAdapter(ArrayList<Recipe> datos) {
        mDatos = datos;
    }

    // Retorna el número de ítems de datos.
    @Override
    public int getItemCount() {
        return mDatos.size();
    }

    // Cuando se debe crear una nueva vista para el elemento.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receta, parent, false);

        // Se crea el contenedor de vistas para la fila.
        final ViewHolder viewHolder = new ViewHolder(itemView);

        // Cuando se hace click sobre el elemento.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    // Se informa al listener.
                    onItemClickListener.onItemClick(v,
                            mDatos.get(viewHolder.getAdapterPosition()),
                            viewHolder.getAdapterPosition());
                }
            }
        });


        // Se retorna el contenedor.
        return viewHolder;
    }

    public void cambiarDatos(ArrayList<Recipe> datos){
        mDatos.clear();
        mDatos.addAll(datos);
        notifyDataSetChanged();
    }


    // Establece el listener a informar cuando se hace click sobre un ítem.
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Cuando se deben escribir los datos en las subvistas de la
    // vista correspondiente al ítem.
    @Override
    public void onBindViewHolder(RecipesAdapter.ViewHolder holder, int position) {
        Recipe recipe = mDatos.get(position);
        holder.bind(recipe);
    }


    // Contenedor de vistas para la vista correspondiente.
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblNombre;
        private final TextView lblDificultad;
        private final TextView lblComensales;
        private final TextView lblTiempo;
        private final ImageView imgFoto;


        // El constructor recibe la vista correspondiente.
        public ViewHolder(View itemView) {
            super(itemView);
            // Se obtienen las subvistas de la vista.
            lblNombre = (TextView) itemView.findViewById(R.id.lblNombre);
            lblDificultad = (TextView) itemView.findViewById(R.id.lblDificultad);
            lblComensales = (TextView) itemView.findViewById(R.id.lblComenzales);
            lblTiempo = (TextView) itemView.findViewById(R.id.lblTiempo);
            imgFoto = (ImageView) itemView.findViewById(R.id.imgFoto);

        }

        public void bind(Recipe recipe) {
            lblNombre.setText(recipe.getName());
            lblDificultad.setText(recipe.getDifficulty().toString());
            lblTiempo.setText(recipe.getTime().toString());
            lblComensales.setText(recipe.getDiners().toString());

            if (recipe.getPicture() != null)
                if (!recipe.getPicture().isEmpty())
                    Picasso.with(itemView.getContext()).load(recipe.getPicture()).into(imgFoto);
            else
                Picasso.with(itemView.getContext()).load(R.drawable.default_recipe).into(imgFoto);

        }
    }

}