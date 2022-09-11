package es.udc.redes.webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class FuncionesHTTP {
    //direccion del directorio donde tenemos los archivos del servidor
    public static String dir= ".\\p1-files\\";
    //nos concatena la cabecera con los bytes del fichero
    public static byte[] concat(String str, byte[] fichero){
        byte[] result=new byte[str.getBytes().length+fichero.length];
        System.arraycopy(str.getBytes(),0,result,0,str.getBytes().length);
        System.arraycopy(fichero,0,result,str.getBytes().length,fichero.length);

        return result;
    }

    //te mira si cumple la condicion del ifModifiedSince
    public static boolean equalsIfModifiedSince(Date actual, String last){
        if(last==null){
            return false;
        }else{
            SimpleDateFormat dateFormat= new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            try {
                Date last1= dateFormat.parse(last);
                Date actual1=dateFormat.parse(dateFormat.format(actual));
                if(actual1.before(last1)){
                    return true;
                }else{
                    return actual1.equals(last1);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
    //es el metodo principal para obtener el Head o el Get en funcion de lo que pases por elegir
    public static byte[] obtenerGetHead(int elegir,String[] str, Date fecha, String modief) throws IOException {
        File fichero=null;
        byte[] filebytes=null;
        int i=str[1].lastIndexOf('/');

        try {//miramos si podemos coger el archivo, si no dará error
            fichero = new File(dir + str[1].substring(i + 1));//lo abrimos como fichero para obtener fechas
            filebytes = Files.readAllBytes(Paths.get(dir + str[1].substring(i + 1)));//lo abrimos como bytes para mandarlo
        }catch (Exception e){//en caso de excepcion que salte el error 404
            return FuncionesHTTP.error(404,fecha,elegir);
        }
        StringBuilder head=new StringBuilder();//construimos la cabecera

        if(!equalsIfModifiedSince(new Date(fichero.lastModified()),modief)){//miramos si se cumple la condicion del if-modified-since

        head.append("HTTP/1.0 200 OK\nDate: ").append(String.format("%tc", fecha)).append("\n").append("Server: WebServer_41B\n").append("Last-Modified: ").append(String.format("%tc", fichero.lastModified())
        ).append("\nContent-Length: ").append(filebytes.length).append("\nContent-Type: ").append(FuncionesHTTP.TipoCotenido(str[1])).append("\n\n");

            if (elegir == 0) {//si elegir está a 0 devolvemos la cabecera
                return FuncionesHTTP.concat(head.toString(), filebytes);
            } else {//sino devolvemos solo el head
                return head.toString().getBytes();
            }
        }else{//sino se cumple preparamos la cabecera para el if-modifed-since
            head.append("HTTP/1.0 304 Not Modified\nDate: ").append(String.format("%tc", fecha)).append("\n").append("Server: WebServer_41B\n").append("Last-Modified: ").append(String.format("%tc", fichero.lastModified())
            ).append("\nContent-Length: ").append(filebytes.length).append("\nContent-Type: ").append(FuncionesHTTP.TipoCotenido(str[1])).append("\n\n");
            return head.toString().getBytes();
        }

    }



    public static byte[] error(int error,Date fecha, int elegir) throws IOException {//prepara las cabeceras y el codigo en aso de error
        StringBuilder head=new StringBuilder();
        File fichero=null;
        byte[] filebytes=null;
        if(error==400){//preparamos las cabeceras y archivos correspondientes al error 400
            fichero=new File(dir+"error400.html");
            filebytes=Files.readAllBytes(Paths.get(dir+"error400.html"));
            head.append("HTTP/1.0  400 Bad Request\nDate: ").append(String.format("%tc", fecha)).append("\n").append("Server: WebServer_41B\n").append("Last-Modified: ").append(String.format("%tc", fichero.lastModified())
            ).append("\nContent-Length: ").append(filebytes.length).append("\nContent-Type: text/html\n\n");

        }else if(error==404){//preparamos las cabeceras y archivos correspondientes al error 404
            fichero=new File(dir+"error404.html");
            filebytes=Files.readAllBytes(Paths.get(dir+"error404.html"));
            head.append("HTTP/1.0 404 Not Found\nDate: ").append(String.format("%tc", fecha)).append("\n").append("Server: WebServer_41B\n").append("Last-Modified: ").append(String.format("%tc", fichero.lastModified())
            ).append("\nContent-Length: ").append(filebytes.length).append("\nContent-Type: text/html\n\n");
        }

        if (filebytes != null && elegir!=1) {//miramos si devolver error a un GET o a un HEAD
            return FuncionesHTTP.concat(head.toString(), filebytes);
        }else if(elegir==1){
            return head.toString().getBytes();
        }else{
            return null;
        }
    }
    //Con esta funcion decimos para el apartado Content-Type de la cabecera el tipo de archivo que mandamos
    public static String TipoCotenido(String name){
        int i=name.lastIndexOf('.');
        switch(name.substring(i+1)){
            case("html"):
                return ("text/html");
            case("txt"):
                return ("text/plain");
            case("gif"):
                return ("image/gif");
            case("png"):
                return ("image/png");
            case("jpg"):
                return ("image/jpg");
            case("pdf"):
                return ("application/pdf");
            default:
                return ("application/octet-stream");
        }
    }
    //nos parsea la cabecera if-ModifiedSince para encontrar donde se encuentra la fecha
    public static String ifMS(String str){
        String[] partes=str.split("\\r?\\n|\\r");
        for (int i = 1; i < partes.length; i++) {
            if (partes[i].contains("If-Modified-Since")) {
                StringBuilder sb = new StringBuilder(partes[i]);
                sb.delete(0, 19);
                return sb.toString();
            }
        }
        return null;
    }
    //parsea la entrada inicial y  identifica si tenemos que hacer un GET, un HEAD o un error 400
    public static byte[] procesar(String str, Date fecha) throws IOException {
        String[] partes=str.split("\\ ");
        if(partes.length>2){
            switch (partes[0]){
                case "GET":
                    return FuncionesHTTP.obtenerGetHead(0,partes,fecha,ifMS(str));
                case "HEAD":
                    return FuncionesHTTP.obtenerGetHead(1,partes,fecha,ifMS(str));
                default:
                    return FuncionesHTTP.error(400,fecha,0);//completar
            }
        }else{
            return null;
        }
    }

}
