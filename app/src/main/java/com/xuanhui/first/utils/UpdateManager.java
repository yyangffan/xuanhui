package com.xuanhui.first.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xuanhui.first.R;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 2017/11/23.
 */

public class UpdateManager implements OnResponseListener<String> {

    private Context context;
    private boolean isInterceptDownload;
    private String apkName="mine_apk.apk";
    private final int COMMIT_WHAT = 0x001;
    private String apkUrl;
    private RequestQueue requestQueue = NoHttp.newRequestQueue();
    private boolean isUpdate;
    private String urlString;
    // 提示信息对话框
    AlertDialog updateDialog;
    // 下载对话框
    AlertDialog downloadDialog;
    private View viewUpdate;
    private TextView tvProgress;
    private TextView tvTotal;
    private boolean isToast = false;
    private int appVersionCode;
    private ProgressBar progressBar;
    private int progress;
    private String versionName;
    private OnCancelClickListener mOnCancelClickListener;
    private static final String TAG = "UpdateManager";
    private IsUpDateListener mIsUpDateListener;

    public UpdateManager(Context context, boolean isToast) {
        super();
        this.context = context;
        this.isToast = isToast;
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        mOnCancelClickListener = onCancelClickListener;
    }

    public void setIsUpDateListener(IsUpDateListener isUpDateListener) {
        mIsUpDateListener = isUpDateListener;
    }

    /**
     * 更新版本,向服务器提交当前版本号,上传当前版本号
     */
    private void request() {
//        Request<String> request = NoHttp.createStringRequest(ConnectUrl.UPDATEAPP, RequestMethod.GET);
//        request.setTag(new Object());
//        request.add("version_num", appVersionCode);
//        request.add("apk_type", "1");
//        requestQueue.add(COMMIT_WHAT, request, this);
    }

    /**
     * 调用这个方法来进行更新
     */
    public void checkUpdate() {
        apkName = "ruijilingshoubang.apk";
        // 获取当前版本信息
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionName = packageInfo.versionName;
            appVersionCode = packageInfo.versionCode;
            request();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void showUpdateDialog(String msg) {
        Log.e(TAG, "showUpdateDialog: show dialog");
        isInterceptDownload = true;
        updateDialog = new AlertDialog.Builder(context).create();
        updateDialog.show();
        viewUpdate = LayoutInflater.from(context).inflate(R.layout.dialog_version, null);
        updateDialog.setContentView(viewUpdate);
        TextView tvTip = (TextView) viewUpdate.findViewById(R.id.tv_tips);
        Button btnCancel = (Button) viewUpdate.findViewById(R.id.btn_cancle);
        Button btnDelete = (Button) viewUpdate.findViewById(R.id.btn_delete);
        Button btnSure = (Button) viewUpdate.findViewById(R.id.btn_sure);
        tvTip.setText(msg);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SPUtils.put(context, "UpdateTip", version);
                updateDialog.dismiss();
                if (mOnCancelClickListener != null) {
                    mOnCancelClickListener.OnCancelClickListener();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.put(context, "DeleteTip", false);
                updateDialog.dismiss();
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInterceptDownload = false;
                updateDialog.dismiss();
                showDownLoadDialog();
            }
        });
        updateDialog.setCancelable(false);

    }

    public interface OnCancelClickListener {
        void OnCancelClickListener();
    }

    /**
     * 弹出下载框
     */
    private void showDownLoadDialog() {

        downloadDialog = new AlertDialog.Builder(context).create();
        downloadDialog.show();
//        downloadDialog.setTitle("版本更新中...");
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_progress, null);
        progressBar = (ProgressBar) v.findViewById(R.id.pb_update_progress);
        Button btnPause = (Button) v.findViewById(R.id.pause);
        tvProgress = (TextView) v.findViewById(R.id.tv_progress);
        tvTotal = (TextView) v.findViewById(R.id.tv_total);
        downloadDialog.setContentView(v);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 终止下载
                isInterceptDownload = true;

                downloadDialog.dismiss();
                Button btn = (Button) viewUpdate.findViewById(R.id.btn_sure);
                btn.setText("继续");
                updateDialog.show();
            }
        });

        downloadDialog.setCancelable(false);

        // 下载apk
        downLoadApk();
    }

    /**
     * 开启新线程下载apk
     */
    public void downLoadApk() {
        // 执行下载的方法
        Log.i(TAG, "downLoadApk: 版本更新，执行下载的方法");
        Thread downloadThread = new Thread(downloadRunnable);
        downloadThread.start();
    }

    /**
     * 下载apk的线程
     */
    private Runnable downloadRunnable = new Runnable() {

        @Override
        public void run() {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 如果没有SD卡
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("当前设备无SD卡，数据无法下载");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

                return;
            } else {
                try {
                    Log.i("UPdateManager", "版本更新，已经在执行下载的方法");
                    // 服务器上新版apk地址,这应该是从服务器上下载的json解析下来的地址 apkUrl
                    // "http://gdown.baidu.com/data/wisegame/3a60158cef157a6d/QQ_372.apk"
                    URL url = new URL("http://gdown.baidu.com/data/wisegame/3a60158cef157a6d/QQ_372.apk");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int length = connection.getContentLength();
                    InputStream is = connection.getInputStream();
                    File file = new File(Environment.getExternalStorageDirectory()+ "/updateApkFile/" + apkName);
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    int count = 0;
                    byte[] buf = new byte[1024];

                    while (true) {

                        // 当点击暂停时，则暂停下载
                        if (!isInterceptDownload) {
                            int numRead = is.read(buf);
                            count += numRead;
                            progress = (int) (((float) count / length) * 100);
                            handler.sendEmptyMessage(1);
                            if (numRead <= 0) {
                                // 下载完成通知安装
                                handler.sendEmptyMessage(0);
                                break;
                            }
                            fos.write(buf, 0, numRead);
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    /**
     * 声明一个handler来跟进进度条
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 更新进度情况
                    progressBar.setProgress(progress);
                    tvProgress.setText(progress + "");
                    break;
                case 0:
                    progressBar.setVisibility(View.INVISIBLE);
                    downloadDialog.dismiss();
                    // 安装apk文件
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 安装apk
     */
    private void installApk() {
        // 获取当前sdcard存储路径, 安装，如果签名不一致，可能出现程序未安装提示
        File apkfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/updateApkFile/" + apkName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 添加这一句表示对目标应用临时授权该Uri所代表的文件
        i.setDataAndType(FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", apkfile), "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        if (what == COMMIT_WHAT) {
            String versionJson = response.get();
            Log.i(TAG, "onSucceed:更新" + versionJson);
            // 解析jsonk看信息是否要更新
            try {
                JSONObject object = new JSONObject(versionJson);
                boolean success = object.optBoolean("success");
                boolean show = object.optBoolean("show");

                if (success) {
                    JSONObject data = object.optJSONObject("data");
                    urlString = data.optString("store_add");
                    showUpdateDialog("请更新到最新版本");
                    if (mIsUpDateListener != null) {
                        mIsUpDateListener.IsUpDateListener(true);
                    }
                }else {
                    if (mIsUpDateListener != null) {
                        mIsUpDateListener.IsUpDateListener(false);
                    }
                }
                if (show) {
//                    String msg = object.optString("msg");
//                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                if (mIsUpDateListener != null) {
                    mIsUpDateListener.IsUpDateListener(false);
                }
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {
        if (mIsUpDateListener != null) {
            mIsUpDateListener.IsUpDateListener(false);
        }
    }

    @Override
    public void onFinish(int what) {
//        if(mIsUpDateListener!=null){
//            mIsUpDateListener.IsUpDateListener(false);
//        }
    }

    public interface IsUpDateListener {
        void IsUpDateListener(boolean isUpdate);
    }

    /**
     * 设置要下载的apk的url地址，在个人中心版本更新页面要用到此方法
     */
    public void setUrl(String urlString) {
        this.urlString = urlString;
    }

    public void setTextView(TextView tvProgress) {
        this.tvProgress = tvProgress;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setDownloadDialog(AlertDialog downloadDialog) {
        this.downloadDialog = downloadDialog;
    }
}
