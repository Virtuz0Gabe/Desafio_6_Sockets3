package com.example.desafio_6_sockets.Controller;

import android.util.Log;

import com.example.desafio_6_sockets.View.MainActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient extends Thread {
    private static final String SERVER_IP = "192.168.0.108"; // Endereço IP do servidor
    private static final int SERVER_PORT = 4000; // Porta do servidor
    private static final String TAG = "Gabes";
    private BufferedWriter out;
    private MainActivity mainActivity;

    public ChatClient (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        try {
            Socket clientSocket = new Socket(SERVER_IP, SERVER_PORT);

            // Para enviar mensgagens para o servidor
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            // Receber resposta do servidor
            receiveMessageLoop(clientSocket);
            //BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //String serverResponse = in.readLine();

            //in.close();
            //clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessageLoop (Socket clientSocket) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        String serverResponse = in.readLine();
                        Log.i(TAG, "Mensagem recebida do servidor: " + serverResponse);
                        mainActivity.showReceiveMessage(serverResponse);
                    } catch (IOException e){
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.start();
    }
    public void sendMessage(String message) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    Log.i(TAG, "Mensagem a ser enviada: " + message);
                    out.write(message);
                    out.newLine();
                    out.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }
}


