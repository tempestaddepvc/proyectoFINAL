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
public class DialogoPasos extends AppCompatDialogFragment {

    // Variables.
    private DialogPasosListener mListener = null;
    private EditText txtStep;

    // Interfaz pública para comunicación con la actividad.
    public interface DialogPasosListener {
        public void onAddPaso(String detalles, String foto);
    }



    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
        b.setTitle("Add Paso");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_pasos, null);
        b.setView(v);
        txtStep = (EditText) v.findViewById(R.id.txtStep);

        b.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            // Al pulsar el botón positivo.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se notifica el evento al listener.
                mListener.onAddPaso(txtStep.getText().toString(),"");
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
            mListener = (DialogPasosListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz, se lanza excepción.
            throw new ClassCastException(activity.toString()
                    + " debe implementar DialogFilterAvanzedListener");
        }
    }
}
