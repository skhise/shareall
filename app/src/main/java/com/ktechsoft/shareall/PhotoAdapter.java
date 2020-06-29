package com.ktechsoft.shareall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class PhotoAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<Model_images> filesPaths =  new ArrayList<>();

    public PhotoAdapter(Context ctx, ArrayList<Model_images> filesPaths) {
        this.ctx = ctx;
        this.filesPaths = filesPaths;
    }
    @Override
    public int getCount() {
        return filesPaths.size();
    }
    @Override
    public Object getItem(int pos) {
        return pos;
    }
    @Override
    public long getItemId(int pos) {
        return pos;
    }
    @Override
    public View getView(int p, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = inflater.inflate(R.layout.image_view, null);
           // TextView textView = (TextView) grid.findViewById(R.id.gridview_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.gridview_image);
           // textView.setText(filesNames[p]);
           // Bitmap bmp = BitmapFactory.decodeFile(filesPaths.get(p).getAl_imagepath());
            //imageView.setImageBitmap(bmp);

            Glide.with(ctx).load("file"+ filesPaths.get(p).getAl_imagepath().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView);
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}