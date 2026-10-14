package com.example.neonsmssimulator
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.*
import android.text.InputType

class MainActivity : Activity() {
    private val bg = Color.rgb(5,10,8)
    private val panel = Color.rgb(13,25,19)
    private val neon = Color.rgb(57,255,130)
    private val soft = Color.rgb(170,205,180)
    private val handler = Handler(Looper.getMainLooper())
    private var running = false
    private var current = 0
    private var total = 0
    private lateinit var countInput: EditText
    private lateinit var log: TextView
    private lateinit var start: Button
    private lateinit var stop: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(20))
            setBackgroundColor(bg)
        }
        root.addView(TextView(this).apply {
            text = "NEON // SMS SIMULATOR"; textSize = 23f; setTextColor(neon)
            typeface = Typeface.MONOSPACE; setTypeface(typeface, Typeface.BOLD)
            gravity = Gravity.CENTER; setPadding(0,dp(12),0,dp(6))
        })
        root.addView(TextView(this).apply {
            text = "OFFLINE MODE  •  NO REAL SMS SENT"; textSize = 12f
            setTextColor(soft); gravity = Gravity.CENTER; setPadding(0,0,0,dp(24))
        })
        root.addView(TextView(this).apply {
            text = "SIMULATION COUNT (1–100)"; textSize = 12f
            setTextColor(neon); typeface = Typeface.MONOSPACE
        })
        countInput = EditText(this).apply {
            hint = "e.g. 20"; setHintTextColor(Color.GRAY); setTextColor(Color.WHITE)
            inputType = InputType.TYPE_CLASS_NUMBER; setPadding(dp(12),dp(10),dp(12),dp(10))
            setBackgroundColor(panel)
        }
        root.addView(countInput, params())
        start = Button(this).apply {
            text = "▶ START SIMULATION"; setTextColor(bg); setBackgroundColor(neon)
            setOnClickListener { startSimulation() }
        }
        root.addView(start, params())
        stop = Button(this).apply {
            text = "■ STOP"; isEnabled = false
            setOnClickListener { running = false; handler.removeCallbacksAndMessages(null); log.append("\\n>> STOPPED BY USER"); updateButtons() }
        }
        root.addView(stop, params())
        log = TextView(this).apply {
            text = ">> READY\\n>> No network access required"; setTextColor(neon)
            textSize = 13f; typeface = Typeface.MONOSPACE
            setPadding(dp(12),dp(16),dp(12),dp(12)); setBackgroundColor(panel)
        }
        root.addView(log, LinearLayout.LayoutParams(-1,0,1f))
        setContentView(root)
    }
    private fun startSimulation() {
        if (running) return
        val n = countInput.text.toString().toIntOrNull()
        if (n == null || n !in 1..100) { log.text = ">> ERROR: Enter a number from 1 to 100."; return }
        total=n; current=0; running=true; log.text=">> SIMULATION STARTED\\n"; updateButtons(); tick()
    }
    private fun tick() {
        if (!running) return
        if (current >= total) {
            running=false; log.append(">> COMPLETE: $total simulated messages\\n>> No real SMS were sent.")
            updateButtons(); return
        }
        current++; log.append("[SIMULATED] Message $current / $total\\n")
        handler.postDelayed({ tick() },250)
    }
    private fun updateButtons() { start.isEnabled = !running; stop.isEnabled = running }
    private fun params() = LinearLayout.LayoutParams(-1,LinearLayout.LayoutParams.WRAP_CONTENT).apply { bottomMargin=dp(12) }
    private fun dp(v:Int) = (v * resources.displayMetrics.density).toInt()
}
