package com.classic.museo.Login

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import com.classic.museo.MainActivity
import com.classic.museo.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import java.util.regex.Pattern

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "cab9f533e59332f0bbbf15b84459522c")
    }
}

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth //전역으로 사용할 FirebaseAuth 생성

    init {
        //로그인화면으로 돌아올경우 모두로그아웃으로 초기화
        FirebaseAuth.getInstance().signOut()
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        auth = Firebase.auth // DB 인스턴스 선언


        //로그인 버튼
        login()

        //비회원 로그인
        binding.nonLogin.setOnClickListener {
            val non_login = Intent(this, MainActivity::class.java)
            startActivity(non_login)
        }

        //카카오 로그인
        kakaoSingUp()

        //회원가입 버튼
        binding.btnSignup.setOnClickListener {
            val SignupActivity = Intent(this, SignupActivity::class.java)
            startActivity(SignupActivity)
        }
    }

    //로그인
    fun login() {

        binding.btnLogin.setOnClickListener {
            //로그인정보 저장하기
            var inforemember = 0
            var autologin = 0
            val pref = getSharedPreferences("pref",0)
            val edit = pref.edit()
            if(binding.LoginRemember.isChecked){
                if(binding.LoginAuto.isChecked){
                    //로그인정보 저장 체크 + 자동로그인 체크
                    inforemember = 1
                    autologin = 1
                    edit.putInt("Auto",autologin)
                    edit.putInt("Remember",inforemember)
                    edit.putString("ID", binding.editId.text.toString())
                    edit.putString("PW", binding.editPw.text.toString())
                    edit.apply()
                } else{
                    //로그인정보 저장 체크 + 자동로그인 해제
                    autologin = 0
                    inforemember = 1
                    edit.putInt("Auto",autologin)
                    edit.putInt("Remember",inforemember)
                    edit.putString("ID", binding.editId.text.toString())
                    edit.putString("PW", binding.editPw.text.toString())
                    edit.apply()
                }
            } else {
                //로그인정보 저장 해제
                autologin = 0
                inforemember = 0
                edit.putInt("Auto",autologin)
                edit.putInt("Remember",inforemember)
                edit.putString("ID", "")
                edit.putString("PW", "")
                edit.apply()
            }

            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()
            if (id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "아이디/비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                //아이디를 이메일 형식으로 유효성 검사
                if (!Pattern.matches("^[_0-9a-zA-Z-]+@[0-9a-zA-Z]+(.[_0-9a-zA-Z-]+)*\$", id)) {
                    Toast.makeText(this, "아이디를 확인해주세요", Toast.LENGTH_SHORT).show()
                }
                //비밀번호는 최소 8자리 이상이며 영문+특수문자+숫자가 합쳐져야 한다
                else if (!Pattern.matches(
                        "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&.])[A-Za-z[0-9]\$@\$!%*#?&.]{8,16}\$",
                        pw
                    )
                ) {
                    Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    auth.signInWithEmailAndPassword(id, pw)
                        .addOnCompleteListener(this) { task ->
                            Log.d("sss", task.toString())
                            if (task.isSuccessful) {
                                //로그인 성공시 메인화면으로 이동
                                Toast.makeText(this, "로그인 성공하였습니다!", Toast.LENGTH_SHORT).show()
                                moveMainPage(task.result.user)

                            } else {
                                //로그인 실패시 Toast 메세지 출력
                                Toast.makeText(this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }
        //로그인정보 값 받아오기
        val pref = getSharedPreferences("pref",0)
        val manualLogout = intent.getStringExtra("ManualLogout")
        val signin = intent.getStringExtra("SignIn")
        binding.editId.setText(pref.getString("ID",""))
        binding.editPw.setText(pref.getString("PW",""))

        if(pref.getInt("Remember",0) == 1){
            if(pref.getInt("Auto",0) == 1){
                if(manualLogout == "Yes"){
                        //수동 로그아웃
                    binding.LoginAuto.isChecked = true
                }else {
                        //자동로그인 체크 + 로그인정보 체크
                        binding.LoginAuto.isChecked = true

                        //로그인 코드
                        val id = binding.editId.text.toString()
                        val pw = binding.editPw.text.toString()
                        auth.signInWithEmailAndPassword(id, pw)
                            .addOnCompleteListener(this) { task ->
                                Log.d("sss", task.toString())
                                if (task.isSuccessful) {
                                    //로그인 성공시 메인화면으로 이동
                                    Toast.makeText(this, "로그인 성공하였습니다!", Toast.LENGTH_SHORT).show()
                                    moveMainPage(task.result.user)

                                } else {
                                    //로그인 실패시 Toast 메세지 출력
                                    Toast.makeText(this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                }
            } else{
                binding.LoginAuto.isChecked = false
            }
            //자동로그인 해제 + 로그인정보 해제
            binding.LoginRemember.isChecked = true
        } else{
            //로그인정보 해제
            binding.LoginRemember.isChecked = false
        }

        if(signin == "True"){
            //새로 회원가입한 경우
            binding.editId.text = null
            binding.editPw.text = null
            binding.LoginAuto.isChecked = false
            binding.LoginRemember.isChecked = false
        }
    }

    //로그인 성공하면 다음 페이지로 넘어가는 함수
    private fun moveMainPage(user: FirebaseUser?) {
        //파이어베이스에 유저 상태가 있어야 다음 페이지로 넘어갈 수 있음
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    //카카오 로그인
    fun kakaoSingUp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        binding.kakaoLogin.setOnClickListener {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("com/classic/museo/Login", "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i("com/classic/museo/Login", "카카오계정으로 로그인 성공 ${token.accessToken}")
                    startActivity(intent)
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("com/classic/museo/Login", "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i("com/classic/museo/Login", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        startActivity(intent)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

    }
}