package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.inventoryapp.Database.ItemDb;
import com.example.inventoryapp.Database.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_REQUEST = 1;
    public static final int EDIT_REQUEST = 2;

    private ItemViewModel itemViewModel;
    private ItemDb itemDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, ADD_REQUEST);
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        final ItemAdapter adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);

        itemViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(ItemViewModel.class);
        itemViewModel.getAllNotes().observe(this, new Observer<List<ItemDb>>() {
            @Override
            public void onChanged(List<ItemDb> items) {
                adapter.submitList(items);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                itemViewModel.delete(adapter.getItemAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemDb itemDb) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(EditActivity.EXTRA_TITLE, itemDb.getTitle());
                intent.putExtra(EditActivity.EXTRA_SUPPLIER, itemDb.getSupplier());
                intent.putExtra(EditActivity.EXTRA_QUANTITY, itemDb.getQuantity());
                intent.putExtra(EditActivity.EXTRA_PRICE, itemDb.getPrice());
                intent.putExtra(EditActivity.EXTRA_IMAGE, itemDb.getImage());
                intent.putExtra(EditActivity.EXTRA_ID, itemDb.getId());
                startActivityForResult(intent, EDIT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            String title = data.getStringExtra(EditActivity.EXTRA_TITLE);
            String supplier = data.getStringExtra(EditActivity.EXTRA_SUPPLIER);
            int quantity = data.getIntExtra(EditActivity.EXTRA_QUANTITY, 1);
            double price = data.getDoubleExtra(EditActivity.EXTRA_PRICE, 1);
            String image = data.getStringExtra(EditActivity.EXTRA_IMAGE);



            itemDb = new ItemDb(image, title, supplier, price, quantity);
            itemViewModel.insert(itemDb);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT)
                    .show();
        } else if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            int id = data.getIntExtra(EditActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Can't be updated", Toast.LENGTH_SHORT)
                        .show();
            }

            String title = data.getStringExtra(EditActivity.EXTRA_TITLE);
            String supplier = data.getStringExtra(EditActivity.EXTRA_SUPPLIER);
            int quantity = data.getIntExtra(EditActivity.EXTRA_QUANTITY, 1);
            double price = data.getDoubleExtra(EditActivity.EXTRA_PRICE, 1);
            String image = data.getStringExtra(EditActivity.EXTRA_IMAGE);


            itemDb = new ItemDb(image, title, supplier, price, quantity);
            itemDb.setId(id);
            itemViewModel.update(itemDb);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT)
                    .show();


        } else {
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT)
                    .show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ((item.getItemId()) == R.id.delete_all) {
            DialogButton();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void DialogButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemViewModel.deleteAllNotes();
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}