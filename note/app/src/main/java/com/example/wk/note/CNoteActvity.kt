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
import org.jetbrains.anko.alert
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction
import org.jetbrains.anko.db.update
import org.jetbrains.anko.find
import org.jetbrains.anko.noButton
import org.jetbrains.anko.okButton
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wk on 2018/6/15.
 */
class CNoteActvity : AppCompatActivity() {
    var id = 0
    lateinit var oNote:Note
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
        val textView: EditText = find(R.id.ccEdit_text)
        val titleView: EditText = find(R.id.ccEdit_title)
        val newText = textView.text.toString()
        val newTitle = titleView.text.toString()
//                val newNote=Note(id,newTitle as String,newText)
        val newTime = getTime().toString()
        val values = ContentValues()
        when (item?.itemId) {
            android.R.id.home -> {
                NoteDatabase.use {
                    val dnote = select("Note")
                            .whereArgs("_id={id}", "id" to id)
                    oNote = dnote.parseSingle(classParser<Note>())
                }
                if (oNote.text!=newText||oNote.title!=newTitle){
                    alert ("您修改了部分内容，如果不保存就退出，这部分内容将会丢失，是否保存？","提醒"){
                        okButton {
                            values.put("title", newTitle)
                            values.put("text", newText)
                            values.put("time", newTime)
                            NoteDatabase.use {
                                transaction {
                                    insert(
                                            "Note",
                                            "_id",
                                            values
                                    )
                                }
                                NoteDatabase.use {
                                    delete("Note", "_id=?", arrayOf(id.toString()))
                                }
                            }
                            var intent = Intent()
                            intent.setClass(this@CNoteActvity, MainActivity::class.java)
                            startActivityForResult(intent, 1)
                            finish()
                        }
                        noButton {
                            finish()
                        }
                    }.show()
                }else{
                    finish()
                }
            }
//            提交
            R.id.submit -> {

                values.put("title", newTitle)
                values.put("text", newText)
                values.put("time", newTime)
                NoteDatabase.use {
                    transaction {
                        insert(
                                "Note",
                                "_id",
                                values
                        )
                    }
                    NoteDatabase.use {
                        delete("Note", "_id=?", arrayOf(id.toString()))
                    }
                }

//                NoteDatabase.use {
//
//                    transaction {
//
//                        //                        update("Note", "title" to newTitle,"_id={id}","id" to id)
////                                .whereArgs("_id={id}",
////                                        "id" to id)
////                                .exec()
//                        update("Note", "title" to newTitle).whereArgs("_id={id}", "id" to id).exec()
//
////                        update("Note", "text" to newText)
////                                .whereArgs("_id={id}",
////                                        "id" to id)
////                                .exec()
////                                .whereSimple("_id=?", id as String)
////                                .exec()
//                        update("Note", "text" to newText).whereArgs("_id={id}", "id" to id).exec()
//                        update("Note", "time" to newTime).whereArgs("_id={id}", "id" to id).exec()
//                    }
//                }
                var intent = Intent()
                intent.setClass(this@CNoteActvity, MainActivity::class.java)
                startActivityForResult(intent, 1)
                finish()
                MainActivity.instance!!.finish()
            }
        }
        return true
    }

    fun getTime(): String {
        val s = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val date = s.format(System.currentTimeMillis())
        println("s  $date")
        val time: String = s.format(Date())
        return time
    }
}
