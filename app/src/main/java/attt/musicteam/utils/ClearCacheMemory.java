package attt.musicteam.utils;

import android.content.Context;

import java.io.File;
/**
 * Created by Hue on 11/8/2016.
 */
public class ClearCacheMemory {

    public static void trimCache(Context context){
        try{
            File dir = context.getCacheDir();
            if(dir != null && dir.isDirectory()){
                deleteDir(dir);
            }
        }catch (Exception e){

        }
    }

    public static boolean deleteDir(File dir){
        if(dir != null && dir.isDirectory()){
            String[] children = dir.list();
            for(int i = 0; i < children.length; i++){
                boolean success = deleteDir(new File(dir, children[i]));
                if(!success){
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
