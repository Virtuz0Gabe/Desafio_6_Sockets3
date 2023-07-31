package com.example.desafio_6_sockets.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.desafio_6_sockets.Controller.ChatClient;
import com.example.desafio_6_sockets.R;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private ChatClient chatClient;

    // buscando elementos do layout
    ImageButton imageButtonSend;
    EditText editTextMessage;
    private ChatClient socketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButtonSend = findViewById(R.id.imageButtonSend);
        editTextMessage = findViewById(R.id.editTextMessage);

        // Inicia a conexão com o servidor Socket quando a Activity for criada
        socketClient = new ChatClient(this);
        socketClient.start();

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = String.valueOf(editTextMessage.getText());
                editTextMessage.setText("");
                socketClient.sendMessage(message);

                LinearLayout layoutParent = findViewById(R.id.message_space);
                View layoutChild = getLayoutInflater().inflate(R.layout.inflate_own_msg, null);
                TextView textViewMessage = layoutChild.findViewById(R.id.txt_message);
                textViewMessage.setText(message);
                layoutParent.addView(layoutChild);

            }
        });
    }

    public void showReceiveMessage (String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout layoutParent = findViewById(R.id.message_space);
                View layoutChild = getLayoutInflater().inflate(R.layout.inflate_other_msg, null);
                TextView textViewMessage = layoutChild.findViewById(R.id.txt_message);
                textViewMessage.setText(message);
                layoutParent.addView(layoutChild);
            }
        });
    }
}



