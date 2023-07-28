package com.example.desafio_6_sockets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarIndividualChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setTitle("Nome do Usuário"); // depois adicionar a variavel do nome do usuario aqui

        // (substituir depois pelas mensagens proprio/outro usuario)
        List<String> messageList = new ArrayList<>();
        messageList.add("Olá, tudo bem?");
        messageList.add("Sim, estou bem!");
        messageList.add("Como foi seu dia?");
        messageList.add("Meu dia foi ótimo!");

        // Encontra os layouts para adicionar as mensagens
        LinearLayout layoutMessageOwn = findViewById(R.id.layoutMessageOwn);
        LinearLayout layoutMessageOther = findViewById(R.id.layoutMessageOther);

        // Loop para adicionar mensagens ao layout (so pra preencher/teste, depois com a logica pode apagar)
        for (int i = 0; i < messageList.size(); i++) {
            String message = messageList.get(i);
            boolean isOwnMessage = (i % 2 == 0); // Alterne entre true e false a cada mensagem

            if (isOwnMessage) {
                addMensagemLayout(message, layoutMessageOwn, true);
            } else {
                addMensagemLayout(message, layoutMessageOther, false);
            }
        }
    }

    // Método para adicionar mensagens ao layout apropriado
    private void addMensagemLayout(String message, LinearLayout layout, boolean isOwnMessage) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(16);
        textView.setPadding(16, 8, 16, 8);

        if (isOwnMessage) {
            textView.setBackgroundColor(getResources().getColor(R.color.coral));
            textView.setTextColor(getResources().getColor(android.R.color.white));
            textView.setGravity(Gravity.END);
        } else {
            textView.setBackgroundColor(getResources().getColor(android.R.color.white));
            textView.setTextColor(getResources().getColor(android.R.color.black));
            textView.setGravity(Gravity.START);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 8, 8, 8);

        textView.setLayoutParams(layoutParams);
        layout.addView(textView);
    }
}



