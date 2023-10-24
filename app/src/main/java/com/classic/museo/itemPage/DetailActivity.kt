package com.classic.museo.itemPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.classic.museo.data.Record
import com.classic.museo.databinding.ActivityDetailBinding
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

        //공공데이터 받아오기
        museoInfo()

    }

    //카카오 맵
    fun kakaoMap(){
        val mapView = MapView(this)
        binding.kakaoMap.addView(mapView)
        val a=intent.getParcelableExtra<Record>("museumData")!!


        //위도 데이터
        val Latitude = a.latitude.toDouble()
        //경도 데이터
        val longitude = a.longitude.toDouble()
        //카카오맵 위치 나타내기
        val mapPoint = MapPoint.mapPointWithGeoCoord(Latitude, longitude)


        // 확대 레벨 설정 (값이 작을수록 더 확대됨)
        mapView.setMapCenterPoint(mapPoint, true)
        mapView.setZoomLevel(1, true)

        //마커 생성
        val marker = MapPOIItem()

        marker.itemName = "이곳은 ${a.fcltyNm} 입니다"
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.RedPin
        marker.selectedMarkerType = MapPOIItem.MarkerType.BluePin
        mapView.addPOIItem(marker)
    }

    //공유버튼 기능
    private fun share() {
        val a=intent.getParcelableExtra<Record>("museumData")!!
        binding.dtShare.setOnClickListener {

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    a.homepageUrl
                )
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }
    }
    //공공api데이터 가져오기
    fun museoInfo() {
        val a=intent.getParcelableExtra<Record>("museumData")!!
        //시설명
        binding.dtTitle.text = a.fcltyNm
        //운영기관전화번호
        binding.dtNumber.text = a.operPhoneNumber
        //소재지 도로명 주소
        binding.dtAddress.text = a.rdnmadr
        //박물관 미술관 소개
        binding.dtIntroduction.text = a.fcltyIntrcn
        //휴관정보
        binding.closeDayTx.text = a.rstdeInfo
        //이용시간
        binding.hoursuseTx.text = "${a.weekdayOperOpenHhmm}~${a.weekdayOperColseHhmm} (공휴일 ${a.holidayOperOpenHhmm} ~ ${a.holidayCloseOpenHhmm})"

        //관람료 기타정보(입장료)
        binding.moneyTx.text = "${a.adultChrge}원 ${a.etcChrgeInfo}"
        //운영홈페이지
        binding.homepageTx.text = a.homepageUrl
        //관리기관명
        binding.organizationTx.text = a.institutionNm
        //박물관 구분
        binding.SortationTx.text = a.fcltyType

    }
}