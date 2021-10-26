
import java.net.*;
import java.io.*;

public class Cliente {

    public static void main(String[] args) {
        try {
            int pto = 1234;
            InetAddress host = null;
            String dir = "localhost";
            try {
                host = InetAddress.getByName(dir);
            } catch (UnknownHostException u) {
                main(args);
            }
            Socket cl = new Socket(host, pto);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            BufferedReader brLocal = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader breExter = new BufferedReader(new InputStreamReader(System.in));
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            ObjetoSopa ob2 = new ObjetoSopa();
            Cliente cte = new Cliente();
            System.out.println("Conexion con el servidor " + dir + ":" + pto + " establecida\n");

            System.out.println("*****BIENVENIDO AL JUEGO SOPA DE LETRAS*****");
            System.out.println("\nSelecciona una categoría para jugar:");
            System.out.println("1. Nombres de animales");
            System.out.println("2. Nombres de frutas");
            System.out.println("3. Países del mundo");
            System.out.println("4. Artículos de oficina");
            System.out.println("5. Salir\n");
            System.out.print("Categoría: ");
            String mensaje = brLocal.readLine();
            pw.println(mensaje);
            pw.flush();
            ObjetoSopa ob = (ObjetoSopa) ois.readObject();
            if (mensaje.compareToIgnoreCase("5") == 0) {
                ois.close();
                oos.close();
                breExter.close();
                brLocal.close();
                pw.close();
                cl.close();
                System.exit(0);
            } else if (mensaje.compareToIgnoreCase("1") == 0) {
                System.out.println("\nCategoría: Nombres de animales ");
                System.out.println("Debes encontrar: " + ob.palabras.size() + " palabras\n");
                cte.imprimeTablero(ob);
                cte.Game(ob2, ob, mensaje, brLocal);
            } else if (mensaje.compareToIgnoreCase("2") == 0) {
                System.out.println("\nCategoría: Nombres de frutas ");
                System.out.println("Debes encontrar: " + ob.palabras.size() + " palabras\n");
                cte.imprimeTablero(ob);
                cte.Game(ob2, ob, mensaje, brLocal);
            } else if (mensaje.compareToIgnoreCase("3") == 0) {
                System.out.println("\nCategoría: Países del mundo ");
                System.out.println("Debes encontrar: " + ob.palabras.size() + " palabras\n");
                cte.imprimeTablero(ob);
                cte.Game(ob2, ob, mensaje, brLocal);
            } else if (mensaje.compareToIgnoreCase("4") == 0) {
                System.out.println("\nCategoría: Artículos de oficina ");
                System.out.println("Debes encontrar: " + ob.palabras.size() + " palabras\n");
                cte.imprimeTablero(ob);
                cte.Game(ob2, ob, mensaje, brLocal);
            }
            oos.writeObject(ob2);
            oos.flush();
            ob2.score = 0;
            ob2.time = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void Game(ObjetoSopa ob2, ObjetoSopa ob, String mensaje, BufferedReader brLocal) throws IOException {
        int xi, yi, xf, yf;
        while (true) {
            ob2.time = System.currentTimeMillis();
            System.out.println("\nIngresa las coordenadas de inicio y final para la palabra que buscas");
            System.out.print("xi: ");
            xi = Integer.parseInt(brLocal.readLine());
            System.out.print("yi: ");
            yi = Integer.parseInt(brLocal.readLine());
            System.out.print("xf: ");
            xf = Integer.parseInt(brLocal.readLine());
            System.out.print("yf: ");
            yf = Integer.parseInt(brLocal.readLine());
            revisaTablero(xi, yi, xf, yf, ob, ob2);
            imprimeTablero(ob);
            if (ob2.getScore() == ob.palabras.size()) {
                ob2.time = (System.currentTimeMillis() - ob2.time) / 1000;
                System.out.println("\nFELICIDADES, HAS GANADO");
                System.out.println("ENCONTRASTE " + (ob2.getScore() / 10) + "/" + ob.palabras.size() + " palabras en " + ob2.time + "seg");
                break;
            }
            System.out.println("\n¿Quieres seguir jugando en esta categoría?");
            System.out.println("1.SI");
            System.out.println("2.NO");
            System.out.print("Opcion: ");
            mensaje = brLocal.readLine();
            if (mensaje.compareToIgnoreCase("2") == 0) {
                ob2.time = (System.currentTimeMillis() - ob2.time) / 1000;
                System.out.println("\nFELICIDADES, LOGRASTE ENCONTRAR " + (ob2.getScore() / 10) + "/" + ob.palabras.size() + " palabras en " + ob2.time + "seg");
                break;
            }
        }
    }

    void imprimeTablero(ObjetoSopa ob) {
        int f = 0;
        String black = "\033[30m";
        String red = "\033[31m";
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
                if (Character.isUpperCase(ob.getSopa(i, j))) {
                    System.out.print(red + ob.getSopa(i, j));
                } else if (Character.isLowerCase(ob.getSopa(i, j))) {
                    System.out.print(black + ob.getSopa(i, j));
                }
            }
            System.out.print(" ");
            System.out.println("|");
            f++;
        }
    }

    boolean compara(String cadena, ObjetoSopa ob, ObjetoSopa ob2) {
        for (int i = 0; i < ob.palabras.size(); i++) {
            if (cadena.compareToIgnoreCase(ob.palabras.get(i)) == 0) {
                System.out.println("\nPalabra encontrada: " + cadena);
                ob2.setScore(10);
                System.out.println("Puntaje: " + ob2.getScore() + " puntos\n");
                return true;
            }
        }
        return false;
    }

    void revisaTablero(int xi, int yi, int xf, int yf, ObjetoSopa ob, ObjetoSopa ob2) {
        if (revisaDerecha(xi, yi, xf, yf, ob, ob2)) {
            for (int i = yi; i <= yf; i++) {
                ob.setSopa(xi, i, Character.toUpperCase(ob.getSopa(xi, i)));
            }
        } else if (revisaD1(xi, yi, xf, yf, ob, ob2)) {
            for (int i = yi; i <= yf; i++) {
                ob.setSopa(xi, i, Character.toUpperCase(ob.getSopa(xi, i)));
                xi++;
            }
        } else if (revisaAbajo(xi, yi, xf, yf, ob, ob2)) {
            for (int i = xi; i <= xf; i++) {
                ob.setSopa(i, yi, Character.toUpperCase(ob.getSopa(i, yi)));
            }
        } else if (revisaD2(xi, yi, xf, yf, ob, ob2)) {
            for (int i = yi; i >= yf; i--) {
                ob.setSopa(xi, i, Character.toUpperCase(ob.getSopa(xi, i)));
                xi++;
            }
        } else if (revisaIzq(xi, yi, xf, yf, ob, ob2)) {
            for (int i = yi; i >= yf; i--) {
                ob.setSopa(xi, i, Character.toUpperCase(ob.getSopa(xi, i)));
            }
        } else if (revisaD3(xi, yi, xf, yf, ob, ob2)) {
            for (int i = yi; i >= yf; i--) {
                ob.setSopa(xi, i, Character.toUpperCase(ob.getSopa(xi, i)));
                xi--;
            }
        } else if (revisaArriba(xi, yi, xf, yf, ob, ob2)) {
            for (int i = xi; i >= xf; i--) {
                ob.setSopa(i, yi, Character.toUpperCase(ob.getSopa(i, yi)));
            }
        } else if (revisaD4(xi, yi, xf, yf, ob, ob2)) {
            for (int i = yi; i <= yf; i++) {
                ob.setSopa(xi, i, Character.toUpperCase(ob.getSopa(xi, i)));
                xi--;
            }
            ob2.setScore(10);
        } else {
            System.out.println("\nPalabra incorrecta, intenta de nuevo\n");
        }
    }

    boolean revisaDerecha(int xi, int yi, int xf, int yf, ObjetoSopa ob, ObjetoSopa ob2) {
        String cadena = "";
        if (xi == xf && yi < yf) {
            for (int i = yi; i <= yf; i++) {
                cadena = cadena + ob.getSopa(xi, i);
            }
            return compara(cadena, ob, ob2);
        }
        return false;
    }

    boolean revisaD1(int xi, int yi, int xf, int yf, ObjetoSopa ob, ObjetoSopa ob2) {
        String cadena = "";
        if (xi < xf && yi < yf) {
            for (int i = yi; i <= yf; i++) {
                cadena = cadena + ob.getSopa(xi, i);
                xi++;
            }
            return compara(cadena, ob, ob2);
        }
        return false;
    }

    boolean revisaAbajo(int xi, int yi, int xf, int yf, ObjetoSopa ob, ObjetoSopa ob2) {
        String cadena = "";
        if (xi < xf && yi == yf) {
            for (int i = xi; i <= xf; i++) {
                cadena = cadena + ob.getSopa(i, yi);
            }
            return compara(cadena, ob, ob2);
        }
        return false;
    }

    boolean revisaD2(int xi, int yi, int xf, int yf, ObjetoSopa ob, ObjetoSopa ob2) {
        String cadena = "";
        if (xi < xf && yi > yf) {
            for (int i = yi; i >= yf; i--) {
                cadena = cadena + ob.getSopa(xi, i);
                xi++;
            }
            return compara(cadena, ob, ob2);
        }
        return false;
    }

    boolean revisaIzq(int xi, int yi, int xf, int yf, ObjetoSopa ob, ObjetoSopa ob2) {
        String cadena = "";
        if (xi == xf && yi > yf) {
            for (int i = yi; i >= yf; i--) {
                cadena = cadena + ob.getSopa(xi, i);
            }
            return compara(cadena, ob, ob2);
        }
        return false;
    }

    boolean revisaD3(int xi, int yi, int xf, int yf, ObjetoSopa ob, ObjetoSopa ob2) {
        String cadena = "";
        if (xi > xf && yi > yf) {
            for (int i = yi; i >= yf; i--) {
                cadena = cadena + ob.getSopa(xi, i);
                xi--;
            }
            return compara(cadena, ob, ob2);
        }
        return false;
    }

    boolean revisaArriba(int xi, int yi, int xf, int yf, ObjetoSopa ob, ObjetoSopa ob2) {
        String cadena = "";
        if (xi > xf && yi == yf) {
            for (int i = xi; i >= xf; i--) {
                cadena = cadena + ob.getSopa(i, yi);
            }
            return compara(cadena, ob, ob2);
        }
        return false;
    }

    boolean revisaD4(int xi, int yi, int xf, int yf, ObjetoSopa ob, ObjetoSopa ob2) {
        String cadena = "";
        if (xi > xf && yi < yf) {
            for (int i = yi; i <= yf; i++) {
                cadena = cadena + ob.getSopa(xi, i);
                xi--;
            }
            return compara(cadena, ob, ob2);
        }
        return false;
    }
}
