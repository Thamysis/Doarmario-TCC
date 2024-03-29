package ifsp.doarmario.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ifsp.doarmario.R;
import ifsp.doarmario.model.vo.Vestuario;

public class DoadasAdapter extends RecyclerView.Adapter<DoadasAdapter.MyViewHolder> {

    private List<Vestuario> listaVestuarios;
    private Context context;

    public DoadasAdapter(List<Vestuario> lista, Context c) {
        this.listaVestuarios = lista;
        context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_listagem_doadas, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Vestuario vestuario = listaVestuarios.get(position);

        if(vestuario.getImagem_vestuario() != null){
            Bitmap img = BitmapFactory.decodeFile(vestuario.getImagem_vestuario());
            holder.imageView.setImageBitmap(img);
        } else{
        }
    }

    @Override
    public int getItemCount() {
        return this.listaVestuarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        //TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            //textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageViewD);

        }
    }
}
