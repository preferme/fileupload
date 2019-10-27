package houlei.caas.mobile.fileupload;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.fileupload.BuildConfig;
import com.example.fileupload.R;
import houlei.caas.mobile.fileupload.login.ui.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import houlei.caas.mobile.fileupload.photo.Photo;
import houlei.caas.mobile.fileupload.photo.PhotoAdeptor;
import houlei.caas.mobile.fileupload.photo.PhotoFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int CAMERA_RESULT_CODE = 1001;
    private static final int PRIVATE_CODE = 1315;//开启GPS权限
    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};

    private static final int REQUEST_CODE_GALLERY = 1234;

    private File workDir = null;

    public File initWorkDir(File baseDir) {
        if (baseDir.exists() && baseDir.isDirectory()) {
            workDir = new File(baseDir, "PhotoUpload");
            if (!workDir.exists()) {
                workDir.mkdirs();
            }
        }
        if (workDir.exists() && workDir.isDirectory()) {
            File photos = new File(workDir, "photos");
            if (!photos.exists()) {
                photos.mkdirs();
            }
        }
        return workDir;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LocationManager loctionManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    Activity#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for Activity#requestPermissions for more details.
//                        ActivityCompat.requestPermissions(MainActivity.this, LOCATIONGPS,
//                                BAIDU_READ_PHONE_STATE);
//                        Snackbar.make(view, "Permission Failed..................", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                        return;
//                    }
//                }
//                Location location = loctionManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                if (location != null) {
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//
//                    Snackbar.make(view, "Location : latitude="+latitude + "  longitude="+longitude, Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                } else {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }

                FunctionConfig config = new FunctionConfig.Builder()
                        .setMutiSelectMaxSize(8)
                        .build();
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, config, new GalleryFinal.OnHanlderResultCallback(){

                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        Toast.makeText(MainActivity.this, "onHanlderSuccess", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                        Toast.makeText(MainActivity.this, "onHanlderFailure", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        workDir = initWorkDir(Environment.getExternalStorageDirectory());

        // 询问权限
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);

        // GPS定位
        LocationManager loctionManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (loctionManager != null && !loctionManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "系统检测到未开启GPS定位服务,请开启", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, PRIVATE_CODE);
        }

        // CoreConfig 设置主题
//ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
        .build();
//配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
        .build();

//配置imageloader
        ImageLoader imageloader = new UILImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this.getApplicationContext(), imageloader, theme)
                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
        .build();
        GalleryFinal.init(coreConfig);

        ImageLoaderConfiguration imgCfg = new ImageLoaderConfiguration.Builder(MainActivity.this).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(imgCfg);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length==2) {
            if (grantResults[0]!=0) {
                Snackbar.make(findViewById(R.id.fab), "必须允许拍照，请重新安装并开放权限。", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            if (grantResults[1]==0) {

            } else {
                Snackbar.make(findViewById(R.id.fab), "必须允许写入照片文件，请重新安装并开放权限。", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String getFileName(Date time) {
        SimpleDateFormat yearmoth = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat datetime = new SimpleDateFormat("HHmmss");
        String preffix = "IMG";
        return new StringBuilder().append(preffix).append('_').append(yearmoth.format(time)).append('_').append(datetime.format(time)).append(".jpg").toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_RESULT_CODE:

                Log.i("FileUpload", "onActivityResult: " + data);
            case PRIVATE_CODE :
                Log.i("FileUpload", "gps::onActivityResult: " + data);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.i("FileUpload", "onNavigationItemSelected: " + getPackageName() + ".provider");

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Uri photoUri = FileProvider.getUriForFile(
                    this.getApplicationContext(),
                    "com.example.fileupload.fileprovider",
                    new File(workDir, getFileName(new Date())));
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(cameraIntent, CAMERA_RESULT_CODE);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            // 显示图片
            List<Photo> photos = PhotoFactory.create(workDir);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            PhotoAdeptor adapter = new PhotoAdeptor(photos);
            recyclerView.setAdapter(adapter);
        } else if (id == R.id.nav_tools) {
            // login
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }
}
