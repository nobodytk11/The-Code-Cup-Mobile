package com.example.thecodecup

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: CartAdapter
    private lateinit var tvTotalPrice: TextView
    private lateinit var layoutEmptyCart: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        layoutEmptyCart = findViewById(R.id.layoutEmptyCart)
        val rvCartItems = findViewById<RecyclerView>(R.id.rvCartItems)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnCheckout = findViewById<Button>(R.id.btnCheckout)

        adapter = CartAdapter(CartManager.items) {
            updateUIState()
            PersistenceManager.saveData()
        }
        rvCartItems.layoutManager = LinearLayoutManager(this)
        rvCartItems.adapter = adapter

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private val deleteIcon: Drawable? = ContextCompat.getDrawable(this@CartActivity, R.drawable.ic_delete)
            private val background = Paint().apply { color = Color.parseColor("#FFEBEE") }

            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder): Boolean = false
            
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
                updateUIState()
                PersistenceManager.saveData()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top
                
                if (dX < 0) {
                    val cornerRadius = 32f
                    val rect = RectF(
                        itemView.right.toFloat() + dX, 
                        itemView.top.toFloat(), 
                        itemView.right.toFloat(), 
                        itemView.bottom.toFloat()
                    )
                    c.drawRoundRect(rect, cornerRadius, cornerRadius, background)

                    deleteIcon?.let {
                        val iconMargin = (itemHeight - it.intrinsicHeight) / 2
                        val iconTop = itemView.top + (itemHeight - it.intrinsicHeight) / 2
                        val iconBottom = iconTop + it.intrinsicHeight
                        
                        val iconRight = itemView.right - iconMargin
                        val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                        
                        it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        it.draw(c)
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(rvCartItems)

        btnBack.setOnClickListener { finish() }

        btnCheckout.setOnClickListener {
            if (CartManager.items.isNotEmpty()) {
                val now = System.currentTimeMillis()
                val sdf = SimpleDateFormat("dd MMMM | hh:mm a", Locale.getDefault())
                val currentDate = sdf.format(Date(now))
                
                val newOrder = Order(
                    id = UUID.randomUUID().toString(),
                    date = currentDate,
                    timestamp = now,
                    items = ArrayList(CartManager.items),
                    totalPrice = CartManager.getTotalCartPrice(),
                    address = UserManager.address,
                    userName = UserManager.fullName
                )
                OrderManager.addOrder(newOrder)
                CartManager.items.clear()
                
                PersistenceManager.saveData()
                
                startActivity(Intent(this, SuccessActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Your cart is empty. Please add items to checkout!", Toast.LENGTH_SHORT).show()
            }
        }

        updateUIState()
    }

    private fun updateUIState() {
        if (CartManager.items.isEmpty()) {
            layoutEmptyCart.visibility = View.VISIBLE
            findViewById<View>(R.id.rvCartItems).visibility = View.GONE
        } else {
            layoutEmptyCart.visibility = View.GONE
            findViewById<View>(R.id.rvCartItems).visibility = View.VISIBLE
        }
        tvTotalPrice.text = "$${String.format("%.2f", CartManager.getTotalCartPrice())}"
    }
}