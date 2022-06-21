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
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.ConsultasMarcadas;
import model.Remedios;

public class AdapterRemedios extends RecyclerView.Adapter<AdapterRemedios.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<Remedios> remedios;
    ArrayList<Remedios> list;


    public AdapterRemedios(Context context, ArrayList<Remedios> remedios) {
        this.context = context;
        this.remedios = remedios;
        list = new ArrayList<>(remedios);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_remedios, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.nome.setText(remedios.get(position).getNome());
        holder.descricao.setText(remedios.get(position).getDescricao());
        holder.quantidade.setText(remedios.get(position).getQuantidade());
        Picasso.get().load(remedios.get(position).getImagem()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return remedios.size();
    }

    @Override
    public Filter getFilter() {
        return FiltroRemedios;
    }

    private  Filter FiltroRemedios = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String searchText = constraint.toString().toLowerCase();
            List<Remedios> tempList = new ArrayList<>();

            if(searchText.length() == 0 || searchText.isEmpty())
            {
                tempList.addAll(list);

            }
            else
            {
                for (Remedios item: list)
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
            remedios.clear();
            remedios.addAll((Collection<? extends Remedios>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    class MyViewHolder extends RecyclerView.ViewHolder
    {

        RelativeLayout Container;
        TextView nome;
        TextView descricao;
        TextView quantidade;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Container = (RelativeLayout) itemView.findViewById(R.id.container);
            nome = (TextView)itemView.findViewById(R.id.n);
            descricao = (TextView)itemView.findViewById(R.id.disc);
            quantidade = (TextView)itemView.findViewById(R.id.q);
            img = (ImageView)itemView.findViewById(R.id.ftt);
        }
    }


}
