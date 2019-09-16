package servidorarchivobarra;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorArchivoBarra {

    public static void main(String[] args) {

        ServerSocket server = null;
        Socket connection = null;

        if (args.length == 1) {
            if (Metodos.Numerico(args[0])) {

                try {
                    server = new ServerSocket(Integer.parseInt(args[0]));
                } catch (IOException ex) {
                    System.out.println("Error al crear el socket " + ex);
                    System.exit(1);
                }

                while (true) {

                    try {
                        connection = server.accept();
                    } catch (IOException ex) {
                        System.out.println("Error con el servidor " + ex);
                        System.exit(2);
                    }

                    BufferedReader lector = null;
                    try {
                        lector = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    } catch (IOException ex) {
                        System.out.println("Error al intentar leer " + ex);
                        System.exit(3);
                    }

                    PrintWriter escritor = null;
                    try {
                        escritor = new PrintWriter(connection.getOutputStream(), true);
                    } catch (IOException ex) {
                        System.out.println("Error al crear el escritor " + ex);
                        System.exit(4);
                    }
                    String entrada = "";
                    try {
                        if ((entrada = lector.readLine()) != null) {
                            System.out.println(entrada);

                           
                            final File localFile = new File(entrada);
                            long size = localFile.length();
                            escritor.println(size);
                            if (localFile.exists()) {
                                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(localFile));
                                BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
                                byte[] byteArray;

                                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                                dos.writeUTF(localFile.getName());

                                byteArray = new byte[8192];
                                int in;
                                while ((in = bis.read(byteArray)) != -1) {
                                    bos.write(byteArray, 0, in);
                                }
                                bis.close();
                                bos.close();
                            } else {
                                System.out.println("Archivo no encontrado");
                            }

                        }
                    } catch (IOException ex) {
                        System.out.println("Error al leer una linea " + ex);
                        System.exit(5);
                    }
                }

            } else {
                System.out.println("!!Error!!. El puerto debe ser númerico (Argumento 1)");

            }
        } else {
            System.out.println("!!Error!!. Ingresa solamente el puerto");

        }

    }

}
