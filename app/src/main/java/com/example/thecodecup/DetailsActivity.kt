package com.example.thecodecup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class DetailsActivity : AppCompatActivity() {

    private var quantity = 1
    private lateinit var coffee: Coffee
    private lateinit var tvQuantity: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var rgShot: RadioGroup
    private lateinit var rgSize: RadioGroup
    private lateinit var rgIce: RadioGroup
    private lateinit var voucherBanner: CardView
    private lateinit var btnApplyVoucher: Button
    
    private var isIced = true
    private var isVoucherApplied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        coffee = intent.getSerializableExtra("coffee") as? Coffee ?: return

        tvQuantity = findViewById(R.id.tvQuantity)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        rgShot = findViewById(R.id.rgShot)
        rgSize = findViewById(R.id.rgSize)
        rgIce = findViewById(R.id.rgIce)
        voucherBanner = findViewById(R.id.voucherBanner)
        btnApplyVoucher = findViewById(R.id.btnApplyVoucher)

        findViewById<TextView>(R.id.detailsName).text = coffee.name
        findViewById<android.widget.ImageView>(R.id.detailsImage).setImageResource(coffee.imageResId)

        // Initial Voucher check
        updateVoucherUI()

        findViewById<TextView>(R.id.btnMinus).setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateUI()
            }
        }

        findViewById<TextView>(R.id.btnPlus).setOnClickListener {
            quantity++
            updateUI()
        }

        rgShot.setOnCheckedChangeListener { _, _ -> updateUI() }
        
        rgSize.setOnCheckedChangeListener { group, checkedId ->
            for (i in 0 until group.childCount) {
                val view = group.getChildAt(i)
                view.alpha = if (view.id == checkedId) 1.0f else 0.3f
            }
            updateUI()
        }

        rgIce.setOnCheckedChangeListener { group, checkedId ->
            for (i in 0 until group.childCount) {
                val view = group.getChildAt(i)
                view.alpha = if (view.id == checkedId) 1.0f else 0.3f
            }
            updateUI()
        }

        val btnHot = findViewById<ImageButton>(R.id.btnHot)
        val btnIced = findViewById<ImageButton>(R.id.btnIced)

        btnHot.alpha = 0.3f
        btnIced.alpha = 1.0f

        btnHot.setOnClickListener {
            isIced = false
            btnHot.alpha = 1.0f
            btnIced.alpha = 0.3f
            updateUI()
        }

        btnIced.setOnClickListener {
            isIced = true
            btnIced.alpha = 1.0f
            btnHot.alpha = 0.3f
            updateUI()
        }

        btnApplyVoucher.setOnClickListener {
            if (!isVoucherApplied) {
                isVoucherApplied = true
                btnApplyVoucher.text = "Remove"
                btnApplyVoucher.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.GRAY))
                Toast.makeText(this, "Voucher applied!", Toast.LENGTH_SHORT).show()
            } else {
                isVoucherApplied = false
                btnApplyVoucher.text = "Apply"
                btnApplyVoucher.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#2E7D32")))
                Toast.makeText(this, "Voucher removed", Toast.LENGTH_SHORT).show()
            }
            updateUI()
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.cartPreviewButton).setOnClickListener {
            val cartSummary = if (CartManager.items.isEmpty()) {
                "Cart is empty"
            } else {
                "Items in cart: ${CartManager.getCartCount()}\nTotal: $${String.format("%.2f", CartManager.getTotalCartPrice())}"
            }
            Toast.makeText(this, cartSummary, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.addToCartButton).setOnClickListener {
            val finalPrice = if (isVoucherApplied) 0.0 else calculateTotal()
            
            val item = CartItem(
                coffee = coffee,
                quantity = quantity,
                shot = if (findViewById<RadioButton>(R.id.rbDouble).isChecked) "Double" else "Single",
                isIced = isIced,
                size = when (rgSize.checkedRadioButtonId) {
                    R.id.rbSmall -> "Small"
                    R.id.rbLarge -> "Large"
                    else -> "Medium"
                },
                iceLevel = when (rgIce.checkedRadioButtonId) {
                    R.id.rbIceSmall -> "Low"
                    R.id.rbIceLarge -> "Full"
                    else -> "Medium"
                },
                totalPrice = finalPrice,
                isVoucherUsed = isVoucherApplied
            )
            
            if (isVoucherApplied) {
                UserManager.availableVouchers--
            }
            
            CartManager.addItem(item)
            PersistenceManager.saveData()
            startActivity(Intent(this, CartActivity::class.java))
            finish()
        }

        updateUI()
    }

    private fun updateVoucherUI() {
        if (UserManager.availableVouchers > 0) {
            voucherBanner.visibility = View.VISIBLE
            findViewById<TextView>(R.id.tvVoucherMessage).text = "You have ${UserManager.availableVouchers} voucher(s)!"
        } else {
            voucherBanner.visibility = View.GONE
        }
    }

    private fun calculateTotal(): Double {
        var base = coffee.price
        if (findViewById<RadioButton>(R.id.rbDouble).isChecked) base += 0.50
        
        base += when (rgSize.checkedRadioButtonId) {
            R.id.rbSmall -> 0.0
            R.id.rbLarge -> 1.0
            else -> 0.5
        }
        
        return base * quantity
    }

    private fun updateUI() {
        tvQuantity.text = quantity.toString()
        val finalPrice = if (isVoucherApplied) 0.0 else calculateTotal()
        tvTotalAmount.text = "$${String.format("%.2f", finalPrice)}"
        
        // Disable quantity changes if voucher is applied (optional, usually 1 voucher per 1 drink)
        if (isVoucherApplied) {
            quantity = 1
            tvQuantity.text = "1"
            findViewById<View>(R.id.layoutQuantity).alpha = 0.5f
            findViewById<View>(R.id.btnMinus).isClickable = false
            findViewById<View>(R.id.btnPlus).isClickable = false
        } else {
            findViewById<View>(R.id.layoutQuantity).alpha = 1.0f
            findViewById<View>(R.id.btnMinus).isClickable = true
            findViewById<View>(R.id.btnPlus).isClickable = true
        }
    }
}