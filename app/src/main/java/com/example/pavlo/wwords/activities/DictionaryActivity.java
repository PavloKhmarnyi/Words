package com.example.pavlo.wwords.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pavlo.wwords.R;
import com.example.pavlo.wwords.adapters.DictionaryAdapter;
import com.example.pavlo.wwords.adapters.OnItemClickListener;
import com.example.pavlo.wwords.local_db_manager.LocalDatabaseManager;
import com.example.pavlo.wwords.models.WordsPair;

import java.util.ArrayList;

/**
 * Created by pavlo on 21.07.16.
 */
public class DictionaryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button toMainActivityButton;
    private Button addToDictionaryButton;

    private EditText englishEditText;
    private EditText ukrainianEditText;

    private RecyclerView dictionaryRecyclerView;

    private DictionaryAdapter adapter;
    private ArrayList<WordsPair> wordsPairs;

    private LocalDatabaseManager databaseManager;

    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        intent = getIntent();

        databaseManager = new LocalDatabaseManager(this);
        wordsPairs = databaseManager.getDictionary();

        initComponents();
    }

    private void initComponents() {
        toMainActivityButton = (Button) findViewById(R.id.toMainActivityButton);
        addToDictionaryButton = (Button) findViewById(R.id.addToDictionaryButton);

        toMainActivityButton.setOnClickListener(this);
        addToDictionaryButton.setOnClickListener(this);

        englishEditText = (EditText) findViewById(R.id.englishEditText);
        ukrainianEditText = (EditText) findViewById(R.id.ukrainianEditText);

        dictionaryRecyclerView = (RecyclerView) findViewById(R.id.dictionaryRecyclerView);
        dictionaryRecyclerView.setLayoutManager(new LinearLayoutManager(DictionaryActivity.this));
        adapter = new DictionaryAdapter(DictionaryActivity.this, wordsPairs, new OnItemClickListener() {
            @Override
            public void onItemClick(final WordsPair item) {
                new AlertDialog.Builder(DictionaryActivity.this).
                        setTitle("Delete words").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseManager.delete(item.getId());
                        int position = wordsPairs.indexOf(item);
                        wordsPairs.remove(item);
                        dictionaryRecyclerView.removeViewAt(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, wordsPairs.size());
                        Toast.makeText(DictionaryActivity.this, "The pair deleted successfuly!", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DictionaryActivity.this, "Actoin was canceled!", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        dictionaryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toMainActivityButton:
                finish();
                break;
            case R.id.addToDictionaryButton:
                String englishWord = englishEditText.getText().toString();
                String ukrainianWord = ukrainianEditText.getText().toString();

                try {
                    if (englishWord != null && !englishWord.equals("") &&
                            ukrainianWord != null && !ukrainianWord.equals("")) {
                        long id = databaseManager.addWord(englishWord, ukrainianWord);
                        wordsPairs.add(new WordsPair(englishWord, ukrainianWord, id));
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemRangeChanged(wordsPairs.size() - 1, wordsPairs.size());
                        englishEditText.setText("");
                        ukrainianEditText.setText("");
                    }
                    Toast.makeText(DictionaryActivity.this, "The pair saved successfuly!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(DictionaryActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
