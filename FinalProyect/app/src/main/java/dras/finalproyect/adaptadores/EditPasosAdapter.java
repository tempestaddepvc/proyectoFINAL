package dras.finalproyect.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import dras.finalproyect.R;
import dras.finalproyect.pojos.Step;

/**
 * Created by Dras on 01/06/2016.
 */
// Adaptador para la lista de recetas.
public class EditPasosAdapter extends RecyclerView.Adapter<EditPasosAdapter.ViewHolder> {

    private final ArrayList<Step> mDatos;
    private OnItemClickListener onItemClickListener;

    // Constructor.
    public EditPasosAdapter(ArrayList<Step> datos) {
        mDatos = datos;
    }

    // Interfaz que debe implementar el listener para cuando se haga click sobre un elemento.
    public interface OnItemClickListener {
        void onStepClick(View view, Step step, int position);
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
    public void addItem(Step step) {
        mDatos.add(step);
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
                .inflate(R.layout.item_detail_paso, parent, false);

        // Se crea el contenedor de vistas para la fila.
        final ViewHolder viewHolder = new ViewHolder(itemView);
        // Cuando se hace click sobre el elemento.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    // Se informa al listener.
                    onItemClickListener.onStepClick(v,
                            mDatos.get(viewHolder.getAdapterPosition()),
                            viewHolder.getAdapterPosition());
                }
            }
        });

         // Se retorna el contenedor.
        return viewHolder;
    }


    // Cuando se deben escribir los datos en las subvistas de la
    // vista correspondiente al ítem.
    @Override
    public void onBindViewHolder(EditPasosAdapter.ViewHolder holder, int position) {
        Step step = mDatos.get(position);
        holder.bind(step);
    }


    // Contenedor de vistas para la vista correspondiente.
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblPaso;
        private final ImageView imgFoto;


        // El constructor recibe la vista correspondiente.
        public ViewHolder(View itemView) {
            super(itemView);
            // Se obtienen las subvistas de la vista.
            lblPaso = (TextView) itemView.findViewById(R.id.lblPaso);
            imgFoto = (ImageView) itemView.findViewById(R.id.imgFoto);

        }

        public void bind(Step step) {
            lblPaso.setText(step.getStep());

            if (step.getPicture() == null || step.getPicture()=="") {
                imgFoto.setVisibility(View.GONE);
            }else {
                imgFoto.setVisibility(View.VISIBLE);
                Picasso.with(itemView.getContext()).load(step.getPicture()).into(imgFoto);
            }
        }
    }

}