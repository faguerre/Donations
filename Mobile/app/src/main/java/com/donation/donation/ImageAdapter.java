package com.donation.donation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.donation.R;
import com.donation.model.in.FileModelIn;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    private final Context mContext;
    private final int[] mImageIds = new int[]{R.drawable.delete, R.drawable.donations};
    private final ArrayList<FileModelIn> images;

    public ImageAdapter(Context context, ArrayList<FileModelIn> files) {
        mContext = context;
        images = files;
    }

    @Override
    public int getCount() {
        if (images == null) {
            return 0;
        } else {
            return images.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (!images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                Bitmap decodedImage = null;
                byte[] imageBytes = null;
                FileModelIn fileModelIn = images.get(position);
                imageBytes = Base64.decode(fileModelIn.getDataFiles(), Base64.DEFAULT);
                decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imageView.setImageBitmap(decodedImage);
            }
        } else {
            imageView.setImageResource(R.drawable.delete);
        }
        container.addView(imageView, 0);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}