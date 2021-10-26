
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Servidor {

    public static void main(String[] args) throws Exception {
        try {
            ServerSocket ss = new ServerSocket(1234);
            ss.setReuseAddress(true);
            System.out.println("Servidor iniciado");
            for (;;) {
                Socket cl = ss.accept();
                System.out.println("\nCliente conectado desde " + cl.getInetAddress() + ":" + cl.getPort() + "\n");
                BufferedReader br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
                ObjetoSopa ob = new ObjetoSopa();
                Servidor serv = new Servidor();
                String respuesta = br.readLine();
                int tam = 0;
                int x = 0;
                int y = 0;
                String palabra = "";
                if (respuesta.compareToIgnoreCase("5") == 0) {
                    System.out.println("Cliente cierra conexion");
                    ois.close();
                    oos.close();
                    br.close();
                    cl.close();
                    break;
                } else if (respuesta.compareToIgnoreCase("1") == 0) {
                    serv.inicializaTablero(ob);
                    for (int i = 0; i < 20; i++) {
                        palabra = ob.getAnimales(i);
                        tam = palabra.length();
                        x = serv.numRandom();
                        y = serv.numRandom();
                        serv.escribePalabras(tam, x, y, ob, palabra);
                    }
                    serv.rellenaTablero(ob);
                    System.out.println();
                    serv.imprimeTablero(ob);
                    oos.writeObject(ob);
                    oos.flush();
                    System.out.println("\nEnviando sopa de letras");
                } else if (respuesta.compareToIgnoreCase("2") == 0) {
                    serv.inicializaTablero(ob);
                    System.out.println("Lista de palabras a encontrar");
                    for (int i = 0; i < 20; i++) {
                        palabra = ob.getFrutas(i);
                        tam = palabra.length();
                        x = serv.numRandom();
                        y = serv.numRandom();
                        serv.escribePalabras(tam, x, y, ob, palabra);
                    }
                    serv.rellenaTablero(ob);
                    System.out.println();
                    serv.imprimeTablero(ob);
                    oos.writeObject(ob);
                    oos.flush();
                    System.out.println("\nEnviando sopa de letras");
                } else if (respuesta.compareToIgnoreCase("3") == 0) {
                    serv.inicializaTablero(ob);
                    System.out.println("Lista de palabras a encontrar");
                    for (int i = 0; i < 20; i++) {
                        palabra = ob.getPaises(i);
                        tam = palabra.length();
                        x = serv.numRandom();
                        y = serv.numRandom();
                        serv.escribePalabras(tam, x, y, ob, palabra);
                    }
                    serv.rellenaTablero(ob);
                    System.out.println();
                    serv.imprimeTablero(ob);
                    oos.writeObject(ob);
                    oos.flush();
                    System.out.println("\nEnviando sopa de letras");
                } else if (respuesta.compareToIgnoreCase("4") == 0) {
                    serv.inicializaTablero(ob);
                    System.out.println("Lista de palabras a encontrar");
                    for (int i = 0; i < 20; i++) {
                        palabra = ob.getOficina(i);
                        tam = palabra.length();
                        x = serv.numRandom();
                        y = serv.numRandom();
                        serv.escribePalabras(tam, x, y, ob, palabra);
                    }
                    serv.rellenaTablero(ob);
                    System.out.println();
                    serv.imprimeTablero(ob);
                    oos.writeObject(ob);
                    oos.flush();
                    System.out.println("\nEnviando sopa de letras");
                }
                ObjetoSopa ob2 = (ObjetoSopa) ois.readObject();
                System.out.println("\nEL CLIENTE HA TERMINADO EL JUEGO");
                System.out.println("Jugador " + cl.getInetAddress() + ":" + cl.getPort() + " -" + " Palabras: " + (ob2.getScore() / 10) + "/" + ob.palabras.size() + " - Puntaje: " + ob2.getScore() + " puntos - Tiempo: " + ob2.time + "seg");
                ob.palabras.clear();
                ob2.score = 0;
                ob2.time = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int numRandom() {
        return (int) (Math.random() * 15 + 0);
    }

    char letraRandom() {
        return (char) Math.floor(Math.random() * (122 - 97) + 97);
    }

    void inicializaTablero(ObjetoSopa ob) {
        for (int i = 0; i < ob.sopa.length; i++) {
            for (int j = 0; j < ob.sopa.length; j++) {
                ob.setSopa(i, j, ' ');
            }
        }
    }

    void imprimeTablero(ObjetoSopa ob) {
        int f = 0;
        System.out.print("  \t0");
        for (int i = 1; i < 16; i++) {
            System.out.print("\t" + i);
        }
        System.out.println();
        for (int i = 0; i < ob.sopa.length; i++) {
            if (f > 9) {
                System.out.print(f);
            } else {
                System.out.print(f + " ");
            }
            System.out.print("|");
            System.out.print(" ");
            for (int j = 0; j < ob.sopa[i].length; j++) {
                if (j != ob.sopa[i].length) {
                    System.out.print("\t");
                }
                System.out.print(ob.getSopa(i, j));

            }
            System.out.print(" ");
            System.out.println("|");
            f++;
        }
    }

    void escribePalabras(int tam, int x, int y, ObjetoSopa ob, String palabra) {
        if (escribeDerecha(tam, x, y, ob, palabra)) {
            ob.palabras.add(palabra);
            System.out.println("Palabra: " + palabra + " xi: " + x + " yi: " + y + " xf: " + x + " yf: " + (y + palabra.length() - 1));
        } else if (escribeD1(tam, x, y, ob, palabra)) {
            ob.palabras.add(palabra);
            System.out.println("Palabra: " + palabra + " xi: " + x + " yi: " + y + " xf: " + (x + palabra.length() - 1) + " yf: " + (y + palabra.length() - 1));
        } else if (escribeAbajo(tam, x, y, ob, palabra)) {
            ob.palabras.add(palabra);
            System.out.println("Palabra: " + palabra + " xi: " + x + " yi: " + y + " xf: " + (x + palabra.length() - 1) + " yf: " + y);
        } else if (escribeD2(tam, x, y, ob, palabra)) {
            ob.palabras.add(palabra);
            System.out.println("Palabra: " + palabra + " xi: " + x + " yi: " + y + " xf: " + (x + palabra.length() - 1) + " yf: " + (y + palabra.length() + 1));
        } else if (escribeIzq(tam, x, y, ob, palabra)) {
            ob.palabras.add(palabra);
            System.out.println("Palabra: " + palabra + " xi: " + x + " yi: " + y + " xf: " + x + " yf: " + (y - palabra.length() + 1));
        } else if (escribeD3(tam, x, y, ob, palabra)) {
            ob.palabras.add(palabra);
            System.out.println("Palabra: " + palabra + " xi: " + x + " yi: " + y + " xf: " + (x - palabra.length() + 1) + " yf: " + (y - palabra.length() + 1));
        } else if (escribeArriba(tam, x, y, ob, palabra)) {
            ob.palabras.add(palabra);
            System.out.println("Palabra: " + palabra + " xi: " + x + " yi: " + y + " xf: " + (x - palabra.length() + 1) + " yf: " + y);
        } else if (escribeD4(tam, x, y, ob, palabra)) {
            ob.palabras.add(palabra);
            System.out.println("Palabra: " + palabra + " xi: " + x + " yi: " + y + " xf: " + (x - palabra.length() + 1) + " yf: " + (y + palabra.length() - 1));
        }
    }

    void rellenaTablero(ObjetoSopa ob) {
        for (int i = 0; i < ob.sopa.length; i++) {
            for (int j = 0; j < ob.sopa[i].length; j++) {
                if (ob.sopa[i][j] == ' ') {
                    ob.setSopa(i, j, letraRandom());
                }
            }
        }
    }

    boolean escribeDerecha(int tam, int x, int y, ObjetoSopa ob, String palabra) {
        char c = ob.getSopa(x, y);
        boolean bandera = false;
        int a = x, b = y;
        if (15 - y - 1 >= tam) {
            for (int i = 0; i < tam; i++) {
                if (c == ' ') {
                    bandera = true;
                    y++;
                    c = ob.getSopa(x, y);
                } else {
                    bandera = false;
                    break;
                }
            }
        }
        if (bandera) {
            for (int j = 0; j < tam; j++) {
                c = palabra.charAt(j);
                ob.setSopa(a, b, c);
                b++;
            }
        }
        return bandera;
    }

    boolean escribeD1(int tam, int x, int y, ObjetoSopa ob, String palabra) {
        char c = ob.getSopa(x, y);
        boolean bandera = false;
        int a = x, b = y;
        if ((15 - y - 1) >= tam && (15 - x - 1) >= tam) {
            for (int i = 0; i < tam; i++) {
                if (c == ' ') {
                    bandera = true;
                    x++;
                    y++;
                    c = ob.getSopa(x, y);
                } else {
                    bandera = false;
                    break;
                }
            }
        }
        if (bandera) {
            for (int j = 0; j < tam; j++) {
                c = palabra.charAt(j);
                ob.setSopa(a, b, c);
                a++;
                b++;
            }
        }
        return bandera;
    }

    boolean escribeAbajo(int tam, int x, int y, ObjetoSopa ob, String palabra) {
        char c = ob.getSopa(x, y);
        boolean bandera = false;
        int a = x, b = y;
        if ((15 - x - 1) >= tam) {
            for (int i = 0; i < tam; i++) {
                if (c == ' ') {
                    bandera = true;
                    x++;
                    c = ob.getSopa(x, y);
                } else {
                    bandera = false;
                    break;
                }
            }
        }
        if (bandera) {
            for (int j = 0; j < tam; j++) {
                c = palabra.charAt(j);
                ob.setSopa(a, b, c);
                a++;
            }
        }
        return bandera;
    }

    boolean escribeD2(int tam, int x, int y, ObjetoSopa ob, String palabra) {
        char c = ob.getSopa(x, y);
        boolean bandera = false;
        int a = x, b = y;
        if ((y + 1) >= tam && (15 - x - 1) >= tam) {
            for (int i = 0; i < tam; i++) {
                if (c == ' ') {
                    bandera = true;
                    x++;
                    y--;
                    c = ob.getSopa(x, y);
                } else {
                    bandera = false;
                    break;
                }
            }
        }
        if (bandera) {
            for (int j = 0; j < tam; j++) {
                c = palabra.charAt(j);
                ob.setSopa(a, b, c);
                a++;
                b--;
            }
        }
        return bandera;
    }

    boolean escribeIzq(int tam, int x, int y, ObjetoSopa ob, String palabra) {
        char c = ob.getSopa(x, y);
        boolean bandera = false;
        int a = x, b = y;
        if ((y + 1) >= tam) {
            for (int i = 0; i < tam; i++) {
                if (c == ' ') {
                    bandera = true;
                    y--;
                    c = ob.getSopa(x, y);
                } else {
                    bandera = false;
                    break;
                }
            }
        }
        if (bandera) {
            for (int j = 0; j < tam; j++) {
                if (b > -1) {
                    c = palabra.charAt(j);
                    ob.setSopa(a, b, c);
                } else {
                    break;
                }
                b--;
            }
        }
        return bandera;
    }

    boolean escribeD3(int tam, int x, int y, ObjetoSopa ob, String palabra) {
        char c = ob.getSopa(x, y);
        boolean bandera = false;
        int a = x, b = y;
        if ((y + 1) >= tam && (x + 1) >= tam) {
            for (int i = 0; i < tam; i++) {
                if (c == ' ') {
                    bandera = true;
                    x--;
                    y--;
                    c = ob.getSopa(x, y);
                } else {
                    bandera = false;
                    break;
                }
            }
        }
        if (bandera) {
            for (int j = 0; j < tam; j++) {
                c = palabra.charAt(j);
                ob.setSopa(a, b, c);
                a--;
                b--;
            }
        }
        return bandera;
    }

    boolean escribeArriba(int tam, int x, int y, ObjetoSopa ob, String palabra) {
        char c = ob.getSopa(x, y);
        boolean bandera = false;
        int a = x, b = y;
        if ((x + 1) >= tam) {
            for (int i = 0; i < tam; i++) {
                if (c == ' ') {
                    bandera = true;
                    x--;
                    c = ob.getSopa(x, y);
                } else {
                    bandera = false;
                    break;
                }
            }
        }
        if (bandera) {
            for (int j = 0; j < tam; j++) {
                c = palabra.charAt(j);
                ob.setSopa(a, b, c);
                a--;
            }
        }
        return bandera;
    }

    boolean escribeD4(int tam, int x, int y, ObjetoSopa ob, String palabra) {
        char c = ob.getSopa(x, y);
        boolean bandera = false;
        int a = x, b = y;
        if ((15 - y - 1) >= tam && (x + 1) >= tam) {
            for (int i = 0; i < tam; i++) {
                if (c == ' ') {
                    bandera = true;
                    x--;
                    y++;
                    c = ob.getSopa(x, y);
                } else {
                    bandera = false;
                    break;
                }
            }
        }
        if (bandera) {
            for (int j = 0; j < tam; j++) {
                c = palabra.charAt(j);
                ob.setSopa(a, b, c);
                a--;
                b++;
            }
        }
        return bandera;
    }
}
