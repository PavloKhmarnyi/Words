package com.example.pavlo.wwords.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavlo.wwords.R;
import com.example.pavlo.wwords.local_db_manager.LocalDatabaseManager;
import com.example.pavlo.wwords.online_translator.api.ApiFactory;
import com.example.pavlo.wwords.online_translator.models.AvailableLanguages;
import com.example.pavlo.wwords.online_translator.models.TranslateResult;
import com.example.pavlo.wwords.online_translator.utilities.Config;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final LinkedHashMap<String, String> storedLanguages = new LinkedHashMap<>();

    private Spinner fromLanguageSpinner;
    private Spinner toLanguageSpinner;

    private EditText fromLanguageEditText;

    private TextView toLanguageTextView;

    private Button openDictionaryButton;
    private Button translateButton;
    private Button addToDictionaryButton;

    private ArrayAdapter<String> adapter;

    private Subscription subscription;
    private ApiFactory apiFactory;

    private LocalDatabaseManager databaseManager;

    private LinkedHashMap<String, String> languagesMap;

    private String fromLanguage;
    private String toLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiFactory = new ApiFactory();

        databaseManager = new LocalDatabaseManager(this);

        initComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAvailableLanguages();
    }

    private void initComponents() {
        fromLanguageSpinner = (Spinner) findViewById(R.id.fromLanguageSpinner);
        toLanguageSpinner = (Spinner) findViewById(R.id.toLanguageSpinner);

        fromLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                fromLanguage = languagesMap.get(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                toLanguage = languagesMap.get(item);

                toLanguageTextView.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fromLanguageEditText = (EditText) findViewById(R.id.fromLanguageEditText);
        fromLanguageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!toLanguageTextView.getText().equals("")) {
                    toLanguageTextView.setText("");
                }
                translate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toLanguageTextView = (TextView) findViewById(R.id.toLanguageTextView);

        openDictionaryButton = (Button) findViewById(R.id.openDictionaryButton);
        translateButton = (Button) findViewById(R.id.translateButton);
        addToDictionaryButton = (Button) findViewById(R.id.addToDictionaryButton);

        openDictionaryButton.setOnClickListener(this);
        translateButton.setOnClickListener(this);
        addToDictionaryButton.setOnClickListener(this);

        storedLanguages.put("en", "English");
        storedLanguages.put("uk", "Ukrainian");
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    private void getAvailableLanguages() {
        boolean internetAccess = isNetworkConnected();

        if (internetAccess) {
            Observable<AvailableLanguages> getLanguagesList = apiFactory.getAvailableLanguages("en");
            getLanguagesList.subscribe(new Action1<AvailableLanguages>() {
                @Override
                public void call(AvailableLanguages availableLanguages) {
                    ArrayList<String> languages = new ArrayList<String>(availableLanguages.getLangs().values());
                    adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, languages);
                    fromLanguageSpinner.setAdapter(adapter);
                    toLanguageSpinner.setAdapter(adapter);

                    setShortNamesLanguages(availableLanguages.getLangs(), languages);
                }
            });
            subscription = getLanguagesList.subscribe();
        } else {
            ArrayList<String> languages = new ArrayList<>(storedLanguages.values());
            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, languages);
            fromLanguageSpinner.setAdapter(adapter);
            toLanguageSpinner.setAdapter(adapter);

            setShortNamesLanguages(storedLanguages, languages);
        }
    }

    private void setShortNamesLanguages(LinkedHashMap<String, String> langMap, ArrayList<String> langs) {
        Object[] keys = langMap.keySet().toArray();

        languagesMap = new LinkedHashMap<String, String>();

        for (int i = 0; i < langs.size(); i++) {
            languagesMap.put(langs.get(i), keys[i].toString());
        }
    }

    private void translate() {
        StringBuilder lang = new StringBuilder();
        lang.append(fromLanguage);
        lang.append("-");
        lang.append(toLanguage);

        String text = fromLanguageEditText.getText().toString();

        if (isNetworkConnected()) {
            Observable<TranslateResult> translateText = apiFactory.translateText(text, lang.toString());
            translateText.subscribe(new Action1<TranslateResult>() {
                @Override
                public void call(TranslateResult translateResult) {
                    if (translateResult != null && translateResult.getCode() == Config.SUCCESS_CODE) {
                        StringBuilder translateText = new StringBuilder();
                        for (String itemResult: translateResult.getText()) {
                            translateText.append(itemResult);
                        }
                        toLanguageTextView.setText(translateText.toString());
                    } else {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            subscription = translateText.subscribe();
        } else {
            String translatedText = databaseManager.searchWord(text, toLanguage);
            toLanguageTextView.setText(translatedText);
        }
    }

    private void addToDictionary() {
        String englishText = "";
        String ukrainianText = "";

        if (fromLanguage.equals("en") && toLanguage.equals("uk")) {
            englishText = fromLanguageEditText.getText().toString();
            ukrainianText = toLanguageTextView.getText().toString();
        } else if (fromLanguage.equals("uk") && toLanguage.equals("en")) {
            englishText = toLanguageTextView.getText().toString();
            ukrainianText = fromLanguageEditText.getText().toString();
        }

        if (isWordsValid(englishText, ukrainianText)) {
            try {
                long id = databaseManager.addWord(englishText, ukrainianText);
                if (id > 0) {
                    Toast.makeText(MainActivity.this, "The words added to dictionary!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Words are not valid!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isWordsValid (String englishWord, String ukrainianWord) {
        return !englishWord.equals("") &&
                !ukrainianWord.equals("") &&
                englishWord != null && ukrainianWord != null &&
                englishWord.indexOf(" ") == -1 &&
                ukrainianWord.indexOf(" ") == -1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openDictionaryButton:
                Intent intent = new Intent(MainActivity.this, DictionaryActivity.class);
                startActivity(intent);
                break;
            case R.id.translateButton:
                if (!fromLanguageEditText.getText().equals("")) {
                    translate();
                } else {
                    Toast.makeText(MainActivity.this, "You must enter word to translate.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.addToDictionaryButton:
                addToDictionary();
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (this.subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }
}
