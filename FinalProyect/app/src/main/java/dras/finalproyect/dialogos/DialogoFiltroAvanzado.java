package dras.finalproyect.dialogos;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;

import dras.finalproyect.R;

/**
 * Created by Dras on 29/05/2016.
 */
public class DialogoFiltroAvanzado extends AppCompatDialogFragment {

    // Variables.
    private DialogFilterAvanzedListener mListener = null;
    private RangeBar rbTiempo;
    private RangeBar rbComensales;
    private RangeBar rbDificultad;
    private TextView lblMin;
    private TextView lblMax;
    private TextView lblComensales;
    private TextView lblDificultad;
    private int min,max,diners,dificultad;

    // Interfaz pública para comunicación con la actividad.
    public interface DialogFilterAvanzedListener {
        public void onAvancedFilter(int min, int max, int comensales, int dificultad);
    }



    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
        b.setTitle("Filtro Avanzado");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_filter_avanzed, null);
        b.setView(v);

        rbTiempo= (RangeBar) v.findViewById(R.id.rbTiempo);
        rbDificultad = (RangeBar) v.findViewById(R.id.rbDificultad);
        rbComensales= (RangeBar) v.findViewById(R.id.rbComensales);
        lblMin  = (TextView) v.findViewById(R.id.lblMin);
        lblMax  = (TextView) v.findViewById(R.id.lblMax);
        lblDificultad  = (TextView) v.findViewById(R.id.lblDificultad);
        lblComensales = (TextView) v.findViewById(R.id.lblComensales);


        rbTiempo.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                min= Integer.parseInt(leftPinValue);
                max= Integer.parseInt(rightPinValue);
                lblMin.setText(leftPinValue);
                lblMax.setText(rightPinValue);
            }
        });
        rbDificultad.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                dificultad=rightPinIndex;
                lblDificultad.setText(""+rightPinIndex);
            }
        });
        rbComensales.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                diners=rightPinIndex+1;
                lblComensales.setText(""+(rightPinIndex+1));
            }
        });


        b.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            // Al pulsar el botón positivo.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se notifica el evento al listener.
                mListener.onAvancedFilter(min,max,diners, dificultad);
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
            mListener = (DialogFilterAvanzedListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz, se lanza excepción.
            throw new ClassCastException(activity.toString()
                    + " debe implementar DialogFilterAvanzedListener");
        }
    }
}
