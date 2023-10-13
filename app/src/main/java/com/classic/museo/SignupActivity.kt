package com.classic.museo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.classic.museo.databinding.ActivitySignupBinding
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //아이디 유효성 검사(알파벳과 숫자만 허용)
        fun isValidId(id: String): Boolean {
            var id = binding.signupEditId.text.toString().trim()
            val idPattern = "^[a-zA-Z0-9]*\$"
            return id.matches(idPattern.toRegex())
        }

        binding.signupEditPw2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnSignup2.setTextColor(Color.WHITE)
                binding.btnSignup2.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnSignup2.setTextColor(Color.WHITE)
                binding.btnSignup2.isEnabled = false
            }

            override fun afterTextChanged(editable: Editable?) {
                val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,16}$"
                val pw1 = binding.signupEditPw.text.toString()
                val pw2 = editable.toString()
                val pattern = Pattern.compile(pwPattern)
                val matcher = pattern.matcher(pw1)

                if(matcher.matches()){
                    binding.passwardAnswer.text = "적합한 비밀번호 입니다."
                    binding.passwardAnswer.setTextColor(Color.parseColor("#026A31"))
                }
                else {
                    binding.passwardAnswer.text = "비밀번호를 확인하여주세요."
                    binding.passwardAnswer.setTextColor(Color.RED)
                }

                if(pw1 == pw2 && matcher.matches()){
                    binding.passwardAnswer2.text = "비밀번호가 일치합니다."
                    binding.passwardAnswer2.setTextColor(Color.parseColor("#026A31"))
                    binding.btnSignup2.setTextColor(Color.BLACK)
                    binding.btnSignup2.isEnabled = true
                }
                else {
                    binding.passwardAnswer2.text = "비밀번호를 확인해 주세요."
                    binding.passwardAnswer2.setTextColor(Color.RED)
                    binding.btnSignup2.setTextColor(Color.WHITE)
                    binding.btnSignup2.isEnabled = false
                }
            }
        })
        binding.btnSignup2.setOnClickListener {
            val name = binding.signupEditName.text.toString()
            val id = binding.signupEditId.text.toString()
            val pw1 = binding.signupEditPw.text.toString()
            val pw2 = binding.signupEditPw2.text.toString()

            //아이디 유효성 검사
            if(!isValidId(id)){
                Log.d("아이디 유효성 검사","실패")
                Toast.makeText(this,"아이디는 영문자 및 숫자만 가능합니다",Toast.LENGTH_SHORT).show()
            } else {
                Log.d("아이디 유효성 검사","성공")
                Toast.makeText(this,"아이디가 적합합니다",Toast.LENGTH_SHORT).show()
            }

            //공백 검사
            if(name.isEmpty()){
                Toast.makeText(this,"닉네임을 입력해 주세요",Toast.LENGTH_SHORT).show()
            }
            if(id.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            if(pw1.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            if(pw2.isEmpty()){
                Toast.makeText(this,"비밀번호를 다시한번 입력해 주세요",Toast.LENGTH_SHORT).show()
            } else {
            Toast.makeText(this,"회원가입 성공",Toast.LENGTH_SHORT).show()
            }
        }
    }
}