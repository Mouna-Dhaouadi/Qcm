package souleima.qcm;

import android.content.Context;
import android.content.Intent;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pc on 19/06/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.RecyclerViewHolder>{
    private ArrayList<Dataprovider> arrayList=new ArrayList<Dataprovider>();

    public  RecyclerAdapter(ArrayList<Dataprovider> arrayList){
        this.arrayList=arrayList;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        RecyclerViewHolder
                 recyclerViewHolder =new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Dataprovider dataprovider=arrayList.get(position);
        holder.imageView.setImageResource(dataprovider.getImg_res());
        holder.f_name.setText(dataprovider.getF_name());
        holder.iconFrame.setBackgroundResource(dataprovider.getColor1());
        holder.texteFrame.setBackgroundResource(dataprovider.getColor2());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  static  class  RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;

        private final Context context;
        TextView f_name;
        PercentRelativeLayout texteFrame,iconFrame;
        public RecyclerViewHolder(View view){
            super(view);
            context = itemView.getContext();
            imageView =(ImageView)view.findViewById(R.id.img);
            f_name =(TextView)view.findViewById(R.id.f_name);
            iconFrame=(PercentRelativeLayout)view.findViewById(R.id.icon);
            texteFrame=(PercentRelativeLayout)view.findViewById(R.id.texte);
            view.setClickable(true);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            final Intent intent;
            switch (getAdapterPosition()){
                case 0:
                    intent =  new Intent(context, MathActivity.class);
                    break;

                case 1:
                    intent =  new Intent(context, ArabicActivity.class);
                    break;
                default:
                    intent =  new Intent(context, DefaultActivity.class);
                    break;
            }
            context.startActivity(intent);
        }
    }
}
