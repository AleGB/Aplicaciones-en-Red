
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.*;
import java.nio.*;
import java.net.*;
import java.util.Iterator;

/**
 *
 * @author Ale
 */
public class Servidor {

    public static void main(String[] args) {
        int pto = 9999;
        try {
            ServerSocketChannel s = ServerSocketChannel.open();
            s.configureBlocking(false);
            s.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            s.socket().bind(new InetSocketAddress(pto));
            Selector sel = Selector.open();
            s.register(sel, SelectionKey.OP_ACCEPT);
            System.out.println("Servicio iniciado..esperando clientes..");
            Servidor serv = new Servidor();
            while (true) {
                sel.select();
                Iterator<SelectionKey> it = sel.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey k = (SelectionKey) it.next();
                    it.remove();
                    if (k.isAcceptable()) {
                        SocketChannel cl = s.accept();
                        System.out.println("Cliente conectado desde->" + cl.socket().getInetAddress().getHostAddress() + ":" + cl.socket().getPort());
                        cl.configureBlocking(false);
                        cl.register(sel, SelectionKey.OP_READ);
                        continue;
                    }//if
                    if (k.isReadable()) {
                        SocketChannel ch = (SocketChannel) k.channel();
                        ByteBuffer b = ByteBuffer.allocate(2000);
                        b.clear();
                        int n = ch.read(b);
                        b.flip();
                        String msj = new String(b.array(), 0, n);
                        if (msj.equalsIgnoreCase("SALIR")) {
                            System.out.println("Mensaje recibido: " + msj + "\nCliente se va..");
                            ch.close();
                            continue;
                        } else {
                            ObjetoSopa ob = new ObjetoSopa();
                            String palabra = "";
                            int tam = 0;
                            int x = 0;
                            int y = 0;
                            serv.inicializaTablero(ob);
                            System.out.println("Lista de palabras a encontrar");
                            if (msj.compareToIgnoreCase("1") == 0) {
                                ob.cate = "1";
                                for (int i = 0; i < 20; i++) {
                                    palabra = ob.getAnimales(i);
                                    tam = palabra.length();
                                    x = serv.numRandom();
                                    y = serv.numRandom();
                                    serv.escribePalabras(tam, x, y, ob, palabra);
                                }
                            } else if (msj.compareToIgnoreCase("2") == 0) {
                                ob.cate = "2";
                                for (int i = 0; i < 20; i++) {
                                    palabra = ob.getFrutas(i);
                                    tam = palabra.length();
                                    x = serv.numRandom();
                                    y = serv.numRandom();
                                    serv.escribePalabras(tam, x, y, ob, palabra);
                                }
                            } else if (msj.compareToIgnoreCase("3") == 0) {
                                ob.cate = "3";
                                for (int i = 0; i < 20; i++) {
                                    palabra = ob.getPaises(i);
                                    tam = palabra.length();
                                    x = serv.numRandom();
                                    y = serv.numRandom();
                                    serv.escribePalabras(tam, x, y, ob, palabra);
                                }
                            } else if (msj.compareToIgnoreCase("4") == 0) {
                                ob.cate = "4";
                                for (int i = 0; i < 20; i++) {
                                    palabra = ob.getOficina(i);
                                    tam = palabra.length();
                                    x = serv.numRandom();
                                    y = serv.numRandom();
                                    serv.escribePalabras(tam, x, y, ob, palabra);
                                }
                            }
                            serv.rellenaTablero(ob);
                            System.out.println();
                            serv.imprimeTablero(ob);
                            ob.score = 0;
                            ob.time = 0;
                            System.out.println("\nEnviando sopa de letras");
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(baos);
                            oos.writeObject(ob);
                            oos.flush();
                            ByteBuffer b2 = ByteBuffer.wrap(baos.toByteArray());
                            SocketChannel sc = (SocketChannel) k.channel();
                            sc.write(b2);
                            continue;
                        }//else
                    }//if_readable

                }//while
            }//while
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }//main

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
