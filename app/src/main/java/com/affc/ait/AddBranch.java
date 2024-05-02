package com.affc.ait;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Branch;

public class AddBranch extends AppCompatActivity {

    private EditText editTextBranchName, editTextLocation;
    private Button btnAddBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_branch);

        editTextBranchName = findViewById(R.id.branchName);
        editTextLocation = findViewById(R.id.link);
        btnAddBranch = findViewById(R.id.addBranch);

        btnAddBranch.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchName = editTextBranchName.getText().toString().trim();
                String location = editTextLocation.getText().toString().trim();

                if (branchName.isEmpty() || location.isEmpty()){
                    Toast.makeText(AddBranch.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                Branch branch = new Branch(branchName, location);

                addBranchToDatabase(branch);
            }
        }));

    }
    private void addBranchToDatabase(Branch branch){
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        dbHandler.addBranch((branch));
        Toast.makeText(this, "Branch added successfully!", Toast.LENGTH_SHORT).show();
    }
}