package com.example.wk.note

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.find
import org.w3c.dom.Text
import android.widget.Toast


/**
 * Created by wk on 2018/6/14.
 */

class NoteAdapter(notes: List<Note>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    val notes = notes

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val note: Note = notes.get(position)
        holder!!.title.setText(note.title)
        holder!!.text.setText(note.text)
        holder!!.time.setText(note.time)
    }

    //    点击/长按 事件
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.notelist_layout, parent, false)
        val holder = ViewHolder(view)
        //    点击事件
        holder.noteView.setOnClickListener(View.OnClickListener { v ->
            val position = holder.adapterPosition
            val note = notes.get(position)
//            var intent=Intent()
//            intent.setClass(this@NoteActivity,MainActivity::class.java)
//            startActivityForResult(intent,1)
////                finish()
            val intent = Intent()
            intent.setClass(v.context, CNoteActvity::class.java)
            intent.putExtra("_id", note._id)
            println(note._id)
            v.context.startActivity(intent)
//            Toast.makeText(v.context,"you chlicked view"+note.title,Toast.LENGTH_SHORT).show()
        })
//        长按事件
        holder.noteView.setOnLongClickListener(View.OnLongClickListener { v ->
            val position = holder.adapterPosition
            val note = notes.get(position)
            val id = note._id.toString()
            var string_array = arrayOf(id)
            val delAlert = AlertDialog.Builder(v.context)
            delAlert.setTitle("删除")
            delAlert.setMessage("一旦删除后无法恢复，确定删除吗？")
            delAlert.setCancelable(false)
            delAlert.setPositiveButton("确定", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                //                Toast.makeText(v.context, "已删除", Toast.LENGTH_SHORT).show()
//                v.context.NoteDatabase.use {
//                    delete("Note", "_id={id}", "id" to id )
//                }
                val NoteDB = NoteDatabaseHelper.getInstance(v.context).writableDatabase
                NoteDB.delete("Note", "_id=?", string_array)
                val intent = Intent()
                intent.setClass(v.context, MainActivity::class.java)
                v.context.startActivity(intent)

            })
            delAlert.setNegativeButton("取消", null)
            delAlert.create()
            delAlert.show()
            return@OnLongClickListener true
        })
        return holder
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var noteView = view
        var text: TextView = view.find(R.id.note_text)
        var title: TextView = view.find(R.id.note_title)
        var time: TextView = view.find(R.id.note_time)
    }

}

