package com.cn.wylee

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.baidu.mapapi.SDKInitializer
import com.cn.wylee.adapter.KaijiangAdapter
import com.cn.wylee.bean.KaiJiangInfo
import com.cn.wylee.util.LocationUtil
import com.cn.wylee.util.ParseJsonUtil
import com.cn.wylee.util.WeatherUtil
import com.cn.wylee.view.ShapeLoadingDialog
import com.stonesun.newssdk.NewsAgent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val idTmp = intArrayOf(R.drawable.ic_stat_temp_0, R.drawable.ic_stat_temp_1, R.drawable.ic_stat_temp_2, R.drawable.ic_stat_temp_3, R.drawable.ic_stat_temp_4, R.drawable.ic_stat_temp_5, R.drawable.ic_stat_temp_6, R.drawable.ic_stat_temp_7, R.drawable.ic_stat_temp_8, R.drawable.ic_stat_temp_9, R.drawable.ic_stat_temp_10, R.drawable.ic_stat_temp_11, R.drawable.ic_stat_temp_12, R.drawable.ic_stat_temp_13, R.drawable.ic_stat_temp_14, R.drawable.ic_stat_temp_15, R.drawable.ic_stat_temp_15, R.drawable.ic_stat_temp_17, R.drawable.ic_stat_temp_18, R.drawable.ic_stat_temp_19, R.drawable.ic_stat_temp_20, R.drawable.ic_stat_temp_21, R.drawable.ic_stat_temp_22, R.drawable.ic_stat_temp_23, R.drawable.ic_stat_temp_24, R.drawable.ic_stat_temp_25, R.drawable.ic_stat_temp_26, R.drawable.ic_stat_temp_27, R.drawable.ic_stat_temp_28, R.drawable.ic_stat_temp_29, R.drawable.ic_stat_temp_30, R.drawable.ic_stat_temp_31, R.drawable.ic_stat_temp_32, R.drawable.ic_stat_temp_33, R.drawable.ic_stat_temp_34, R.drawable.ic_stat_temp_35, R.drawable.ic_stat_temp_36, R.drawable.ic_stat_temp_37, R.drawable.ic_stat_temp_38, R.drawable.ic_stat_temp_39, R.drawable.ic_stat_temp_40, R.drawable.ic_stat_temp_41, R.drawable.ic_stat_temp_42)

    private val dzTmp = intArrayOf(R.drawable.ic_stat_temp_bz1, R.drawable.ic_stat_temp_bz2, R.drawable.ic_stat_temp_bz3, R.drawable.ic_stat_temp_bz4, R.drawable.ic_stat_temp_bz5, R.drawable.ic_stat_temp_bz6, R.drawable.ic_stat_temp_bz7, R.drawable.ic_stat_temp_bz8, R.drawable.ic_stat_temp_bz9, R.drawable.ic_stat_temp_bz10, R.drawable.ic_stat_temp_bz11, R.drawable.ic_stat_temp_bz12, R.drawable.ic_stat_temp_bz13, R.drawable.ic_stat_temp_bz14, R.drawable.ic_stat_temp_bz15, R.drawable.ic_stat_temp_bz15, R.drawable.ic_stat_temp_bz17, R.drawable.ic_stat_temp_bz18, R.drawable.ic_stat_temp_bz19, R.drawable.ic_stat_temp_bz20, R.drawable.ic_stat_temp_bz21, R.drawable.ic_stat_temp_bz22, R.drawable.ic_stat_temp_bz23, R.drawable.ic_stat_temp_bz24, R.drawable.ic_stat_temp_bz25, R.drawable.ic_stat_temp_bz26, R.drawable.ic_stat_temp_bz27, R.drawable.ic_stat_temp_bz28, R.drawable.ic_stat_temp_bz29, R.drawable.ic_stat_temp_bz30, R.drawable.ic_stat_temp_bz31, R.drawable.ic_stat_temp_bz32, R.drawable.ic_stat_temp_bz33, R.drawable.ic_stat_temp_bz34, R.drawable.ic_stat_temp_bz35, R.drawable.ic_stat_temp_bz36, R.drawable.ic_stat_temp_bz37, R.drawable.ic_stat_temp_bz38, R.drawable.ic_stat_temp_bz39, R.drawable.ic_stat_temp_bz40, R.drawable.ic_stat_temp_bz41, R.drawable.ic_stat_temp_bz42)

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when {
                msg?.what == 1 -> {
                    val intent = Intent(this@MainActivity, ZoushiActivty::class.java)
                    startActivity(intent)
                }
                msg?.what == 2 -> {
                    Toast.makeText(this@MainActivity,"当前为最新版本",Toast.LENGTH_LONG).show()
                    shapeLoadingDialog?.dismiss()
                }
                msg?.what == 3 -> {
                    val intent = Intent(this@MainActivity, ShuoMingActivity::class.java)
                    startActivity(intent)
                }
                msg?.what == 4 -> {
                    val intent = Intent(this@MainActivity, WanFaActivity::class.java)
                    startActivity(intent)
                }
            }

        }
    }

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
        nav_view.setNavigationItemSelectedListener(this)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val caipiao = getSharedPreferences("CAIPIAO", Context.MODE_PRIVATE)
                shouDialog("获取数据中...")
                getCaipiaoDate(caipiao.getString(textArray[position], ""))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPri()
        }

        getLocation()
    }

    var USER: SharedPreferences? = null
    private fun checkData() {
        USER = getSharedPreferences("USER", Context.MODE_PRIVATE)
        val name = USER?.getString("USERNAME", "")
        if (TextUtils.isEmpty(name)) {
            shouLoginDia()
        } else {
            shouUser(name!!)
        }

    }
    private fun shouUser(name :String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("用户信息")
        builder.setMessage("账号："+name)
        builder.setPositiveButton("修改") {
            dialog: DialogInterface?, which ->
            shouLoginDia()
        }
        builder.setNegativeButton("确定"){
            dialog: DialogInterface?, which: Int ->

        }
        builder.create().show()
   }

    private fun shouLoginDia(){
        val view = LayoutInflater.from(this).inflate(R.layout.dialog, null)
        val username = view.findViewById<EditText>(R.id.username)
        val password = view.findViewById<EditText>(R.id.password)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("用户注册")
        builder.setView(view)
        builder.setPositiveButton("确定") {
            dialog: DialogInterface?, which ->

        }
        builder.setNegativeButton("取消"){
            dialog: DialogInterface?, which: Int ->

        }
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if(TextUtils.isEmpty(username.text)||TextUtils.isEmpty(password.text)){
                Toast.makeText(this,"数据不能为空",Toast.LENGTH_SHORT).show()
            }else{
                USER?.edit()?.putString("USERNAME",username.text.toString().trim())?.commit()
                USER?.edit()?.putString("PASSWORD",password.text.toString().trim())?.commit()
                Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }

        }
    }

    private fun getLocation() {
        LocationUtil.init(this)
        LocationUtil.startLocal({ city1 ->
            runOnUiThread(Runnable { city.text = city1 })
            getWeather(city1)
        }, true)
    }

    private fun getWeather(city1: String) {

        WeatherUtil.getWeather(this, city1) { date ->
            getWeatherData(date)
        }

    }

    private fun getWeatherData(date: String?) {
        val strings = ParseJsonUtil.parseJson(date)
        val wea = strings[0]
        val tmp = strings[1]
        Log.d("lee", wea)
        Log.d("lee", tmp)
        val parseInt = Integer.parseInt(tmp)
        runOnUiThread(Runnable { cityweather.text = wea })
        if (parseInt < 0) {
            runOnUiThread(Runnable { temp.setImageResource(dzTmp[parseInt * -1]) })

        } else {
            runOnUiThread(Runnable { temp.setImageResource(idTmp[parseInt]) })
        }

        runOnUiThread(Runnable {
            when {
                wea.contains("晴") -> weather_src.setImageResource(R.drawable.ic_sun)
                wea.contains("多云") -> weather_src.setImageResource(R.drawable.ic_cloudy)
                wea.contains("阴") -> weather_src.setImageResource(R.drawable.ic_overcast)
                wea.contains("雨") -> weather_src.setImageResource(R.drawable.ic_dayu)
                wea.contains("雪") -> weather_src.setImageResource(R.drawable.ic_daxue)
                wea.contains("雾") -> weather_src.setImageResource(R.drawable.ic_fog)
                wea.contains("霾") -> weather_src.setImageResource(R.drawable.ic_mai)
                wea.contains("雷") -> weather_src.setImageResource(R.drawable.ic_leizhenyu)
            }
        })
    }

    var shapeLoadingDialog: ShapeLoadingDialog? = null
    private fun shouDialog(string:String) {
        shapeLoadingDialog = ShapeLoadingDialog.Builder(this)
                .loadText(string)
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
            R.id.action_settings -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // hint

            }
            R.id.nav_gallery -> {
                //走势
                handler.sendEmptyMessageDelayed(1, 500)
            }
            R.id.nav_slideshow -> {
                //说明
                handler.sendEmptyMessageDelayed(3,500)
            }
            R.id.nav_manage -> {
                //玩法
                handler.sendEmptyMessageDelayed(4,500)
            }
            R.id.nav_share -> {
                //登陆
                checkData()
            }
            R.id.nav_send -> {
                //更新
                shouDialog("检查可用更新...")
                handler.sendEmptyMessageDelayed(2,1800)
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
                    runOnUiThread(Runnable {
                        Toast.makeText(this@MainActivity, "暂无数据，请稍后再试！", Toast.LENGTH_LONG).show()
                        shapeLoadingDialog?.dismiss()
                    })
                })
                mRequestQueue!!.add(request)
            }
        }.start()

    }

    private fun showKaijiangView(list: List<KaiJiangInfo>) {

        val adapter = KaijiangAdapter(this@MainActivity, list)
        runOnUiThread(Runnable {
            listview.adapter = adapter
            shapeLoadingDialog?.dismiss()
        })

    }
}
