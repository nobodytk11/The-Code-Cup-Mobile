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
import kotlin.math.min

class DetailsActivity : AppCompatActivity() {

    private var quantity = 1
    private lateinit var coffee: Coffee
    private lateinit var tvQuantity: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvOriginalAmount: TextView
    private lateinit var tvDiscountAmount: TextView
    private lateinit var tvDetailsCartBadge: TextView
    private lateinit var dividerCalculation: View
    private lateinit var rgShot: RadioGroup
    private lateinit var rgSize: RadioGroup
    private lateinit var rgIce: RadioGroup
    private lateinit var voucherBanner: CardView
    private lateinit var btnApplyVoucher: Button
    private lateinit var tvVoucherMessage: TextView
    private lateinit var layoutIce: View
    private lateinit var dividerIce: View
    
    private var isIced = true
    private var isVoucherApplied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        coffee = intent.getSerializableExtra("coffee") as? Coffee ?: return

        tvQuantity = findViewById(R.id.tvQuantity)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvOriginalAmount = findViewById(R.id.tvOriginalAmount)
        tvDiscountAmount = findViewById(R.id.tvDiscountAmount)
        tvDetailsCartBadge = findViewById(R.id.tvDetailsCartBadge)
        dividerCalculation = findViewById(R.id.dividerCalculation)
        
        rgShot = findViewById(R.id.rgShot)
        rgSize = findViewById(R.id.rgSize)
        rgIce = findViewById(R.id.rgIce)
        layoutIce = findViewById(R.id.layoutIce)
        dividerIce = findViewById(R.id.dividerIce)
        voucherBanner = findViewById(R.id.voucherBanner)
        btnApplyVoucher = findViewById(R.id.btnApplyVoucher)
        tvVoucherMessage = findViewById(R.id.tvVoucherMessage)

        findViewById<TextView>(R.id.detailsName).text = coffee.name
        findViewById<android.widget.ImageView>(R.id.detailsImage).setImageResource(coffee.imageResId)

        updateVoucherBanner()

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
            if (UserManager.availableVouchers <= 0) {
                Toast.makeText(this, "You don't have any vouchers yet!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            isVoucherApplied = !isVoucherApplied
            if (isVoucherApplied) {
                btnApplyVoucher.text = "Remove"
                btnApplyVoucher.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.GRAY))
                Toast.makeText(this, "Vouchers applied!", Toast.LENGTH_SHORT).show()
            } else {
                btnApplyVoucher.text = "Apply"
                btnApplyVoucher.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#2E7D32")))
                Toast.makeText(this, "Vouchers removed", Toast.LENGTH_SHORT).show()
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
            val originalTotal = calculateBaseTotal()
            val vouchersUsed = if (isVoucherApplied) min(quantity, UserManager.availableVouchers) else 0
            val unitPrice = originalTotal / quantity
            val discount = unitPrice * vouchersUsed
            val finalPrice = originalTotal - discount
            
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
                iceLevel = if (isIced) {
                    when (rgIce.checkedRadioButtonId) {
                        R.id.rbIceSmall -> "Low"
                        R.id.rbIceLarge -> "Full"
                        else -> "Medium"
                    }
                } else "N/A",
                totalPrice = finalPrice,
                vouchersUsedCount = vouchersUsed
            )
            
            if (vouchersUsed > 0) {
                UserManager.availableVouchers -= vouchersUsed
            }
            
            CartManager.addItem(item)
            PersistenceManager.saveData()
            startActivity(Intent(this, CartActivity::class.java))
            finish()
        }

        updateUI()
    }

    private fun updateVoucherBanner() {
        voucherBanner.visibility = View.VISIBLE
        if (UserManager.availableVouchers > 0) {
            tvVoucherMessage.text = "You have ${UserManager.availableVouchers} voucher(s) available!"
            tvVoucherMessage.alpha = 1.0f
            btnApplyVoucher.isEnabled = true
            btnApplyVoucher.alpha = 1.0f
        } else {
            tvVoucherMessage.text = "You have 0 vouchers. Earn stamps to get one!"
            tvVoucherMessage.alpha = 0.6f
            btnApplyVoucher.isEnabled = true
            btnApplyVoucher.alpha = 0.5f
        }
    }

    private fun calculateBaseTotal(): Double {
        var base = coffee.price
        if (findViewById<RadioButton>(R.id.rbDouble).isChecked) base += 0.50
        
        base += when (rgSize.checkedRadioButtonId) {
            R.id.rbSmall -> 0.0
            R.id.rbLarge -> 1.0
            else -> 0.5
        }
        
        return base * quantity
    }

    private fun updateCartBadge() {
        val count = CartManager.getCartCount()
        if (count > 0) {
            tvDetailsCartBadge.visibility = View.VISIBLE
            tvDetailsCartBadge.text = count.toString()
        } else {
            tvDetailsCartBadge.visibility = View.GONE
        }
    }

    private fun updateUI() {
        tvQuantity.text = quantity.toString()
        val originalTotal = calculateBaseTotal()
        
        if (isIced) {
            layoutIce.visibility = View.VISIBLE
            dividerIce.visibility = View.VISIBLE
        } else {
            layoutIce.visibility = View.GONE
            dividerIce.visibility = View.GONE
        }

        if (isVoucherApplied && UserManager.availableVouchers > 0) {
            val vouchersUsed = min(quantity, UserManager.availableVouchers)
            val unitPrice = originalTotal / quantity
            val discount = unitPrice * vouchersUsed
            val finalTotal = originalTotal - discount

            tvOriginalAmount.visibility = View.VISIBLE
            tvDiscountAmount.visibility = View.VISIBLE
            dividerCalculation.visibility = View.VISIBLE
            
            tvOriginalAmount.text = "$${String.format("%.2f", originalTotal)}"
            tvDiscountAmount.text = "- $${String.format("%.2f", discount)} ($vouchersUsed vouchers)"
            tvTotalAmount.text = "$${String.format("%.2f", finalTotal)}"
        } else {
            tvOriginalAmount.visibility = View.GONE
            tvDiscountAmount.visibility = View.GONE
            dividerCalculation.visibility = View.GONE
            tvTotalAmount.text = "$${String.format("%.2f", originalTotal)}"
        }
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
    }
}