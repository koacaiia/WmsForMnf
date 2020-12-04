package fine.koaca.wmsformnf;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IncargoListAdapter extends RecyclerView.Adapter<IncargoListAdapter.ListViewHolder> implements OnListItemClickListener,OnItemLongClickListener{

    ArrayList<ListIncargo> list;
    OnListItemClickListener listener;
    OnItemLongClickListener longClickListener;
    private SparseBooleanArray mSelectedItems=new SparseBooleanArray(0);

    public IncargoListAdapter(ArrayList<ListIncargo> list, Incargo incargo) {
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
        holder.working.setText(list.get(position).getWorking());
        holder.date.setText(list.get(position).getDate());
        holder.consignee.setText(list.get(position).getConsignee());
        holder.container.setText(list.get(position).getContainer());
        holder.container40.setText(list.get(position).getContainer40());
        holder.container20.setText(list.get(position).getContainer20());
        holder.lclcargo.setText(list.get(position).getLclcargo());
        holder.remark.setText(list.get(position).getRemark());

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
    public void onLongItemClick(ListAdapter.ListViewHolder listViewHolder, View v, int pos) {
        if(longClickListener !=null){
            longClickListener.onLongItemClick(listViewHolder,v,pos);
        }

    }

    @Override
    public void onItemClick(ListAdapter.ListViewHolder holder, View view, int position) {
        if(listener !=null){
            listener.onItemClick(holder,view,position);
        }


    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView working;
        TextView date;
        TextView container;
        TextView consignee;
        TextView container40;
        TextView container20;
        TextView lclcargo;
        TextView remark;
        public ListViewHolder(@NonNull View itemView,final OnListItemClickListener listener,
                              final OnItemLongClickListener longClickListener) {
            super(itemView);
            this.working=itemView.findViewById(R.id.incargo_working);
            this.date=itemView.findViewById(R.id.incargo_date);
            this.consignee=itemView.findViewById(R.id.incargo_consignee);
            this.container=itemView.findViewById(R.id.incargo_container);
            this.container40=itemView.findViewById(R.id.incargo_container40);
            this.container20=itemView.findViewById(R.id.incargo_container20);
            this.lclcargo=itemView.findViewById(R.id.incargo_lclcargo);
            this.remark=itemView.findViewById(R.id.incargo_remark);
        }
    }
}
