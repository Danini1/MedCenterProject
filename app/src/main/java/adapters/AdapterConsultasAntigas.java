package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetomobile.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.ConsultasAntigas;
import model.ConsultasMarcadas;

public class AdapterConsultasAntigas extends RecyclerView.Adapter<AdapterConsultasAntigas.MyViewHolder> implements Filterable {

    Context context;
    List<ConsultasAntigas> consultasAntigas;
    ArrayList<ConsultasAntigas> list;

    public AdapterConsultasAntigas(Context context, List<ConsultasAntigas> consultasAntigas) {
        this.context = context;
        this.consultasAntigas = consultasAntigas;
        list = new ArrayList<>(consultasAntigas);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_consultas_antigas, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.Container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.diaa.setText(consultasAntigas.get(position).getData());
        holder.descricaoo.setText(consultasAntigas.get(position).getDescricao());
        holder.horarioo.setText(consultasAntigas.get(position).getHorario());
        holder.locall.setText(consultasAntigas.get(position).getLocal());
        holder.doutorr.setText(consultasAntigas.get(position).getDoutor());

    }

    @Override
    public int getItemCount() {
        return consultasAntigas.size();
    }

    @Override
    public Filter getFilter() {
        return FiltroConsultasAntigas;
    }
    private  Filter FiltroConsultasAntigas = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String searchText = constraint.toString().toLowerCase();
            List<ConsultasAntigas> tempList = new ArrayList<>();

            if(searchText.length() == 0 || searchText.isEmpty())
            {
                tempList.addAll(list);

            }
            else
            {
                for (ConsultasAntigas item: list)
                {
                    if(item.getDoutor().toLowerCase().contains(searchText) || item.getLocal().toLowerCase().contains(searchText))
                    {
                        tempList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = tempList;
            return  filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            consultasAntigas.clear();
            consultasAntigas.addAll((Collection<? extends ConsultasAntigas>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout Container;
        TextView diaa;
        TextView descricaoo;
        TextView horarioo;
        TextView locall;
        TextView doutorr;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Container = (RelativeLayout) itemView.findViewById(R.id.container);
            diaa = (TextView) itemView.findViewById(R.id.dia);
            descricaoo = (TextView) itemView.findViewById(R.id.descricao);
            horarioo = (TextView)itemView.findViewById(R.id.horario) ;
            locall = (TextView) itemView.findViewById(R.id.local);
            doutorr = (TextView) itemView.findViewById(R.id.doutor);



        }
    }

}
