package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetomobile.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.AgendaMedicos;
import model.ConsultasMarcadas;

public class AdapterAgendaMedicos extends   RecyclerView.Adapter<AdapterAgendaMedicos.MyViewHolder> implements Filterable {

    Context context;
    List<AgendaMedicos> agendaMedicosList;
    ArrayList<AgendaMedicos> list;

    public AdapterAgendaMedicos(Context context, List<AgendaMedicos> agendaMedicosList) {
        this.context = context;
        this.agendaMedicosList = agendaMedicosList;
        list = new ArrayList<>(agendaMedicosList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_agenda_medico, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.nome.setText(agendaMedicosList.get(position).getNome());
        holder.local.setText(agendaMedicosList.get(position).getLocal());
        holder.hora.setText(agendaMedicosList.get(position).getHora());
        holder.data.setText(agendaMedicosList.get(position).getData());
        Picasso.get().load(agendaMedicosList.get(position).getFotoPaciente()).into(holder.fotopaciente);

    }

    @Override
    public int getItemCount() {
        return agendaMedicosList.size();
    }

    @Override
    public Filter getFilter() {
        return FiltroAgenda;
    }

    private  Filter FiltroAgenda = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String searchText = constraint.toString().toLowerCase();
            List<AgendaMedicos> tempList = new ArrayList<>();

            if(searchText.length() == 0 || searchText.isEmpty())
            {
                tempList.addAll(list);

            }
            else
            {
                for (AgendaMedicos item: list)
                {
                    if(item.getNome().toLowerCase().contains(searchText) || item.getLocal().toLowerCase().contains(searchText))
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
            agendaMedicosList.clear();
            agendaMedicosList.addAll((Collection<? extends AgendaMedicos>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        RelativeLayout Container;
        TextView nome;
        TextView local;
        TextView hora;
        TextView data;
        ImageView fotopaciente;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Container = (RelativeLayout) itemView.findViewById(R.id.container);
            nome = (TextView)itemView.findViewById(R.id.NomePaciente);
            local = (TextView)itemView.findViewById(R.id.LocalConsulta);
            hora = (TextView)itemView.findViewById(R.id.HoraConsulta);
            data = (TextView)itemView.findViewById(R.id.DataConsulta);
            fotopaciente = (ImageView)itemView.findViewById(R.id.FotoPaciente);
        }
    }
}
