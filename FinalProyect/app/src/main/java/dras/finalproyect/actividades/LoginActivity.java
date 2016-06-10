package dras.finalproyect.actividades;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.dialogos.DialogoRegistro;
import dras.finalproyect.pojos.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements DialogoRegistro.DialogRegsListener {

    private App.APIinterface servicio;
    private SharedPreferences preferences;
    private Button button;
    private TextView txtUser;
    private TextView txtPass;
    private Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        servicio = App.getServicio();
        preferences = getSharedPreferences(App.PREF_NAME, MODE_PRIVATE);

        String user = preferences.getString(App.PREF_USER, "");
        String api = preferences.getString(App.PREF_API, "");
        if (!user.isEmpty() && !api.isEmpty())
            logear(user,api);

        init();
    }

    private void init() {
        txtUser = (TextView) findViewById(R.id.txtUser);
        txtPass = (TextView) findViewById(R.id.txtPass);
        switch1 = (Switch) findViewById(R.id.switch1);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUser.getText().toString().isEmpty() ||  txtPass.getText().toString().isEmpty() )
                    Toast.makeText(getApplicationContext(), "Los campos usuario y contraseña no pueden estar vacios", Toast.LENGTH_SHORT).show();
                else
                    ok();
            }
        });
    }

    private void ok() {
        servicio.login(txtUser.getText().toString(), txtPass.getText().toString()).enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                //No se ha podido loguear
                Respuesta respuesta = response.body();
                if (respuesta.getError()) {
                    //Si usuario no existe
                    if (respuesta.getMessage().equals("1"))
                        new DialogoRegistro().show(getSupportFragmentManager(),"Registro");
                    else if (respuesta.getMessage().equals("2"))
                    //Si la contraseña es incorrecta
                        Toast.makeText(getApplicationContext(),"Contraseña Incorrecta",Toast.LENGTH_SHORT).show();
                    else
                    //Si ocurrre un reror inesperado
                        Toast.makeText(getApplicationContext(),"Error Inesperado",Toast.LENGTH_SHORT).show();

                }
                //Se ha logeado correctamente
                else {
                    if (switch1.isChecked())
                        guardarPref(txtUser.getText().toString(), respuesta.getMessage().toString());
                    logear(txtUser.getText().toString(), respuesta.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Log.e("FAIL", t.toString());
            }
        });


    }

    private void logear(String usuario, String api_key) {
        App.user_id = usuario;
        App.api_key = api_key;
        MainActivity.start(this);
    }

    private void guardarPref(String usuario, String api_key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(App.PREF_USER, usuario);
        editor.putString(App.PREF_API, api_key);
        editor.apply();
        Log.e("FAIL", "Guardadas");
    }


    @Override
    public void onPositiveButtonClick() {
        registro();
    }

    private void registro() {
        servicio.registro(txtUser.getText().toString(),txtPass.getText().toString()).enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                if (respuesta.getError()) {
                        Toast.makeText(getApplicationContext(),respuesta.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
                else {
                    ok();
                }
            }
            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Log.e("FAIL", t.toString());
            }
        });
    }


}
