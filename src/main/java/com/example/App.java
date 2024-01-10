package com.example;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        try {
            System.out.println("Server started and waiting for connections...");
            ServerSocket server = new ServerSocket(8080);
            while (true) {

                Socket s = server.accept();
                BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(s.getInputStream()));
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                String[] line;
                String f;
                line = in.readLine().split(" ");
                String filePath = line[1];
                do {
                    f = in.readLine();
                    //System.out.println(f);
                    
                } while (!f.isEmpty());
                if(filePath.endsWith("/")){
                    filePath = "index.html";
                }
                File file=null ;
                if( filePath.equals("/test")){
                    System.out.println(filePath);
                    out.writeBytes("HTTP/1.1 301 Moved Permanently"+"\n");
                    out.writeBytes("Location: https://www.google.com"+"\n");
                    out.writeBytes("\n");
                    }
                    else{
                        file = new File("htdocs" + filePath);        
                    }

                

                
            
                if (file.exists()) {
                    
                    sendBinaryFile(s, file);

                    
                } else {
                    String msg = "file non trovato";
                    out.writeBytes("HTTP/1.1 404 not found");
                    out.writeBytes("Content-Length: " + msg.length() + "\n");
                    out.writeBytes("Content-Type: text/plain\n");
                    out.writeBytes("\n");
                    out.writeBytes(msg);
                }
                s.close();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void sendBinaryFile(Socket s, File f) throws IOException {
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        out.writeBytes("HTTP/1.1 200 OK\n");
        out.writeBytes("Content-Length: " + f.length() + "\n");
        out.writeBytes("Content-Type: " + getContentType(f) + "\n");
        out.writeBytes("\n");
        InputStream input = new FileInputStream(f);
        byte[] buf = new byte[8192];
        int n;
        while ((n = input.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
        input.close();

    }

    private static String getContentType(File fa) {
        String[] s = fa.getName().split("\\.");
        String ext = s[s.length - 1];
        switch (ext) {
            case "html":
                return "text/html";
            case "png":
                return "image/png";
            case "css":
                return "text/css";
            default:
                return "";
        }

    }
}