package fine.koaca.wmsformnf;

import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static android.graphics.Color.red;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> implements OnListItemClickListener,OnItemLongClickListener{
    ArrayList<List> list;
    OnListItemClickListener listener;
    OnItemLongClickListener longClickListener;

    //onClick 이벤트 발생시, 클릭된 아이템의 position( ViewHolder.getAdapterPosition() )과 선택상태를 저장해 놓고 토글 시키면 될것 같습니다.
    //position별 선택상태를 저장하는 구조는 SparseBooleanArray를 사용하겠습니다.

    private SparseBooleanArray mSelectedItems=new SparseBooleanArray(0);
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list,parent,false);
        return new ListViewHolder(view,this,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        String location_chk=list.get(position).getLocation();

        holder.bl.setText(list.get(position).getBl());
        holder.description.setText(list.get(position).getDescription());
        holder.location.setText(list.get(position).getLocation());
        holder.date.setText(list.get(position).getDate());
        holder.count.setText(list.get(position).getCount());
        holder.remark.setText(list.get(position).getRemark());




        if(location_chk.equals("")){
            holder.itemView.setBackgroundColor(Color.BLUE);
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

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
        TextView count;
        TextView remark;


        public  ListViewHolder(@NonNull View itemView,final OnListItemClickListener listener,final OnItemLongClickListener longClickListener) {
            super(itemView);
            this.bl=itemView.findViewById(R.id.textView2);
            this.description=itemView.findViewById(R.id.textView);
            this.location=itemView.findViewById(R.id.text_location);
            this.date=itemView.findViewById(R.id.text_Date);
            this.count=itemView.findViewById(R.id.textView_Rotate);
            this.remark=itemView.findViewById(R.id.textView_list_mark);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if(listener !=null){
                        listener.onItemClick(ListViewHolder.this,v,pos);
                    }
                    if(mSelectedItems.get(pos,true)){
                        mSelectedItems.put(pos,false);
                        bl.setTextColor(RED);
                        description.setTextColor(RED);
                        count.setTextColor(RED);
                    }else{
                        mSelectedItems.put(pos,true);
                        bl.setTextColor(BLACK);
                        description.setTextColor(BLACK);
                        count.setTextColor(BLACK);
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
