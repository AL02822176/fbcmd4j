package org.fbcmd4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.logging.log4j.LogManager;
import org.fbcmd4j.util.fbcmd;


public class Main {
    public static org.apache.logging.log4j.Logger log = LogManager.getLogger(Main.class);
    static fbcmd Facebook;
    
    //Clase principal
    public static void main(String[] args) {
        log.trace("> main() -> Acceso");
        load();
    }
    
    //Método para cargar instancia de Facebook
    private static void load(){
        int Opcion;
        BufferedReader Leer = new BufferedReader(new InputStreamReader(System.in));
        Facebook = new fbcmd();
        
        log.trace("> load() -> Acceso");
        
        log.trace("> main() -> Verificando conexión a Facebook");
        if(Facebook.Conectar()){
            log.trace("> main() -> Conexión establecida");
            mostrarMenu();
        }else{
            try {
                log.warn("> main() -> Confiuración no encontrada");
                print("\nNo se encontró archivo de propiedades o Error de conexión ainternet!");
                print("1- Ingresar datos\n2- Salir");
                Opcion = Integer.parseInt(Leer.readLine());
                
                if(Opcion == 1){
                    setProperties();
                }
            } catch (IOException ex) {
                log.error("> main() -> " + ex);
            }
        }
    }
    
    //Método para solicitar datos de acceso de la aplicación-> Si no se encuentra archivo facebook.properties
    private static void setProperties(){
        log.trace("> setProperties() -> Acceso");
        try {
            String id,secret,token;
            BufferedReader Leer = new BufferedReader(new InputStreamReader(System.in));
            log.trace("> setProperties() -> Obteniendo datos de conexión");
            print("Ingresa el AppId:");
            id = Leer.readLine();
            
            print("Ingresa el AppSecret:");
            secret = Leer.readLine();
            
            print("Ingresa el UserAccessToken:");
            token = Leer.readLine();
            
            if(id == "" | secret == "" | token == ""){
                log.warn("> setProperties() -> Datos de acceso incompletos");
                print("\nDatos incompletos");
            }else{
                Facebook.setProperties(id, secret, token);
                load();
            }
        } catch (IOException ex) {
            log.error("> setProperties() -> " + ex);
        }
    }
    
    //Metodo Mostrar menu -> Muestra menú de la aplicacion
    public static void mostrarMenu(){
        log.trace("> mostrarMenu() -> Acceso");
        boolean Menu = true;
        String Cadena, Aux, Encabezado = "----------", Pie = "__________";
        BufferedReader Leer = new BufferedReader(new InputStreamReader(System.in));
        int Opcion;
        
        print("\nBIENVENIDO " + Facebook.getName() + " A LA APLICACION DE FACEBOOK");
        while (Menu) {
            try {
                print("--------------- MENU --------------");
                print("1 -> Publicar Estado");
                print("2 -> Publicar Link");
                print("3 -> Obtener Wall");
                print("4 -> Obtener NewsFeed");
                print("5 -> Salir");
                print("-----------------------------------");
                
                Opcion = Integer.parseInt(Leer.readLine());
                log.debug("> mostrarMenu() -> Opción seleccionada: " + Opcion);
                switch (Opcion){
                    case 1:
                        print(Encabezado);
                        print("Escriba su estado:");
                        Cadena = Leer.readLine();
                        Facebook.publicState(Cadena);
                        log.info("> mostrarMenu() -> Estado publicado");
                        print(Pie);
                        break;
                    case 2:
                        print(Encabezado);
                        print("Ingresa el Link:");
                        Cadena = Leer.readLine();
                        print("\nEscriba un mensaje");
                        Aux = Leer.readLine();
                        Facebook.publicLink(Aux,Cadena);
                        log.info("> mostrarMenu() -> Link publicado");
                        print(Pie);
                        break;
                    case 3:
                        print(Encabezado);
                        print("Ingrese el numero de los ultimos N Post del Wall a obtener:");
                        Facebook.getWall(Integer.parseInt(Leer.readLine()));
                        log.info("> mostrarMenu() -> Wall obtenido");
                        print(Pie);
                        break;
                    case 4:
                        print(Encabezado);
                        print("Ingrese el numero de los ultimos N Post del las NewsFeed a obtener:");
                        Facebook.getNewsFeed(Integer.parseInt(Leer.readLine()));
                        log.info("> mostrarMenu() -> NewsFeed obtenido");
                        print(Pie);
                        break;
                    case 5:
                        Menu = false;
                        break;
                }
            } catch (IOException | NumberFormatException ex) {
                log.error("> mostrarMenu() -> " + ex);
            }
        }
    }
    
    //Metodo para facilitar impresion enconsola
    private static void print(String Cadena){
        System.out.println(Cadena);
    }
    
}