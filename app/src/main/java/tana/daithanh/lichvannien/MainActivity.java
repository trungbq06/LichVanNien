package tana.daithanh.lichvannien;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ViewFlipper;

import java.util.ArrayList;

import tana.daithanh.adapter.FrameAdapter;

import tana.daithanh.adapter.FrameAdapterMonth;
import tana.daithanh.database.DanhNgon;
import tana.daithanh.database.DataSourceDanhNgon;
import tana.daithanh.thaotac.AmDuong;

public class MainActivity extends FragmentActivity {

   // private TextView mTextMessage;
    ViewPager viewpagerNgay;
    FrameAdapter adapterNgay;

    ViewPager viewpagerThang;
    FrameAdapterMonth adapterThang;

    ViewFlipper vfHome;
    private DataSourceDanhNgon datasource;
   //AmDuong amDuong;
   private ArrayList<DanhNgon> lstVN;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   // mTextMessage.setText(R.string.title_home);
                    vfHome.setDisplayedChild(0);
                    return true;
                case R.id.navigation_dashboard:
                   // mTextMessage.setText(R.string.title_dashboard);
                    vfHome.setDisplayedChild(1);
                    return true;
                case R.id.navigation_notifications:
                   // mTextMessage.setText(R.string.title_notifications);
                    vfHome.setDisplayedChild(2);
                    return true;
            }
            return false;
        }
    };

    public void OnClick_HomNay(View view)
    {

        viewpagerNgay.setCurrentItem(183);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new DataSourceDanhNgon(this);
        datasource.open();
        lstVN = new ArrayList<DanhNgon>();

        vfHome=(ViewFlipper) findViewById(R.id.viewFliper);

        viewpagerNgay = (ViewPager) findViewById(R.id.viewpagerNgay);
        adapterNgay = new FrameAdapter(getSupportFragmentManager(),lstVN);
        adapterNgay.setmCount(360);
        viewpagerNgay.setAdapter(adapterNgay);
        viewpagerNgay.setCurrentItem(183);

        viewpagerThang = (ViewPager) findViewById(R.id.viewpagerThang);
        adapterThang = new FrameAdapterMonth(getSupportFragmentManager());
        adapterThang.setmCount(72);
        viewpagerThang.setAdapter(adapterThang);
        viewpagerThang.setCurrentItem(36);

        threadLoadData();

//        amDuong=new AmDuong();
//
//        int a[]= amDuong.convertSolar2Lunar(9,9,1989,7);
//        Log.e("xam",""+a[0]+"--"+a[1]+"--"+a[2]);

       // mTextMessage = (TextView) findViewById(R.id.message);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    /*
Lay du lieu do vao arraylist
*/
    private void threadLoadData() {


        new Thread(new Runnable() {
            public void run() {
                try {


                    getDefaultDataOffline();


                } catch (Exception e) {

                }

            }
        }).start();
    }

    /*
Load du lieu vao arraylist
*/
    public void getDefaultDataOffline() {
        try {
            ArrayList<Object> values = datasource.getAllNews();

            for (Object item : values) {
                DanhNgon n1 = new DanhNgon();


                n1.setId(((DanhNgon) item).getId().trim());
                n1.setContent(((DanhNgon) item).getContent().trim());
                n1.setAuthor(((DanhNgon) item).getAuthor().trim());


                lstVN.add(n1);

            }

            datasource.close();
            viewpagerNgay.getAdapter().notifyDataSetChanged();
        } catch (Exception ex) {

        }


    }

}
