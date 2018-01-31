package com.cn.wylee

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.baidu.mapapi.SDKInitializer
import com.cn.wylee.adapter.KaijiangAdapter
import com.cn.wylee.bean.KaiJiangInfo
import com.cn.wylee.util.ParseJsonUtil
import com.cn.wylee.view.ShapeLoadingDialog
import com.stonesun.newssdk.NewsAgent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SDKInitializer.initialize(applicationContext);
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        // 开启调试模式
        NewsAgent.setDebugMode(true);
        // 初始化黑牛
        NewsAgent.init(this);
        fab.setOnClickListener {
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/
            val intent = Intent(this@MainActivity, NewsActivity::class.java)
            startActivity(intent)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        initData()
        val textArray = resources.getStringArray(R.array.caipiao)
        shouDialog()
        nav_view.setNavigationItemSelectedListener(this)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val caipiao = getSharedPreferences("CAIPIAO", Context.MODE_PRIVATE)
                getCaipiaoDate(caipiao.getString(textArray[position], ""))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPri()
        }


    }
     var shapeLoadingDialog: ShapeLoadingDialog? = null
    private fun shouDialog() {
        shapeLoadingDialog = ShapeLoadingDialog.Builder(this)
                .loadText("数据获取中...")
                .build()
        shapeLoadingDialog?.setCanceledOnTouchOutside(false)
        shapeLoadingDialog?.show()
    }


    private fun initData() {
        val caipiao = getSharedPreferences("CAIPIAO", Context.MODE_PRIVATE)
        caipiao.edit().putString("超级大乐透", "http://f.apiplus.net/dlt-10.json").commit()
        caipiao.edit().putString("福彩3d", "http://f.apiplus.net/fc3d-10.json").commit()
        caipiao.edit().putString("排列3", "http://f.apiplus.net/pl3-10.json").commit()
        caipiao.edit().putString("排列5", "http://f.apiplus.net/pl5-10.json").commit()
        caipiao.edit().putString("七乐彩", "http://f.apiplus.net/qlc-10.json").commit()
        caipiao.edit().putString("七星彩", "http://f.apiplus.net/qxc-10.json").commit()
        caipiao.edit().putString("双色球", "http://f.apiplus.net/ssq-10.json").commit()
        caipiao.edit().putString("六场半全场", "http://f.apiplus.net/zcbqc-10.json").commit()
        caipiao.edit().putString("四场进球彩", "http://f.apiplus.net/zcjqc-10.json").commit()
        caipiao.edit().putString("安徽11选5 - 高频", "http://f.apiplus.net/ah11x5-10.json").commit()
        caipiao.edit().putString("北京11选5 - 高频", "http://f.apiplus.net/bj11x5-10.json").commit()
        caipiao.edit().putString("福建11选5 - 高频", "http://f.apiplus.net/fj11x5-10.json").commit()
        caipiao.edit().putString("广东11选5 - 高频", "http://f.apiplus.net/gd11x5-10.json").commit()
        caipiao.edit().putString("甘肃11选5 - 高频", "http://f.apiplus.net/gs11x5-10.json").commit()
        caipiao.edit().putString("广西11选5 - 高频", "http://f.apiplus.net/fx11x5-10.json").commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings ->{
                val intent= Intent(this,MapActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private val perss = arrayOf(Manifest.permission.LOCATION_HARDWARE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    private fun checkPri() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.LOCATION_HARDWARE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, perss, 321)
            return
        }
    }

    private var mRequestQueue: RequestQueue? = null
    private fun getCaipiaoDate(url: String?) {
        object : Thread() {
            override fun run() {
                mRequestQueue = Volley.newRequestQueue(this@MainActivity)
                val request = StringRequest(Request.Method.GET, url, Response.Listener { s ->
                    Log.d("lee", s)
                    val kaiJiangInfos = ParseJsonUtil.ParseKaijiang(s)
                    Log.d("lee", "kaiJiangInfos.size()" + kaiJiangInfos.size)
                    showKaijiangView(kaiJiangInfos)
                }, Response.ErrorListener { volleyError ->
                    Log.d("lee", volleyError.toString())
                    runOnUiThread(Runnable { Toast.makeText(this@MainActivity, "暂无数据，稍后再试！", Toast.LENGTH_SHORT).show() })
                })
                mRequestQueue!!.add(request)
            }
        }.start()

    }

    private fun showKaijiangView(list: List<KaiJiangInfo>) {

        val adapter = KaijiangAdapter(this@MainActivity, list)
        runOnUiThread(Runnable { listview.adapter = adapter
            shapeLoadingDialog?.dismiss()
        })

    }
}
