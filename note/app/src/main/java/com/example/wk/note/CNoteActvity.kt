package com.example.wk.note

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import kotlinx.android.synthetic.main.note_layout.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction
import org.jetbrains.anko.db.update
import org.jetbrains.anko.find
import org.w3c.dom.Text

/**
 * Created by wk on 2018/6/15.
 */
class CNoteActvity : AppCompatActivity() {
    lateinit var newNote: Note
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cnote_layout)
//        toolbar
        setSupportActionBar(ntoolbar)
//        菜单按钮
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        val textView: EditText = find(R.id.ccEdit_text)
        val titleView: EditText = find(R.id.ccEdit_title)
        //      根据id查询，设置id值为点击获取的
        val intent = intent
        val _id: Int = intent.getIntExtra("_id", 0)
//        将_id变成全局变量
        id = _id
        println(id)
//        数据库查询操作
        NoteDatabase.use {
            //            查找点击数据
            val note = select("Note")
                    .whereArgs("_id={id}",
                            "id" to id)
            val myNote = note.parseSingle(classParser<Note>())
//            映射数据到界面
            textView.setText(myNote.text)
            titleView.setText(myNote.title)
            println(myNote)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.submit -> {
                val textView: EditText = find(R.id.ccEdit_text)
                val titleView: EditText = find(R.id.ccEdit_title)
                val newText = textView.text.toString()
                val newTitle = titleView.text.toString()
//                val newNote=Note(id,newTitle as String,newText)
                println("================================================================")
                println(newTitle)
                println(newText)
                println("================================================================")
                NoteDatabase.use {
                    println("================================================================")
                    transaction {
                        println("================================================================")
//                        update("Note", "title" to newTitle,"_id={id}","id" to id)
//                                .whereArgs("_id={id}",
//                                        "id" to id)
//                                .exec()
                        update("Note", "title" to newTitle).whereArgs("_id={id}", "id" to id).exec()

                        println("================================================================")
//                        update("Note", "text" to newText)
//                                .whereArgs("_id={id}",
//                                        "id" to id)
//                                .exec()
//                                .whereSimple("_id=?", id as String)
//                                .exec()
                        update("Note", "text" to newText).whereArgs("_id={id}", "id" to id).exec()
                    }
                }
                var intent = Intent()
                intent.setClass(this@CNoteActvity, MainActivity::class.java)
                startActivityForResult(intent, 1)
            }
        }
        return true
    }
}
