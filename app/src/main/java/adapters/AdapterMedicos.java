package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetomobile.CallingActivity;
import com.example.projetomobile.MarcarConsulta;
import com.example.projetomobile.R;
import com.example.projetomobile.RecycleView_Remedios;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.ConsultasAntigas;
import model.Medicos;

public class AdapterMedicos extends  RecyclerView.Adapter<AdapterMedicos.MyViewHolder> implements Filterable {

    String Email;
    String UID;
    Context context;
    ArrayList<Medicos> medicos;
    ArrayList<Medicos> list;


    public AdapterMedicos(Context context, ArrayList<Medicos> medicos, String email) {
        this.context = context;
        this.medicos = medicos;
        list = new ArrayList<>(medicos);
        Email = email;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_medicos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.Container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.Nome.setText(medicos.get(position).getNome());
        holder.Especialidade.setText(medicos.get(position).getEspecialidade());
        holder.Hospital.setText(medicos.get(position).getLocal());
        Picasso.get().load(medicos.get(position).getImagem()).into(holder.Img);

        holder.Consultaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MarcarConsulta.class);
                intent.putExtra("medico", medicos.get(position).getNome());
                intent.putExtra("especialidade", medicos.get(position).getEspecialidade());
                intent.putExtra("local", medicos.get(position).getLocal());
                intent.putExtra("email", Email);
                intent.putExtra("UID", UID);
                context.startActivity(intent);

            }
        });

        holder.Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callingIntent = new Intent(context, CallingActivity.class);
                callingIntent.putExtra("UID_LIGACAO", medicos.get(position).getUid());
                context.startActivity(callingIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicos.size();
    }

    @Override
    public Filter getFilter() {
        return FiltroMedicos;
    }

    private  Filter FiltroMedicos = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String searchText = constraint.toString().toLowerCase();
            List<Medicos> tempList = new ArrayList<>();

            if(searchText.length() == 0 || searchText.isEmpty())
            {
                tempList.addAll(list);

            }
            else
            {
                for (Medicos item: list)
                {
                    if(item.getEspecialidade().toLowerCase().contains(searchText) || item.getNome().toLowerCase().contains(searchText))
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
            medicos.clear();
            medicos.addAll((Collection<? extends Medicos>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout Container;
        TextView Nome;
        TextView Especialidade;
        TextView Hospital;
        ImageView Img;
        Button Consultaa;
        Button Video;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Container = (RelativeLayout) itemView.findViewById(R.id.container);
            Nome = (TextView)itemView.findViewById(R.id.Nome);
            Especialidade = (TextView)itemView.findViewById(R.id.Especialidade);
            Hospital = (TextView)itemView.findViewById(R.id.Hospital);
            Img = (ImageView)itemView.findViewById(R.id.FotO);
            Consultaa = (Button)itemView.findViewById(R.id.BtnMarcarConsulta);
            Video = (Button)itemView.findViewById(R.id.VideoCall);

        }
    }

}
