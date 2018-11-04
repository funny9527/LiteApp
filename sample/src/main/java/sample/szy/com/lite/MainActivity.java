package sample.szy.com.lite;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.app.light.componet.activity.AppStarter;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                AppStarter.getInstance().start(this, "bundle1.js");
                break;
            case R.id.btn2:
                AppStarter.getInstance().start(this, "bundle2.js");
                break;
            case R.id.btn3:
                AppStarter.getInstance().start(this, "bundle3.js");
                break;
            case R.id.btn4:
                AppStarter.getInstance().start(this, "bundle4.js");
                break;
            case R.id.btn5:
                AppStarter.getInstance().start(this, "bundle5.js");
                break;
            case R.id.btn6:
                AppStarter.getInstance().start(this, "bundle6.js");
                break;
        }
    }
}
