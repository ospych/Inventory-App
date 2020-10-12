package com.example.inventoryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inventoryapp.Database.ItemDb;

public class ItemAdapter extends ListAdapter<ItemDb, ItemAdapter.NoteHolder> {
    private OnItemClickListener listener;
    ItemAdapter adapter;
    private Context context;

    public ItemAdapter() {
        super(DIFF_CALLBACK);

    }

    private static final DiffUtil.ItemCallback<ItemDb> DIFF_CALLBACK = new DiffUtil.ItemCallback<ItemDb>() {
        @Override
        public boolean areItemsTheSame(@NonNull ItemDb oldItem, @NonNull ItemDb newItem) {
            return oldItem.getId() == newItem.getId();
        }


        @Override
        public boolean areContentsTheSame(@NonNull ItemDb oldItem, @NonNull ItemDb newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getSupplier().equals(newItem.getSupplier()) &&
                    oldItem.getQuantity() == newItem.getQuantity();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new NoteHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        ItemDb currentNote = getItem(position);
        Uri uri = Uri.parse(currentNote.getImage());
        Context context = holder.imageView.getContext();


        holder.title.setText("Item: " + currentNote.getTitle());
        holder.supplier.setText("Supplier: " + currentNote.getSupplier());
        holder.quantity.setText("Quantity: " + currentNote.getQuantity());
        holder.price.setText(currentNote.getPrice() + "$");

        Glide.with(context).load(uri).into(holder.imageView);
    }

    public ItemDb getItemAt(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView supplier;
        private TextView quantity;
        private TextView price;
        private ImageView imageView;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            supplier = itemView.findViewById(R.id.supplier);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(ItemDb itemDb);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
