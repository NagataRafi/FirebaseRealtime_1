package com.nagata.firebaserealtime_1

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListViewBiodata(
    private val context: Activity,
    private val biodataList: List<Biodata>
) : ArrayAdapter<Biodata>(context, R.layout.layout_listview, biodataList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = convertView ?: inflater.inflate(R.layout.layout_listview, parent, false)

        val textViewNama = listViewItem.findViewById<TextView>(R.id.textViewNama)
        val textViewUmur = listViewItem.findViewById<TextView>(R.id.textViewUmur)
        val textViewJk = listViewItem.findViewById<TextView>(R.id.textViewJk)

        val biodata = biodataList[position]

        textViewNama.text = biodata.nama
        textViewUmur.text = biodata.umur
        textViewJk.text = biodata.jenisKelamin

        return listViewItem
    }
}
