package org.fbcmd4j.util;

import facebook4j.*;
import facebook4j.conf.ConfigurationBuilder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;


public class fbcmd {
    public static org.apache.logging.log4j.Logger log = LogManager.getLogger(fbcmd.class);
    private Facebook facebook;
    private ResponseList<Post> ListaPost;
    
    //Contructor de clase vacio
    public fbcmd(){
        log.trace("> fbcmd() -> Acceso");
    }
    
    //Funcion Conectar
    public boolean Conectar(){
        log.trace("> Conectar() -> Acceso");
        String id;
        
        try {
            log.trace("> Conectar() -> Iniciando ConfigurationBuilder");
            Properties p = new Properties(loadProperties());
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(Boolean.valueOf(p.getProperty("debug")))
                    .setOAuthAppId(p.getProperty("oauth.appId"))
                    .setOAuthAppSecret(p.getProperty("oauth.appSecret"))
                    .setOAuthAccessToken(p.getProperty("oauth.accessToken"))
                    .setOAuthPermissions(p.getProperty("oauth.permissions"));
            FacebookFactory ff = new FacebookFactory(cb.build());
            facebook = ff.getInstance();
            log.info("> Conectar() -> Instancia de facebook obtenida");
            
            id = facebook.getName();
        } catch (FacebookException | IllegalStateException e) {
            log.error("> Conectar() -> " + e);
            return false;
        }
        log.trace("> Conectar() -> Conexión establecida");
        return true;
    }
    
    //Funcion para obtener nombre del usuario
    public String getName(){
        log.trace("> getName() -> Acceso");
        try {
            return facebook.getName();
        } catch (FacebookException | IllegalStateException ex) {
            log.error("> getName() -> " + ex);
            return null;
        }
    }
    
    //Funcion para publicar un estado
    public void publicState(String Mensaje){
        log.trace("> publicState() -> Acceso");
        try {
            facebook.postStatusMessage(Mensaje);
        } catch (FacebookException ex) {
            log.error("> publicState() -> " + ex);
        }
    }
    
    //Funcion para publicar un Link
    public void publicLink(String Mensaje, String Link) throws MalformedURLException{
        log.trace("> publicLink() -> Acceso");
        try {
            facebook.postLink(new URL(Link), Mensaje);
        } catch (FacebookException ex) {
            log.error("> publicLink() -> " + ex);
        }
    }
    
    //Funcion para obtener los ultimos N post del usuario
    public void getWall(int Num) throws IOException{
        log.trace("> getWall() -> Acceso");
        try {
            ListaPost = facebook.getPosts(new Reading().limit(Num));
            Guardar("Wall.txt");
        } catch (FacebookException ex) {
            log.error("> getWall() -> " + ex);
        }
    }
    
    //Funcion para obtener las ultimas N NewsFeed del usuario
    public void getNewsFeed(int Num) throws IOException{
        log.trace("> getNewsFeed() -> Acceso");
        try {
            ListaPost = facebook.getFeed(new Reading().limit(Num));
            Guardar("NewsFeed.txt");
        } catch (FacebookException ex) {
            log.error("> getNewsFeed() -> " + ex);
        }
    }
    
    //Funcion para guardar los post en un archivo de texto
    private void Guardar(String Nombre) throws IOException{
        log.trace("> Guardar() -> Acceso");
        File Out;
        Out = new File(Nombre);
        FileWriter w = new FileWriter(Out);
        BufferedWriter bw = new BufferedWriter(w);
        PrintWriter wr = new PrintWriter(bw);

        String Cadena;
        String Encabezado = "-----------> ";
        String Pie = "------------------------------------------------------------";

        try{
 
            for(Post aux : ListaPost){
                print("\n" + Encabezado);
                wr.println("");
                wr.println(Encabezado);
                
                if(aux.getCreatedTime() != null){
                    Cadena = "Create Time: " + aux.getCreatedTime();
                    print(Cadena);
                    wr.println(Cadena);
                }
                print(Pie);
                wr.println(Pie);
                
                if(aux.getId() != null){
                    Cadena = "ID: " + aux.getId();
                    print(Cadena);
                    wr.println(Cadena);
                }
                if(aux.getMessage() != null){
                    Cadena = "Post: " + aux.getMessage();
                    print(Cadena);
                    wr.println(Cadena);
                }
                if(aux.getStory() != null){
                    Cadena = "Story: " + aux.getStory();
                    print(Cadena);
                    wr.println(Cadena);
                }
                if(aux.getPlace() != null){
                    Cadena = "Place: " + aux.getPlace();
                    print(Cadena);
                    wr.println(Cadena);
                }
                
                print(Pie);
                wr.println(Pie);
                
            }
            
            
            wr.close();
            bw.close();
            log.info("> Guardar() -> Post obtenidos");
        }catch(IOException e){
            wr.close();
            bw.close();
            log.error("> Guardar() -> " + e);
        }
    }
    
    //Funcion para guardar propiedades si no se encuantra el archivo facebook4j.properties
    public void setProperties(String AppID, String AppSecret, String UserAccessToken){
        log.trace("> setProperties() -> Acceso");
        boolean crear = true;
        File Out;
        File folder = new File("config");

        log.trace("> setProperties() -> Verificando existencia de configuración");
        if (folder.exists()) {
            folder = new File("config/facebook4j.properties");
            if (folder.exists()) {
                crear = false;
            }
        } else {
            folder.mkdir();
        }

        Out = new File("config/facebook4j.properties");
        try {
            FileWriter w = new FileWriter(Out);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);

            if (crear) {
                wr.println("debug=false");
                wr.println("oauth.appId=" + AppID);
                wr.println("oauth.appSecret=" + AppSecret);
                wr.println("oauth.accessToken=" + UserAccessToken);
                wr.println("oauth.permissions=public_profile,user_post,publish_actions");
            }

            w.close();
            bw.close();
            wr.close();
            log.trace("> setProperties() -> Archivo facebook4j.properties creado");
        } catch (IOException ex) {
            log.error("> setProperties() -> " + ex);
        }
    }
    
    //Funcion que carga propiedades de facebook4j.properties
    private Properties loadProperties(){
        log.trace("> loadProperties() -> Acceso");
        Properties p = new Properties();
        
        try{
            p.load(new FileReader("config/facebook4j.properties"));
            return p;
        }catch(IOException ex){
            log.error("> loadProperties() -> " + ex);
            return null;
        }
    }
    
    private void print(String Cadena){
        System.out.println(Cadena);
    }
}

