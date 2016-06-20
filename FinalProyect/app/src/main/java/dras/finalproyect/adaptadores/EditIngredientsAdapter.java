package dras.finalproyect.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import dras.finalproyect.R;
import dras.finalproyect.pojos.Ingredient;
import dras.finalproyect.pojos.Quantity;
import dras.finalproyect.pojos.Step;

/**
 * Created by Dras on 01/06/2016.
 */
// Adaptador para la lista de recetas.
public class EditIngredientsAdapter extends RecyclerView.Adapter<EditIngredientsAdapter.ViewHolder>{

    private final ArrayList<Quantity> mDatos;
    private OnItemClickListener onItemClickListener;

    // Constructor.
    public EditIngredientsAdapter(ArrayList<Quantity> datos) {
        mDatos = datos;
    }


    // Interfaz que debe implementar el listener para cuando se haga click sobre un elemento.
    public interface OnItemClickListener {
        void onIngredientClick(View view, Quantity ingredient, int position);
    }

    // Establece el listener a informar cuando se hace click sobre un ítem.
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Elimina un elemento de la lista.
    public void removeItem(int position) {
        mDatos.remove(position);
        notifyItemRemoved(position);
    }
    // Inserta un elemento a la lista.
    public void addItem(Quantity quantity) {
        mDatos.add(quantity);
        notifyItemInserted(mDatos.size()-1);
    }

    // Intercambia dos elementos de la lista.
    public void swapItems(int from, int to) {
        // Se realiza el intercambio.
        Collections.swap(mDatos, from, to);
        // Se notifica el movimiento.
        notifyItemMoved(from, to);
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
    public void onBindViewHolder(EditIngredientsAdapter.ViewHolder holder, int position) {
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