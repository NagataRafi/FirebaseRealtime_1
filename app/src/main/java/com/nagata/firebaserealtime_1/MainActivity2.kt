package com.nagata.firebaserealtime_1

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity2 : AppCompatActivity() {

    private lateinit var editTextUpdateNama: EditText
    private lateinit var editTextUpdateUmur: EditText
    private lateinit var spinnerUpdateJk: Spinner

    private var id: String = ""
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        editTextUpdateNama = findViewById(R.id.editText_updateNama)
        editTextUpdateUmur = findViewById(R.id.editText_updateUmur)
        spinnerUpdateJk = findViewById(R.id.spinner_updateJk)

        id = intent.getStringExtra("id") ?: ""
        database = FirebaseDatabase.getInstance("https://fir-realtime-1-62771-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("biodata")
            .child(id)
    }

    override fun onStart() {
        super.onStart()
        if (id.isEmpty()) return

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    return
                }
                val biodata = snapshot.getValue(Biodata::class.java)
                if (biodata != null) {
                    editTextUpdateNama.setText(biodata.nama)
                    editTextUpdateUmur.setText(biodata.umur)

                    val items = resources.getStringArray(R.array.jeniskelamin)
                    val index = items.indexOf(biodata.jenisKelamin)
                    if (index >= 0) {
                        spinnerUpdateJk.setSelection(index)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity2, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun buttonUpdateBiodata(view: View) {
        val nama = editTextUpdateNama.text.toString().trim()
        val umur = editTextUpdateUmur.text.toString().trim()
        val jk = spinnerUpdateJk.selectedItem?.toString() ?: ""

        if (nama.isEmpty() || umur.isEmpty()) {
            Toast.makeText(this, "Input tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val biodata = Biodata(id, nama, umur, jk)
        database.setValue(biodata)
        Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun buttonDeleteBiodata(view: View) {
        database.removeValue()
        Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
        finish()
    }
}
