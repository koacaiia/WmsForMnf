package fine.koaca.wmsformnf;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IncargoListAdapter extends RecyclerView.Adapter<IncargoListAdapter.ListViewHolder> implements OnListItemClickListener,OnItemLongClickListener{

    ArrayList<Fine2IncargoList> list;
    OnListItemClickListener listener;
    OnItemLongClickListener longClickListener;
    private SparseBooleanArray mSelectedItems=new SparseBooleanArray(0);

    public IncargoListAdapter(ArrayList<Fine2IncargoList> list, Incargo incargo) {
        this.list = list;
    }

    @NonNull
    @Override
    public IncargoListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.incargolist,parent,false);
                return new ListViewHolder(view,this,this);
    }

    @Override
    public void onBindViewHolder(@NonNull IncargoListAdapter.ListViewHolder holder, int position) {
        String str_container20=String.valueOf(list.get(position).getContainer20());
        Log.i("kocaca",str_container20);
        String str_container40=String.valueOf(list.get(position).getContainer40());
        String str_lclcargo=String.valueOf(list.get(position).getLclcargo());
        String cargotype;
        if(str_container20.equals("1")){
            cargotype="20FT";}
        else if(str_container40 .equals("1")){
            cargotype="40FT";
        }else if(str_lclcargo .equals("")){
            cargotype="LcLCargo";
        }else{cargotype="미정";}



        holder.working.setText(list.get(position).getWorking());
        holder.date.setText(list.get(position).getDate());
        holder.consignee.setText(list.get(position).getConsignee());
        holder.container.setText(list.get(position).getContainer());
        holder.cargotype.setText(cargotype);
        holder.remark.setText(list.get(position).getRemark());
        holder.bl.setText(list.get(position).getBl());
        holder.des.setText(list.get(position).getDescription());

    }

    public void setOnItemClicklistener(OnListItemClickListener listener){
        this.listener=listener;
    }

    public void setLongClickListener(OnItemLongClickListener longClickListener){
        this.longClickListener=longClickListener;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onLongItemClick(Fine2IncargoListAdapter.ListViewHolder listViewHolder, View v, int pos) {
        if(longClickListener !=null){
            longClickListener.onLongItemClick(listViewHolder,v,pos);
        }

    }

    @Override
    public void onItemClick(Fine2IncargoListAdapter.ListViewHolder holder, View view, int position) {
        if(listener !=null){
            listener.onItemClick(holder,view,position);
        }


    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView working;
        TextView date;
        TextView container;
        TextView consignee;
        TextView cargotype;
        TextView remark;
        TextView bl;
        TextView des;
        public ListViewHolder(@NonNull View itemView,final OnListItemClickListener listener,
                              final OnItemLongClickListener longClickListener) {
            super(itemView);
            this.working=itemView.findViewById(R.id.incargo_working);
            this.date=itemView.findViewById(R.id.incargo_date);
            this.consignee=itemView.findViewById(R.id.incargo_consignee);
            this.container=itemView.findViewById(R.id.incargo_container);
            this.cargotype=itemView.findViewById(R.id.incargo_cargotype);
            this.remark=itemView.findViewById(R.id.incargo_remark);
            this.bl=itemView.findViewById(R.id.incargo_bl);
            this.des=itemView.findViewById(R.id.incargo_des);
        }
    }
}
