package com.classic.museo.Login

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.classic.museo.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth //DB 인스턴스 선언

        // 유효성 검사
        validation()
        // 뒤로가기
        binding.signupBack.setOnClickListener {
            finish()
//            val Intent = Intent(this, LoginActivity::class.java)
//            startActivity(Intent)
        }


    }

    //유효성 검사
    fun validation() {
        //아이디 유효성 검사(알파벳과 숫자만 허용)
        fun isValidId(id: String): Boolean {
            var id = binding.signupEditId.text.toString().trim()
            val idPattern = "^[a-zA-Z0-9]+@[0-9a-zA-Z]+(.[_0-9a-zA-Z-]+)*\$"
            return id.matches(idPattern.toRegex())
        }

        //아이디 텍스트체인지 리스너
        binding.signupEditId.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnSignup2.setTextColor(Color.WHITE)
                binding.btnSignup2.isEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {
                binding.btnSignup2.setTextColor(Color.WHITE)
                //binding.btnSignup2.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val idPattern = "^[a-zA-Z0-9]+@[0-9a-zA-Z]+(.[_0-9a-zA-Z-]+)*\$"
                val id = binding.signupEditId.text.toString()
                val patternid = Pattern.compile(idPattern)
                val matcher2 = patternid.matcher(id)

                if (matcher2.matches()) {
                    binding.idAnswer.text = "적합한 아이디 입니다."
                    binding.idAnswer.setTextColor(Color.parseColor("#026A31"))
                    binding.btnSignup2.isEnabled = true
                    binding.btnSignup2.setBackgroundColor(Color.parseColor("#D8B674"))
                } else {
                    binding.idAnswer.text = "아이디를 확인하여주세요."
                    binding.idAnswer.setTextColor(Color.RED)
                    binding.btnSignup2.isEnabled = false
                    binding.btnSignup2.setBackgroundColor(Color.parseColor("#D3D3D3"))
                }
            }

        })

        //비밀번호 텍스트체인지 리스너
        binding.signupEditPw.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnSignup2.setTextColor(Color.WHITE)
                binding.btnSignup2.isEnabled = false
            }
            override fun afterTextChanged(s: Editable?) {
                binding.btnSignup2.setTextColor(Color.WHITE)
                //binding.btnSignup2.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pwPattern =
                    "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,16}$"
                val pw1 = binding.signupEditPw.text.toString()
                val pw2 = binding.signupEditPw2.text.toString()
                val pattern = Pattern.compile(pwPattern)
                val matcher = pattern.matcher(pw1)

                if (matcher.matches()) {
                    binding.passwardAnswer.text = "적합한 비밀번호 입니다."
                    binding.passwardAnswer.setTextColor(Color.parseColor("#026A31"))
                    binding.btnSignup2.setBackgroundColor(Color.parseColor("#D8B674"))
                } else {
                    binding.passwardAnswer.text = "비밀번호를 확인하여주세요."
                    binding.passwardAnswer.setTextColor(Color.RED)
                    binding.btnSignup2.setBackgroundColor(Color.parseColor("#D3D3D3"))
                }

                if (pw1 == pw2 && matcher.matches()) {
                    binding.passwardAnswer2.text = "비밀번호가 일치합니다."
                    binding.passwardAnswer2.setTextColor(Color.parseColor("#026A31"))
                    binding.btnSignup2.setTextColor(Color.BLACK)
                    binding.btnSignup2.setBackgroundColor(Color.parseColor("#D8B674"))
                    binding.btnSignup2.isEnabled = true
                } else {
                    binding.passwardAnswer2.text = "비밀번호를 확인해 주세요."
                    binding.passwardAnswer2.setTextColor(Color.RED)
                    binding.btnSignup2.setTextColor(Color.WHITE)
                    binding.btnSignup2.isEnabled = false
                    binding.btnSignup2.setBackgroundColor(Color.parseColor("#D3D3D3"))
                }
            }


        })

        //비밀번호 확인 텍스트체인지 리스너
        binding.signupEditPw2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnSignup2.setTextColor(Color.WHITE)
                binding.btnSignup2.isEnabled = false
            }

            override fun afterTextChanged(editable: Editable?) {
                binding.btnSignup2.setTextColor(Color.WHITE)
                //binding.btnSignup2.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pwPattern =
                    "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,16}$"
                val pw1 = binding.signupEditPw.text.toString()
                val pw2 = binding.signupEditPw2.text.toString()
                val pattern = Pattern.compile(pwPattern)
                val matcher = pattern.matcher(pw1)

                if (matcher.matches()) {
                    binding.passwardAnswer.text = "적합한 비밀번호 입니다."
                    binding.passwardAnswer.setTextColor(Color.parseColor("#026A31"))
                    binding.btnSignup2.setBackgroundColor(Color.parseColor("#D8B674"))
                } else {
                    binding.passwardAnswer.text = "비밀번호를 확인하여주세요."
                    binding.passwardAnswer.setTextColor(Color.RED)
                    binding.btnSignup2.setBackgroundColor(Color.parseColor("#D3D3D3"))
                }

                if (pw1 == pw2 && matcher.matches()) {
                    binding.passwardAnswer2.text = "비밀번호가 일치합니다."
                    binding.passwardAnswer2.setTextColor(Color.parseColor("#026A31"))
                    binding.btnSignup2.setBackgroundColor(Color.parseColor("#D8B674"))
                    binding.btnSignup2.setTextColor(Color.BLACK)
                    binding.btnSignup2.isEnabled = true
                } else {
                    binding.passwardAnswer2.text = "비밀번호를 확인해 주세요."
                    binding.passwardAnswer2.setTextColor(Color.RED)
                    binding.btnSignup2.setTextColor(Color.WHITE)
                    binding.btnSignup2.isEnabled = false
                    binding.btnSignup2.setBackgroundColor(Color.parseColor("#D3D3D3"))
                }
            }

        })
        binding.btnSignup2.setOnClickListener {
            val name = binding.signupEditName.text.toString()
            val id = binding.signupEditId.text.toString()
            val pw1 = binding.signupEditPw.text.toString()
            val pw2 = binding.signupEditPw2.text.toString()


            //아이디 유효성 검사
            if (!isValidId(id)) {
                Log.d("아이디 유효성 검사", "실패")

                Toast.makeText(this, "아이디는 영문자 및 숫자만 가능합니다", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("아이디 유효성 검사", "성공")
//                Toast.makeText(this,"아이디가 적합합니다",Toast.LENGTH_SHORT).show()
            }

            //공백 검사
            if (name.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else if (id.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else if (pw1.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else if (pw2.isEmpty()) {
                Toast.makeText(this, "비밀번호를 다시한번 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                singUp()
            }
        }
    }

    fun singUp() {
        val id = binding.signupEditId.text.toString()
        val pw = binding.signupEditPw.text.toString()
        val Nickname = binding.signupEditName.text.toString()


        auth.createUserWithEmailAndPassword(id, pw)
            .addOnCompleteListener(this) { task ->
                Log.d("ddd", task.toString())
                if (task.isSuccessful) {
                    Toast.makeText(this, "회원가입에 성공했습니다!", Toast.LENGTH_SHORT).show()
                    //신규 생성 시 firestor에 데이터 넣어주기
                    var uid: String? = auth?.currentUser?.uid

                    val User = hashMapOf(
                        "UserId" to id,
                        "Pw" to pw,
                        "NickName" to Nickname,
                        "UID" to uid
                    )
                    if (uid != null) {
                        Log.e("dddd", uid)
                        db.collection("users").document(uid)
                            .set(User)
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot successfully written!"
                                )
                            }
                            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                    }

                    //회원가입 완료 후 화면이동
                    if (auth.currentUser != null) {
                        var intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("SignIn","True")
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "이미 존재하는 계정이거나, 회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}