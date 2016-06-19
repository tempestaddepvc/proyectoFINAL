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
public class DialogoFiltroName extends AppCompatDialogFragment {

    // Variables.
    private DialogFilterNameListener mListener = null;
    private EditText txt;

    // Interfaz pública para comunicación con la actividad.
    public interface DialogFilterNameListener {
        public void onFilterName(String filtro);
    }



    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
        b.setTitle("Error Usuario");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_filtro_name, null);
        b.setView(v);
        txt= (EditText) v.findViewById(R.id.txt);

        b.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            // Al pulsar el botón positivo.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se notifica el evento al listener.
                mListener.onFilterName(txt.getText().toString());
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
            mListener = (DialogFilterNameListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz, se lanza excepción.
            throw new ClassCastException(activity.toString()
                    + " debe implementar DialogFilterAvanzedListener");
        }
    }
}
