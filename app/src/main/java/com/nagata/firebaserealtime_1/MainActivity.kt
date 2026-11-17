package com.nagata.firebaserealtime_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var editTextNama: EditText
    private lateinit var editTextUmur: EditText
    private lateinit var spinnerJK: Spinner
    private lateinit var listViewBio: ListView

    private lateinit var database: DatabaseReference
    private val biodataList = mutableListOf<Biodata>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextNama = findViewById(R.id.editTextNama)
        editTextUmur = findViewById(R.id.editTextUmur)
        spinnerJK = findViewById(R.id.spinnerJK)
        listViewBio = findViewById(R.id.listView_bio)

        database = FirebaseDatabase.getInstance("https://fir-realtime-1-62771-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("biodata")

        listViewBio.setOnItemClickListener { _, _, position, _ ->
            val biodata = biodataList[position]
            val intent = Intent(this@MainActivity, MainActivity2::class.java).apply {
                putExtra("id", biodata.id)
                putExtra("nama", biodata.nama)
                putExtra("umur", biodata.umur)
                putExtra("jenisKelamin", biodata.jenisKelamin)
            }
            startActivity(intent)
        }
    }

    fun addBiodata(view: View) {
        val nama = editTextNama.text.toString().trim()
        val umur = editTextUmur.text.toString().trim()
        val jk = spinnerJK.selectedItem?.toString() ?: ""

        if (nama.isEmpty() || umur.isEmpty()) {
            Toast.makeText(this, "Nama dan umur tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val id = database.push().key
        if (id != null) {
            val biodata = Biodata(id, nama, umur, jk)
            database.child(id).setValue(biodata)
            editTextNama.text.clear()
            editTextUmur.text.clear()
            Toast.makeText(this, "Data biodata berhasil ditambahkan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                biodataList.clear()
                for (postSnapshot in snapshot.children) {
                    val biodata = postSnapshot.getValue(Biodata::class.java)
                    if (biodata != null) {
                        biodataList.add(biodata)
                    }
                }
                val adapter = ListViewBiodata(this@MainActivity, biodataList)
                listViewBio.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
