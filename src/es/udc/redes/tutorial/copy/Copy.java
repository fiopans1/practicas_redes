package es.udc.redes.tutorial.copy;
//Diego Suarez Ramos : diego.suarez.ramos@udc.es
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Copy {

    public static void main(String[] args) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        if (args.length != 2) {
            System.out.println("ERROR, la entrada tiene que ser : \n java es.udc.redes.tutorial.copy.Copy <fichero origen> <fichero destino> \n");
        } else {
            try {
                in = new FileInputStream(args[0]);
                out = new FileOutputStream(args[1]);
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            }catch (FileNotFoundException e){
                System.out.println("Error, la entrada no es valida");
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        }
    }
    }


