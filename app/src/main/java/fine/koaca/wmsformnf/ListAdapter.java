package fine.koaca.wmsformnf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> implements OnListItemClickListener,OnItemLongClickListener{
    ArrayList<List> list;
    OnListItemClickListener listener;
    OnItemLongClickListener longClickListener;
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list,parent,false);
        return new ListViewHolder(view,this,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.bl.setText(list.get(position).getBl());
        holder.description.setText(list.get(position).getDescription());
        holder.location.setText(list.get(position).getLocation());
        holder.date.setText(list.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setOnItemClicklistener(OnListItemClickListener listener){
        this.listener=listener;
    }

    public void setLongClickListener(OnItemLongClickListener longClickListener){
        this.longClickListener=longClickListener;
    }

    @Override
    public void onItemClick(ListViewHolder holder, View view, int position) {
        if(listener !=null){
            listener.onItemClick(holder,view,position);
        }

    }
    @Override
    public void onLongItemClick(ListViewHolder holder, View view, int position) {
        if(longClickListener !=null){
            longClickListener.onLongItemClick(holder,view,position);
        }

    }



    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView bl;
        TextView description;
        TextView location;
        TextView date;


        public  ListViewHolder(@NonNull View itemView,final OnListItemClickListener listener,final OnItemLongClickListener longClickListener) {
            super(itemView);
            this.bl=itemView.findViewById(R.id.textView2);
            this.description=itemView.findViewById(R.id.textView);
            this.location=itemView.findViewById(R.id.text_location);
            this.date=itemView.findViewById(R.id.text_Date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if(listener !=null){
                        listener.onItemClick(ListViewHolder.this,v,pos);
//                    if(pos !=RecyclerView.NO_POSITION){
//
//                        String recycler_bl=list.get(pos).getBl();
//                        String recycler_des=list.get(pos).getDescription();
//                        Log.i("recyler",recycler_bl);
////                        MainActivity mainActivity=new MainActivity();
////
////                       mainActivity.textView.setText(recycler_bl);
////                       mainActivity.textView.setText(recycler_des);
//
//                        Toast.makeText(MainActivity.this, "koaca", Toast.LENGTH_SHORT).show();
//                        notifyItemChanged(pos);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos=getAdapterPosition();
                    if(longClickListener !=null){
                        longClickListener.onLongItemClick(ListViewHolder.this,v,pos);

                    }
                    return true;
                }
            });
        }
    }

    public ListAdapter(ArrayList<List> list, MainActivity mainActivity) {
        this.list = list;
    }
}
