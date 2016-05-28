package dras.finalproyect.actividades;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import dras.finalproyect.App;
import dras.finalproyect.R;
import dras.finalproyect.pojos.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

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
                ok();
            }
        });
    }

    private void ok() {

        servicio.login(txtUser.getText().toString(), txtPass.getText().toString()).enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                //No se ha podido loguear
                Respuesta resuesta = response.body();
                if (resuesta.getError()=="true") {
                    Log.e("FAIL1", resuesta.getMessage());
                }
                //Se ha logeado correctamente
                else {
                    Log.e("FAIL", "Todo Ok");
                    if (switch1.isChecked())
                        guardarPref(txtUser.getText().toString(), resuesta.getMessage());
                    logear(txtUser.getText().toString(), resuesta.getMessage());
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


}
