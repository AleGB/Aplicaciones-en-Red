
import java.net.*;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author ale
 */
public class Servidor {

    public static void main(String[] args) {
        try {
            int pto = 8000;
            ServerSocket s = new ServerSocket(pto);
            s.setReuseAddress(true);
            System.out.println("Servidor iniciado");
            File f = new File("");
            String path = f.getAbsolutePath();
            String carpeta = "SERVIDOR";
            String pathLocal = path + "\\" + carpeta + "\\";
            String carpetaCliente = "CLIENTE";
            String pathCliente = path + "\\" + carpetaCliente + "\\";
            File f2 = new File(pathLocal);
            f2.mkdirs();
            f2.setWritable(true);

            for (;;) {
                Socket cl = s.accept();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                System.out.println("Cliente conectado desde " + cl.getInetAddress() + ":" + cl.getPort());
                System.out.println("HOME:" + pathLocal);
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                String respuesta;
                Servidor servidor = new Servidor();
                while (true) {
                    respuesta = br.readLine();
                    if (respuesta.compareToIgnoreCase("7") == 0) {
                        System.out.println("Cliente cierra conexion");
                        dis.close();
                        br.close();
                        pw.close();
                        cl.close();
                        break;
                    } else if (respuesta.compareToIgnoreCase("3") == 0) {
                        respuesta = br.readLine();
                        String pathNvo = br.readLine();
                        if (respuesta.compareToIgnoreCase("archivo") == 0) {
                            System.out.println("Petición para subir archivo");
                            servidor.recibir(cl, dis, pathNvo, "");
                        } else if (respuesta.compareToIgnoreCase("carpeta") == 0) {
                            System.out.println("Petición para subir carpeta");
                            String directoryName = br.readLine();
                            String destino = pathNvo + "\\" + directoryName + "\\";
                            File NuevaCarpeta = new File(destino);
                            NuevaCarpeta.mkdirs();
                            NuevaCarpeta.setWritable(true);
                            servidor.recibir(cl, dis, pathNvo, directoryName);
                            servidor.descomprimir(pathNvo + "/" + directoryName + ".zip", pathNvo, directoryName + ".zip");
                            File fileDelete = new File(pathNvo + "\\" + directoryName + ".zip" + "\\");
                            fileDelete.delete();
                        }
                    } else if (respuesta.compareToIgnoreCase("4") == 0) {
                        respuesta = br.readLine();
                        if (respuesta.compareToIgnoreCase("archivo") == 0) {
                            System.out.println("Petición para descargar archivo");
                            servidor.recibir(cl, dis, pathCliente, "");
                        } else if (respuesta.compareToIgnoreCase("carpeta") == 0) {
                            System.out.println("Petición para descargar carpeta");
                            String directoryName = br.readLine();
                            servidor.recibir(cl, dis, pathCliente, directoryName);
                            servidor.descomprimir(pathCliente + "/" + directoryName + ".zip", pathCliente, directoryName + ".zip");
                            File fileDelete = new File(pathCliente + "\\" + directoryName + ".zip" + "\\");
                            fileDelete.delete();
                        }
                    } else if (respuesta.compareToIgnoreCase("5") == 0) {
                        respuesta = br.readLine();
                        File nvaCarpeta = new File(respuesta);
                        nvaCarpeta.mkdirs();
                        nvaCarpeta.setWritable(true);
                    } else if (respuesta.compareToIgnoreCase("6") == 0) {
                        String directorio = br.readLine();
                        File fileDelete = new File(directorio);
                        servidor.EliminarCarpeta(fileDelete);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recibir(Socket cl, DataInputStream dis, String ruta_archivos, String directoryName) throws Exception {
        dis = new DataInputStream(cl.getInputStream());
        String nombre = dis.readUTF();
        long tam = dis.readLong();
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(ruta_archivos + nombre));
        long recibidos = 0;
        int l = 0, porcentaje = 0;
        while (recibidos < tam) {
            byte[] b = new byte[1500];
            l = dis.read(b);
            dos.write(b, 0, l);
            dos.flush();
            recibidos = recibidos + l;
            porcentaje = (int) ((recibidos * 100) / tam);
        }
        dos.close();
    }

    public void descomprimir(String ficheroZip, String directorioSalida, String nombre) throws Exception {
        System.out.println(directorioSalida);
        final int TAM_BUFFER = 4096;
        byte[] buffer = new byte[TAM_BUFFER];

        ZipInputStream flujo = null;
        try {
            flujo = new ZipInputStream(new BufferedInputStream(
                    new FileInputStream(ficheroZip)));
            ZipEntry entrada;
            while ((entrada = flujo.getNextEntry()) != null) {
                String nombreSalida = directorioSalida + File.separator
                        + entrada.getName();
                if (entrada.isDirectory()) {
                    File directorio = new File(nombreSalida);
                    directorio.mkdir();
                    directorio.setWritable(true);
                } else {
                    BufferedOutputStream salida = null;
                    try {
                        int leido;
                        salida = new BufferedOutputStream(
                                new FileOutputStream(nombreSalida), TAM_BUFFER);
                        while ((leido = flujo.read(buffer, 0, TAM_BUFFER)) != -1) {
                            salida.write(buffer, 0, leido);
                        }
                    } finally {
                        if (salida != null) {
                            salida.close();
                        }
                    }
                }
            }
        } finally {
            if (flujo != null) {
                flujo.close();
            }

        }
    }

    private void EliminarCarpeta(File pArchivo) {
        if (!pArchivo.exists()) {
            return;
        }

        if (pArchivo.isDirectory()) {
            for (File f : pArchivo.listFiles()) {
                EliminarCarpeta(f);
            }
        }
        pArchivo.delete();
    }
}
