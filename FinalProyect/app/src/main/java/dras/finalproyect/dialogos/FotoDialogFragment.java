package dras.finalproyect.dialogos;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dras.finalproyect.R;
import dras.finalproyect.fragmentos.RecipeEdit1Fragment;


/**
 * Created by Usuario on 26/11/2015.
 */
public class FotoDialogFragment extends DialogFragment {


    public static final int RC_SELECCIONAR_FOTO = 6;
    public static final int RC_CAPTURAR_FOTO = 5;

    private void solicitarCapturaFoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            // Se crea el archivo para la foto en el directorio público (true).
            // Se obtiene la fecha y hora actual.
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String nombre = "IMG_" + timestamp + "_" + ".jpg";
            File fotoFile = crearArchivoFoto(nombre, true);
            if (fotoFile != null) {
                // Se añade como extra del intent la uri donde debe guardarse.
                RecipeEdit1Fragment.sPathFotoOriginal = fotoFile.getAbsolutePath();
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fotoFile));
                getActivity().startActivityForResult(i, RC_CAPTURAR_FOTO);
            }
        }
    }

    private File crearArchivoFoto(String nombre, boolean publico) {
        // Se obtiene el directorio en el que almacenarlo.
        File directorio;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (publico) {
                // En el directorio público para imágenes del almacenamiento externo.
                directorio = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            } else {
                directorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            }
        } else {
            // En almacenamiento interno.
            directorio = getActivity().getFilesDir();
        }
        // Su no existe el directorio, se crea.
        if (directorio != null && !directorio.exists()) {
            if (!directorio.mkdirs()) {
                Log.d(getString(R.string.app_name), "error al crear el directorio");
                return null;
            }
        }
        // Se crea un archivo con ese nombre y la extensión jpg en ese
        // directorio.
        File archivo = null;
        if (directorio != null) {
            archivo = new File(directorio.getPath() + File.separator +
                    nombre);
            Log.d(getString(R.string.app_name), archivo.getAbsolutePath());
        }
        // Se retorna el archivo creado.
        return archivo;
    }

    public void buscarFotoEnGaleria() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        getActivity().startActivityForResult(i, RC_SELECCIONAR_FOTO);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());

        View vista = LayoutInflater.from(getActivity()).inflate(R.layout.foto_dialog, null);
        b.setView(vista);

        ImageView imgCamara = (ImageView) vista.findViewById(R.id.imgCamara);
        ImageView imgGaleria = (ImageView) vista.findViewById(R.id.imgGaleria);

        imgCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarCapturaFoto();
                dismiss();
            }
        });
        imgGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarFotoEnGaleria();
                dismiss();
            }
        });

        b.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {

            }
        });



        return b.create();
    }


}
