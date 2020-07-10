package com.example.kotlinuploadpdf

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterClass(
    var recycle: RecyclerView,
    var context: Context,
    var list: ArrayList<String>,
    private var urls: ArrayList<String?>,
    acc: Int,
    itemClickListener: ItemClickListener
) :
    RecyclerView.Adapter<AdapterClass.MyViewholder>() {
    var acc: Int
    var itemClickListener: ItemClickListener
    fun update(fileName: String, url: String, access: Int) {
        acc = access
        list.add(fileName)
        urls.add(url)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewholder {
        return MyViewholder(
            LayoutInflater.from(context).inflate(com.example.kotlinuploadpdf.R.layout.independent_files, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: MyViewholder,
        position: Int
    ) {
        holder.tvFileName.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewholder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvFileName: TextView

        init {
            tvFileName = itemView.findViewById(com.example.kotlinuploadpdf.R.id.tvFileName)
            tvFileName.setOnClickListener { view ->
                val rootView =
                    view.parent.parent as View
                val pos = recycle.getChildLayoutPosition(rootView)
                val a = Intent(Intent.ACTION_VIEW)
                a.data = Uri.parse(urls[pos])
                context.startActivity(a)

                /*Intent intent = new Intent(Intent.ACTION_VIEW);

                                    intent.setDataAndType(Uri.parse( "http://docs.google.com/viewer?url=" + urls.get(pos)), "text/html");

                                    context.startActivity(intent);
                */
            }
        }
    }

    companion object {
        lateinit var urls: ArrayList<String>
    }

    init {
        this.urls = urls
        this.acc = acc
        this.itemClickListener = itemClickListener
    }
}

