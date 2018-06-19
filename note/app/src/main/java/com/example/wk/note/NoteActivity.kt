package com.example.wk.note

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.note_layout.*
import kotlinx.android.synthetic.main.notelist_layout.*
import org.jetbrains.anko.*
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer


/**
 * Created by wk on 2018/6/13.
 */
class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_layout)
//        toolbar
        setSupportActionBar(ntoolbar)
//        菜单按钮
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }
    }

    //    toolbar按钮
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val titletView: EditText = find(R.id.cEdit_title)
                val title: String = titletView.text.toString()
                val textView: EditText = find(R.id.cEdit_text)
                val text: String = textView.text.toString()
                val values = ContentValues()
                values.put("title", title)
                values.put("text", text)
//                values.put("time",)
                Log.d("values", values.toString())
                Log.d("asd", "xxx")
                val date = getTime()
                values.put("time", date)
                if (title.isNotEmpty() || text.isNotEmpty()) {
                    println("000000000000000000000000000")
                    println(title)
                    println(text)
                    println("000000000000000000000000000")
                    alert("您输入了一些东西，请问是否保存？", "提醒") {
                        yesButton {
                            NoteDatabase.use {
                                insert(
                                        "Note",
                                        "_id",
                                        values
                                )
                            }
                            var intent = Intent()
                            intent.setClass(this@NoteActivity, MainActivity::class.java)
                            startActivityForResult(intent, 1)
                            MainActivity.instance!!.finish()
                        }
                        noButton {
                            finish()
                        }
                    }.show()
                } else {
                    finish()
                }
            }

//                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
//                val currentDate = sdf.format(Date())
//            System.out.println(" C DATE is  "+currentDate)
            R.id.submit -> {
                val titletView: EditText = find(R.id.cEdit_title)
                val title: String = titletView.text.toString()
                val textView: EditText = find(R.id.cEdit_text)
                val text: String = textView.text.toString()
                val values = ContentValues()
                values.put("title", title)
                values.put("text", text)
                val date = getTime()
                values.put("time", date)
                if (title.isEmpty() && text.isEmpty()) {
                    alert("您没有输入任何东西，请问是否保存？", "提醒") {
                        yesButton {
                            NoteDatabase.use {
                                insert(
                                        "Note",
                                        "_id",
                                        values
                                )
                            }
                            var intent = Intent()
                            intent.setClass(this@NoteActivity, MainActivity::class.java)
                            startActivityForResult(intent, 1)
                            MainActivity.instance!!.finish()
                            finish()
                        }
                        noButton {
                            var intent = Intent()
                            intent.setClass(this@NoteActivity, MainActivity::class.java)
                            startActivityForResult(intent, 1)
                        }
                    }.show()
                } else {
                    NoteDatabase.use {
                        insert(
                                "Note",
                                "_id",
                                values
                        )
                    }
                    var intent = Intent()
                    intent.setClass(this@NoteActivity, MainActivity::class.java)
                    startActivityForResult(intent, 1)
                    MainActivity.instance!!.finish()
                    finish()
                }
            }
        }
        return true
    }

    //    //    菜单
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//    //    菜单点击事件
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            android.R.id.home->mDrawerLayout.openDrawer(GravityCompat.START)
//        }
//        return true
//    }
    fun getTime(): String {
        val s = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val date = s.format(System.currentTimeMillis())
        println("s  $date")
        val time: String = s.format(Date())
        return time
    }
}
//val parser= classParser<Note>();
