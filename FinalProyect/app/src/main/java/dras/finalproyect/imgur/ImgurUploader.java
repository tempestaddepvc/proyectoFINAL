package dras.finalproyect.imgur;


import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import dras.finalproyect.App;
import dras.finalproyect.pojos.MyClass;
import dras.finalproyect.pojos.Recipe;
import dras.finalproyect.pojos.Respuesta;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImgurUploader {

    private final LinkedList<MyClass> mImagenes;


    public ImgurUploader(Recipe recipe) {
        mImagenes = new LinkedList<>();
        mImagenes.add((MyClass) recipe);
        mImagenes.addAll((Collection<? extends MyClass>) recipe.getSteps());

    }


    public void upload() {
        if (mImagenes.size() > 0) {
            if (mImagenes.getFirst().getPicture().isEmpty()) {
                mImagenes.removeFirst();
                upload();
            } else if (!mImagenes.getFirst().getPicture().contains("http")) {
                subirImagen(mImagenes.getFirst());
            }
        } else {
            Gson gson = new Gson();
            String a = gson.toJson(App.mRecipeActual);
            App.getServicio().registrarReceta(App.api_key, a).enqueue(new Callback<Respuesta>() {
                @Override
                public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                    Respuesta respuesta = response.body();
                    if (respuesta != null)
                        if (!respuesta.getError()) {
                            App.mRecipeActual.setIdrecipe(((Double) respuesta.getMessage()).intValue());

                        }
                        //Se ha producido un error
                        else {
                            Log.e("FAIL1 - Edit:", respuesta.getMessage().toString());
                        }

                }

                @Override
                public void onFailure(Call<Respuesta> call, Throwable t) {
                    Log.e("FAIL2 - Edit:", t.toString());
                }
            });
        }


    }

    //Sube las imagenes a la Api Imgur y guarda las url que den como resultado en el objeto Anuncio.
    private void subirImagen(MyClass myClass) {
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), new File(myClass.getPicture()));

        Call<ImgurResponse> llamada = ImgurAPI.getMInstance().getService().uploadImage(body);
        llamada.enqueue(new Callback<ImgurResponse>() {
            @Override
            public void onResponse(Call<ImgurResponse> call, Response<ImgurResponse> response) {
                ImgurResponse respuesta = response.body();
                //Se añade la urls del bitmap escogido
                if (respuesta != null) {
                    mImagenes.getFirst().setPicture(respuesta.getData().getLink());
                }
                mImagenes.removeFirst();
                upload();
            }

            @Override
            public void onFailure(Call<ImgurResponse> call, Throwable t) {

            }
        });
    }
}

