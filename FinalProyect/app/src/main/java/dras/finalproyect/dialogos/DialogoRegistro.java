package dras.finalproyect.dialogos;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dras.finalproyect.R;

/**
 * Created by Dras on 29/05/2016.
 */
public class DialogoRegistro extends AppCompatDialogFragment {

    // Variables.
    private DialogRegsListener mListener = null;

    // Interfaz pública para comunicación con la actividad.
    public interface DialogRegsListener {
        public void onPositiveButtonClick();
    }



    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
        b.setTitle("Error Usuario");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialogo_registro, null);
        b.setView(v);

        b.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            // Al pulsar el botón positivo.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se notifica el evento al listener.
                mListener.onPositiveButtonClick();
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
            mListener = (DialogRegsListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz, se lanza excepción.
            throw new ClassCastException(activity.toString()
                    + " debe implementar DialogRegsListener");
        }
    }
}
