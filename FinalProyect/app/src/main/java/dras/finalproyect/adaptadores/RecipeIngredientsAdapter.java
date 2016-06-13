package dras.finalproyect.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dras.finalproyect.R;
import dras.finalproyect.pojos.Quantity;

/**
 * Created by Dras on 01/06/2016.
 */
// Adaptador para la lista de recetas.
public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {

    private final ArrayList<Quantity> mDatos;

    // Constructor.
    public RecipeIngredientsAdapter(ArrayList<Quantity> datos) {
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
                .inflate(R.layout.item_detail_ingredient, parent, false);

        // Se crea el contenedor de vistas para la fila.
        final ViewHolder viewHolder = new ViewHolder(itemView);

         // Se retorna el contenedor.
        return viewHolder;
    }


    // Cuando se deben escribir los datos en las subvistas de la
    // vista correspondiente al ítem.
    @Override
    public void onBindViewHolder(RecipeIngredientsAdapter.ViewHolder holder, int position) {
        Quantity quantity = mDatos.get(position);
        holder.bind(quantity);
    }


    // Contenedor de vistas para la vista correspondiente.
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblNombre;
        private final TextView lblCantidad;
        private final TextView lblMedida;


        // El constructor recibe la vista correspondiente.
        public ViewHolder(View itemView) {
            super(itemView);
            // Se obtienen las subvistas de la vista.
            lblNombre = (TextView) itemView.findViewById(R.id.lblNombre);
            lblCantidad = (TextView) itemView.findViewById(R.id.lblCantidad);
            lblMedida = (TextView) itemView.findViewById(R.id.lblMedida);

        }

        public void bind(Quantity q) {
            lblNombre.setText(q.getName());
            lblCantidad.setText(q.getCant().toString());
            lblMedida.setText(q.getMeasure());

        }
    }

}