package com.vanard.tutorial.androidphpmysql;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {
    private List<Data> mItems;
    private Context context;

    public AdapterData(Context context, List<Data> items) {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        Data md = mItems.get(position);
        holder.keterangan.setText(md.getKeterangan());
        holder.status.setText(md.getStatus());
        holder.tgl_p.setText(md.getTgl_pengajuan());
        holder.tgl.setText(md.getTgl_pensiun());
        holder.nama.setText(md.getNama());
        holder.id.setText(md.getId());

        holder.md = md;

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder {
        TextView id, nama, tgl, tgl_p, status, keterangan;
        Data md;

        public HolderData(View view) {
            super(view);

            id = (TextView) view.findViewById(R.id.id);
            nama = (TextView) view.findViewById(R.id.name);
            tgl = (TextView) view.findViewById(R.id.tgl);
            tgl_p = (TextView)view.findViewById(R.id.tgl_p);
            status = (TextView) view.findViewById(R.id.sts);
            keterangan = (TextView)view.findViewById(R.id.keter);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, ApproveActivity.class);
                    update.putExtra("update",1);
                    update.putExtra("id", md.getId());
                    update.putExtra("name",md.getNama());
                    update.putExtra("unit_kerja",md.getUnitkerja());
                    update.putExtra("alasan",md.getAlasan());
                    update.putExtra("tgl_pensiun",md.getTgl_pensiun());
                    update.putExtra("tgl_pengajuan",md.getTgl_pengajuan());
                    update.putExtra("status", md.getStatus());
                    update.putExtra("keterangan", md.getKeterangan());

                    context.startActivity(update);
                }
            });

        }
    }
}
