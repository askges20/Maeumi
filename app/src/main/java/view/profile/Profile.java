package profile;

import android.app.Activity;
import android.os.Bundle;

import com.hanium.android.maeumi.R;

public class Profile extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
    }
}
