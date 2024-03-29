package ifsp.doarmario.view.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ifsp.doarmario.R;
import ifsp.doarmario.model.dao.DbHelper;
import ifsp.doarmario.model.dao.UsuarioDao;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtSenha;
    private EditText txtRecEmail;

    private String emailDigitado;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //inicializar variaveis estáticas usadas p/ conexão c/ banco
        DbHelper.database = new DbHelper(getApplicationContext());
        UsuarioDao.usuarioDao = new UsuarioDao();

        //referenciar componentes da tela
        txtEmail = findViewById(R.id.editEmail);
        txtSenha = findViewById(R.id.editSenha);

        //obter preferências do usuário
        preferences = getSharedPreferences("usuario_detalhes", MODE_PRIVATE);

        //se o usuário já fez o login no sistema
        if(preferences.getString("nome_usuario", null) != null) {
            String usuarioLogado =  preferences.getString("nome_usuario", null);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("usuario", usuarioLogado);
            startActivity(intent);
            finish();
        }
    }

    //executado ao clicar no botão de login
    public void login(View view) {
        boolean verifica = false;
        String email = txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();

        //verifica se e-mail está cadastrado
        verifica = UsuarioDao.usuarioDao.login(email);

        if(verifica){
            String nomeUsuario = UsuarioDao.usuarioDao.verificaSenha(email, senha);

            //verifica se a senha é compatível
            if(nomeUsuario != null) {
                // adiciona nome_usuario às preferências
                // para que na próxima vez que o usuário iniciar o app
                // não apareça o login novamente
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nome_usuario", nomeUsuario);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("usuario", nomeUsuario);
                startActivity(intent);
                finish();
            } else {
                txtSenha.setError(getString(R.string.senha_incorreta));
            }
        }else {
            txtEmail.setError(getString(R.string.email_inexistente));
        }
    }

    //executado ao clicar no botão nova conta
    public void novaConta(View view) {
        //iniciar activity cadastro
        Intent i = new Intent(LoginActivity.this, AddUsuarioActivity.class);
        startActivity(i);
        finish();
    }
}
