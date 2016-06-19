package dras.finalproyect.dialogos;



import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import dras.finalproyect.R;


/**
 * Created by Usuario on 26/11/2015.
 */
public class InfoDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());

        View vista = LayoutInflater.from(getActivity()).inflate(R.layout.info_dialog, null);
        b.setView(vista);


        b.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
            }
        });



        return b.create();
    }


}
