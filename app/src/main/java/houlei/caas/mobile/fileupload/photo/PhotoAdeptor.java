package houlei.caas.mobile.fileupload.photo;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fileupload.R;

import java.io.File;
import java.util.List;

public class PhotoAdeptor extends RecyclerView.Adapter<PhotoAdeptor.ViewHolder> {

    private List<Photo> mPhotoList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photoImage;
        TextView photoDesc;

        public ViewHolder (View view)
        {
            super(view);
            photoImage = (ImageView) view.findViewById(R.id.photo_image);
            photoDesc = (TextView) view.findViewById(R.id.photo_desc);
        }

    }

    public  PhotoAdeptor (List <Photo> fruitList){
        mPhotoList = fruitList;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        Photo photo = mPhotoList.get(position);
//        holder.photoImage.setImageResource(R.drawable.ic_gf_done);
        holder.photoDesc.setText(photo.getDesc());

        holder.photoImage.setImageURI(Uri.parse(photo.getPath()));
    }

    @Override
    public int getItemCount(){
        return mPhotoList.size();
    }
}
