package com.example.inventoryapp.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemRepository {
    private ItemDao itemDao;
    private LiveData<List<ItemDb>> allItems;

    public ItemRepository(Application application) {
        ItemDatabase database = ItemDatabase.getInstance(application);
        itemDao = database.itemDao();
        allItems = itemDao.getAllItems();
    }

    public void insert(ItemDb itemDb) {
        new InsertNoteAsyncTask(itemDao).execute(itemDb);
    }

    public void update(ItemDb itemDb) {
        new UpdateNoteAsyncTask(itemDao).execute(itemDb);
    }

    public void delete(ItemDb itemDb) {
        new DeleteNoteAsyncTask(itemDao).execute(itemDb);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesNoteAsyncTask(itemDao).execute();
    }

    public LiveData<List<ItemDb>> getAllItems() {
        return allItems;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<ItemDb, Void, Void> {
        private ItemDao itemDao;

        private InsertNoteAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(ItemDb... items) {
            itemDao.insert(items[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<ItemDb, Void, Void> {
        private ItemDao itemDao;

        private UpdateNoteAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(ItemDb... itemDbs) {
            itemDao.update(itemDbs[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<ItemDb, Void, Void> {
        private ItemDao itemDao;

        private DeleteNoteAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(ItemDb... itemDbs) {
            itemDao.delete(itemDbs[0]);
            return null;
        }
    }

    private static class DeleteAllNotesNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private ItemDao itemDao;

        private DeleteAllNotesNoteAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemDao.deleteAllItems();
            return null;
        }
    }
}
