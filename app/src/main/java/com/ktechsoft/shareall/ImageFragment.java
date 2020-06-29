package com.ktechsoft.shareall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

/**
 ** This fragment will read image files from phone and shows to the user.
 */

public class ImageFragment extends Fragment {

    private File[] files;
    private String[] filesPaths;
    GridView gridView;
    boolean boolean_folder;
    public static ArrayList<Model_images> al_images = new ArrayList<>();
    public ImageFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_image, container, false);

        // Check for SD Card
        /*if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getContext(), "Error! No SDCARD Found!", Toast.LENGTH_LONG).show();
        } else {
            File dirDownload = Environment.getExternalStoragePublicDirectory(Environment.);
            if (dirDownload.isDirectory()) {
                files = dirDownload.listFiles();
                filesPaths = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    filesPaths[i] = files[i].getAbsolutePath();
                }
            }
        }*/
        try{

            final ArrayList<Model_images> aa = fn_imagespath();
            gridView = (GridView) view.findViewById(R.id.imageGridView);
            gridView.setAdapter(new Adapter_PhotosFolder(getContext(), aa));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getContext(), folderView.class);
                    intent.putExtra("value",i);
                    intent.putExtra("List",aa);
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            Log.e("Empty",e.getLocalizedMessage());
        }
        return  view;
    }
     private ArrayList<String> getAllShownImagesPath(Activity activity) {
         Uri uri;
         Cursor cursor;
         int column_index_data, column_index_folder_name;
         ArrayList<String> listOfAllImages = new ArrayList<String>();
         String absolutePathOfImage = null;
         uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

         String[] projection = { MediaStore.MediaColumns.DATA,
                 MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

         cursor = activity.getContentResolver().query(uri, projection, null,
                 null, null);

         column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
         column_index_folder_name = cursor
                 .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

         while (cursor.moveToNext()) {

             absolutePathOfImage = cursor.getString(column_index_data);

             listOfAllImages.add(absolutePathOfImage);
         }
         return listOfAllImages;
     }
    public ArrayList<Model_images> fn_imagespath() {
        al_images.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getActivity().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < al_images.size(); i++) {
                if (al_images.get(i).getStr_folder().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }


            if (boolean_folder) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(al_images.get(int_position).getAl_imagepath());
                al_path.add(absolutePathOfImage);
                al_images.get(int_position).setAl_imagepath(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                Model_images obj_model = new Model_images();
                obj_model.setStr_folder(cursor.getString(column_index_folder_name));
                obj_model.setAl_imagepath(al_path);

                al_images.add(obj_model);


            }


        }


        for (int i = 0; i < al_images.size(); i++) {
            Log.e("FOLDER", al_images.get(i).getStr_folder());
            for (int j = 0; j < al_images.get(i).getAl_imagepath().size(); j++) {
                Log.e("FILE", al_images.get(i).getAl_imagepath().get(j));
            }
        }
    //    obj_adapter = new Adapter_PhotosFolder(getApplicationContext(),al_images);
      //  gv_folder.setAdapter(obj_adapter);
        return al_images;
    }
}

