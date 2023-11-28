package com.example.th8;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBHelper db;
    private ListView listView;
    private Spinner spinner;
    private Button applyButton, allButton, closeButton;
    private ArrayAdapter<NhanVien> arrayAdapter;
    private ArrayAdapter<String> adapter;  // Adapter for the listView
    private int selectedEmployeePosition;   // To keep track of the selected employee

    ArrayList<NhanVien> arrayList = new ArrayList<NhanVien>();
    ArrayList<PhongBan> arrayList2 = new ArrayList<PhongBan>();
    private ContextMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new DBHelper(this);
        listView = findViewById(R.id.listView);
        spinner = findViewById(R.id.spinner);
        applyButton = findViewById(R.id.applyButton);
        allButton = findViewById(R.id.allButton);
        closeButton = findViewById(R.id.closeButton);
        registerForContextMenu(listView);
        PhongBan p =new PhongBan(1,"Phong Hanh Chinh");
        db.addPhong(p);
        arrayList2.add(p);
        p =new PhongBan(2,"Phong Ke Toan");
        db.addPhong(p);
        arrayList2.add(p);
        p =new PhongBan(3,"Phong Nhan Su");
        db.addPhong(p);
        arrayList2.add(p);
        NhanVien nv=new NhanVien(1, "Nguyễn Văn A",1);
        arrayList.add(nv);
        db.addNhanvien(nv);
        nv=new NhanVien(2, "Nguyễn Kim D",2);
        arrayList.add(nv);
        db.addNhanvien(nv);
        nv=new NhanVien(3, "Nguyễn Thị B",1);
        arrayList.add(nv);
        db.addNhanvien(nv);
        nv=new NhanVien(4, "Nguyễn Văn C",2);
        arrayList.add(nv);
        db.addNhanvien(nv);

        // Populate spinner with phong ban data
        ArrayAdapter<PhongBan> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, arrayList2
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        // Set up onClickListeners
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhongBan selectedPhongBan = (PhongBan) spinner.getSelectedItem();
                locnv(selectedPhongBan);
            }
        });


        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fetch and display all employees
                loadall();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close the application
            }
        });

        // Set up listView item long click listener for context menu
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedEmployeePosition = i;
                openContextMenu(view);
                return false;

            }
        });

        loadall();
    }

    public void loadall() {
        // Get list of Product from database
        ArrayList<NhanVien> list = db.getAllnv();
        // Create a new instance of ProductAdapter with the list of products
        arrayAdapter = new ArrayAdapter<NhanVien>(this,android.R.layout.simple_list_item_1, list);
        // Set the array adapter to listView
        listView.setAdapter(arrayAdapter);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() ==R.id.menu_add)
        {
                Toast.makeText(this, "Thêm clicked", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() ==R.id.menu_edit)
        {

                Toast.makeText(this, "Sửa clicked", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.menu_delete)
        {
                showDeleteConfirmationDialog();
        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xoá");
        builder.setMessage("Bạn có chắc chắn muốn xoá nhân viên này không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: Implement logic to delete the selected employee from the database
                String deletedEmployee = adapter.getItem(selectedEmployeePosition);
                adapter.remove(deletedEmployee);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Nhân viên đã được xoá", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    private void locnv(PhongBan selectedPhongBan) {
        // Get list of employees based on the selected PhongBan
        ArrayList<NhanVien> filteredList = db.getEmployeesByPhongBan(selectedPhongBan.getMaph());

        // Update the array adapter with the filtered list
        arrayAdapter.clear();
        arrayAdapter.addAll(filteredList);
        arrayAdapter.notifyDataSetChanged();
    }

}