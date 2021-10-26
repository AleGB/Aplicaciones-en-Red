
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;

/**
 *
 * @author ale
 */
public class Cliente {

    public static void main(String[] args) {
        try {
            int pto = 8000;
            String dir = "localhost";
            Socket cl = new Socket(dir, pto);
            System.out.println("Conexion con servidor establecida...");
            File f = new File("");
            String path = f.getAbsolutePath();
            String carpetaLocal = "CLIENTE";
            String carpetaServidor = "SERVIDOR";
            String pathLocal = path + "\\" + carpetaLocal + "\\";
            String pathServidor = path + "\\" + carpetaServidor + "\\";
            File f2 = new File(pathLocal);
            f2.mkdirs();
            f2.setWritable(true);

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            JFileChooser jfC = new JFileChooser(pathLocal);
            JFileChooser jfS = new JFileChooser(pathServidor);
            String respuesta;
            Cliente cliente = new Cliente();

            while (true) {
                System.out.println();
                System.out.println("----------------------------------------------");
                System.out.println("HOME:" + pathServidor);
                System.out.println("\n1. Cambiar directorio\n");
                System.out.println("2. Ver elementos del directorio actual\n");
                System.out.println("3. Subir archivo/carpeta\n");
                System.out.println("4. Descargar archivo/carpeta\n");
                System.out.println("5. Crear nuevo directorio\n");
                System.out.println("6. Eliminar archivo/carpeta\n");
                System.out.println("7. Salir\n");
                System.out.println("Elige una opcion: ");
                respuesta = br.readLine();
                pw.println(respuesta);
                pw.flush();
                if (respuesta.compareToIgnoreCase("7") == 0) {
                    dos.close();
                    br1.close();
                    br.close();
                    pw.close();
                    System.exit(0);
                } else if (respuesta.compareToIgnoreCase("1") == 0) {
                    File Directorios = new File(pathServidor);
                    File[] carpetas = Directorios.listFiles();
                    if (carpetas == null || carpetas.length == 0) {
                        System.out.println("\nNo hay directorios");
                        return;
                    } else {
                        System.out.println("\nDirectorios: \n");
                        for (int i = 0; i < carpetas.length; i++) {
                            if (carpetas[i].isDirectory()) {
                                System.out.println("'" + carpetas[i].getName() + "'");
                            }
                        }
                    }
                    System.out.println("\nEscribe el nombre de la carpeta: ");
                    respuesta = br.readLine();
                    if (respuesta.compareToIgnoreCase("..") == 0) {
                    } else {
                        pathServidor = pathServidor + "\\" + respuesta + "\\";
                    }
                    cliente.promptEnterKey();
                } else if (respuesta.compareToIgnoreCase("2") == 0) {
                    cliente.listarElementos(pathServidor);
                    cliente.promptEnterKey();
                } else if (respuesta.compareToIgnoreCase("3") == 0) {
                    jfC.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    int r = jfC.showOpenDialog(null);
                    if (r == JFileChooser.APPROVE_OPTION) {
                        File fe = jfC.getSelectedFile();
                        String nombre = fe.getName();
                        String pathSelect = fe.getAbsolutePath();
                        File file = new File(pathSelect);
                        if (file.isFile()) {
                            respuesta = "archivo";
                            pw.println(respuesta);
                            pw.flush();
                            respuesta = pathServidor;
                            pw.println(respuesta);
                            pw.flush();
                            cliente.enviar(fe, pathSelect, nombre, dos);
                        } else if (file.isDirectory()) {
                            respuesta = "carpeta";
                            pw.println(respuesta);
                            pw.flush();
                            respuesta = pathServidor;
                            pw.println(respuesta);
                            pw.flush();
                            respuesta = file.getName();
                            pw.println(respuesta);
                            pw.flush();

                            Cliente comp = new Cliente();
                            String nuevaPath = pathSelect;
                            String destino = nuevaPath + ".zip";
                            comp.comprimir(nuevaPath, destino);
                            File filezip = new File(destino);
                            String nombre2 = filezip.getName();
                            cliente.enviar(filezip, destino, nombre2, dos);
                            System.out.println("\nSu archivo se agrego exitosamente al servidor...");
                            File fileDelete = new File(destino);
                            fileDelete.delete();
                        }
                    }
                    cliente.promptEnterKey();
                } else if (respuesta.compareToIgnoreCase("4") == 0) {
                    jfS.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    int r = jfS.showOpenDialog(null);
                    if (r == JFileChooser.APPROVE_OPTION) {
                        File fe = jfS.getSelectedFile();
                        String nombre = fe.getName();
                        String pathSelect = fe.getAbsolutePath();
                        File file = new File(pathSelect);
                        if (file.isFile()) {
                            respuesta = "archivo";
                            pw.println(respuesta);
                            pw.flush();
                            cliente.enviar(fe, pathSelect, nombre, dos);
                        } else if (file.isDirectory()) {
                            respuesta = "carpeta";
                            pw.println(respuesta);
                            pw.flush();
                            respuesta = file.getName();
                            pw.println(respuesta);
                            pw.flush();

                            String nuevaPath = pathSelect;
                            String destino = nuevaPath + ".zip";
                            cliente.comprimir(nuevaPath, destino);
                            File filezip = new File(destino);
                            String nombre2 = filezip.getName();
                            cliente.enviar(filezip, destino, nombre2, dos);
                            System.out.println("\nSu archivo se descargó exitosamente...");
                            File fileDelete = new File(destino);
                            fileDelete.delete();
                        }
                    }
                    cliente.promptEnterKey();
                } else if (respuesta.compareToIgnoreCase("5") == 0) {
                    cliente.listarElementos(pathServidor);
                    System.out.println("\nNombre de la carpeta: ");
                    respuesta = br.readLine();
                    String nvaCarpeta = pathServidor + "\\" + respuesta + "\\";
                    pw.println(nvaCarpeta);
                    pw.flush();
                    cliente.promptEnterKey();
                } else if (respuesta.compareToIgnoreCase("6") == 0) {
                    cliente.listarElementos(pathServidor);
                    System.out.println("\nNombre del archivo o carpeta: ");
                    respuesta = br.readLine();
                    String directorio = pathServidor + "\\" + respuesta + "\\";
                    pw.println(directorio);
                    pw.flush();
                    cliente.promptEnterKey();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarCarpeta(String ruta, String carpeta, ZipOutputStream zip) throws Exception {
        File directorio = new File(carpeta);
        for (String nombreArchivo : directorio.list()) {
            if (ruta.equals("")) {
                agregarArchivo(directorio.getName(), carpeta + "/" + nombreArchivo, zip);
            } else {
                agregarArchivo(ruta + "/" + directorio.getName(), carpeta + "/" + nombreArchivo, zip);
            }
        }
    }

    public void agregarArchivo(String ruta, String directorio, ZipOutputStream zip) throws Exception {
        File archivo = new File(directorio);
        if (archivo.isDirectory()) {
            agregarCarpeta(ruta, directorio, zip);
        } else {
            byte[] buffer = new byte[4096];
            int leido;
            FileInputStream entrada = new FileInputStream(archivo);
            zip.putNextEntry(new ZipEntry(ruta + "/" + archivo.getName()));
            while ((leido = entrada.read(buffer)) > 0) {
                zip.write(buffer, 0, leido);
            }
        }
    }

    public void comprimir(String archivo, String archivoZIP) throws Exception {
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(archivoZIP));
        agregarCarpeta("", archivo, zip);
        zip.flush();
        zip.close();
    }

    public void enviar(File fe, String path, String nombre, DataOutputStream dos) throws Exception {
        long tam = fe.length();
        DataInputStream dis = new DataInputStream(new FileInputStream(path));
        dos.writeUTF(nombre);
        dos.flush();
        dos.writeLong(tam);
        dos.flush();
        long enviados = 0;
        int l = 0, porcentaje = 0;
        while (enviados < tam) {
            byte[] b = new byte[1500];
            l = dis.read(b);
            dos.write(b, 0, l);
            dos.flush();
            enviados = enviados + l;
            porcentaje = (int) ((enviados * 100) / tam);
        }
        dis.close();
    }

    public void listarElementos(String pathServidor) {
        File Directorio = new File(pathServidor);
        File[] listado = Directorio.listFiles();
        if (listado == null || listado.length == 0) {
            System.out.println("No hay elementos dentro de la carpeta actual");
        } else {
            System.out.println("Directorios: \n");
            for (int i = 0; i < listado.length; i++) {
                if (listado[i].isDirectory()) {
                    System.out.println("'" + listado[i].getName() + "'");
                }
            }
            System.out.println("\nArchivos: \n");
            for (int i = 0; i < listado.length; i++) {
                if (listado[i].isFile()) {
                    System.out.println("'" + listado[i].getName() + "'");
                }
            }
        }
    }

    public void promptEnterKey() {
        System.out.println("\nPress \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}
