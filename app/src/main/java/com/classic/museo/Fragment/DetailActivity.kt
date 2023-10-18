package com.classic.museo.Fragment

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.classic.museo.databinding.ActivityDetailBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint

//import com.kakao.vectormap.MapView
import net.daum.mf.map.api.MapView


class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기 버튼
        binding.dtBack.setOnClickListener {
            finish()
        }
        //공유버튼
        share()
        //카카오 맵
        kakaoMap()

    }

    //카카오 맵
    fun kakaoMap(){
        val mapView = MapView(this)
        binding.kakaoMap.addView(mapView)

        //위치 나타내기 위도,경도
        //클릭해서 들어오면 위도 경도 데이터값을 넣어줘야함      *수정필요*
        val mapPoint = MapPoint.mapPointWithGeoCoord(
            37.86401877, 127.7521907
        )

        // 확대 레벨 설정 (값이 작을수록 더 확대됨)
        mapView.setMapCenterPoint(mapPoint, true)
        mapView.setZoomLevel(1, true)

        //마커 생성
        val marker = MapPOIItem()

        //받아온 박물관 이름데이터 itemName에 넣어줘야 함    *수정필요*
        marker.itemName = "이곳은 국립춘천박물관 입니다"
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.RedPin
        marker.selectedMarkerType = MapPOIItem.MarkerType.BluePin
        mapView.addPOIItem(marker)
    }

    //공유버튼 기능
    private fun share() {
        binding.dtShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    // intent로 넘어오는 홈페이지 값 받아오기     *수정필요*
                    intent.getStringExtra("homepage")
                )
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }
    }
}