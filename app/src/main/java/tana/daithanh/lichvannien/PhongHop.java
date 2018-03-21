package tana.daithanh.lichvannien;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;

import tana.daithanh.adapter.HopAdapter;
import tana.daithanh.mode.QuestionHop;
import tana.daithanh.thaotac.CheckConection;

public class PhongHop extends Activity {

    ViewFlipper viewFlipper;
    EditText etCode;
    ListView lvHop;
    ArrayList<Object> lst;
    HopAdapter adapter;
    Button btGuiHop;
    CheckConection online;
    TextView tvThongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong_hop);

        lvHop=(ListView)findViewById(R.id.lvHop);
        viewFlipper=(ViewFlipper)findViewById(R.id.vfHopKin);
        etCode=(EditText)findViewById(R.id.etCode);
        btGuiHop=(Button)findViewById(R.id.btGuiHop);
        btGuiHop.setVisibility(View.GONE);
        tvThongBao=(TextView)findViewById(R.id.tvThongBao);
        tvThongBao.setText("");

        lst=new ArrayList<Object>();



//        etCode.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
//                        (i == KeyEvent.KEYCODE_ENTER)) {
//                    // Perform action on key press
////hideKeyboard(etCode);
//                    return true;
//                }
//                return false;
//            }
//        });



    }

    /**
     * Tuyến tải dữ liệu
     */
    private void threadLoadData() {

        new Thread(new Runnable() {
            public void run() {
                try {

                    lst=new ArrayList<Object>();


                    for(int i=0;i<4;i++)
                    {
                        QuestionHop qa=new QuestionHop("Xem quảng cáo ủng hộ chúng tôi. Ứng dụng này hoàn toàn miễn phí.","");
                        lst.add(qa);
                    }

                    runOnUiThread(NotifyDataToListView);

                } catch (Exception e) {
                    //Log.e("Mon:", "kk"+e);
                }

            }
        }).start();
    }

    /**
     * Cập nhật lại giao diện sau khi chạy tuyến
     */
    private Runnable NotifyDataToListView = new Runnable() {
        public void run() {


            adapter=new HopAdapter(PhongHop.this,R.layout.activity_phong_hop,lst);
            lvHop.setAdapter(adapter);
            adapter.setListView(lst);
            btGuiHop.setVisibility(View.VISIBLE);
        }
    };

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    void  doThongBaoOk()
    {



        AlertDialog.Builder  builder=    new AlertDialog.Builder(this)
                .setTitle("" + getString(R.string.app_name))
                .setMessage("" + getString(R.string.hoigui))
                .setIcon(R.drawable.thenao)

                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        for(int i=0;i<lst.size();i++)
                        {
                            String tam="";
                            View item=lvHop.getChildAt(i);
                            EditText iET=(EditText) item.findViewById(R.id.etItemAnswers);
                            if(iET.getText()!=null) {
                                tam = "" + iET.getText();
                            }
                            ((QuestionHop)lst.get(i)).setAnswers(tam);

                        }

                        viewFlipper.setDisplayedChild(0);
                        Toast.makeText(PhongHop.this,"Bạn đã gửi câu trả lời thành công !",Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton(R.string.huy, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing

                    }
                })
                .setCancelable(false)
                ;


        AlertDialog alert = builder.create();
        alert.show();

        ((Button)alert.findViewById(android.R.id.button1)).setBackgroundResource(R.drawable.dialog);
        ((Button)alert.findViewById(android.R.id.button1)).setTextColor(getResources().getColor(android.R.color.white));

        ((Button)alert.findViewById(android.R.id.button2)).setBackgroundResource(R.drawable.dialog);
        ((Button)alert.findViewById(android.R.id.button2)).setTextColor(getResources().getColor(android.R.color.white));
    }

    public void  onClickSendAnswers(View view)
    {
        doThongBaoOk();

    }

    public  void  onClickCode(View view)
    {
        online = new CheckConection(getApplicationContext());

        if(online.checkMobileInternetConn()) {
            tvThongBao.setText("");
            threadLoadData();
            viewFlipper.setDisplayedChild(1);
        }else
        {
            tvThongBao.setText("Cần kết nối Internet để tiếp tục !");
        }
    }

    public void onClickQuayLai(View view)
    {
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
