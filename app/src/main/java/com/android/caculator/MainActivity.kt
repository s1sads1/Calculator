package com.android.caculator

import android.graphics.Color.green
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.caculator.databinding.ActivityMainBinding
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    private var isOperator = false
    private var hasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        Calculate()
    }

    fun Calculate() {
      binding.expressText.text

    }

    fun buttonClicked(v: View) {

        when(v.id) {
            R.id.Btn0 -> numberButtonClicked("0")
            R.id.Btn1 -> numberButtonClicked("1")
            R.id.Btn2 -> numberButtonClicked("2")
            R.id.Btn3 -> numberButtonClicked("3")
            R.id.Btn4 -> numberButtonClicked("4")
            R.id.Btn5 -> numberButtonClicked("5")
            R.id.Btn6 -> numberButtonClicked("6")
            R.id.Btn7 -> numberButtonClicked("7")
            R.id.Btn8 -> numberButtonClicked("8")
            R.id.Btn9 -> numberButtonClicked("9")
            R.id.Btn00 -> numberButtonClicked("00")

            R.id.BtnPlus -> operatorButtonClicked("+")
            R.id.BtnMinus -> operatorButtonClicked("-")
            R.id.BtnX -> operatorButtonClicked("x")
            R.id.BtnNanugi -> operatorButtonClicked("/")
            R.id.BtnPercent -> operatorButtonClicked("%")

        }
    }

    private fun numberButtonClicked(number: String){
        if (isOperator) {
            binding.expressText.append(" ")
        }
        isOperator = false

        val expressionText = binding.expressText.text.split(" ")
        if ( !expressionText.isNullOrEmpty() && expressionText.last().length >= 15 ) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if ( expressionText.last().isNullOrEmpty() && number =="0" ){
            Toast.makeText(this, "0은 제일앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        binding.expressText.append(number)
        binding.resultText.text = calculateExpression()
    }

    private fun calculateExpression(): String {
        val expressionText = binding.expressText.text.split(" ")
        if(hasOperator.not() || expressionText.size !=3) {
            return ""
        } else if (expressionText[0].isNumber().not() || expressionText[2].isNumber().not()) {
            return ""
        }

        val expression1 = expressionText[0].toBigInteger()
        val expression2 = expressionText[2].toBigInteger()
        val operator = expressionText[1]

        return when(operator) {
            "+" -> (expression1+expression2).toString()
            "-" -> (expression1-expression2).toString()
            "x" -> (expression1*expression2).toString()
            "%" -> (expression1%expression2).toString()
            "/" -> (expression1/expression2).toString()
            else -> ""
        }

    }

    fun String.isNumber():Boolean {
        return try {
            this.toBigInteger()
            true
        }catch (e : NumberFormatException) {
            false
        }
    }

    private fun operatorButtonClicked(operator : String) {
        if (binding.expressText.text.isNullOrEmpty()) {
            return
        }

        when {
            isOperator -> {
                binding.expressText.text = binding.expressText.text.dropLast(1).toString() + operator
            }
            hasOperator -> {
                Toast.makeText(this, "연산자는 한번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                binding.expressText.append(" $operator")
            }
        }
        val ssb = SpannableStringBuilder(binding.expressText.text)
        ssb.setSpan(
                ForegroundColorSpan(getColor(R.color.green)),
                binding.expressText.text.length -1, binding.expressText.text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.expressText.text = ssb
        isOperator = true
        hasOperator = true
    }
    fun resultButtonClicked(v: View){
        val expressionText = binding.expressText.text.split(" ")
        if( binding.expressText.text.isNullOrEmpty() || expressionText.size ==1 ) {
            return
        }
        if( expressionText.size != 3 && hasOperator) {
            Toast.makeText(this, "수식을 완성해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if( expressionText[0].isNumber().not() || expressionText[2].isNumber().not()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val resultText = calculateExpression()
        binding.resultText.text = ""
        binding.expressText.text = resultText

        isOperator = false
        hasOperator = false
    }
    fun historyButtonClicked(v: View){
    }
    fun clearButtonClicked(v: View){
    }



}