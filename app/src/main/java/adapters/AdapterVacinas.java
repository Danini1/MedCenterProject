package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetomobile.MarcarConsulta;
import com.example.projetomobile.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.ConsultasMarcadas;
import model.Vacinas;

public class AdapterVacinas extends RecyclerView.Adapter<AdapterVacinas.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<Vacinas> vacinas;
    ArrayList<Vacinas> list;

    public AdapterVacinas(Context context, ArrayList<Vacinas> vacinas) {
        this.context = context;
        this.vacinas = vacinas;
        list = new ArrayList<>(vacinas);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_vacina, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.Container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.nome.setText(vacinas.get(position).getNome());
        holder.descricao.setText(vacinas.get(position).getDescricao());
        holder.indicacao.setText(vacinas.get(position).getIndicacao());
        Picasso.get().load(vacinas.get(position).getImagem()).into(holder.img);

    }
    @Override
    public int getItemCount() {
        return vacinas.size();
    }

    @Override
    public Filter getFilter() {
        return FiltroVacinas;
    }

    private  Filter FiltroVacinas = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String searchText = constraint.toString().toLowerCase();
            List<Vacinas> tempList = new ArrayList<>();

            if(searchText.length() == 0 || searchText.isEmpty())
            {
                tempList.addAll(list);

            }
            else
            {
                for (Vacinas item: list)
                {
                    if(item.getNome().toLowerCase().contains(searchText))
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
            vacinas.clear();
            vacinas.addAll((Collection<? extends Vacinas>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        RelativeLayout Container;
        TextView nome;
        TextView descricao;
        TextView indicacao;
        ImageView img;
        VideoView video;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Container = itemView.findViewById(R.id.container);
            nome = itemView.findViewById(R.id.nome);
            descricao = itemView.findViewById(R.id.descrica);
            indicacao = itemView.findViewById(R.id.indicacao);
            img = itemView.findViewById(R.id.ftt);
        }
    }

}
