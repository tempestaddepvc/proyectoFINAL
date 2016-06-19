package dras.finalproyect.fragmentos;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.adaptadores.EditIngredientsAdapter;
import dras.finalproyect.dialogos.FotoDialogFragment;
import dras.finalproyect.pojos.Quantity;
import dras.finalproyect.pojos.Recipe;


public class RecipeEdit1Fragment extends Fragment implements EditIngredientsAdapter.OnItemClickListener {

    public static String sPathFotoOriginal="";
    private Recipe mRecipe;
    private RecyclerView rvLista;
    private EditIngredientsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private String sRutaArchivo="";
    private ImageView imgFoto;
    private EditText txtNombre;
    private EditText txtDificultad;
    private EditText txtComensales;
    private EditText txtTiempo;
    private EditText txtDescripcion;

    public RecipeEdit1Fragment() {
        // Required empty public constructor
    }


    public static RecipeEdit1Fragment newInstance() {
        RecipeEdit1Fragment fragment = new RecipeEdit1Fragment();
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
        return inflater.inflate(R.layout.fragment_recipe_edit1, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initViews();
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews() {
        txtNombre = (EditText) getView().findViewById(R.id.lblNombre);
        txtDificultad = (EditText) getView().findViewById(R.id.lblDificultad);
        txtComensales = (EditText) getView().findViewById(R.id.lblComensales);
        txtTiempo = (EditText) getView().findViewById(R.id.lblTiempo);
        txtDescripcion = (EditText) getView().findViewById(R.id.lblDescripcion);
        imgFoto = (ImageView) getView().findViewById(R.id.imgFoto);


        if (mRecipe.getIdrecipe()!=0) {
            txtNombre.setText(mRecipe.getName());
            txtTiempo.setText(mRecipe.getTime().toString());
            txtDificultad.setText(mRecipe.getDifficulty().toString());
            txtComensales.setText(mRecipe.getDiners().toString());
            txtDescripcion.setText(mRecipe.getDetails());

            if (mRecipe.getPicture() != null)
                Picasso.with(getContext()).load(mRecipe.getPicture()).into(imgFoto);
            else
                Picasso.with(getContext()).load(R.drawable.default_recipe).into(imgFoto);
        }
        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarFoto();
            }
        });


        //Lista de ingredientes
        rvLista = (RecyclerView) getView().findViewById(R.id.rvLista);
        mAdapter = new EditIngredientsAdapter((ArrayList<Quantity>) mRecipe.getQuantities());
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

    public void solicitarFoto() {
        FotoDialogFragment frgMiDialogo = new FotoDialogFragment();
        frgMiDialogo.show(getActivity().getSupportFragmentManager(), "DialogFoto");
    }

    @Override
    public void onIngredientClick(View view, Quantity ingredient, int position) {
        Toast.makeText(getContext(),"Al hacer click Ingredient",Toast.LENGTH_SHORT).show();
    }

    // ------------------------ Multimedia ---------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case FotoDialogFragment.RC_CAPTURAR_FOTO:
                    // Se agrega la foto a la Galería
                    agregarFotoAGaleria(sPathFotoOriginal);
                    // Se escala la foto, se almacena en archivo propio y se muestra en ImageView.
                    cargarImagenEscalada(sPathFotoOriginal);
                    break;
                case FotoDialogFragment.RC_SELECCIONAR_FOTO:
                    // Se obtiene el path real a partir de la uri retornada por la galería.
                    Uri uriGaleria = data.getData();
                    sPathFotoOriginal = getRealPath(uriGaleria);
                    // Se escala la foto, se almacena en archivo propio y se muestra en ImageView.
                    cargarImagenEscalada(sPathFotoOriginal);
                    break;
            }
        }
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

    // Escala y muestra la imagen en el visor.
    private void cargarImagenEscalada(String pathFoto) {
        // Se utiliza una tarea asíncrona, para escalar, guardar en archivo propio y mostrar
        // la foto en el ImageView.
        MostrarFotoAsyncTask tarea = new MostrarFotoAsyncTask();
        tarea.execute(pathFoto);
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

    private class MostrarFotoAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // Se escala la foto, cuyo path corresponde al primer parámetro,
            // retornado el Bitmap correspondiente.
            return escalarFoto(
                    params[0],
                    getResources().getDimensionPixelSize(R.dimen.foto),
                    getResources().getDimensionPixelSize(R.dimen.foto));
        }

        // Una vez finalizado el hilo de trabajo. Se ejecuta en el hilo
        // principal. Recibe el Bitmap de la foto escalada (o null si error).
        @Override
        protected void onPostExecute(Bitmap bitmapFoto) {
            if (bitmapFoto != null) {
                // Se guarda la copia propia de la imagen.
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
                String nombre = "IMG_" + timestamp + "_" + ".jpg";
                File archivo = crearArchivoFoto(nombre, false);
                if (archivo != null) {
                    sRutaArchivo=archivo.getAbsolutePath();
                    if (guardarBitmapEnArchivo(bitmapFoto, archivo)) {
                        // Se muestra la foto en el ImageView.
                        imgFoto.setImageBitmap(bitmapFoto);
                    }
                }
            }
        }


        // Escala la foto indicada, para ser mostarda en un visor determinado.
        // Retorna el bitmap correspondiente a la imagen escalada o null si
        // se ha producido un error.
        private Bitmap escalarFoto(String pathFoto, int anchoVisor,
                                   int altoVisor) {
            try {
                // Se obtiene el tamaño de la imagen.
                BitmapFactory.Options opciones = new BitmapFactory.Options();
                opciones.inJustDecodeBounds = true; // Solo para cálculo.
                BitmapFactory.decodeFile(pathFoto, opciones);
                int anchoFoto = opciones.outWidth;
                int altoFoto = opciones.outHeight;
                // Se obtiene el factor de escalado para la imagen.
                int factorEscalado = Math.min(anchoFoto / anchoVisor, altoFoto
                        / altoVisor);
                // Se escala la imagen con dicho factor de escalado.
                opciones.inJustDecodeBounds = false; // Se escalará.
                opciones.inSampleSize = factorEscalado;
                return BitmapFactory.decodeFile(pathFoto, opciones);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // Guarda el bitamp de la foto en un archivo. Retorna si ha ido bien.
        private boolean guardarBitmapEnArchivo(Bitmap bitmapFoto, File archivo) {
            try {
                FileOutputStream flujoSalida = new FileOutputStream(
                        archivo);
                bitmapFoto.compress(Bitmap.CompressFormat.JPEG, 100, flujoSalida);
                flujoSalida.flush();
                flujoSalida.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public void save() {
        mRecipe.setDiners(Integer.valueOf(txtComensales.getText().toString()));
        mRecipe.setDetails(txtDescripcion.getText().toString());
        mRecipe.setTime(Integer.valueOf(txtTiempo.getText().toString()));
        mRecipe.setName(txtNombre.getText().toString());
        mRecipe.setDifficulty(Integer.valueOf(txtDificultad.getText().toString()));

    }
}

