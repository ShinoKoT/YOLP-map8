package jp.ac.asojuku.jousena.yolp_map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;

public class MainActivity extends AppCompatActivity implements LocationListener {

    //LocationManagerを準備
    LocationManager mLocationManager=null;
    //MapViewを準備
    MapView mMapView=null;
    //直前の緯度(1000000精度)
    int lastLatitude=0;
    //直前の経度(1000000精度)
    int lastLongitude=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //地図表示用のYahooライブラリを用意
        mMapView = new MapView(this,"dj0zaiZpPXZUWXhpTENYVnZhWSZzPWNvbnN1bWVyc2VjcmV0Jng9OTU-");
        //ズームボタンを画面に配置
        mMapView.setBuiltInZoomControls(true);
        //地図縮尺バーを画面に配置
        mMapView.setScalebar(true);
        //初期場所をセット(渋谷駅にしてみた)
        double lat =35.658517;
        double lon =139.701334;
        GeoPoint gp=new GeoPoint((int)(lat*1000000),(int)(lon*1000000));
        //地図本体を取得
        MapController c=mMapView.getMapController();

        //地図本体にGeoPointを設定
        c.setCenter(gp);

        //地図本体のズームを3に設定
        c.setZoom(3);
        //地図本体を画面にセット
        setContentView(mMapView);

        //ここからGPSの使用
        //LocationManagerを取得
        mLocationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //位置測定のためのGPS精度や使用消費電力を設定する
        Criteria criteria=new Criteria();

        //Accuracy(低輝度)
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        //PowerRewuirement(低消費電力)
        criteria.setPowerRequirement(Criteria.ACCURACY_LOW);

        //位置情報を伝達してくれるロケーションプロバイダの取得
        String provider=mLocationManager.getBestProvider(criteria,true);

       //位置情報のイベントリスナーであるLocationListernerを登録
        //常に最新の位置情報を受け取るとる
        //パーミッションの確認が必要なのでif文で記載する
        if(mLocationManager !=null){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                mLocationManager.requestLocationUpdates(provider,0,0,this);
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        //緯度の取得
        double lat =location.getLatitude();
        int latitude=(int)(lat*1000000);

        //経度の取得
        double lon =location.getLatitude();
        int longitude=(int)(lon*1000000);

        if(latitude/1000 !=this.lastLatitude/1000||
                longitude/1000 !=this.lastLongitude/1000){

            //緯度の情報(GeoPoint)の作成
            GeoPoint gp=new GeoPoint(latitude,longitude);
            //地図本体を取得
            MapController c=mMapView.getMapController();
            //地図本体にGeoPointを設定
            c.setCenter(gp);


            //今回の緯度経度を覚える
            this.lastLatitude=lastLatitude;
            this.lastLatitude=longitude;
        }

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
