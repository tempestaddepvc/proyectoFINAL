package dras.finalproyect.dialogos;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import dras.finalproyect.R;

/**
 * Created by Dras on 29/05/2016.
 */
public class DialogoIngredientes extends AppCompatDialogFragment {

    // Variables.
    private DialogIngredientListener mListener = null;
    private EditText txtIngredients,txtCantidad,txtMedida;

    // Interfaz pública para comunicación con la actividad.
    public interface DialogIngredientListener {
        public void onAddIngredient(String nombre,int cant, String medida);
    }



    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
        b.setTitle("Add Ingrediente");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_ingredients, null);
        b.setView(v);
        txtIngredients= (EditText) v.findViewById(R.id.txtIngredients);
        txtCantidad= (EditText) v.findViewById(R.id.txtCantidad);
        txtMedida= (EditText) v.findViewById(R.id.txtMedida);

        b.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            // Al pulsar el botón positivo.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se notifica el evento al listener.
                mListener.onAddIngredient(txtIngredients.getText().toString(), Integer.parseInt(txtCantidad.getText().toString()),txtMedida.getText().toString());
            }
        });
        b.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            // Al pulsar el botón negativo.
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return b.create();
    }


    // Al enlazar el fragmento con la actividad.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Establece la actividad como listener de los eventos del diálogo.
        try {
            mListener = (DialogIngredientListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz, se lanza excepción.
            throw new ClassCastException(activity.toString()
                    + " debe implementar DialogFilterAvanzedListener");
        }
    }
}
