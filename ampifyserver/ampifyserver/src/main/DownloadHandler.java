/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import constants.RequestCode;
import constants.ResponseCode;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import request.DownloadSongRequest;
import request.DownloadSrtrequest;
import request.Request;
import request.Response;

/**
 *
 * @author rishi
 */
public class DownloadHandler implements  Runnable{
    
    private final Socket downloadSocket;
    private  OutputStream downloadOutputStream;
    private final ObjectInputStream downloadInputStream;
    
    public DownloadHandler(Socket downloadSocket) throws IOException {
        
        this.downloadSocket=downloadSocket;
        downloadOutputStream =downloadSocket.getOutputStream();
        downloadInputStream =new ObjectInputStream(downloadSocket.getInputStream());
        
        
    }
    
    
    @Override
    public void run() {
        Request request =null;
        while(true){
                
            try {
                Object o;
                o = downloadInputStream.readObject();
                
                System.out.println(o.getClass());
                // type casting object to request type, to identify different requests by their request code
                request = (Request) o;
                if(request.getRequestCode().equals(RequestCode.DOWNLOAD_SONG_REQUEST)){
                DownloadSongRequest downloadSongRequest =(DownloadSongRequest)request;
                String Path = downloadSongRequest.getSong();
                File file=null;
                if(new File("C:\\Users\\rishi\\Documents\\NetBeansProjects\\ampifyserver\\ampifyserver\\songsdata\\"+Path+".mp3").exists()){
                    file =new File("C:\\Users\\rishi\\Documents\\NetBeansProjects\\ampifyserver\\ampifyserver\\songsdata\\"+Path+".mp3");
                    downloadOutputStream.write(0);
                    
                }
                else if(new File("C:\\Users\\rishi\\Documents\\NetBeansProjects\\ampifyserver\\ampifyserver\\songsdata\\"+Path+".mp4").exists()){
                     file =new File("C:\\Users\\rishi\\Documents\\NetBeansProjects\\ampifyserver\\ampifyserver\\songsdata\\"+Path+".mp4");
                downloadOutputStream.write(1);
                }
                downloadOutputStream.flush();
                
                byte[] buffer =new byte[10*1024];
                InputStream inputstream =new FileInputStream(file);
                int count;
                while((count=inputstream.read(buffer))>0){
                    downloadOutputStream.write(buffer, 0, count);
                }
                System.out.println("file sended");
                downloadOutputStream.flush();
                downloadOutputStream.close();
                return;
                //downloadOutputStream =downloadSocket.getOutputStream();
            } 
                
                else{
                    DownloadSrtrequest downloadSrtrequest =(DownloadSrtrequest) request;
                    String srtpath=downloadSrtrequest.getSongName();
                    File file1=null;
                    file1=new File("C:\\Users\\rishi\\Documents\\NetBeansProjects\\ampifyserver\\ampifyserver\\srtdata\\"+srtpath+".srt");
                
                    byte[] buffer1 =new byte[10*1024];
                    InputStream inputstream =new FileInputStream(file1);
                    int count1;
                    while((count1=inputstream.read(buffer1))>0){
                        downloadOutputStream.write(buffer1, 0, count1);
                    }
                    System.out.println("file sended");
                    downloadOutputStream.flush();
                    downloadOutputStream.close();
                    return;

                
                
                }
            }
                
                catch (IOException ex) {
                Logger.getLogger(DownloadHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DownloadHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
                       
        }
       
    }
    
    
    
}
