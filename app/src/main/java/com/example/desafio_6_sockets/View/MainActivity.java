package com.example.desafio_6_sockets.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.desafio_6_sockets.Controller.ChatClient;
import com.example.desafio_6_sockets.R;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Gabes";
    private ChatClient chatClient;

    ImageButton imageButtonSend;
    EditText editTextMessage;
    ImageButton imageButtonAttach;
    private ActivityResultLauncher<Intent> galleryLauncher;

    private ChatClient socketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButtonSend = findViewById(R.id.imageButtonSend);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageButtonAttach = findViewById(R.id.imageButtonAttach);

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

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Uri selectedImageUri = result.getData().getData();
                            try {
                                // Obtenha os bytes da imagem selecionada
                                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                                String imageBytes = convertInputStreamToBase64(inputStream);

                                // Envie a imagem para o servidor através do ChatClient
                                // ALTERAR O METODO PARA ENVIAR OS 64BITE PARA O SERVIDOR
                                socketClient.sendImage(imageBytes);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // Listener de clique para o botão de anexo
        imageButtonAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Botão de anexo clicado.");
                openGallery();
            }
        });
    }

    private void openGallery() {
        Log.d(TAG, "Botão de anexo clicado. Abrindo a galeria.");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Inicie a atividade esperando um resultado usando o launcher para ActivityResult
        galleryLauncher.launch(intent);

    }

    private String convertInputStreamToBase64(InputStream inputStream) throws IOException {
        byte[] imageBytes = IOUtils.toByteArray(inputStream);
        // Codifique os bytes da imagem em base64
        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return base64Image;
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Finalizando o app");
        //socketClient.close();
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



