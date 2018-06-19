package com.example.wk.note

import android.content.BroadcastReceiver
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import org.jetbrains.anko.find
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import android.widget.Toast
import java.text.SimpleDateFormat
import android.didikee.donate.AlipayDonate
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity() {
    lateinit var noteList: List<Note>
    lateinit var mDrawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var time: String
    var egg:Int=0
    companion object{
        var instance: MainActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//      --------------------------------------------------------------------------------------------
//        toolbar
        setSupportActionBar(mtoolbar)
//        菜单按钮
        mDrawerLayout = find(R.id.drawer_layout)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.menu)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        instance=this


//        侧边栏
        navView = find(R.id.nav_view)
        navView.setNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.nav_1->alert ("本软件只有记录文字功能，且并不能导出，使用请谨慎！"){yesButton {  }  }.show()
                R.id.nav_2 -> donateAlipay("FKX07578ZGTM83YE51QU12")
                R.id.nav_3 -> alert("版本号：V1.1\n\n开发者:WK", "关于") { yesButton { } }.show()
                R.id.nav_4->{
                    if (egg<10){
                        egg++
                    }else if(egg==10){
                        alert ("呀哈哈，恭喜您发现了彩蛋","彩蛋"){yesButton {  }  }.show()
                    }else{

                    }
                    println(egg)
                }
            }
            navView.setCheckedItem(R.id.nav_1)
            mDrawerLayout.closeDrawer(GravityCompat.START)
            true
        }
//        菜单
//        mtoolbar.inflateMenu(R.menu.main)
////        菜单监听
//        mtoolbar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.version_item -> Toast.makeText(this, "1.0", Toast.LENGTH_SHORT).show()
//                R.id.about_item -> Toast.makeText(this, "开发自WK", Toast.LENGTH_SHORT).show()
//            }
//            true
//        }
//      --------------------------------------------------------------------------------------------

//        获取笔记数据
        NoteDatabase.use {
            val notes = select("Note")
            noteList = notes.parseList(classParser<Note>())
        }
//        RecyclerView

        val recyclerView: RecyclerView = find(R.id.recycler_view)
        val layout = LinearLayoutManager(this)
        layout.stackFromEnd = true
        layout.reverseLayout = true
        recyclerView.layoutManager = layout
//        recyclerView.layoutManager=LinearLayoutManager(this)
//        recyclerView.layoutManager.
        recyclerView.adapter = NoteAdapter(noteList)
//        ListView
//        ListView显示部分
//        adaptr = NoteAdapter(noteList as ArrayList<Note>, this)
//        val listView: ListView = find(R.id.list_view)
////        var listView = findViewById<ListView>(R.id.list_view)
//        listView.adapter = adaptr
//        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
//            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
////                ListView点击事件
//                Toast.makeText(this@MainActivity, "test", Toast.LENGTH_SHORT).show()
//            }
//        }

//      --------------------------------------------------------------------------------------------
//        悬浮按钮
        println("--------------------------------------------------------------------------")
        val fab: FloatingActionButton = find(R.id.fab)
//        val fab:FloatingActionButton= findViewById<FloatingActionButton>(R.id.fab)

//        悬浮按钮点击事件
        fab.setOnClickListener { view ->
            //            跳转活动
            val intent = Intent()
            intent.setClass(this@MainActivity, NoteActivity::class.java)
            startActivity(intent)
        }
        println("--------------------------------------------------------------------------")
    }

    //    ----------------------------------------------------------------------------------------------
////    菜单
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//        toobar菜单点击事件
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> mDrawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    //    fun ArrayList<Note>.getData() {
//        var note: Note = Note("text", "xxxxxxxxxxxxxxxx")
//        for (i in 0..100) {
//            note = Note(i.toString(), "text")
//            this.add(note)
//        }
//    }
    //    捐赠
    private fun donateAlipay(payCode: String) {
        val hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this)
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(this, payCode)
        } else {
            toast("支付宝未安装")
        }
    }

//    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {
//        super.overridePendingTransition(enterAnim,0)
//    }
}
