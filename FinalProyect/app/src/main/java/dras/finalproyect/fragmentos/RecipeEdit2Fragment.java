package dras.finalproyect.fragmentos;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.adaptadores.EditPasosAdapter;
import dras.finalproyect.dialogos.FotoDialogFragment;
import dras.finalproyect.pojos.Recipe;
import dras.finalproyect.pojos.Step;

public class RecipeEdit2Fragment extends Fragment implements EditPasosAdapter.OnItemClickListener{

    private Recipe mRecipe;
    private RecyclerView rvLista;
    private EditPasosAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public RecipeEdit2Fragment() {
        // Required empty public constructor
    }

    public static RecipeEdit2Fragment newInstance() {
        RecipeEdit2Fragment fragment = new RecipeEdit2Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            mRecipe = App.mRecipeActual;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_detail2, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initViews();
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews() {
        //Lista de ingredientes
        rvLista = (RecyclerView) getView().findViewById(R.id.rvLista);
        mAdapter = new EditPasosAdapter((ArrayList<Step>) mRecipe.getSteps());
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mAdapter.setOnItemClickListener(this);

        rvLista.setAdapter(mAdapter);
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
                        ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        mAdapter.swapItems(fromPos, toPos);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        // Se elimina el elemento.
                        mAdapter.removeItem(viewHolder.getAdapterPosition());
                    }
                });
        itemTouchHelper.attachToRecyclerView(rvLista);
    }

    @Override
    public void onStepClick(View view, Step step, int position) {
        Toast.makeText(getContext(),"Al hacer click Step",Toast.LENGTH_SHORT).show();
    }


    public void addStep(Step step) {
        mAdapter.addItem(step);
        mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
        solicitarFoto();
    }


    // ------------------------ Multimedia ---------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case FotoDialogFragment.RC_CAPTURAR_FOTO:
                    // Se agrega la foto a la Galería
                    agregarFotoAGaleria(App.sPathFotoOriginal);
                    // Se escala la foto, se almacena en archivo propio y se muestra en ImageView.
                    guardarImagen(App.sPathFotoOriginal);
                    break;
                case FotoDialogFragment.RC_SELECCIONAR_FOTO:
                    // Se obtiene el path real a partir de la uri retornada por la galería.
                    Uri uriGaleria = data.getData();
                    App.sPathFotoOriginal = getRealPath(uriGaleria);
                    // Se escala la foto, se almacena en archivo propio y se muestra en ImageView.
                    guardarImagen(App.sPathFotoOriginal);
                    break;
            }
        }
    }

    private void guardarImagen(String sPathFotoOriginal) {
        mAdapter.getItem(mAdapter.getItemCount()-1).setPicture(sPathFotoOriginal);
        mAdapter.notifyItemChanged(mAdapter.getItemCount()-1);
        mAdapter.notifyItemChanged(mAdapter.getItemCount()-1);
    }
    public void solicitarFoto() {
        FotoDialogFragment frgMiDialogo = new FotoDialogFragment();
        frgMiDialogo.show(getActivity().getSupportFragmentManager(), "DialogFoto");
    }

    // Obtiene el path real de una imagen a partir de la URI de Galería obtenido con ACTION_PICK.
    private String getRealPath(Uri uriGaleria) {
        // Se consulta en el content provider de la galería el path real del archivo de la foto.
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = getActivity().getContentResolver().query(uriGaleria, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String path = c.getString(columnIndex);
        c.close();
        return path;
    }
    // Agrega a la Galería la foto indicada.
    private void agregarFotoAGaleria(String pathFoto) {
        // Se crea un intent implícito con la acción de
        // escaneo de un fichero multimedia.
        Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // Se obtiene la uri del archivo a partir de su path.
        File archivo = new File(pathFoto);
        Uri uri = Uri.fromFile(archivo);
        // Se establece la uri con datos del intent.
        i.setData(uri);
        // Se envía un broadcast con el intent.
        getActivity().sendBroadcast(i);
    }



    // Crea un archivo de foto con el nombre indicado en almacenamiento externo si es posible, o si
    // no en almacenamiento interno, y lo retorna. Retorna null si fallo.
    // Si publico es true -> en la carpeta pública de imágenes.
    // Si publico es false, en la carpeta propia de imágenes.
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

}
