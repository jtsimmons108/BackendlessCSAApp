package com.example.jeremiahsimmons.backednlesscsapdp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String APPLICATION_ID = "1C1B1608-7887-2AA2-FFE9-20EEDB959800";
    private static final String SECRET_KEY = "C4A482B4-3102-5941-FFF7-4A81A77ED800";

    private EditText firstNameEditText, lastNameEditText;
    private Button saveButton, findButton;
    private TextView resultView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Backendless.initApp(this, APPLICATION_ID, SECRET_KEY);

        firstNameEditText = findViewById(R.id.et_first_name);
        lastNameEditText = findViewById(R.id.et_last_name);
        resultView = findViewById(R.id.tv_result);
        saveButton = findViewById(R.id.btn_save_person);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();

                if(!firstName.isEmpty() && !lastName.isEmpty()){
                    Person person = new Person(firstName, lastName);
                    Backendless.Persistence.save(person, new AsyncCallback<Person>() {
                        @Override
                        public void handleResponse(Person response) {
                            Toast.makeText(MainActivity.this, "" + response.getFirstName() + " was saved", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.e("MainActivity", fault.toString());
                        }
                    });

                    firstNameEditText.getText().clear();
                    lastNameEditText.getText().clear();
                }
            }
        });

        findButton = findViewById(R.id.btn_find_person);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = firstNameEditText.getText().toString();
                DataQueryBuilder query = DataQueryBuilder.create();

                String whereClause = String.format("firstName = '%s'", firstName);
                query.setWhereClause(whereClause);

                Backendless.Persistence.of(Person.class)
                        .find(query, new AsyncCallback<List<Person>>() {
                            @Override
                            public void handleResponse(List<Person> response) {
                                resultView.setText("");
                                for(Person p : response){
                                    resultView.append(p.toString() + "\n");
                                }
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Log.e("MainActivity", fault.toString());
                            }
                        });

            }
        });


    }
}
