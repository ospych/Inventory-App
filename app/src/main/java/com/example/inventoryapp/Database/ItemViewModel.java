package com.example.inventoryapp.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepository repository;
    private LiveData<List<ItemDb>> allItems;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        allItems = repository.getAllItems();

    }

    public void insert(ItemDb itemDb) {
        repository.insert(itemDb);
    }

    public void update(ItemDb itemDb) {
        repository.update(itemDb);
    }

    public void delete(ItemDb itemDb) {
        repository.delete(itemDb);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<ItemDb>> getAllNotes() {
        return allItems;
    }
}
