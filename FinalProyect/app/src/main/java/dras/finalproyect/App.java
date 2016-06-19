package dras.finalproyect;

import android.app.Application;


import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import dras.finalproyect.pojos.Recipe;
import dras.finalproyect.pojos.Respuesta;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Dras on 28/05/2016.
 */
public class App extends Application {
    private static final String BASE_URL = "http://52.31.144.145/api/v1/";
    private static final String AUTH = "authorization";
    public static final String PREF_USER = "saved user_id";
    public static final String PREF_API = "saved api_key";
    public static final String PREF_NAME = "pref";
    private static APIinterface servicio;
    public static String api_key;
    public static String user_id;
    public static Recipe mRecipeActual;


    public interface APIinterface {
        @FormUrlEncoded
        @POST("register")
        Call<Respuesta> registro(@Field("user") String user, @Field("password") String pass);

        @FormUrlEncoded
        @POST("login")
        Call<Respuesta> login(@Field("user") String user, @Field("password") String pass);


        @GET("recipes")
        Call<Respuesta> obtenerRecetas();

        @GET("ingredients")
        Call<Respuesta> obtenerIngredientes();

        @GET("favs/user/{id}")
        Call<Respuesta> obtenerFavs(@Path("id") String idusuario);

        @GET("recipes/user/{id}")
        Call<Respuesta> obtenerCreados(@Path("id") String idusuario);

        @GET("createdandfav/user/{id}")
        Call<Respuesta> obtenerFavsAndCreados(@Path("id") String idusuario);

        @GET("recipes/{id}")
        Call<Respuesta> obtenerDatosReceta(@Path("id") int idreceta);

        @GET("filter/{name}")
        Call<Respuesta> filtrarNombre(@Path("name") String filtro);

        @GET("filter")
        Call<Respuesta> filtrarAvanzado(@Field("min") int min, @Field("max") int max, @Field("diners") int comensales, @Field("dificultad") int dificultad);

        @FormUrlEncoded
        @POST("recipes")
        Call<Respuesta> registrarReceta(@Header(AUTH) String key, @Field("recipe") String recetaJson);

        @POST("favs/{id}")
        Call<Respuesta> agregarFav(@Header(AUTH) String key, @Path("id") int idreceta);

        @GET("favs/{id}")
        Call<Respuesta> comprobarFav(@Header(AUTH) String key, @Path("id") int idreceta);

        @DELETE("favs/{id}")
        Call<Respuesta> borrarFav(@Header(AUTH) String key, @Path("id") int idreceta);


    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Se crea el servicio.
        servicio = retrofit.create(APIinterface.class);

    }

    public static APIinterface getServicio() {
        return servicio;
    }


}
