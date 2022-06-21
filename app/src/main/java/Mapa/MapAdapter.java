package Mapa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.projetomobile.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.Medicos;

public class MapAdapter implements GoogleMap.InfoWindowAdapter, Filterable {

    private final View mWindow;
    private Context mContext;
    ArrayList<MapModel> mapa;
    ArrayList<MapModel> list;

    public MapAdapter(Context context, ArrayList<MapModel> mapa) {
        this.mContext = context;
        this.mapa = mapa;
        list = new ArrayList<>(mapa);
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void rendowWindowtext(Marker marker, View view)
    {
        String title = marker.getTitle();
        TextView tvNome;
        tvNome = (TextView) view.findViewById(R.id.Nomi);

        if(!title.equals(""))
        {
            tvNome.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet;
        tvSnippet = (TextView) view.findViewById(R.id.snippet);

        if(!snippet.equals(""))
        {
            tvSnippet.setText(snippet);
        }
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        rendowWindowtext(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowtext(marker, mWindow);
        return mWindow;
    }

    @Override
    public Filter getFilter() {
        return FiltroMapa;
    }

    private  Filter FiltroMapa = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String searchText = constraint.toString().toLowerCase();
            List<MapModel> tempList = new ArrayList<>();

            if(searchText.length() == 0 || searchText.isEmpty())
            {
                tempList.addAll(list);

            }
            else
            {
                for (MapModel item: list)
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
            mapa.clear();
            mapa.addAll((Collection<? extends MapModel>) filterResults.values);
            MapAdapter.this.notify();

        }
    };
}
